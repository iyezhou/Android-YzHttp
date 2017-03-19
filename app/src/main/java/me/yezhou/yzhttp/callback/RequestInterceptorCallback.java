package me.yezhou.yzhttp.callback;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

/**
 * Created by yezhou on 2017/3/16.
 */
public abstract class RequestInterceptorCallback extends Callback<Object> {

    @Override
    public Object onNetworkResponse(Response response) throws Exception {
        InputStream is = response.body().byteStream();
        try {
            byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
