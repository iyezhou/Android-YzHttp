package me.yezhou.yzhttp.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by yezhou on 2017/3/17.
 */
public interface YzCookieStore {

    void add(HttpUrl httpUrl, Cookie cookie);

    void add(HttpUrl httpUrl, List<Cookie> cookies);

    List<Cookie> get(HttpUrl httpUrl);

    List<Cookie> getCookies();

    boolean remove(HttpUrl httpUrl, Cookie cookie);

    boolean removeAll();

    List<HttpUrl> getHttpUrls();

}
