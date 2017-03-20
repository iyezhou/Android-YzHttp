package me.yezhou.yzhttp.request.builder;

import me.yezhou.yzhttp.request.OtherRequest;
import me.yezhou.yzhttp.request.RequestCall;

/**
 * Created by yezhou on 2017/3/20.
 */

public class HeadBuilder extends GetBuilder {

    @Override
    public RequestCall build() {
        return new OtherRequest(url, OtherRequest.METHOD.HEAD, tag, params, headers, null, null).build();
    }

}
