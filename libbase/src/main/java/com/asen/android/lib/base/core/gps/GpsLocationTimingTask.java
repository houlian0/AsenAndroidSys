package com.asen.android.lib.base.core.gps;

import android.location.Location;

import com.asen.android.lib.base.core.gps.extension.ExtensionASingleLocation;
import com.asen.android.lib.base.core.gps.extension.IExtensionLocation;
import com.asen.android.lib.base.core.network.task.SenTimingTask;

/**
 * 位置刷新的定时任务
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:18
 */
class GpsLocationTimingTask extends SenTimingTask<Void, Void, Void> {

    /**
     * 刷新GPS位置的时间间隔、扩展性定位主动定位的时间间隔
     */
    public static final long TIME_INTERVAL = 5000;

    private GpsLocationMain mGpsLocation;

    GpsLocationTimingTask(GpsLocationMain gpsLocation) {
        mGpsLocation = gpsLocation;
    }

    @Override
    protected long startTimeInterval() {
        return 200;
    }

    @Override
    protected long runnableTimeInterval() {
        return TIME_INTERVAL;
    }

    @Override
    protected Void processingData(Void... params) {
        return null;
    }

    @Override
    protected void doInBackground(Void params) throws RuntimeException {
        IExtensionLocation extensionLocation = mGpsLocation.getExtensionLocation();
        if (extensionLocation != null && extensionLocation instanceof ExtensionASingleLocation) {
            Location location = extensionLocation.getLocation();
            extensionLocation.refreshLocation(location); // 主动获取GPS定位
        }
        if (mGpsLocation.hasGpsPoint())
            publishProgress();
    }

    @Override
    protected void onProgressUpdate(Void... progresses) {
        super.onProgressUpdate(progresses);
        mGpsLocation.sendLocationListener();
    }

}
