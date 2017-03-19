package me.yezhou.yzhttp.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yezhou on 2017/3/14.
 */
public abstract class Callback<T> {

    /**
     * UI Thread
     * @param request
     */
    public void onBefore(Request request) {

    }

    /**
     * UI Thread
     * @param
     */
    public void onAfter() {

    }

    /**
     * 传输过程进度
     * UI Thread
     * @param progress
     */
    public void inProgress(float progress) {

    }

    /**
     * 传输完成
     * UI Thread
     */
    public void onDone(long length) {

    }

    /**
     * Thread Pool Thread
     * @param response
     */
    public abstract T onNetworkResponse(Response response) throws Exception;

    /**
     * UI Thread
     * @param call
     * @param e
     */
    public abstract void onError(Call call, Exception e);

    /**
     * UI Thread
     * @param response
     */
    public abstract void onResponse(T response);

    public static Callback CALLBACK_DEFAULT = new Callback() {
        @Override
        public Object onNetworkResponse(Response response) throws Exception {
            return null;
        }

        @Override
        public void onError(Call call, Exception e) {

        }

        @Override
        public void onResponse(Object response) {

        }
    };
}
