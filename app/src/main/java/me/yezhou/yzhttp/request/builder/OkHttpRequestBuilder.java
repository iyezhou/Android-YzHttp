package me.yezhou.yzhttp.request.builder;

import java.util.LinkedHashMap;
import java.util.Map;

import me.yezhou.yzhttp.request.RequestCall;

/**
 * Created by yezhou on 2017/3/14.
 */
public abstract class OkHttpRequestBuilder {
    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;

    public abstract RequestCall build();

    public OkHttpRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public OkHttpRequestBuilder tag(Object tag) {
        this.tag = tag ;
        return this;
    }

    public OkHttpRequestBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public OkHttpRequestBuilder addParam(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    public OkHttpRequestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public OkHttpRequestBuilder addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }
}
