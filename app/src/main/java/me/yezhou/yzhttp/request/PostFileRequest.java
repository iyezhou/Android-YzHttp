package me.yezhou.yzhttp.request;

import java.io.File;
import java.util.Map;

import me.yezhou.yzhttp.exception.ParamIllegalException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * binary
 * Created by yezhou on 2017/3/16.
 */
public class PostFileRequest extends OkHttpRequest {

    public static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain; charset=utf-8");
    public static MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    private File file;
    private MediaType mediaType;

    public PostFileRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, File file, MediaType mediaType) {
        super(url, tag, params, headers);
        this.file = file;
        this.mediaType = mediaType;

        if (this.file == null) {
            new ParamIllegalException("文件为空").throwException();
        }
        if (this.mediaType == null) {
            this.mediaType = MEDIA_TYPE_STREAM;
        }

    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mediaType, file);
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    @Override
    public String toString() {
        return super.toString() + ", RequestBody{uploadFilePath=" + file.getAbsolutePath() + "}";
    }
}
