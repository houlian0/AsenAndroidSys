package com.asen.android.lib.base.core.network.download.core;

import com.asen.android.lib.base.core.network.SenThreadPool;
import com.asen.android.lib.base.core.network.download.OnDownloadFileListener;
import com.asen.android.lib.base.core.network.download.bean.DownConfigInfo;
import com.asen.android.lib.base.core.network.download.bean.DownProgressInfo;
import com.asen.android.lib.base.core.network.download.bean.DownProgressItem;
import com.asen.android.lib.base.core.network.download.bean.SaveContext;
import com.asen.android.lib.base.core.network.download.exception.IErrorCode;
import com.asen.android.lib.base.tool.util.FileUtil;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * 速度等计算与保存信息的线程
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
class DownFileCalculateThread extends Thread {

    private DownloadFileService mFileService; // 下载的主功能类

    private final DownProgressInfo mProgressInfo; // 下载的进度信息

    private DownConfigInfo mConfigInfo; // 下载的配置信息

    private OnDownloadFileListener onDownloadFileListener; // 下载的监听接口

    private DownloadContextXml downloadContextXml; // 下载的配置信息读写操作

    private String featid; // 唯一编码

    private SenThreadPool pool = null; // JAVA的线程池，用于保存xml

    /**
     * 构造函数
     *
     * @param service            下载服务
     * @param progressInfo       总进度信息
     * @param configInfo         下载配置信息
     * @param downloadContextXml 下载的配置信息读写操作
     */
    DownFileCalculateThread(DownloadFileService service, DownProgressInfo progressInfo, DownConfigInfo configInfo, DownloadContextXml downloadContextXml) {
        mFileService = service;
        mProgressInfo = progressInfo;
        mConfigInfo = configInfo;
        onDownloadFileListener = mFileService.getOnDownloadFileListener();
        this.downloadContextXml = downloadContextXml;
        pool = new SenThreadPool(1);

        String name = mProgressInfo.getTmpFile().getName();
        featid = name.substring(0, name.lastIndexOf("."));
    }

    @Override
    public void run() {
        long prevLength;
        synchronized (mProgressInfo) {
            prevLength = mProgressInfo.getDownloadLength();
        }

        int count = 0;

        if (onDownloadFileListener != null) {
            long downloadLength = mProgressInfo.getDownloadLength();
            long fileLength = mProgressInfo.getFileInfo().getFileLength();
            onDownloadFileListener.progress(downloadLength, fileLength, downloadLength * 100.0f / fileLength, 0);
            prevLength = downloadLength;
        }

        while (mFileService.getDownStatus() == DownloadFileService.STATUS_START) {
            long prevTime = new Date().getTime();

            if (count == 5) { // 一段时间保存一次信息
                pool.execute(saveRunnable);
                count = 0;
            }

            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (onDownloadFileListener != null && mFileService.getDownStatus() == DownloadFileService.STATUS_START) {
                long downloadLength = mProgressInfo.getDownloadLength();
                long fileLength = mProgressInfo.getFileInfo().getFileLength();
                long time = new Date().getTime() - prevTime;
                onDownloadFileListener.progress(downloadLength, fileLength, downloadLength * 100.0f / fileLength, (downloadLength - prevLength) * 1f / time * 1000);
                prevLength = downloadLength;
            }
        }

        pool.getPool().shutdown();

        if (mFileService.getDownStatus() == DownloadFileService.STATUS_FINISH) { // 下载完成
            // 返回最终的文件名
            if (onDownloadFileListener != null) {
                File lastFile; // 最终的文件名
                boolean isRenameSuccess; // 改名是否成功
                synchronized (mProgressInfo) {
                    File folder = mProgressInfo.getTmpFile().getParentFile();
                    if (mConfigInfo.isOriginal()) { // 改为原文件名
                        lastFile = new File(folder, mProgressInfo.getFileInfo().getFileName());
                        isRenameSuccess = FileUtil.rename(mProgressInfo.getTmpFile(), lastFile);
                    } else { // 不用原文件名
                        File saveFile = mProgressInfo.getTmpFile();
                        lastFile = new File(folder, saveFile.getName().substring(0, saveFile.getName().lastIndexOf(".") + 1) + mProgressInfo.getFileInfo().getFileSuffix());
                        isRenameSuccess = FileUtil.rename(saveFile, lastFile);
                    }
                }

                if (lastFile.exists()) { // 如果最终文件存在
                    int i = 0;
                    String fileName = lastFile.getName().substring(0, lastFile.getName().lastIndexOf("."));
                    while (!isRenameSuccess) { // 并且改名失败
                        i++;
                        lastFile = new File(mProgressInfo.getTmpFile().getParentFile(), fileName + "(" + i + ")." + mProgressInfo.getFileInfo().getFileSuffix());
                        isRenameSuccess = FileUtil.rename(mProgressInfo.getTmpFile(), lastFile);
                    }
                }

                onDownloadFileListener.success(isRenameSuccess ? lastFile : mProgressInfo.getTmpFile());
            }
            // 删除断点信息
            try {
                deleteMessage();
            } catch (TransformerException | IOException e) {
                if (onDownloadFileListener != null) {
                    onDownloadFileListener.error(IErrorCode.FILE_CONFIG_ERROR, e);
                }
            }
        } else if (mFileService.getDownStatus() == DownloadFileService.STATUS_STOP && mFileService.isStop()) { // 通过调用stopDownload方法时执行
            // 删除断点信息
            try {
                deleteMessage();
            } catch (TransformerException | IOException e) {
                if (onDownloadFileListener != null) {
                    onDownloadFileListener.error(IErrorCode.FILE_CONFIG_ERROR, e);
                }
            }
        }

    }

    private SaveRunnable saveRunnable = new SaveRunnable();

    class SaveRunnable implements Runnable {
        @Override
        public void run() {
            try {
                saveMessage(); // 保存断点信息
            } catch (SAXException | TransformerException | IOException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    // 保存断点等信息
    protected void saveMessage() throws SAXException, TransformerException, ParserConfigurationException, IOException {
        long currentLength;
        long downloadLength;
        List<DownProgressItem> progressItems = new ArrayList<>();
        synchronized (mProgressInfo) {
            currentLength = mProgressInfo.getCurrentLength();
            downloadLength = mProgressInfo.getDownloadLength();
            List<DownProgressItem> items = mProgressInfo.getProgressItems();
            for (DownProgressItem item : items) {
                progressItems.add(new DownProgressItem(item.getStartSeek(), item.getEndSeek(), item.getDownSize()));
            }
        }
        updateContext(currentLength);
        updateConfig(progressItems, downloadLength);
    }

    private void updateContext(long currentLength) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        // 获取下载的上下文信息
        SaveContext context = downloadContextXml.getDownloadContext(mFileService.getUrlStr());
        context.setCurrentLength(currentLength);
        // 保存上下文信息
        downloadContextXml.setDownloadContext(context);
    }

    private void updateConfig(List<DownProgressItem> progressItems, long downloadLength) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        downloadContextXml.createOrUpdateDownConfig(featid, downloadLength, progressItems);
    }

    // 删除断点信息
    protected void deleteMessage() throws TransformerException, IOException {
        synchronized (mProgressInfo) {
            downloadContextXml.removeContextElement(mFileService.getUrlStr());
            downloadContextXml.deleteDownConfig(featid);
        }
    }

}
