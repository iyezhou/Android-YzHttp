package me.yezhou.yzhttp.cookie;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by yezhou on 2017/3/14.
 */
public class SimpleCookieJar implements CookieJar {

    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
        Log.i("yezhou", "saveFromResponse: " + cookies.size());
        cookieStore.put(httpUrl.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        List<Cookie> cookies = cookieStore.get(httpUrl.host());
        if (cookies != null) {
            Log.i("yezhou", "loadForRequest: " + cookies.size());
        }
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

    public List<Cookie> getCookies(String url){
        HttpUrl httpUrl = HttpUrl.parse(url);
        return cookieStore.get(httpUrl.host());
    }

    public List<Cookie> getCookies(HttpUrl httpUrl){
        return cookieStore.get(httpUrl.host());
    }

    /*
    private final List<Cookie> cookies = new ArrayList<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies.addAll(cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> result = new ArrayList<>();
        for (Cookie cookie : cookies) {
            if (cookie.matches(url)) {
                result.add(cookie);
            }
        }
        return result;
    }

    public List<Cookie> getCookies(){
        return cookies;
    }
    */

}
