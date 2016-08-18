package com.asen.android.lib.base.core.gps;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;

import com.asen.android.lib.base.tool.util.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * GPS 状态监听
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
class GpsStatusListener implements GpsStatus.Listener {

    private static final String TAG = GpsStatusListener.class.getSimpleName();

    private GpsLocationMain mGpsLocation;

    private List<GpsSatellite> mGpsSatellites;

    GpsStatusListener(GpsLocationMain gpsLocation) {
        mGpsLocation = gpsLocation;
        mGpsSatellites = new ArrayList<>();
    }

    @Override
    public void onGpsStatusChanged(int event) {
        LocationManager locationManager = mGpsLocation.getLocationManager();
        if (locationManager == null) return;

        GpsStatus gpsStatus = locationManager.getGpsStatus(null);
        switch (event) {
            case GpsStatus.GPS_EVENT_FIRST_FIX: {
                int i = gpsStatus.getTimeToFirstFix();
                LogUtil.d(TAG, "GPS 第一次修正用时  " + i);
                mGpsLocation.refreshGpsStatus(event, null);
                break;
            }
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS: {// 周期的报告卫星状态
                // 得到所有收到的卫星的信息，包括 卫星的高度角、方位角、信噪比、和伪随机号（及卫星编号）
                Iterable<GpsSatellite> allSatellites = gpsStatus.getSatellites();
                Iterator<GpsSatellite> iterator = allSatellites.iterator();

                mGpsSatellites.clear();
                while (iterator.hasNext()) {
                    mGpsSatellites.add(iterator.next());
                }

                LogUtil.d(TAG, "总共搜索到" + mGpsSatellites.size() + "颗卫星");
                mGpsLocation.refreshGpsStatus(event, mGpsSatellites);

                // 通过遍历重新整理为ArrayList
//                if (GpsLocation.DEBUG) {
//                    // 输出卫星信息
//                    for (int i = 0; i < mGpsSatellites.size(); i++) {
//                        GpsSatellite satellite = mGpsSatellites.get(i);
//                        // 卫星的方位角，浮点型数据
//                        mGpsLocation.logD("卫星的方位角" + satellite.getAzimuth());
//                        // 卫星的高度，浮点型数据
//                        mGpsLocation.logD("卫星的高度" + satellite.getElevation());
//                        // 卫星的伪随机噪声码，整形数据
//                        mGpsLocation.logD("卫星的伪随机噪声码" + satellite.getPrn());
//                        // 卫星的信噪比，浮点型数据
//                        mGpsLocation.logD("卫星的信噪比" + satellite.getSnr());
//                        // 卫星是否有年历表，布尔型数据
//                        mGpsLocation.logD("卫星是否有年历表" + satellite.hasAlmanac());
//                        // 卫星是否有星历表，布尔型数据
//                        mGpsLocation.logD("卫星是否有星历表" + satellite.hasEphemeris());
//                        // 卫星是否被用于近期的GPS修正计算
//                        mGpsLocation.logD("卫星是否被用于近期的GPS修正计算" + satellite.hasAlmanac());
//                        // 分隔符
//                        mGpsLocation.logD("*******************************");
//                    }
//                }
                break;
            }
            case GpsStatus.GPS_EVENT_STARTED: {
                LogUtil.d(TAG, "定位启动");
                mGpsLocation.refreshGpsStatus(event, null);
                break;
            }
            case GpsStatus.GPS_EVENT_STOPPED: {
                LogUtil.d(TAG, "定位结束");
                mGpsLocation.refreshGpsStatus(event, null);
                break;
            }
            default:
                break;
        }


    }

}