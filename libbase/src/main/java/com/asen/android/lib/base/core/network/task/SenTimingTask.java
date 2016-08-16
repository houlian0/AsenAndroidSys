package com.asen.android.lib.base.core.network.task;

import android.os.Handler;

import com.asen.android.lib.base.core.network.SenThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * 定时任务类
 *
 * @param <Params>    执行参数（一段时间从中自己取值）
 * @param <Progress>  中途发送信息到 主线程处理
 * @param <Performer> 每次的执行结果
 * @author Asen
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
     * 首次执行定时任务的缓冲时间间隔
     *
     * @return long 时间毫秒值
     */
    protected abstract long startTimeInterval();

    /**
     * 除首次外，其余每次执行定时任务的时间间隔
     *
     * @return long 时间毫秒值
     */
    protected abstract long runnableTimeInterval();

    /**
     * 是否在UI线程中执行处理数据的方法{@link SenTimingTask#processingData(Object[])}，默认非UI线程内执行
     *
     * @return boolean true，在主线程中执行数据处理方法；false，在非主线程中执行数据处理方法
     */
    protected boolean isProcessingDataInUiThread() {
        return false;
    }

    /**
     * 处理数据（生成执行者），根据{@link SenTimingTask#isProcessingDataInUiThread()}方法判断是否在主线程中执行此方法
     *
     * @param params 参数
     * @return 处理完的数据信息
     */
    protected abstract Performer processingData(Params... params);

    /**
     * 执行线程（必在非UI线程中执行）
     *
     * @param params 执行者（处理完后的数据）
     * @throws RuntimeException
     */
    protected abstract void doInBackground(Performer params) throws RuntimeException;

    /**
     * 正常执行完非UI线程中的操作后，执行此方法
     *
     * @param success 成功的结果-执行者
     */
    protected void onResultOK(Performer success) {

    }

    /**
     * 非正常执行完非UI线程中的操作后，执行此方法
     *
     * @param error 失败的结果-执行者
     */
    protected void onResultError(Performer error) {

    }

    /**
     * 后台执行报错时，处理错误信息
     *
     * @param message 从异常中拿到的message信息
     */
    protected void onErrorMessage(String message) {

    }

    /**
     * 在主线程中执行操作
     *
     * @param progresses 通过{@link SenTimingTask#publishProgress(Object[])}方法传递的参数
     */
    protected void onProgressUpdate(Progress... progresses) {

    }

    /**
     * 向主线程中传递参数信息
     *
     * @param progresses 参数信息
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
     * 是否已开始执行定时任务
     *
     * @return true，定时任务已经开始执行；false，定时任务未开始执行
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * 开始定时任务
     *
     * @param params 执行时传递的参数
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