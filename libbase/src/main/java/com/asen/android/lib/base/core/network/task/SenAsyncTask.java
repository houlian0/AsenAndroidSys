package com.asen.android.lib.base.core.network.task;

import android.os.Handler;

import com.asen.android.lib.base.core.network.SenThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 异步任务类 - 采用Handler的方式，仿照AsyncTask写的异步任务类
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
     * 在后台执行操作
     *
     * @param params 通过{@link SenAsyncTask#execute(Object[])} 方法传递的参数
     * @return 返回定义的结果
     * @throws RuntimeException
     */
    protected abstract Result doInBackground(Params... params) throws RuntimeException;

    /**
     * 在主线程中执行操作
     *
     * @param values 通过{@link SenAsyncTask#publishProgress(Object[])}方法传递的参数
     */
    protected void onProgressUpdate(Progress... values) {
    }

    /**
     * 向主线程中传递参数信息
     *
     * @param values 参数信息
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
     * 发送错误异常的信息
     *
     * @param msg 需要传递到{@link SenAsyncTask#onResultError(String)}方法中作为参数的信息
     * @throws RuntimeException
     */
    protected void errorMessge(String msg) throws RuntimeException {
        throw new RuntimeException(msg);
    }

    /**
     * 异步任务类执行前，调用此方法（该方法在主线程中执行）
     */
    protected void onPreExecute() {

    }

    /**
     * 后台执行完成后，处理结果信息
     *
     * @param result {@link SenAsyncTask#doInBackground(Object[])} 返回的结果信息
     */
    protected abstract void onResultOK(Result result);

    /**
     * 后台执行报错时，处理错误信息
     *
     * @param msg 从异常中拿到的message信息
     */
    protected abstract void onResultError(String msg);

    /**
     * 开始执行异步任务类
     *
     * @param params 参数
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
     * 结束执行异步任务类
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
