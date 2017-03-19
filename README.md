# Android-YzHttp
基于OkHttp3的HTTP请求框架

## 请求方法

- 支持GET/POST/PUT/DELETE等请求方式
- POST支持内容
- - Form表单：multipart/form-data
- - 字符串：application/x-www-form-urlencoded、application/json、text/xml、text/plain、text/html
- - 字节数组(Raw)
- - 文件

## 过程监测

- 支持上传文件和下载文件的过程状态监测
- 支持拦截器，内置文件上传下载拦截器，也支持自定义拦截器

## Cookie

- 支持简单内存Cookie
- 支持磁盘序列化Cookie
- 支持自定义Cookie
- 提供登录知乎及获取知乎主页时间线的Cookie操作实例

## HTTPS

- 支持自定义KeyStore和TrustStore
- 支持自签名证书单向和双向验证

## 扩展

- 支持自定义Callback
- 支持自定义拦截器
- 支持自定义Cookie

## 使用

```
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
```

```
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
```

![image](https://github.com/iyezhou/Android-YzHttp/blob/master/YzHttp.jpg)
