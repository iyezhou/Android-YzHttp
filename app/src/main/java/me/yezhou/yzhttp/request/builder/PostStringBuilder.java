package me.yezhou.yzhttp.request.builder;

import me.yezhou.yzhttp.request.PostStringRequest;
import me.yezhou.yzhttp.request.RequestCall;
import okhttp3.MediaType;

/**
 * Created by yezhou on 2017/3/16.
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder> {
    private String content;
    private MediaType mediaType;

    public PostStringBuilder content(String content) {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostStringRequest(url, tag, params, headers, content, mediaType).build();
    }
}
