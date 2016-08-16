package com.asen.android.lib.base.core.network.task;

import com.asen.android.lib.base.core.network.download.OnDownloadFileListener;
import com.asen.android.lib.base.core.network.download.core.DownloadFileService;
import com.asen.android.lib.base.tool.util.FileUtil;

import java.io.File;

/**
 * ���ص�����֧���첽��
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class SenAsyncDownTask extends SenAsyncTask<Void, Object, File> implements OnDownloadFileListener {

    private DownloadFileService mDownloadFileService;

    private OnDownloadFileListener mOnDownloadFileListener;

    private File resultFile; // ����ļ�

    /**
     * ���캯��
     *
     * @param url      ���ص�ַ
     * @param folder   �ļ���
     * @param listener ���ؼ���
     */
    public SenAsyncDownTask(String url, File folder, OnDownloadFileListener listener) {
        mDownloadFileService = new DownloadFileService(url, folder, this);
        mDownloadFileService.getDownConfig().setThreadNumber(1);
        this.mOnDownloadFileListener = listener;
        FileUtil.createFolder(folder); // �����ļ���
    }

    @Override
    protected File doInBackground(Void... params) throws RuntimeException {
        mDownloadFileService.startDownload();
        return resultFile;
    }

    @Override
    protected void onResultOK(File file) {
        if (mOnDownloadFileListener != null && file != null)
            mOnDownloadFileListener.success(file);
    }

    @Override
    protected void onResultError(String msg) {
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        if (mOnDownloadFileListener != null) {
            Object value = values[0];
            if (value instanceof ProgressInfo) {
                ProgressInfo info = (ProgressInfo) value;
                mOnDownloadFileListener.progress(info.getDownloadSize(), info.getTotalSize(), info.getProgress(), info.getSpeed());
            } else if (value instanceof ErrorInfo) {
                ErrorInfo info = (ErrorInfo) value;
                mOnDownloadFileListener.error(info.getErrorCode(), info.getException());
            }
        }
    }

    @Override
    public void progress(long downloadSize, long totalSize, float progress, float speed) {
        publishProgress(new ProgressInfo(downloadSize, totalSize, progress, speed));
    }

    @Override
    public void success(File file) {
        resultFile = file;
    }

    @Override
    public void error(int errorCode, Exception e) {
        publishProgress(new ErrorInfo(errorCode, e));
    }

    /**
     * ������Ϣ
     */
    class ProgressInfo {

        private long downloadSize;
        private long totalSize;
        private float progress;
        private float speed;

        ProgressInfo(long downloadSize, long totalSize, float progress, float speed) {
            this.downloadSize = downloadSize;
            this.totalSize = totalSize;
            this.progress = progress;
            this.speed = speed;
        }

        long getDownloadSize() {
            return downloadSize;
        }

        long getTotalSize() {
            return totalSize;
        }

        float getProgress() {
            return progress;
        }

        float getSpeed() {
            return speed;
        }

    }

    /**
     * ������Ϣ
     */
    class ErrorInfo {

        private int errorCode;
        private Exception exception;

        ErrorInfo(int errorCode, Exception exception) {
            this.errorCode = errorCode;
            this.exception = exception;
        }

        int getErrorCode() {
            return errorCode;
        }

        Exception getException() {
            return exception;
        }

    }

}