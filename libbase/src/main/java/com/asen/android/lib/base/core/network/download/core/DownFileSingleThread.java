package com.asen.android.lib.base.core.network.download.core;

import com.asen.android.lib.base.core.network.download.bean.DownProgressInfo;
import com.asen.android.lib.base.core.network.download.bean.DownProgressItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件下载线程
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
class DownFileSingleThread extends Thread {

    private static final int BUFFER_SIZE = 16 * 1024; // 缓冲区大小

    private final DownProgressInfo mProgressInfo; // 下载的总进度信息

    private DownProgressItem mProgressItem; // 当前线程下载的进度信息

    private DownloadFileService downloadFileService; // 下载的主服务类

    /**
     * 构造函数
     *
     * @param service      下载服务
     * @param progressInfo 总进度信息
     * @param progressItem 当前线程的进度信息
     */
    DownFileSingleThread(DownloadFileService service, DownProgressInfo progressInfo, DownProgressItem progressItem) {
        downloadFileService = service;
        mProgressInfo = progressInfo;
        mProgressItem = progressItem;
        mProgressItem.setRunning(true);
    }

    @Override
    public void run() {
        super.run();
        HttpURLConnection conn = null;
        InputStream inputStream = null;

        try {
            conn = (HttpURLConnection) mProgressInfo.getUrl().openConnection();

            conn.setConnectTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(
                    "Accept",
                    "image/gif, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Referer", downloadFileService.getUrlStr());
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Range", "bytes=" + (mProgressItem.getStartSeek() + mProgressItem.getDownSize()) + "-" + mProgressItem.getEndSeek());// 设置获取实体数据的范围
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();

            byte[] buffer = new byte[BUFFER_SIZE];
            inputStream = conn.getInputStream();

            downloadFileService.setFailureNumber(0); // 连接成功，将失败次数设为0

            int offset;
            while (downloadFileService.getDownStatus() == DownloadFileService.STATUS_START && (offset = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                synchronized (mProgressInfo) { // 设置文件下载大小
                    if (offset == 0) continue;
                    MappedByteBuffer map = mProgressInfo.getFileOutputStream().getChannel().map(FileChannel.MapMode.READ_WRITE, mProgressItem.getStartSeek() + mProgressItem.getDownSize(), offset);
                    map.put(ByteBuffer.wrap(buffer, 0, offset));
                    mProgressInfo.setDownloadLength(mProgressInfo.getDownloadLength() + offset);
                    mProgressItem.setDownSize(mProgressItem.getDownSize() + offset);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            downloadFileService.setFailureNumber(downloadFileService.getFailureNumber() + 1); // 增加失败次数
        } finally {
            try {
//                    mProgressInfo.getFileOutputStream().flush(); // 将缓存的信息写入文件
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (mProgressInfo) {
                if (conn != null) conn.disconnect();
                mProgressItem.setRunning(false);
            }
        }
    }

}
