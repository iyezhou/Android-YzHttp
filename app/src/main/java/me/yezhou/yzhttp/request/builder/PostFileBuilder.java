package me.yezhou.yzhttp.request.builder;

import java.io.File;

import me.yezhou.yzhttp.request.PostFileRequest;
import me.yezhou.yzhttp.request.RequestCall;
import okhttp3.MediaType;

/**
 * Created by yezhou on 2017/3/16.
 */
public class PostFileBuilder extends OkHttpRequestBuilder<PostFileBuilder> {

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

}
