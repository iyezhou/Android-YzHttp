package me.yezhou.yzhttp.callback;

/**
 * Created by yezhou on 2017/3/16.
 */
public abstract class ProgressCallback {

    /**
     * UI线程
     * @param progress
     */
    public abstract void inProgress(float progress);

    /**
     * UI线程
     * @param totalSize
     */
    public void onDone(long totalSize) {

    }

}
