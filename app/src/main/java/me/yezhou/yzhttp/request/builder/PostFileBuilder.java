package me.yezhou.yzhttp.request.builder;

import java.io.File;
import java.util.Map;

import me.yezhou.yzhttp.request.PostFileRequest;
import me.yezhou.yzhttp.request.RequestCall;
import okhttp3.MediaType;

/**
 * Created by yezhou on 2017/3/16.
 */
public class PostFileBuilder extends OkHttpRequestBuilder {

    private File file;
    private MediaType mediaType;

    @Override
    public RequestCall build() {
        return new PostFileRequest(url, tag, params, headers, file, mediaType).build();
    }

    public PostFileBuilder file(File file) {
        this.file = file;
        return this;
    }

    public PostFileBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public PostFileBuilder url(String url) {
        return (PostFileBuilder) super.url(url);
    }

    @Override
    public PostFileBuilder tag(Object tag) {
        return (PostFileBuilder) super.tag(tag);
    }

    @Override
    public PostFileBuilder params(Map<String, String> params) {
        return (PostFileBuilder) super.params(params);
    }

    @Override
    public PostFileBuilder addParam(String key, String val) {
        return (PostFileBuilder) super.addParam(key, val);
    }

    @Override
    public PostFileBuilder headers(Map<String, String> headers) {
        return (PostFileBuilder) super.headers(headers);
    }

    @Override
    public PostFileBuilder addHeader(String key, String val) {
        return (PostFileBuilder) super.addHeader(key, val);
    }
}
