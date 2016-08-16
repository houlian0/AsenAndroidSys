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
 * �ٶȵȼ����뱣����Ϣ���߳�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
class DownFileCalculateThread extends Thread {

    private DownloadFileService mFileService; // ���ص���������

    private final DownProgressInfo mProgressInfo; // ���صĽ�����Ϣ

    private DownConfigInfo mConfigInfo; // ���ص�������Ϣ

    private OnDownloadFileListener onDownloadFileListener; // ���صļ����ӿ�

    private DownloadContextXml downloadContextXml; // ���ص�������Ϣ��д����

    private String featid; // Ψһ����

    private SenThreadPool pool = null; // JAVA���̳߳أ����ڱ���xml

    /**
     * ���캯��
     *
     * @param service            ���ط���
     * @param progressInfo       �ܽ�����Ϣ
     * @param configInfo         ����������Ϣ
     * @param downloadContextXml ���ص�������Ϣ��д����
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

            if (count == 5) { // һ��ʱ�䱣��һ����Ϣ
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

        if (mFileService.getDownStatus() == DownloadFileService.STATUS_FINISH) { // �������
            // �������յ��ļ���
            if (onDownloadFileListener != null) {
                File lastFile; // ���յ��ļ���
                boolean isRenameSuccess; // �����Ƿ�ɹ�
                synchronized (mProgressInfo) {
                    File folder = mProgressInfo.getTmpFile().getParentFile();
                    if (mConfigInfo.isOriginal()) { // ��Ϊԭ�ļ���
                        lastFile = new File(folder, mProgressInfo.getFileInfo().getFileName());
                        isRenameSuccess = FileUtil.rename(mProgressInfo.getTmpFile(), lastFile);
                    } else { // ����ԭ�ļ���
                        File saveFile = mProgressInfo.getTmpFile();
                        lastFile = new File(folder, saveFile.getName().substring(0, saveFile.getName().lastIndexOf(".") + 1) + mProgressInfo.getFileInfo().getFileSuffix());
                        isRenameSuccess = FileUtil.rename(saveFile, lastFile);
                    }
                }

                if (lastFile.exists()) { // ��������ļ�����
                    int i = 0;
                    String fileName = lastFile.getName().substring(0, lastFile.getName().lastIndexOf("."));
                    while (!isRenameSuccess) { // ���Ҹ���ʧ��
                        i++;
                        lastFile = new File(mProgressInfo.getTmpFile().getParentFile(), fileName + "(" + i + ")." + mProgressInfo.getFileInfo().getFileSuffix());
                        isRenameSuccess = FileUtil.rename(mProgressInfo.getTmpFile(), lastFile);
                    }
                }

                onDownloadFileListener.success(isRenameSuccess ? lastFile : mProgressInfo.getTmpFile());
            }
            // ɾ���ϵ���Ϣ
            try {
                deleteMessage();
            } catch (TransformerException | IOException e) {
                if (onDownloadFileListener != null) {
                    onDownloadFileListener.error(IErrorCode.FILE_CONFIG_ERROR, e);
                }
            }
        } else if (mFileService.getDownStatus() == DownloadFileService.STATUS_STOP && mFileService.isStop()) { // ͨ������stopDownload����ʱִ��
            // ɾ���ϵ���Ϣ
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
                saveMessage(); // ����ϵ���Ϣ
            } catch (SAXException | TransformerException | IOException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    // ����ϵ����Ϣ
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
        // ��ȡ���ص���������Ϣ
        SaveContext context = downloadContextXml.getDownloadContext(mFileService.getUrlStr());
        context.setCurrentLength(currentLength);
        // ������������Ϣ
        downloadContextXml.setDownloadContext(context);
    }

    private void updateConfig(List<DownProgressItem> progressItems, long downloadLength) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        downloadContextXml.createOrUpdateDownConfig(featid, downloadLength, progressItems);
    }

    // ɾ���ϵ���Ϣ
    protected void deleteMessage() throws TransformerException, IOException {
        synchronized (mProgressInfo) {
            downloadContextXml.removeContextElement(mFileService.getUrlStr());
            downloadContextXml.deleteDownConfig(featid);
        }
    }

}
