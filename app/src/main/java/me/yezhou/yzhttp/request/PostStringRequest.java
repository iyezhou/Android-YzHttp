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
public class PostStringRequest extends OkHttpRequest {

    public static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain; charset=utf-8");
    public static MediaType MEDIA_TYPE_HTML = MediaType.parse("text/html; charset=utf-8");
    public static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static MediaType MEDIA_TYPE_XML = MediaType.parse("text/xml; charset=utf-8");

    private String content;
    private MediaType mediaType;

    public PostStringRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, String content, MediaType mediaType) {
        super(url, tag, params, headers);
        this.content = content;
        this.mediaType = mediaType;

        if (this.content == null) {
            new ParamIllegalException("内容为空").throwException();
        }
        if (this.mediaType == null) {
            this.mediaType = MEDIA_TYPE_PLAIN;
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
