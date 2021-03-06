package me.yezhou.yzhttp.request;

import java.util.Map;

import me.yezhou.yzhttp.exception.ParamIllegalException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * raw
 * Created by yezhou on 2017/3/16.
 */
public class PostBytesRequest extends OkHttpRequest {

    private static MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");

    private byte[] content;

    public PostBytesRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, byte[] content) {
        super(url, tag, params, headers);
        this.content = content;

        if (this.content == null || this.content.length == 0) {
            new ParamIllegalException("内容为空").throwException();
        }
    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mediaType, content);
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    @Override
    public String toString() {
        return super.toString() + ", RequestBody{content=" + content + "}";
    }
}
