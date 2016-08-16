package com.asen.android.lib.base.core.network.download.core;

import com.asen.android.lib.base.core.network.SenThreadPool;
import com.asen.android.lib.base.core.network.download.OnDownloadFileListener;
import com.asen.android.lib.base.core.network.download.bean.DownConfigInfo;
import com.asen.android.lib.base.core.network.download.bean.DownProgressInfo;
import com.asen.android.lib.base.core.network.download.bean.DownProgressItem;
import com.asen.android.lib.base.core.network.download.exception.DownloadFileException;
import com.asen.android.lib.base.core.network.download.exception.IErrorCode;

import java.io.IOException;
import java.util.ListIterator;

/**
 * �����ļ������߳�
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
     * ���캯��
     *
     * @param service      ���ط���
     * @param progressInfo �ܽ�����Ϣ
     * @param configInfo   ����������Ϣ
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

        while (mFileService.getDownStatus() == DownloadFileService.STATUS_START) { // ��ʼ����
            if (mProgressInfo.getDownloadLength() >= mProgressInfo.getFileInfo().getFileLength()) { // �Ѿ��������
                downStatus = DownloadFileService.STATUS_FINISH;
                break;
            }
            if (mFileService.getFailureNumber() >= DownloadFileService.CONN_ERROR_MAX_NUMBER) { // ����ʧ�ܴ����ж�
                downStatus = DownloadFileService.STATUS_STOP; // �ر�����
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
                        if (next.getDownSize() >= next.getEndSeek() - next.getStartSeek()) { // �ý����Ѿ��������
                            iterator.remove();
                        } else {
                            if (!next.isRunning()) { // ���ĳ��δ������̶߳ϵ��ˣ���������
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

        // ȫ��������ɣ��ر��ļ���
        synchronized (mProgressInfo) {
            try {
//                mProgressInfo.getFileOutputStream().flush();
                mProgressInfo.getFileOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        senThreadPool.getPool().shutdown(); // �ر��̳߳�

        if (downStatus == DownloadFileService.STATUS_FINISH) { // ����Ѿ��������
            mFileService.setDownStatus(DownloadFileService.STATUS_FINISH); // �������
        } else if (downStatus == DownloadFileService.STATUS_STOP) { // �쳣�ж�����ʱִ��
            OnDownloadFileListener onDownloadFileListener = mFileService.getOnDownloadFileListener();
            if (onDownloadFileListener != null) {
                onDownloadFileListener.error(IErrorCode.NETWORK_CONN_ERROR, new DownloadFileException("Network conn error in center!!!"));
            }
            mFileService.setDownStatus(DownloadFileService.STATUS_STOP); // �����ж�
        }

    }

    /**
     * ���DownProgressItem
     *
     * @return �����һ��DownProgressItem�����û���򷵻�null
     */
    private DownProgressItem getNextProgressItem() {
        if (mProgressInfo.getCurrentLength() >= mProgressInfo.getFileInfo().getFileLength()) { // �Ѿ�û����Ҫ�ٿ����̵߳���
            return null;
        }

        long startSeek = mProgressInfo.getCurrentLength();
        long endSeek = startSeek + DownloadFileService.SINGLE_NODE_BYTE_SIZE;
        if (endSeek > mProgressInfo.getFileInfo().getFileLength())
            endSeek = mProgressInfo.getFileInfo().getFileLength();
        mProgressInfo.setCurrentLength(endSeek);

        return new DownProgressItem(startSeek, endSeek - 1);
    }

}
