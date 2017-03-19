package me.yezhou.yzhttp.cookie;

import java.util.List;

import me.yezhou.yzhttp.exception.ParamIllegalException;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by yezhou on 2017/3/17.
 */
public class YzCookieJar implements CookieJar {

    private YzCookieStore cookieStore;

    public YzCookieJar(YzCookieStore cookieStore) {
        if (cookieStore == null) {
            new ParamIllegalException("CookieStore为空").throwException();
        }
        this.cookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
        cookieStore.add(httpUrl, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        return cookieStore.get(httpUrl);
    }

    public List<Cookie> getCookies(String url){
        HttpUrl httpUrl = HttpUrl.parse(url);
        return cookieStore.get(httpUrl);
    }

    public List<Cookie> getCookies(HttpUrl httpUrl){
        return cookieStore.get(httpUrl);
    }

    public YzCookieStore getCookieStore() {
        return cookieStore;
    }
}
