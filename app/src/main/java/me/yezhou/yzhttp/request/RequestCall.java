package me.yezhou.yzhttp.request;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.yezhou.yzhttp.YzHttp;
import me.yezhou.yzhttp.callback.Callback;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yezhou on 2017/3/14.
 */
public class RequestCall {
    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;

    private long readTimeOut;
    private long writeTimeOut;
    private long connectTimeOut;

    public RequestCall(OkHttpRequest request) {
        this.okHttpRequest = request;
    }

    public RequestCall readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall connectTimeOut(long connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        return this;
    }

    // 构造Call
    public Call generateCall(Callback callback) {
        request = generateRequest(callback);

        if (readTimeOut > 0 || writeTimeOut > 0 || connectTimeOut > 0) {
            readTimeOut = readTimeOut > 0 ? readTimeOut : YzHttp.DEFAULT_TIMEOUT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : YzHttp.DEFAULT_TIMEOUT_MILLISECONDS;
            connectTimeOut = connectTimeOut > 0 ? connectTimeOut : YzHttp.DEFAULT_TIMEOUT_MILLISECONDS;

            YzHttp.getInstance().getOkHttpClientBuilder()
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
                .build();

            call = YzHttp.getInstance().getOkHttpClient().newCall(request);
        } else {
            call = YzHttp.getInstance().buildOkHttpClient().getOkHttpClient().newCall(request);
        }
        return call;
    }

    private Request generateRequest(Callback callback) {
        return okHttpRequest.generateRequest(callback);
    }

    //发送异步请求(回调方式)
    public void enqueue(Callback callback) {
        generateCall(callback);

        if (callback != null) {
            callback.onBefore(request);
        }

        YzHttp.getInstance().enqueue(this, callback);
    }

    //发送同步请求(返回方式)
    public Response execute() throws IOException {
        generateCall(null);
        return call.execute();
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public OkHttpRequest getOkHttpRequest() {
        return okHttpRequest;
    }
}
