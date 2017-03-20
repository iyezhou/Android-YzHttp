package me.yezhou.yzhttp.request.builder;

import java.util.Map;

import me.yezhou.yzhttp.request.PostBytesRequest;
import me.yezhou.yzhttp.request.RequestCall;

/**
 * Created by yezhou on 2017/3/16.
 */
public class PostBytesBuilder extends OkHttpRequestBuilder<PostBytesBuilder> {
    private byte[] content;

    public PostBytesBuilder content(byte[] content) {
        this.content = content;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostBytesRequest(url, tag, params, headers, content).build();
    }
}
