package com.asen.android.lib.base.core.network.task;

import android.os.Handler;

import com.asen.android.lib.base.core.network.SenThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * ��ʱ������
 *
 * @param <Params>    ִ�в�����һ��ʱ������Լ�ȡֵ��
 * @param <Progress>  ��;������Ϣ�� ���̴߳���
 * @param <Performer> ÿ�ε�ִ�н��
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public abstract class SenTimingTask<Params, Progress, Performer> {

    private static final int POOL_SIZE_TASK = 6;

    private final ExecutorService pool = new SenThreadPool(POOL_SIZE_TASK).getPool();

    private static final int HANDLER_PROGRESS = 0x1001;

    private static final int HANDLER_EXECUTE = 0x1002; // ִ��Handler��ǩ

    private static final int HANDLER_FINISH_OK = 0x1003;

    private static final int HANDLER_FINISH_ERROR = 0x1004;

    private static final int HANDLER_ERROR_MESSAGE = 0x1005; // ������Ϣ

    private final Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_PROGRESS: {
                    Progress[] progress = (Progress[]) msg.obj;
                    onProgressUpdate(progress);
                    break;
                }
                case HANDLER_EXECUTE: {
                    Performer performer = null;
                    if (isProcessingDataInUiThread()) {
                        try {
                            performer = processingData(mParams);
                        } catch (Exception e) {
                            e.printStackTrace();
                            resultErrorMessage(e.getMessage());
                        }
                    }
                    submit = pool.submit(new TaskWithResult(performer));
                    mHandler.sendEmptyMessageDelayed(HANDLER_EXECUTE, runnableTimeInterval());
                    break;
                }
                case HANDLER_FINISH_OK: {
                    Performer result = (Performer) msg.obj;
                    onResultOK(result);
                    break;
                }
                case HANDLER_FINISH_ERROR: {
                    Performer result = (Performer) msg.obj;
                    onResultError(result);
                    break;
                }
                case HANDLER_ERROR_MESSAGE: {
                    String result = (String) msg.obj;
                    onErrorMessage(result);
                    break;
                }
                default:
                    break;
            }
        }

    };

    /**
     * �״�ִ�ж�ʱ����Ļ���ʱ����
     *
     * @return long ʱ�����ֵ
     */
    protected abstract long startTimeInterval();

    /**
     * ���״��⣬����ÿ��ִ�ж�ʱ�����ʱ����
     *
     * @return long ʱ�����ֵ
     */
    protected abstract long runnableTimeInterval();

    /**
     * �Ƿ���UI�߳���ִ�д������ݵķ���{@link SenTimingTask#processingData(Object[])}��Ĭ�Ϸ�UI�߳���ִ��
     *
     * @return boolean true�������߳���ִ�����ݴ�������false���ڷ����߳���ִ�����ݴ�����
     */
    protected boolean isProcessingDataInUiThread() {
        return false;
    }

    /**
     * �������ݣ�����ִ���ߣ�������{@link SenTimingTask#isProcessingDataInUiThread()}�����ж��Ƿ������߳���ִ�д˷���
     *
     * @param params ����
     * @return �������������Ϣ
     */
    protected abstract Performer processingData(Params... params);

    /**
     * ִ���̣߳����ڷ�UI�߳���ִ�У�
     *
     * @param params ִ���ߣ������������ݣ�
     * @throws RuntimeException
     */
    protected abstract void doInBackground(Performer params) throws RuntimeException;

    /**
     * ����ִ�����UI�߳��еĲ�����ִ�д˷���
     *
     * @param success �ɹ��Ľ��-ִ����
     */
    protected void onResultOK(Performer success) {

    }

    /**
     * ������ִ�����UI�߳��еĲ�����ִ�д˷���
     *
     * @param error ʧ�ܵĽ��-ִ����
     */
    protected void onResultError(Performer error) {

    }

    /**
     * ��ִ̨�б���ʱ�����������Ϣ
     *
     * @param message ���쳣���õ���message��Ϣ
     */
    protected void onErrorMessage(String message) {

    }

    /**
     * �����߳���ִ�в���
     *
     * @param progresses ͨ��{@link SenTimingTask#publishProgress(Object[])}�������ݵĲ���
     */
    protected void onProgressUpdate(Progress... progresses) {

    }

    /**
     * �����߳��д��ݲ�����Ϣ
     *
     * @param progresses ������Ϣ
     */
    public void publishProgress(Progress... progresses) {
        mHandler.obtainMessage(HANDLER_PROGRESS, progresses).sendToTarget();
    }

    private void resultOK(Performer result) {
        mHandler.obtainMessage(HANDLER_FINISH_OK, result).sendToTarget();
    }

    private void resultError(Performer message) {
        mHandler.obtainMessage(HANDLER_FINISH_ERROR, message).sendToTarget();
    }

    private void resultErrorMessage(String message) {
        mHandler.obtainMessage(HANDLER_ERROR_MESSAGE, message).sendToTarget();
    }


    private Params[] mParams = null;

    private boolean isStarted = false;

    /**
     * �Ƿ��ѿ�ʼִ�ж�ʱ����
     *
     * @return true����ʱ�����Ѿ���ʼִ�У�false����ʱ����δ��ʼִ��
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * ��ʼ��ʱ����
     *
     * @param params ִ��ʱ���ݵĲ���
     */
    public void start(Params... params) {
        isStarted = true;
        mParams = params;
        mHandler.sendEmptyMessageDelayed(HANDLER_EXECUTE, startTimeInterval());
    }

    /**
     * ������ʱ����
     */
    public void stop() {
        isStarted = false;
        mHandler.removeMessages(HANDLER_EXECUTE);
        cancel();
    }

    class TaskWithResult implements Callable<Boolean> {

        private Performer performer;

        TaskWithResult(Performer performer) {
            this.performer = performer;
        }

        @Override
        public Boolean call() throws Exception {
            try {
                if (!isProcessingDataInUiThread()) {
                    performer = processingData(mParams);
                }
                doInBackground(performer);
                resultOK(performer);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                resultErrorMessage(e.getMessage());
                resultError(performer);
            }
            return false;
        }
    }

    private Future<Boolean> submit = null;

    private void cancel() {
        if (submit != null && !submit.isCancelled()) {
            try {
                submit.cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}