package me.yezhou.yzhttp.callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by yezhou on 2017/3/16.
 */
public abstract class StringCallback extends Callback<String> {

    @Override
    public String onNetworkResponse(Response response) throws Exception {
        return response.body().string();
    }

}
