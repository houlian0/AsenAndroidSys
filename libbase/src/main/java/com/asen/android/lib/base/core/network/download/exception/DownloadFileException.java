package com.asen.android.lib.base.core.network.download.exception;

/**
 * 文件下载异常
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownloadFileException extends RuntimeException {

    public DownloadFileException() {
        super();
    }

    public DownloadFileException(String message) {
        super(message);
    }

    public DownloadFileException(Throwable throwable) {
        super(throwable);
    }

    public DownloadFileException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
