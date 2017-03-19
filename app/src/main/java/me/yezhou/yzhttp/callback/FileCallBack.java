package me.yezhou.yzhttp.callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.yezhou.yzhttp.YzHttp;
import okhttp3.Response;

/**
 * Created by yezhou on 2017/3/16.
 */
public abstract class FileCallBack  extends Callback<File> {

    /**
     * 目标文件存储的文件夹路径
     */
    private String fileDir;
    /**
     * 目标文件存储的文件名
     */
    private String fileName;

    public abstract void inProgress(float progress);

    public FileCallBack(String fileDir, String fileName) {
        this.fileDir = fileDir;
        this.fileName = fileName;
    }

    @Override
    public File onNetworkResponse(Response response) throws Exception {
        return saveFile(response);
    }

    public File saveFile(Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;

            File dir = new File(fileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                fos.flush();
                final int progress = (int) (sum * 1.0f / total * 100);
                YzHttp.getInstance().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        inProgress(progress);
                    }
                });
            }
            return file;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }
}
