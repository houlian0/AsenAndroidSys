package com.asen.android.lib.base.core.network.download.core;

import com.asen.android.lib.base.core.network.download.IDownloadFileService;
import com.asen.android.lib.base.core.network.download.OnDownloadFileListener;
import com.asen.android.lib.base.core.network.download.bean.DownConfigInfo;
import com.asen.android.lib.base.core.network.download.bean.DownProgressInfo;
import com.asen.android.lib.base.core.network.download.bean.DownProgressItem;
import com.asen.android.lib.base.core.network.download.bean.SaveContext;
import com.asen.android.lib.base.core.network.download.bean.SaveProgress;
import com.asen.android.lib.base.core.network.download.exception.DownloadFileException;
import com.asen.android.lib.base.core.network.download.exception.IDownErrorCode;
import com.asen.android.lib.base.core.network.urlconn.DownloadUtil;
import com.asen.android.lib.base.core.network.urlconn.bean.DownFileInfo;
import com.asen.android.lib.base.tool.util.AppUtil;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * 文件下载服务
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownloadFileService implements IDownloadFileService {

    /**
     * 单文件节点的大小，每个线程一次性分配下载任务的最大值
     */
    public static final int SINGLE_NODE_BYTE_SIZE = 1024 * 1024;

    /**
     * 断点最多支持30天的文件下载，超过这个时间会重新下载
     */
    public static final long REDOWNLOAD_TIME_INTERVAL = 30 * 28800000; // 30天

    /**
     * 连接失败的最大 失败次数，成功时次数清零
     */
    public static final int CONN_ERROR_MAX_NUMBER = 10;

    /**
     * 下载文件的上下文
     */
    public static final String DOWNLOAD_CONTEXT_FILENAME = "download.context";

    /**
     * 下载缓存文件后缀
     */
    public static final String DOWNLOAD_TMP_SUFFIX = ".tmp~";

    /**
     * 下载文件的断点信息 文件后缀
     */
    public static final String DOWNLOAD_BREAKPOINT_SUFFIX = ".config";

    /**
     * 开始下载
     */
    public static final int STATUS_START = 0x01;

    /**
     * 结束状态
     */
    public static final int STATUS_STOP = 0x02;

    /**
     * 完成状态
     */
    public static final int STATUS_FINISH = 0x03;

    /**
     * 下载的状态
     */
    private int downStatus = STATUS_STOP;

    /**
     * 失败次数
     */
    private int failureNumber = 0;

    // 获取失败次数
    synchronized int getFailureNumber() {
        return failureNumber;
    }

    // 设置失败次数
    synchronized void setFailureNumber(int failureNumber) {
        this.failureNumber = failureNumber;
    }

    /**
     * 设置下载状态
     *
     * @param downStatus 下载状态
     */
    synchronized void setDownStatus(int downStatus) {
        this.downStatus = downStatus;
    }

    /**
     * 获取下载状态
     *
     * @return 下载状态
     */
    public synchronized int getDownStatus() {
        return downStatus;
    }

    /**
     * 下载的进度信息
     */
    private DownProgressInfo mDownProgressInfo;

    /**
     * 下载的基本配置信息
     */
    private DownConfigInfo mDownConfig;

    /**
     * 下载地址
     */
    private String mUrlStr;

    /**
     * 下载保存的文件夹位置
     */
    private File mSaveFolder;

    private DownloadContextXml downloadContextXml;

//    /**
//     * 下载的进度信息
//     *
//     * @return 进度信息
//     */
//    DownProgressInfo getDownProgressInfo() {
//        return mDownProgressInfo;
//    }

    /**
     * 获取配置信息
     *
     * @return 配置信息
     */
    public DownConfigInfo getDownConfig() {
        return mDownConfig;
    }

    /**
     * 下载的地址
     *
     * @return 下载的地址
     */
    public String getUrlStr() {
        return mUrlStr;
    }

    /**
     * 下载监听接口
     */
    private OnDownloadFileListener mOnDownloadFileListener;

    /**
     * 获取下载监听接口
     *
     * @return 下载监听接口
     */
    public OnDownloadFileListener getOnDownloadFileListener() {
        return mOnDownloadFileListener;
    }

    /**
     * 构造函数
     *
     * @param urlStr     下载地址
     * @param saveFolder 下载保存的文件夹位置
     * @param listener   下载监听接口
     */
    public DownloadFileService(String urlStr, File saveFolder, OnDownloadFileListener listener) {
        this.mUrlStr = urlStr;
        this.mSaveFolder = saveFolder;
        this.mOnDownloadFileListener = listener;
        mDownConfig = new DownConfigInfo();
    }

    DownFileCalculateThread calculateThread; // 计算速度等的线程

    /**
     * 开始下载，该处有网络请求，一定要放到非UI线程里
     */
    @Override
    public void startDownload() {
        if (!mSaveFolder.exists()) {
            if (!mSaveFolder.mkdirs()) { // 文件创建失败异常
                if (mOnDownloadFileListener != null)
                    mOnDownloadFileListener.error(IDownErrorCode.FILE_CREATE_ERROR, new DownloadFileException("SaveFolder [" + mSaveFolder.getPath() + "] is create error!!!"));
                return;
            }
        }
        if (getDownStatus() != STATUS_STOP) { // 如果已经在下载 或 已经完成的，抛出异常
            if (mOnDownloadFileListener != null) {
                mOnDownloadFileListener.error(IDownErrorCode.DOWNLOAD_IS_STARTED, new DownloadFileException("DownLoad is started or finish!!!")); // 正在下载异常
            }
            return;
        }

        downloadContextXml = new DownloadContextXml(mSaveFolder);
        isStop = false;
        setDownStatus(STATUS_START);

        HttpURLConnection conn = null;
        try {
            URL url = new URL(mUrlStr);
            HttpURLConnection.setFollowRedirects(true);
            conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(
                    "Accept",
                    "image/gif, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Referer", mUrlStr);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();

            DownFileInfo downFileInfo = DownloadUtil.getFileInfoByConn(conn);
            if (downFileInfo.getFileLength() == 0) {
                throw new DownloadFileException("File length is zero!!!");
            }

            initContext(downFileInfo, url);

            // 启动下载管理线程
            DownFileManagerThread downFileManagerThread = new DownFileManagerThread(this, mDownProgressInfo, mDownConfig);
            downFileManagerThread.start();
            // 启动计算保存管理线程（此处在当前线程内执行）
            calculateThread = new DownFileCalculateThread(this, mDownProgressInfo, mDownConfig, downloadContextXml);
            calculateThread.run();

        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            if (mOnDownloadFileListener != null) {
                mOnDownloadFileListener.error(IDownErrorCode.NETWORK_CONN_ERROR, e);
            }
            setDownStatus(STATUS_STOP);
        } catch (DownloadFileException e) {
            if (mOnDownloadFileListener != null) {
                mOnDownloadFileListener.error(IDownErrorCode.FILE_LENGTH_ZERO, e);
            }
            setDownStatus(STATUS_STOP);
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    // 初始化上下文信息
    private void initContext(DownFileInfo downFileInfo, URL url) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        // 获取下载的上下文信息
        SaveContext context = downloadContextXml.getDownloadContext(mUrlStr);

        List<DownProgressItem> downProgressItems = null;
        long downloadLength = 0; // 下载的总大小
        String featid;
        if (context == null) { // 新的文件
            featid = AppUtil.getUUid();
            context = new SaveContext(mUrlStr, featid, new Date().getTime(), downFileInfo.getFileLength(), 0, mDownConfig.isOriginal(), mDownConfig.getThreadNumber());
        } else {
            featid = context.getFeatid();
            long time = new Date().getTime();
            if (downFileInfo.getFileLength() != context.getFileSize() || time - context.getTime() > REDOWNLOAD_TIME_INTERVAL) { // 文件大小不一致或者长时间未继续下载
                downloadContextXml.deleteDownConfig(featid);
                context = new SaveContext(mUrlStr, featid, time, downFileInfo.getFileLength(), 0, mDownConfig.isOriginal(), mDownConfig.getThreadNumber());
            } else {
                SaveProgress saveProgress = downloadContextXml.queryDownConfig(featid);
                downloadLength = saveProgress.getDownloadLength();
                downProgressItems = saveProgress.getProgressInfoList();
                if (downloadLength == 0 || downProgressItems.size() == 0) {  // 下载过程中 保证肯定有子进程在下载，否则重下
                    context.setCurrentLength(0);
                    downProgressItems = null;
                }
                mDownConfig.setOriginal(context.isOriginal());
                mDownConfig.setThreadNumber(context.getThreadNumber());
                context.setTime(time);
            }
        }
        // 保存上下文信息
        downloadContextXml.setDownloadContext(context);

        File tmpFile = new File(mSaveFolder, featid + DOWNLOAD_TMP_SUFFIX);
        mDownProgressInfo = new DownProgressInfo(url, downFileInfo, tmpFile);
        mDownProgressInfo.setCurrentLength(context.getCurrentLength());
        mDownProgressInfo.setDownloadLength(downloadLength);

        if (downProgressItems != null) {
            mDownProgressInfo.getProgressItems().addAll(downProgressItems);
        }
    }

    private boolean isStop = false;

    synchronized boolean isStop() {
        return isStop;
    }

    /**
     * 结束下载，断点记录将被删除
     */
    @Override
    public void stopDownload() {
        isStop = true;
        setDownStatus(STATUS_STOP); // 终止
    }

    /**
     * 暂停下载，断点记录不会被删除
     */
    @Override
    public void pauseDownload() {
        isStop = false;
        setDownStatus(STATUS_STOP); // 中断
    }

}
