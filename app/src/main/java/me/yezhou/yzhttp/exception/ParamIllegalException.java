package me.yezhou.yzhttp.exception;

/**
 * Created by yezhou on 2017/3/14.
 */
public class ParamIllegalException {

    private String msg;

    public ParamIllegalException(String msg) {
        this.msg = msg;
    }

    public void throwException() {
        throw new IllegalArgumentException(msg);
    }

}
