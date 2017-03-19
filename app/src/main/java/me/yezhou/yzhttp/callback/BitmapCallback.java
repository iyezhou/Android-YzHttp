package me.yezhou.yzhttp.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Response;

/**
 * Created by yezhou on 2017/3/16.
 */
public abstract class BitmapCallback extends Callback<Bitmap> {

    @Override
    public Bitmap onNetworkResponse(Response response) throws Exception {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }

}
