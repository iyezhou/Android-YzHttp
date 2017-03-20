package me.yezhou.yzhttp.request;

import android.text.TextUtils;

import java.util.Map;

import me.yezhou.yzhttp.exception.ParamIllegalException;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;

/**
 * Created by yezhou on 2017/3/20.
 */

public class OtherRequest extends OkHttpRequest {
    private static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain; charset=utf-8");
    private RequestBody requestBody;
    private String method;
    private String content;

    public OtherRequest(String url, String method, Object tag, Map<String, String> params, Map<String, String> headers, RequestBody requestBody, String content) {
        super(url, tag, params, headers);
        this.requestBody = requestBody;
        this.method = method;
        this.content = content;
    }

    @Override
    protected RequestBody buildRequestBody() {
        if (requestBody == null) {
            if (!TextUtils.isEmpty(content)) {
                requestBody = RequestBody.create(MEDIA_TYPE_PLAIN, content);
            } else if (params != null && params.size() > 0) {
                FormBody.Builder builder = new FormBody.Builder();
                if (params != null && !params.isEmpty()) {
                    for (String key : params.keySet()) {
                        builder.add(key, params.get(key));
                    }
                }
                requestBody = builder.build();
            } else {
                new ParamIllegalException("RequestBody or content can not be null in method: " + method).throwException();
            }
        }
        return requestBody;
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        if (method.equals(OtherRequest.METHOD.PUT)) {
            builder.put(requestBody);
        } else if (method.equals(OtherRequest.METHOD.DELETE)) {
            if (requestBody == null)
                builder.delete();
            else
                builder.delete(requestBody);
        } else if (method.equals(OtherRequest.METHOD.HEAD)) {
            builder.head();
        } else if (method.equals(OtherRequest.METHOD.PATCH)) {
            builder.patch(requestBody);
        }
        return builder.build();
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}
