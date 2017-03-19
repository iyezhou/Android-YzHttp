package me.yezhou.yzhttp.request;

import android.text.TextUtils;

import java.util.Map;

import me.yezhou.yzhttp.callback.Callback;
import me.yezhou.yzhttp.exception.ParamIllegalException;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by yezhou on 2017/3/14.
 */
public abstract class OkHttpRequest {
    protected String url;
    protected Object tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;

    protected Request.Builder builder = new Request.Builder();

    protected OkHttpRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;

        if (TextUtils.isEmpty(url)) {
            new ParamIllegalException("URL不能为空").throwException();
        }
    }

    // 构造RequestBody，POST请求需要用到
    protected abstract RequestBody buildRequestBody();

    // 包装RequestBody，POST请求需要重写
    protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback) {
        return requestBody;
    }

    // 构造Request
    protected abstract Request buildRequest(Request.Builder builder, RequestBody requestBody);

    // 执行请求类：构造Call并execute、enqueue
    public RequestCall build() {
        return new RequestCall(this);
    }

    public Request generateRequest(Callback callback) {
        RequestBody requestBody = wrapRequestBody(buildRequestBody(), callback);
        prepareBuilder();
        return buildRequest(builder, requestBody);
    }

    private void prepareBuilder() {
        builder.url(url).tag(tag);
        appendHeaders();
    }

    protected void appendHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    @Override
    public String toString() {
        return "OkHttpRequest{" +
                "url='" + url + '\'' +
                ", tag=" + tag +
                ", params=" + params +
                ", headers=" + headers +
                '}';
    }
}
