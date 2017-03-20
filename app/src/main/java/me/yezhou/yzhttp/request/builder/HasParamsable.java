package me.yezhou.yzhttp.request.builder;

import java.util.Map;

/**
 * Created by yezhou on 2017/3/20.
 */

public interface HasParamsable {
    OkHttpRequestBuilder params(Map<String, String> params);
    OkHttpRequestBuilder addParam(String key, String val);
}
