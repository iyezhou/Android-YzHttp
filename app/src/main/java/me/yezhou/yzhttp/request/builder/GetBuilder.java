package me.yezhou.yzhttp.request.builder;

import java.util.LinkedHashMap;
import java.util.Map;

import me.yezhou.yzhttp.request.GetRequest;
import me.yezhou.yzhttp.request.RequestCall;

/**
 * Created by yezhou on 2017/3/14.
 */
public class GetBuilder extends OkHttpRequestBuilder {

    @Override
    public RequestCall build() {
        if (params != null) {
            url = appendParams(url, params);
        }
        return new GetRequest(url, tag, params, headers).build();
    }

    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
