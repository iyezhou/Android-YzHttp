package me.yezhou.yzhttp.request;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import me.yezhou.yzhttp.YzHttp;
import me.yezhou.yzhttp.callback.Callback;
import me.yezhou.yzhttp.intercept.ProgressRequestBody;
import me.yezhou.yzhttp.request.builder.PostFormBuilder;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * multipart/form-data
 * Created by yezhou on 2017/3/14.
 */
public class PostFormRequest extends OkHttpRequest {

    private List<PostFormBuilder.FileInput> files;

    public PostFormRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, List<PostFormBuilder.FileInput> files) {
        super(url, tag, params, headers);
        this.files = files;
    }

    @Override
    protected RequestBody buildRequestBody() {
        // POST请求需要构建RequestBody
        if (files == null || files.isEmpty()) {  // FormBody
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder);
            return builder.build();
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            // 构建MultipartBody的Form表单参数
            addParams(builder);
            // 构建MultipartBody的文件参数
            for (int i = 0; i < files.size(); i++) {
                PostFormBuilder.FileInput fileInput = files.get(i);
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.filename)), fileInput.file);
                builder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
            }
            return builder.build();
        }
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    // 包装请求拦截器
    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback) {
        if (callback == null) return requestBody;
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody, new ProgressRequestBody.RequestProgressListener() {
            @Override
            public void onProgress(final float progress) {
                YzHttp.getInstance().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callback.inProgress(progress);
                    }
                });
            }

            @Override
            public void onDone(final long length) {
                YzHttp.getInstance().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDone(length);
                    }
                });
            }
        });
        return progressRequestBody;
    }

    // 构建FormBody
    private void addParams(FormBody.Builder builder) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }

    // 构建MultipartBody的Form表单参数
    private void addParams(MultipartBody.Builder builder) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        if (files != null && files.size() > 0) {
            sb.append(", ");
            for (PostFormBuilder.FileInput file : files) {
                sb.append(file.toString() + " ");
            }
        }
        return sb.toString();
    }
}
