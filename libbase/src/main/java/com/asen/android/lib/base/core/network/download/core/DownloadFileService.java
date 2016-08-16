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
 * �ļ����ط���
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownloadFileService implements IDownloadFileService {

    /**
     * ���ļ��ڵ�Ĵ�С��ÿ���߳�һ���Է���������������ֵ
     */
    public static final int SINGLE_NODE_BYTE_SIZE = 1024 * 1024;

    /**
     * �ϵ����֧��30����ļ����أ��������ʱ�����������
     */
    public static final long REDOWNLOAD_TIME_INTERVAL = 30 * 28800000; // 30��

    /**
     * ����ʧ�ܵ���� ʧ�ܴ������ɹ�ʱ��������
     */
    public static final int CONN_ERROR_MAX_NUMBER = 10;

    /**
     * �����ļ���������
     */
    public static final String DOWNLOAD_CONTEXT_FILENAME = "download.context";

    /**
     * ���ػ����ļ���׺
     */
    public static final String DOWNLOAD_TMP_SUFFIX = ".tmp~";

    /**
     * �����ļ��Ķϵ���Ϣ �ļ���׺
     */
    public static final String DOWNLOAD_BREAKPOINT_SUFFIX = ".config";

    /**
     * ��ʼ����
     */
    public static final int STATUS_START = 0x01;

    /**
     * ����״̬
     */
    public static final int STATUS_STOP = 0x02;

    /**
     * ���״̬
     */
    public static final int STATUS_FINISH = 0x03;

    /**
     * ���ص�״̬
     */
    private int downStatus = STATUS_STOP;

    /**
     * ʧ�ܴ���
     */
    private int failureNumber = 0;

    // ��ȡʧ�ܴ���
    synchronized int getFailureNumber() {
        return failureNumber;
    }

    // ����ʧ�ܴ���
    synchronized void setFailureNumber(int failureNumber) {
        this.failureNumber = failureNumber;
    }

    /**
     * ��������״̬
     *
     * @param downStatus ����״̬
     */
    synchronized void setDownStatus(int downStatus) {
        this.downStatus = downStatus;
    }

    /**
     * ��ȡ����״̬
     *
     * @return ����״̬
     */
    public synchronized int getDownStatus() {
        return downStatus;
    }

    /**
     * ���صĽ�����Ϣ
     */
    private DownProgressInfo mDownProgressInfo;

    /**
     * ���صĻ���������Ϣ
     */
    private DownConfigInfo mDownConfig;

    /**
     * ���ص�ַ
     */
    private String mUrlStr;

    /**
     * ���ر�����ļ���λ��
     */
    private File mSaveFolder;

    private DownloadContextXml downloadContextXml;

//    /**
//     * ���صĽ�����Ϣ
//     *
//     * @return ������Ϣ
//     */
//    DownProgressInfo getDownProgressInfo() {
//        return mDownProgressInfo;
//    }

    /**
     * ��ȡ������Ϣ
     *
     * @return ������Ϣ
     */
    public DownConfigInfo getDownConfig() {
        return mDownConfig;
    }

    /**
     * ���صĵ�ַ
     *
     * @return ���صĵ�ַ
     */
    public String getUrlStr() {
        return mUrlStr;
    }

    /**
     * ���ؼ����ӿ�
     */
    private OnDownloadFileListener mOnDownloadFileListener;

    /**
     * ��ȡ���ؼ����ӿ�
     *
     * @return ���ؼ����ӿ�
     */
    public OnDownloadFileListener getOnDownloadFileListener() {
        return mOnDownloadFileListener;
    }

    /**
     * ���캯��
     *
     * @param urlStr     ���ص�ַ
     * @param saveFolder ���ر�����ļ���λ��
     * @param listener   ���ؼ����ӿ�
     */
    public DownloadFileService(String urlStr, File saveFolder, OnDownloadFileListener listener) {
        this.mUrlStr = urlStr;
        this.mSaveFolder = saveFolder;
        this.mOnDownloadFileListener = listener;
        mDownConfig = new DownConfigInfo();
    }

    DownFileCalculateThread calculateThread; // �����ٶȵȵ��߳�

    /**
     * ��ʼ���أ��ô�����������һ��Ҫ�ŵ���UI�߳���
     */
    @Override
    public void startDownload() {
        if (!mSaveFolder.exists()) {
            if (!mSaveFolder.mkdirs()) { // �ļ�����ʧ���쳣
                if (mOnDownloadFileListener != null)
                    mOnDownloadFileListener.error(IDownErrorCode.FILE_CREATE_ERROR, new DownloadFileException("SaveFolder [" + mSaveFolder.getPath() + "] is create error!!!"));
                return;
            }
        }
        if (getDownStatus() != STATUS_STOP) { // ����Ѿ������� �� �Ѿ���ɵģ��׳��쳣
            if (mOnDownloadFileListener != null) {
                mOnDownloadFileListener.error(IDownErrorCode.DOWNLOAD_IS_STARTED, new DownloadFileException("DownLoad is started or finish!!!")); // ���������쳣
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

            // �������ع����߳�
            DownFileManagerThread downFileManagerThread = new DownFileManagerThread(this, mDownProgressInfo, mDownConfig);
            downFileManagerThread.start();
            // �������㱣������̣߳��˴��ڵ�ǰ�߳���ִ�У�
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

    // ��ʼ����������Ϣ
    private void initContext(DownFileInfo downFileInfo, URL url) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        // ��ȡ���ص���������Ϣ
        SaveContext context = downloadContextXml.getDownloadContext(mUrlStr);

        List<DownProgressItem> downProgressItems = null;
        long downloadLength = 0; // ���ص��ܴ�С
        String featid;
        if (context == null) { // �µ��ļ�
            featid = AppUtil.getUUid();
            context = new SaveContext(mUrlStr, featid, new Date().getTime(), downFileInfo.getFileLength(), 0, mDownConfig.isOriginal(), mDownConfig.getThreadNumber());
        } else {
            featid = context.getFeatid();
            long time = new Date().getTime();
            if (downFileInfo.getFileLength() != context.getFileSize() || time - context.getTime() > REDOWNLOAD_TIME_INTERVAL) { // �ļ���С��һ�»��߳�ʱ��δ��������
                downloadContextXml.deleteDownConfig(featid);
                context = new SaveContext(mUrlStr, featid, time, downFileInfo.getFileLength(), 0, mDownConfig.isOriginal(), mDownConfig.getThreadNumber());
            } else {
                SaveProgress saveProgress = downloadContextXml.queryDownConfig(featid);
                downloadLength = saveProgress.getDownloadLength();
                downProgressItems = saveProgress.getProgressInfoList();
                if (downloadLength == 0 || downProgressItems.size() == 0) {  // ���ع����� ��֤�϶����ӽ��������أ���������
                    context.setCurrentLength(0);
                    downProgressItems = null;
                }
                mDownConfig.setOriginal(context.isOriginal());
                mDownConfig.setThreadNumber(context.getThreadNumber());
                context.setTime(time);
            }
        }
        // ������������Ϣ
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
     * �������أ��ϵ��¼����ɾ��
     */
    @Override
    public void stopDownload() {
        isStop = true;
        setDownStatus(STATUS_STOP); // ��ֹ
    }

    /**
     * ��ͣ���أ��ϵ��¼���ᱻɾ��
     */
    @Override
    public void pauseDownload() {
        isStop = false;
        setDownStatus(STATUS_STOP); // �ж�
    }

}
