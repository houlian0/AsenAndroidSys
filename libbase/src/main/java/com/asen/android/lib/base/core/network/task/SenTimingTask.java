package com.asen.android.lib.base.core.network.task;

import android.os.Handler;

import com.asen.android.lib.base.core.network.SenThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * Simple to Introduction
 * 定时任务类
 *
 * @param <Params>    执行参数（一段时间从中自己取值）
 * @param <Progress>  中途发送信息到 主线程处理
 * @param <Performer> 每次的执行结果
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public abstract class SenTimingTask<Params, Progress, Performer> {

    private static final int POOL_SIZE_TASK = 6;

    private final ExecutorService pool = new SenThreadPool(POOL_SIZE_TASK).getPool();

    private static final int HANDLER_PROGRESS = 0x1001;

    private static final int HANDLER_EXECUTE = 0x1002; // 执行Handler标签

    private static final int HANDLER_FINISH_OK = 0x1003;

    private static final int HANDLER_FINISH_ERROR = 0x1004;

    private static final int HANDLER_ERROR_MESSAGE = 0x1005; // 错误信息

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
     * 首次执行的缓冲时间间隔
     *
     * @return long
     */
    protected abstract long startTimeInterval();

    /**
     * 每次执行线程的时间间隔
     *
     * @return long
     */
    protected abstract long runnableTimeInterval();

    /**
     * 是否在UI线程中执行处理数据的方法，默认非UI线程内执行
     *
     * @return boolean
     */
    protected boolean isProcessingDataInUiThread() {
        return false;
    }

    /**
     * 处理数据（生成执行者）
     *
     * @param params 参数
     * @return 计算后的结果
     */
    protected abstract Performer processingData(Params... params);

    /**
     * 执行线程（必在非UI线程中执行）
     *
     * @param params 执行者
     * @throws RuntimeException
     */
    protected abstract void doInBackground(Performer params) throws RuntimeException;

    /**
     * 返回正确结果
     *
     * @param success 成功的结果
     * @Description: 返回正确结果
     */
    protected void onResultOK(Performer success) {

    }

    /**
     * 返回失败结果
     *
     * @param error 失败的结果
     * @Description: 从异常中拿值
     */
    protected void onResultError(Performer error) {

    }

    protected void onProgressUpdate(Progress... progresses) {

    }

    /**
     * 从异常中拿值
     *
     * @param message 异常中拿值
     */
    protected void onErrorMessage(String message) {

    }

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
     * 是否已开始执行
     *
     * @return 是否开始执行
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * 开始定时任务
     *
     * @param params 执行参数
     */
    public void start(Params... params) {
        isStarted = true;
        mParams = params;
        mHandler.sendEmptyMessageDelayed(HANDLER_EXECUTE, startTimeInterval());
    }

    /**
     * 结束定时任务
     */
    public void stop() {
        isStarted = false;
        mHandler.removeMessages(HANDLER_EXECUTE);
        cancel();
    }

    class TaskWithResult implements Callable<Boolean> {

        private Performer performer;

        public TaskWithResult(Performer performer) {
            this.performer = performer;
        }

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