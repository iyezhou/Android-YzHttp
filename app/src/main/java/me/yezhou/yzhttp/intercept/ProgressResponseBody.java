package me.yezhou.yzhttp.intercept;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by yezhou on 2017/3/16.
 */
public class ProgressResponseBody extends ResponseBody {

    //实际的待包装响应体
    private ResponseBody responseBody;
    //进度回调接口
    private ProgressListener progressListener;
    //包装完成的BufferedSource
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener listener) {
        this.responseBody = responseBody;
        this.progressListener = listener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null)
            bufferedSource = Okio.buffer(getSource(responseBody.source()));
        return bufferedSource;
    }

    private Source getSource(Source source) {
        return new ForwardingSource(source) {
            long contentLength = contentLength();
            long readLength = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long len = super.read(sink, byteCount);
                //Log.i("yezhou", "ProgressResponseBody: len=" + len + ", byteCount=" + byteCount);
                if (len != -1) {
                    readLength += len;
                    float progress = readLength * 1.0f / contentLength * 100;
                    progressListener.inProgress(progress);
                } else {
                    progressListener.onDone(contentLength);
                }
                return len;
            }
        };
    }

    public interface ProgressListener {
        void inProgress(float progress);
        void onDone(long totalLength);
    }

}
