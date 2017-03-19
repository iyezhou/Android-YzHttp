package me.yezhou.yzhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import me.yezhou.yzhttp.callback.Callback;
import me.yezhou.yzhttp.callback.ProgressCallback;
import me.yezhou.yzhttp.cookie.SimpleCookieJar;
import me.yezhou.yzhttp.cookie.YzCookieJar;
import me.yezhou.yzhttp.cookie.YzPersistentCookieStore;
import me.yezhou.yzhttp.exception.HttpsInitialException;
import me.yezhou.yzhttp.exception.ParamIllegalException;
import me.yezhou.yzhttp.https.HttpsUtil;
import me.yezhou.yzhttp.https.YzX509TrustManager;
import me.yezhou.yzhttp.intercept.ProgressRequestBody;
import me.yezhou.yzhttp.intercept.ProgressResponseBody;
import me.yezhou.yzhttp.intercept.WriteFileResponseBody;
import me.yezhou.yzhttp.request.RequestCall;
import me.yezhou.yzhttp.request.builder.GetBuilder;
import me.yezhou.yzhttp.request.builder.PostBytesBuilder;
import me.yezhou.yzhttp.request.builder.PostFileBuilder;
import me.yezhou.yzhttp.request.builder.PostFormBuilder;
import me.yezhou.yzhttp.request.builder.PostStringBuilder;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yezhou on 2017/3/14.
 */
public class YzHttp {
    public static final String TAG = "yezhou";
    public static final long DEFAULT_TIMEOUT_MILLISECONDS = 10000;
    public static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    private static YzHttp mInstance;

    private OkHttpClient mOkHttpClient;
    private OkHttpClient.Builder mOkHttpClientBuilder;
    private SimpleCookieJar mSimpleCookieJar;  //内存Cookie
    private YzCookieJar mPersistentCookieJar;  //序列化Cookie

    private boolean isHttps = false;
    private Interceptor fileDownloadWriteFileInterceptor;
    private Interceptor fileDownloadProgressInterceptor;
    private Interceptor fileUploadInterceptor;

    private Handler mHandler;

    private YzHttp() {
        mOkHttpClientBuilder = new OkHttpClient.Builder();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public YzHttp buildOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = mOkHttpClientBuilder.build();
        }
        return this;
    }

    // 支持内存Cookie
    public YzHttp cookieSimple() {
        if (mSimpleCookieJar == null) {
            mSimpleCookieJar = new SimpleCookieJar();
            mOkHttpClientBuilder.cookieJar(mSimpleCookieJar);
            mOkHttpClient = mOkHttpClientBuilder.build();
        }
        return this;
    }

    // 支持序列化Cookie
    public YzHttp cookiePersistent(Context context) {
        if (mPersistentCookieJar == null) {
            mPersistentCookieJar = new YzCookieJar(new YzPersistentCookieStore(context));
            mOkHttpClientBuilder.cookieJar(mPersistentCookieJar);
            mOkHttpClient = mOkHttpClientBuilder.build();
        }
        return this;
    }

    // 支持自定义Cookie
    public YzHttp cookieCustom(CookieJar cookieJar) {
        if (cookieJar != null) {
            new HttpsInitialException("CookieJar为空").throwException();
        }
        mOkHttpClientBuilder.cookieJar(cookieJar);
        mOkHttpClient = mOkHttpClientBuilder.build();
        return this;
    }

    // 是否支持HTTPS
    public YzHttp https() {
        if (!isHttps) {
            mOkHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            mOkHttpClient = mOkHttpClientBuilder.build();
        }
        isHttps = true;
        return this;
    }

    public static YzHttp getInstance() {
        if (mInstance == null) {
            synchronized (YzHttp.class) {
                if (mInstance == null) {
                    mInstance = new YzHttp();
                }
            }
        }
        return mInstance;
    }

    // 构造GET请求
    public GetBuilder get() {
        return new GetBuilder();
    }

    // 构造Post请求(multipart/form-data方式)
    public PostFormBuilder post() {
        return new PostFormBuilder();
    }

    // 构造Post请求(raw方式)
    public PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    // 构造Post请求(binary方式)
    public PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    // 构造Post请求
    public PostBytesBuilder postBytes() {
        return new PostBytesBuilder();
    }

    // 公开自定义网络请求拦截器接口
    public YzHttp addNetworkInterceptor(Interceptor interceptor) {
        if (interceptor == null) {
            new ParamIllegalException("Interceptor为空").throwException();
        }
        if (!mOkHttpClientBuilder.interceptors().contains(interceptor)) {
            mOkHttpClientBuilder.addNetworkInterceptor(interceptor);
            mOkHttpClientBuilder.build();
        }
        return this;
    }

    // 移除自定义网络请求拦截器
    public YzHttp removeNetworkInterceptor(Interceptor interceptor) {
        if (interceptor == null) {
            new ParamIllegalException("Interceptor为空").throwException();
        }
        if (mOkHttpClientBuilder.interceptors().contains(interceptor)) {
            mOkHttpClientBuilder.interceptors().remove(interceptor);
            mOkHttpClientBuilder.build();
        }
        return this;
    }

    // 封装文件下载响应进度拦截器
    public YzHttp addFileDownloadInterceptor(final String filePath, final ProgressCallback callback) {
        boolean addFlag = false;
        if (fileDownloadWriteFileInterceptor == null) {
            fileDownloadWriteFileInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    return response.newBuilder()
                            .body(new WriteFileResponseBody(response.body(), filePath))  //封装文件写入拦截器
                            .build();
                }
            };
        }
        if (!mOkHttpClientBuilder.interceptors().contains(fileDownloadWriteFileInterceptor)) {
            mOkHttpClientBuilder.addNetworkInterceptor(fileDownloadWriteFileInterceptor);
            addFlag = true;
        }
        if (fileDownloadProgressInterceptor == null) {
            fileDownloadProgressInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    //拦截
                    Response response = chain.proceed(chain.request());
                    //包装响应体并返回
                    return response.newBuilder()
                            .body(new ProgressResponseBody(response.body(), new ProgressResponseBody.ProgressListener() {  //封装进度拦截器
                                @Override
                                public void inProgress(final float progress) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.inProgress(progress);
                                        }
                                    });
                                }

                                @Override
                                public void onDone(final long totalLength) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onDone(totalLength);
                                        }
                                    });
                                }
                            }))
                            .build();
                }
            };
        }
        if (!mOkHttpClientBuilder.interceptors().contains(fileDownloadProgressInterceptor)) {
            mOkHttpClientBuilder.addNetworkInterceptor(fileDownloadProgressInterceptor);
            addFlag = true;
        }
        if (addFlag) {
            mOkHttpClient = mOkHttpClientBuilder.build();
        }
        return this;
    }

    // 封装文件上传请求进度拦截器
    public YzHttp addFileUploadInterceptor(final ProgressCallback callback) {
        if (fileUploadInterceptor == null) {
            fileUploadInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originRequest = chain.request();
                    Request targetRequest = originRequest.newBuilder()
                            .post(new ProgressRequestBody(originRequest.body(), new ProgressRequestBody.RequestProgressListener() {  //封装上传进度拦截器

                                @Override
                                public void onProgress(final float progress) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.inProgress(progress);
                                        }
                                    });
                                }

                                @Override
                                public void onDone(final long totalLength) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onDone(totalLength);
                                        }
                                    });
                                }
                            }))
                            .build();
                    return chain.proceed(targetRequest);
                }
            };
        }
        if (!mOkHttpClientBuilder.interceptors().contains(fileUploadInterceptor)) {
            mOkHttpClientBuilder.addNetworkInterceptor(fileUploadInterceptor);
            mOkHttpClient = mOkHttpClientBuilder.build();
        }
        return this;
    }

    // 移除文件下载响应进度拦截器
    public YzHttp removeFileDownloadInterceptor() {
        boolean removeFlag = false;
        if (mOkHttpClientBuilder.interceptors().contains(fileDownloadWriteFileInterceptor)) {
            mOkHttpClientBuilder.interceptors().remove(fileDownloadWriteFileInterceptor);
            removeFlag = true;
        }
        if (mOkHttpClientBuilder.interceptors().contains(fileDownloadProgressInterceptor)) {
            mOkHttpClientBuilder.interceptors().remove(fileDownloadProgressInterceptor);
            removeFlag = true;
        }
        if (removeFlag) {
            mOkHttpClient = mOkHttpClientBuilder.build();
        }
        return this;
    }

    // 移除文件上传请求进度拦截器
    public YzHttp removeFileUploadInterceptor() {
        if (mOkHttpClientBuilder.interceptors().contains(fileUploadInterceptor)) {
            mOkHttpClientBuilder.interceptors().remove(fileUploadInterceptor);
            mOkHttpClient = mOkHttpClientBuilder.build();
        }
        return this;
    }

    public void enqueue(final RequestCall requestCall, Callback callback) {
        Log.d(TAG, "Method:" + requestCall.getRequest().method() + ", Info:" + requestCall.getOkHttpRequest().toString());

        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailResultCallback(call, e, finalCallback);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                if (response.isSuccessful()) {  //请求成功
                    try {
                        Object object = finalCallback.onNetworkResponse(response);
                        sendSuccessResultCallback(object, finalCallback);
                    } catch (Exception e) {
                        sendFailResultCallback(call, e, finalCallback);
                    }
                } else if (response.isRedirect()) {  //请求重定向

                } else if (response.code() >= 400 && response.code() <= 599) {  //请求失败
                    try {
                        Log.i(TAG, "Response Code: " + response.code());
                        sendFailResultCallback(call, new RuntimeException(response.body().string()), finalCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback) {
        if (callback == null) return;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback) {
        if (callback == null) return;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public Handler getHandler() {
        return mHandler;
    }

    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return mOkHttpClientBuilder;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public List<Cookie> getSimpleCookies(String url) {
        if (mSimpleCookieJar != null) {
            return mSimpleCookieJar.getCookies(url);
        }
        return null;
    }

    public List<Cookie> getPersistentCookies(String url) {
        if (mPersistentCookieJar != null) {
            return mPersistentCookieJar.getCookies(url);
        }
        return null;
    }

    public void clearCookies(String url) {
        List<Cookie> simpleCookies = getSimpleCookies(url);
        if (simpleCookies != null) {
            simpleCookies.clear();
        }
        List<Cookie> persistentCookies = getPersistentCookies(url);
        if (persistentCookies != null) {
            persistentCookies.clear();
        }
        if (mPersistentCookieJar != null) {
            mPersistentCookieJar.getCookieStore().removeAll();
        }
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public YzHttp setCertificates(InputStream... certificates) {
        mOkHttpClient = mOkHttpClientBuilder
                .sslSocketFactory(HttpsUtil.getSslSocketFactory(certificates, null, null))
                .build();
        return this;
    }

    public YzHttp setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        mOkHttpClient = mOkHttpClientBuilder
                .sslSocketFactory(HttpsUtil.getSslSocketFactory(certificates, bksFile, password), new YzX509TrustManager())
                //.sslSocketFactory(HttpsUtil.getSslSocketFactory(certificates, bksFile, password))
                .build();
        return this;
    }
}
