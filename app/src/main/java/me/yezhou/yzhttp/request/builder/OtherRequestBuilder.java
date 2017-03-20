package me.yezhou.yzhttp.request.builder;

import java.util.LinkedHashMap;
import java.util.Map;

import me.yezhou.yzhttp.request.OtherRequest;
import me.yezhou.yzhttp.request.RequestCall;
import okhttp3.RequestBody;

/**
 * Created by yezhou on 2017/3/20.
 */

public class OtherRequestBuilder extends OkHttpRequestBuilder<OtherRequestBuilder> implements HasParamsable {
    private String method;
    private RequestBody requestBody;
    private String content;

    public OtherRequestBuilder(String method) {
        this.method = method;
    }

    @Override
    public RequestCall build() {
        return new OtherRequest(url, method, tag, params, headers, requestBody, content).build();
    }

    public OtherRequestBuilder requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public OtherRequestBuilder requestBody(String content) {
        this.content = content;
        return this;
    }

    @Override
    public OtherRequestBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public OtherRequestBuilder addParam(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }
}
