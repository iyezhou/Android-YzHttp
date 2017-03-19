package me.yezhou.yzhttp.intercept;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
public class WriteFileResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private BufferedSource bufferedSource;
    private FileOutputStream fos;
    private String filePath;

    public WriteFileResponseBody(ResponseBody responseBody, String filePath) {
        this.responseBody = responseBody;
        this.filePath = filePath;
        try {
            fos = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long len = super.read(sink, byteCount);
                //Log.i("yezhou", "WriteFileResponseBody: len=" + len + ", byteCount=" + byteCount);
                if (len != -1) {
                    sink.copyTo(fos, 0, len);
                } else {
                    fos.close();
                }
                return len;
            }
        };
    }

}
