package me.yezhou.yzhttp.request.builder;

import java.util.Map;

import me.yezhou.yzhttp.request.RequestCall;

/**
 * Created by yezhou on 2017/3/16.
 */
public class PostBytesBuilder extends OkHttpRequestBuilder {
    private byte[] content;

    public PostBytesBuilder content(byte[] content) {
        this.content = content;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostBytesRequest(url, tag, headers, content).build();
    }

    @Override
    public PostBytesBuilder url(String url) {
        return (PostBytesBuilder) super.url(url);
    }

    @Override
    public PostBytesBuilder tag(Object tag) {
        return (PostBytesBuilder) super.tag(tag);
    }

    @Override
    public PostBytesBuilder headers(Map<String, String> headers) {
        return (PostBytesBuilder) super.headers(headers);
    }

    @Override
    public PostBytesBuilder addHeader(String key, String val) {
        return (PostBytesBuilder) super.addHeader(key, val);
    }
}
