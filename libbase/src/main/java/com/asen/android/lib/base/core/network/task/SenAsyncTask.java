package com.asen.android.lib.base.core.network.task;

import android.os.Handler;

import com.asen.android.lib.base.core.network.SenThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * �첽������ - ����Handler�ķ�ʽ������AsyncTaskд���첽������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public abstract class SenAsyncTask<Params, Progress, Result> {

    private static final int POOL_SIZE_TASK = 12;

    private static final ExecutorService pool = new SenThreadPool(POOL_SIZE_TASK).getPool();

    private static final int HANDLER_PROGRESS = 0x1001;

    private static final int HANDLER_FINISH_OK = 0x1002;

    private static final int HANDLER_FINISH_ERROR = 0x1003;

    private final Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_PROGRESS:
                    Progress[] progress = (Progress[]) msg.obj;
                    onProgressUpdate(progress);
                    break;
                case HANDLER_FINISH_OK: {
                    Result result = (Result) msg.obj;
                    onResultOK(result);
                    break;
                }
                case HANDLER_FINISH_ERROR: {
                    String result = (String) msg.obj;
                    onResultError(result);
                    break;
                }
                default:
                    break;
            }
        }

    };

    /**
     * �ں�ִ̨�в���
     *
     * @param params ͨ��{@link SenAsyncTask#execute(Object[])} �������ݵĲ���
     * @return ���ض���Ľ��
     * @throws RuntimeException
     */
    protected abstract Result doInBackground(Params... params) throws RuntimeException;

    /**
     * �����߳���ִ�в���
     *
     * @param values ͨ��{@link SenAsyncTask#publishProgress(Object[])}�������ݵĲ���
     */
    protected void onProgressUpdate(Progress... values) {
    }

    /**
     * �����߳��д��ݲ�����Ϣ
     *
     * @param values ������Ϣ
     */
    protected void publishProgress(Progress... values) {
        mHandler.obtainMessage(HANDLER_PROGRESS, values).sendToTarget();
    }


    private void resultOK(Result result) {
        mHandler.obtainMessage(HANDLER_FINISH_OK, result).sendToTarget();
    }

    private void resultError(String message) {
        mHandler.obtainMessage(HANDLER_FINISH_ERROR, message).sendToTarget();
    }

    /**
     * ���ʹ����쳣����Ϣ
     *
     * @param msg ��Ҫ���ݵ�{@link SenAsyncTask#onResultError(String)}��������Ϊ��������Ϣ
     * @throws RuntimeException
     */
    protected void errorMessge(String msg) throws RuntimeException {
        throw new RuntimeException(msg);
    }

    /**
     * �첽������ִ��ǰ�����ô˷������÷��������߳���ִ�У�
     */
    protected void onPreExecute() {

    }

    /**
     * ��ִ̨����ɺ󣬴�������Ϣ
     *
     * @param result {@link SenAsyncTask#doInBackground(Object[])} ���صĽ����Ϣ
     */
    protected abstract void onResultOK(Result result);

    /**
     * ��ִ̨�б���ʱ�����������Ϣ
     *
     * @param msg ���쳣���õ���message��Ϣ
     */
    protected abstract void onResultError(String msg);

    /**
     * ��ʼִ���첽������
     *
     * @param params ����
     */
    public void execute(Params... params) {
        onPreExecute();
        submit = pool.submit(new TaskWithResult(params));
    }

    class TaskWithResult implements Callable<Result> {
        private Params[] params;

        public TaskWithResult(Params... params) {
            this.params = params;
        }

        public Result call() throws Exception {
            try {
                Result result = doInBackground(params);
                resultOK(result);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                resultError(e.getMessage());
            }
            return null;
        }
    }

    private Future<Result> submit = null;

    /**
     * ����ִ���첽������
     */
    public void cancel() {
        if (submit != null && !submit.isCancelled()) {
            try {
                submit.cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
