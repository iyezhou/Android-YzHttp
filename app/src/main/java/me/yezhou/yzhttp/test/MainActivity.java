package me.yezhou.yzhttp.test;

import android.Manifest;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yezhou.yzhttp.R;
import me.yezhou.yzhttp.YzHttp;
import me.yezhou.yzhttp.callback.Callback;
import me.yezhou.yzhttp.callback.FileCallBack;
import me.yezhou.yzhttp.callback.RequestInterceptorCallback;
import me.yezhou.yzhttp.callback.ResponseInterceptorCallback;
import me.yezhou.yzhttp.callback.ProgressCallback;
import me.yezhou.yzhttp.request.PostFileRequest;
import me.yezhou.yzhttp.request.PostStringRequest;
import me.yezhou.yzhttp.request.builder.PostFormBuilder;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "yezhou";

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        requestPermissions();

        mProgressBar = (ProgressBar) this.findViewById(R.id.progress_bar);

    }

    public void get(View view) {
        YzHttp.getInstance().get().url("http://api.yezhou.me/api/user").addParam("id", "1").build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                ResponseBody body = response.body();
                if (body != null) {
                    JSONObject json = JSON.parseObject(body.string());
                    body.close();
                    if (json.getInteger("code") == 0) {
                        return JSON.parseObject(json.getString("data"), User.class);
                    }
                }
                return null;
            }

            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Object response) {
                User user = (User) response;
                if (user != null) {
                    Toast.makeText(MainActivity.this, user.getName(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void postForm(View view) {
        YzHttp.getInstance().post().url("http://api.yezhou.me/api/user").addParam("username", "yezhou").addParam("password", "123456").build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                ResponseBody body = response.body();
                if (body != null) {
                    JSONObject json = JSON.parseObject(body.string());
                    body.close();
                    if (json.getInteger("code") == 0) {
                        return JSON.parseObject(json.getString("data"), User.class);
                    }
                }
                return null;
            }

            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Object response) {
                User user = (User) response;
                if (user != null) {
                    Toast.makeText(MainActivity.this, user.getName(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void postFormMultipart(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "songqian.jpg");
        YzHttp.getInstance().post().url("http://api.yezhou.me/api/image").addParam("name", "宋茜").addFile("image", "宋茜.jpg", file).build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                ResponseBody body = response.body();
                if (body != null) {
                    Log.i(TAG, body.string());
                    body.close();
                }
                return null;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Object response) {
                Toast.makeText(MainActivity.this, "提交成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void inProgress(float progress) {
                mProgressBar.setProgress((int) progress);
            }
        });
    }

    public void postMultiFiles(View view) {
        File file1 = new File(Environment.getExternalStorageDirectory(), "songqian.jpg");
        File file2 = new File(Environment.getExternalStorageDirectory(), "songqian.jpg");
        List<PostFormBuilder.FileInput> fileInputs = new ArrayList<>();
        fileInputs.add(new PostFormBuilder.FileInput("images[]", "宋茜1.jpg", file1));
        fileInputs.add(new PostFormBuilder.FileInput("images[]", "宋茜2.jpg", file2));
        YzHttp.getInstance().post().url("http://api.yezhou.me/api/images").addParam("name", "宋茜").files(fileInputs).build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                ResponseBody body = response.body();
                if (body != null) {
                    Log.i(TAG, body.string());
                    body.close();
                }
                return null;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Object response) {
                Toast.makeText(MainActivity.this, "提交成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void inProgress(float progress) {
                Log.i(TAG, (int) progress + "%");
            }
        });
    }

    public void postJson(View view) {
        String content = "{\"username\":\"yezhou\",\"password\":\"123456\"}";
        YzHttp.getInstance().postString().url("http://api.yezhou.me/api/user/json").mediaType(PostStringRequest.MEDIA_TYPE_JSON).content(content).build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                ResponseBody body = response.body();
                if (body != null) {
                    JSONObject json = JSON.parseObject(body.string());
                    if (json.getInteger("code") == 0) {
                        return JSON.parseObject(json.getString("data"), User.class);
                    }
                    body.close();
                }
                return null;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Object response) {
                User user = (User) response;
                if (user != null) {
                    Toast.makeText(MainActivity.this, user.getName(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void postFile(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "test.txt");
        YzHttp.getInstance().postFile().url("https://api.github.com/markdown/raw").addHeader("Content-Type", "text/plain").mediaType(PostFileRequest.MEDIA_TYPE_STREAM).file(file).build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                ResponseBody body = response.body();
                if (body != null) {
                    Log.i(TAG, body.string());
                    body.close();
                }
                return null;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Object response) {
                Toast.makeText(MainActivity.this, "提交成功", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void postBytes(View view) {
        byte[] bytes = "中国: CHINA".getBytes();
        YzHttp.getInstance().postBytes().url("http://api.yezhou.me/api/plain").content(bytes).build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                ResponseBody body = response.body();
                if (body != null) {
                    String result = body.string();
                    body.close();
                    Log.i(TAG, result);
                    JSONObject json = JSON.parseObject(result);
                    if (json.getInteger("code") == 0) {
                        return json.getString("data");
                    }
                }
                return null;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Object response) {
                String result = (String) response;
                if (!TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void downloadFile(View view) {
        String fileDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "weixin.apk";
        YzHttp.getInstance().get().url("http://dldir1.qq.com/weixin/android/weixin654android1000.apk").build().enqueue(new FileCallBack(fileDir, fileName) {
            @Override
            public void inProgress(float progress) {
                mProgressBar.setProgress((int)progress);
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(File response) {
                Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void downloadFileWithInterceptor(View view) {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "weixin.apk";
        YzHttp.getInstance().addFileDownloadInterceptor(filePath, new ProgressCallback() {
            @Override
            public void inProgress(float progress) {
                Log.i(TAG, "progress: " + progress);
                mProgressBar.setProgress((int)progress);
            }
        }).get().url("http://dldir1.qq.com/weixin/android/weixin654android1000.apk").build().enqueue(new ResponseInterceptorCallback() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Object response) {
                Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void uploadFileWithInterceptor(View view) {
        //File file = new File(Environment.getExternalStorageDirectory(), "weixin.apk");  //服务器端有上传文件大小限制
        File file = new File(Environment.getExternalStorageDirectory(), "generated.apk");
        YzHttp.getInstance().addFileUploadInterceptor(new ProgressCallback() {
            @Override
            public void inProgress(float progress) {
                mProgressBar.setProgress((int)progress);
            }
        }).post().url("http://api.yezhou.me/api/file").addParam("name", "合并Apk").addFile("file", "generated.apk", file)/*.addParam("name", "微信").addFile("file", "weixin.apk", file)*/.build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                Log.i(TAG, response.body().string());
                return null;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Object response) {
                Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loginZhihu(View view) {
        // 1.知乎登陆接口和参数分析: _xsrf, password, remember_me, email
        // 2.模拟登陆
        //   2.1.获取_xsrf隐藏域值
        //   2.2.模拟登陆
        // 3.Cookie持久化
        YzHttp.getInstance().get().url("https://www.zhihu.com/#signin").build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                String html = response.body().string();
                Document parse = Jsoup.parse(html);
                Elements select = parse.select("input[type=hidden]");
                Element element = select.get(0);
                String xsrf = element.attr("value");
                Log.i(TAG, "_xsrf: " + xsrf);
                return xsrf;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Object response) {
                String xsrf = (String) response;
                if (!TextUtils.isEmpty(xsrf)) {
                    loginZhihu(xsrf);
                }
            }
        });
    }

    private void loginZhihu(String xsrf) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("_xsrf", xsrf);
        params.put("password", "xxxxxx");
        params.put("remember_me", "true");
        //params.put("phone_num", "150XXXXXXXX");
        params.put("email", "yezhou@yezhou.org");
        Map<String, String> headers = new HashMap<String, String>();
        //headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
        YzHttp.getInstance().https().post().url("https://www.zhihu.com/login/email")/*.url("http://www.zhihu.com/login/phone_num")*/.params(params).build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                ResponseBody body = response.body();
                String result = body.string();
                Log.i(TAG, "登录知乎返回: " + result);
                return result;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Object response) {
                String result = (String) response;
                JSONObject json = JSON.parseObject(result);
                if (json.getInteger("r") == 0) {
                    /*
                    {"r":0,
                      msg": "\u767b\u5f55\u6210\u529f"
                     }
                     */
                    Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, json.getString("msg"), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void loginZhihuWithSimpleCookie(View view) {
        YzHttp.getInstance().cookieSimple().get().url("https://www.zhihu.com/#signin").build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                String html = response.body().string();
                Document parse = Jsoup.parse(html);
                Elements select = parse.select("input[type=hidden]");
                Element element = select.get(0);
                String xsrf = element.attr("value");
                Log.i(TAG, "_xsrf: " + xsrf);
                return xsrf;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Object response) {
                String xsrf = (String) response;
                if (!TextUtils.isEmpty(xsrf)) {
                    loginZhihu(xsrf);
                }
            }
        });
    }

    public void loginZhihuWithPersistentCookie(View view) {
        YzHttp.getInstance().cookiePersistent(getApplicationContext()).get().url("https://www.zhihu.com/#signin").build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                String html = response.body().string();
                Document parse = Jsoup.parse(html);
                Elements select = parse.select("input[type=hidden]");
                Element element = select.get(0);
                String xsrf = element.attr("value");
                Log.i(TAG, "_xsrf: " + xsrf);
                return xsrf;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Object response) {
                String xsrf = (String) response;
                if (!TextUtils.isEmpty(xsrf)) {
                    loginZhihu(xsrf);
                }
            }
        });
    }

    public void getZhihuTimeline(View view) {
        YzHttp.getInstance().cookiePersistent(getApplicationContext()).get()/*.url("http://api.yezhou.me/zhihu.html")*/.url("https://www.zhihu.com/").build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                String html = response.body().string();
                //Log.i(TAG, "知乎主页时间线内容: " + html);
                Document parse = Jsoup.parse(html);
                Elements select = parse.select("h2[class=feed-title]");
                if (select != null && select.size() > 0) {
                    for (int i = 0; i < select.size(); i++) {
                        Element element = select.get(i);
                        Element a = element.getElementsByTag("a").first();
                        Log.i(TAG, "标题: " + a.text() + ", 链接: " + a.attr("href"));
                    }
                    return select.size();
                }
                return 0;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Object response) {
                int count = (int) response;
                Toast.makeText(MainActivity.this, "返回数量: " + count, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getCookies(View view) {
        List<Cookie> simpleCookies = YzHttp.getInstance().getSimpleCookies("https://www.zhihu.com/");
        if (simpleCookies != null && simpleCookies.size() > 0) {
            for (int i=0; i<simpleCookies.size(); i++) {
                Cookie simpleCookie = simpleCookies.get(i);
                Log.i(TAG, "SimpleCookie: " + simpleCookie.toString());
            }
        }
        List<Cookie> persistentCookies = YzHttp.getInstance().getPersistentCookies("https://www.zhihu.com/");
        if (persistentCookies != null && persistentCookies.size() > 0) {
            for (int i=0; i<persistentCookies.size(); i++) {
                Cookie persistentCookie = persistentCookies.get(i);
                Log.i(TAG, "PersistentCookie: " + persistentCookie.toString());
            }
        }
    }

    public void clearCookies(View view) {
        YzHttp.getInstance().clearCookies("https://www.zhihu.com/");
    }

    public void https(View view) {
        YzHttp.getInstance().get().url("https://kyfw.12306.cn/otn/").build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                return null;
            }

            @Override
            public void onError(Call call, Exception e) {
                //javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.
                e.printStackTrace();
            }

            @Override
            public void onResponse(Object response) {

            }
        });
    }
    public void httpsCert(View view) {
        //keytool -printcert -rfc -file 12306.cer
        String CER_12306 = "-----BEGIN CERTIFICATE-----\n" +
                "MIICsTCCAhqgAwIBAgIIODtw6bZEH1kwDQYJKoZIhvcNAQEFBQAwRzELMAkGA1UEBhMCQ04xKTAn\n" +
                "BgNVBAoTIFNpbm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMB4X\n" +
                "DTE0MDUyNjAxNDQzNloXDTE5MDUyNTAxNDQzNlowazELMAkGA1UEBhMCQ04xKTAnBgNVBAoTIFNp\n" +
                "bm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MRkwFwYDVQQLHhCUwY3vW6JiN2cNUqFOLV/D\n" +
                "MRYwFAYDVQQDEw1reWZ3LjEyMzA2LmNuMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8Cxlz\n" +
                "+V/4KkUk8YTxVxzii7xp2gZPWuuVBiwQ6iwL98it75WNGiYCUasDXy3O8wY+PtZFvgEKkpHqQ1U6\n" +
                "uemiHStthUS1xTBsU/TuXF6AHc+oduP6zCGKcUnHRAksRb8BGSgzBA/X3B9CUKnYa9YA2EBIYccr\n" +
                "zIh6aRAjDHbvYQIDAQABo4GBMH8wHwYDVR0jBBgwFoAUeV62d7fiUoND7cdRiExjhSwAQ1gwEQYJ\n" +
                "YIZIAYb4QgEBBAQDAgbAMAsGA1UdDwQEAwIC/DAdBgNVHQ4EFgQUj/0m74jhq993ItPCldNHYLJ8\n" +
                "84MwHQYDVR0lBBYwFAYIKwYBBQUHAwIGCCsGAQUFBwMBMA0GCSqGSIb3DQEBBQUAA4GBAEXeoTkv\n" +
                "UVSeQzAxFIvqfC5jvBuApczonn+Zici+50Jcu17JjqZ0zEjn4HsNHm56n8iEbmOcf13fBil0aj4A\n" +
                "Qz9hGbjmvQSufaB6//LM1jVe/OSVAKB4C9NUdY5PNs7HDzdLfkQjjDehCADa1DH+TP3879N5zFoW\n" +
                "DgejQ5iFsAh0\n" +
                "-----END CERTIFICATE-----";
        /*
        try {
            YzHttp.getInstance().setCertificates(getAssets().open("12306.cer")).get().url("https://kyfw.12306.cn/otn/").build().enqueue(new Callback() {
                @Override
                public Object onNetworkResponse(Response response) throws Exception {
                    Log.i(TAG, "HTTPS请求返回: " + response.body().string());
                    return null;
                }

                @Override
                public void onError(Call call, Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Object response) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        YzHttp.getInstance().setCertificates(new Buffer().writeUtf8(CER_12306).inputStream()).get().url("https://kyfw.12306.cn/otn/").build().enqueue(new Callback() {
            @Override
            public Object onNetworkResponse(Response response) throws Exception {
                Log.i(TAG, "HTTPS请求返回: " + response.body().string());
                return null;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Object response) {

            }
        });
    }

    public void httpsCustom(View view) {
        try {
            //不带签名证书访问报错: javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.
            //如果带上签名证书报错: javax.net.ssl.SSLPeerUnverifiedException: Hostname api.yezhou.me not verified，则调用 https()
            YzHttp.getInstance().https().setCertificates(getAssets().open("yezhou.cer")).get().url("https://api.yezhou.me/api/user").addParam("id", "1").build().enqueue(new Callback() {
                @Override
                public Object onNetworkResponse(Response response) throws Exception {
                    ResponseBody body = response.body();
                    if (body != null) {
                        JSONObject json = JSON.parseObject(body.string());
                        if (json.getInteger("code") == 0) {
                            return JSON.parseObject(json.getString("data"), User.class);
                        }
                        body.close();
                    }
                    return null;
                }

                @Override
                public void onError(Call call, Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Object response) {
                    User user = (User) response;
                    if (user != null) {
                        Toast.makeText(MainActivity.this, user.getName(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void httpsBilateral(View view) {
        try {
            //不带签名证书访问报错: javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.
            //如果带上签名证书报错: javax.net.ssl.SSLPeerUnverifiedException: Hostname api.yezhou.me not verified，则调用 https()
            YzHttp.getInstance()
                .https().setCertificates(new InputStream[]{getAssets().open("server_public.cer")}, getAssets().open("client.bks"), "123456")
                .get().url("https://api.yezhou.me/api/user").addParam("id", "1")
                .build().enqueue(new Callback() {
                    @Override
                    public Object onNetworkResponse(Response response) throws Exception {
                        ResponseBody body = response.body();
                        if (body != null) {
                            JSONObject json = JSON.parseObject(body.string());
                            if (json.getInteger("code") == 0) {
                                return JSON.parseObject(json.getString("data"), User.class);
                            }
                            body.close();
                        }
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Object response) {
                        User user = (User) response;
                        if (user != null) {
                            Toast.makeText(MainActivity.this, user.getName(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Dexter.withActivity(this)
                .withPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        for (PermissionGrantedResponse grantedResponse : report.getGrantedPermissionResponses()) {
                            Log.i(TAG, "以获取权限: " + grantedResponse.getPermissionName());
                        }
                        for (PermissionDeniedResponse deniedResponse : report.getDeniedPermissionResponses()) {
                            Log.i(TAG, "权限被拒绝: " + deniedResponse.getPermissionName());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
                .check();
        }
    }

}
