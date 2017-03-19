package me.yezhou.yzhttp.exception;

/**
 * Created by yezhou on 2017/3/14.
 */
public class HttpResponseException {

    private String msg;

    public HttpResponseException(String msg) {
        this.msg = msg;
    }

    public void throwException() {
        throw new RuntimeException(msg);
    }

}
