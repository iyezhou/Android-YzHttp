package me.yezhou.yzhttp.request.builder;

import java.util.Map;

import me.yezhou.yzhttp.request.PostStringRequest;
import me.yezhou.yzhttp.request.RequestCall;
import okhttp3.MediaType;

/**
 * Created by yezhou on 2017/3/16.
 */
public class PostStringBuilder extends OkHttpRequestBuilder {
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

    @Override
    public PostStringBuilder url(String url) {
        return (PostStringBuilder) super.url(url);
    }

    @Override
    public PostStringBuilder tag(Object tag) {
        return (PostStringBuilder) super.tag(tag);
    }

    @Override
    public PostStringBuilder params(Map<String, String> params) {
        return (PostStringBuilder) super.params(params);
    }

    @Override
    public PostStringBuilder addParam(String key, String val) {
        return (PostStringBuilder) super.addParam(key, val);
    }

    @Override
    public PostStringBuilder headers(Map<String, String> headers) {
        return (PostStringBuilder) super.headers(headers);
    }

    @Override
    public PostStringBuilder addHeader(String key, String val) {
        return (PostStringBuilder) super.addHeader(key, val);
    }
}
