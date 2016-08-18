package com.asen.android.lib.base.core.network.download.core;

import com.asen.android.lib.base.core.network.SenThreadPool;
import com.asen.android.lib.base.core.network.download.OnDownloadFileListener;
import com.asen.android.lib.base.core.network.download.bean.DownConfigInfo;
import com.asen.android.lib.base.core.network.download.bean.DownProgressInfo;
import com.asen.android.lib.base.core.network.download.bean.DownProgressItem;
import com.asen.android.lib.base.core.network.download.exception.DownloadFileException;
import com.asen.android.lib.base.core.network.download.exception.IDownErrorCode;

import java.io.IOException;
import java.util.ListIterator;

/**
 * 下载文件管理线程
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
class DownFileManagerThread extends Thread {

    private SenThreadPool senThreadPool;

    private final DownProgressInfo mProgressInfo;

    private DownConfigInfo mConfigInfo;

    private DownloadFileService mFileService;

    /**
     * 构造函数
     *
     * @param service      下载服务
     * @param progressInfo 总进度信息
     * @param configInfo   下载配置信息
     */
    DownFileManagerThread(DownloadFileService service, DownProgressInfo progressInfo, DownConfigInfo configInfo) {
        mFileService = service;
        mProgressInfo = progressInfo;
        mConfigInfo = configInfo;
        senThreadPool = new SenThreadPool(mConfigInfo.getThreadNumber() + 1);
    }

    @Override
    public void run() {
        super.run();

        int downStatus = DownloadFileService.STATUS_START;

        while (mFileService.getDownStatus() == DownloadFileService.STATUS_START) { // 开始下载
            if (mProgressInfo.getDownloadLength() >= mProgressInfo.getDownFileInfo().getFileLength()) { // 已经下载完成
                downStatus = DownloadFileService.STATUS_FINISH;
                break;
            }
            if (mFileService.getFailureNumber() >= DownloadFileService.CONN_ERROR_MAX_NUMBER) { // 连接失败次数判定
                downStatus = DownloadFileService.STATUS_STOP; // 关闭下载
                break;
            }

            synchronized (mProgressInfo) {
                int number = 0;
                if (mProgressInfo.isProgressItemsEmpty()) {
                    number = 0;
                } else {
                    ListIterator<DownProgressItem> iterator = mProgressInfo.getProgressItems().listIterator();
                    while (iterator.hasNext()) {
                        DownProgressItem next = iterator.next();
                        if (next.getDownSize() >= next.getEndSeek() - next.getStartSeek()) { // 该进度已经下载完成
                            iterator.remove();
                        } else {
                            if (!next.isRunning()) { // 如果某个未下完的线程断掉了，重新连接
                                senThreadPool.execute(new DownFileSingleThread(mFileService, mProgressInfo, next));
                            }
                            number++;
                        }
                    }
                }

                int threadNumber = mConfigInfo.getThreadNumber();
                for (; number < threadNumber; number++) {
                    DownProgressItem nextProgressItem = getNextProgressItem();
                    if (nextProgressItem == null) break;
                    mProgressInfo.addDownProgressItem(nextProgressItem);
                    senThreadPool.execute(new DownFileSingleThread(mFileService, mProgressInfo, nextProgressItem));
                }
            }
        }

        // 全部下载完成，关闭文件流
        synchronized (mProgressInfo) {
            try {
//                mProgressInfo.getFileOutputStream().flush();
                mProgressInfo.getFileOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        senThreadPool.getPool().shutdown(); // 关闭线程池

        if (downStatus == DownloadFileService.STATUS_FINISH) { // 如果已经下载完成
            mFileService.setDownStatus(DownloadFileService.STATUS_FINISH); // 下载完成
        } else if (downStatus == DownloadFileService.STATUS_STOP) { // 异常中断下载时执行
            OnDownloadFileListener onDownloadFileListener = mFileService.getOnDownloadFileListener();
            if (onDownloadFileListener != null) {
                onDownloadFileListener.error(IDownErrorCode.NETWORK_CONN_ERROR, new DownloadFileException("Network conn error in center!!!"));
            }
            mFileService.setDownStatus(DownloadFileService.STATUS_STOP); // 下载中断
        }

    }

    /**
     * 获得DownProgressItem
     *
     * @return 获得下一个DownProgressItem，如果没有则返回null
     */
    private DownProgressItem getNextProgressItem() {
        if (mProgressInfo.getCurrentLength() >= mProgressInfo.getDownFileInfo().getFileLength()) { // 已经没有需要再开辟线程的了
            return null;
        }

        long startSeek = mProgressInfo.getCurrentLength();
        long endSeek = startSeek + DownloadFileService.SINGLE_NODE_BYTE_SIZE;
        if (endSeek > mProgressInfo.getDownFileInfo().getFileLength())
            endSeek = mProgressInfo.getDownFileInfo().getFileLength();
        mProgressInfo.setCurrentLength(endSeek);

        return new DownProgressItem(startSeek, endSeek - 1);
    }

}
