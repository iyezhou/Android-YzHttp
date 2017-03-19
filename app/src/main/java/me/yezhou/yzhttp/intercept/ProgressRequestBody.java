package me.yezhou.yzhttp.intercept;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by yezhou on 2017/3/14.
 */
public class ProgressRequestBody extends RequestBody {

    //实际的待包装请求体
    private final RequestBody requestBody;
    //进度回调接口
    private final RequestProgressListener progressListener;
    //包装完成的BufferedSink
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody, RequestProgressListener progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(getSink(sink));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    private Sink getSink(Sink sink) throws IOException {
        return new ForwardingSink(sink) {
            long contentLength = contentLength();
            long writeLength = 0;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                //Log.i("yezhou", "ProgressRequestBody: writeLength=" + writeLength + ", byteCount=" + byteCount);
                if (writeLength < contentLength) {
                    writeLength += byteCount;
                    float progress = writeLength * 1.0f / contentLength * 100;
                    progressListener.onProgress(progress);
                } else {
                    progressListener.onDone(contentLength);
                }
            }
        };
    }

    public interface RequestProgressListener {
        void onProgress(float progress);
        void onDone(long totalLength);
    }
}
