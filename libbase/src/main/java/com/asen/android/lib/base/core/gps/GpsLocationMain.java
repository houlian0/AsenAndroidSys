package com.asen.android.lib.base.core.gps;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.Nullable;

import com.asen.android.lib.base.core.gps.bean.GpsPoint;
import com.asen.android.lib.base.core.gps.bean.LocationInfo;
import com.asen.android.lib.base.core.gps.bean.MapPoint;
import com.asen.android.lib.base.core.gps.bean.TmpGpsInfo;
import com.asen.android.lib.base.core.gps.extension.ExtensionASingleLocation;
import com.asen.android.lib.base.core.gps.extension.ExtensionContinuousLocation;
import com.asen.android.lib.base.core.gps.extension.IExtensionLocation;
import com.asen.android.lib.base.core.gps.geocode.GeocodeReverse;
import com.asen.android.lib.base.core.gps.geocode.tdt.TianDTGeocodeReverse;
import com.asen.android.lib.base.core.gps.listener.OnAddressChangedListener;
import com.asen.android.lib.base.core.gps.listener.OnLocationChangedListener;
import com.asen.android.lib.base.core.gps.listener.OnSatelliteChangedListener;
import com.asen.android.lib.base.core.util.IMaxStack;
import com.asen.android.lib.base.core.util.SenMaxListStack;
import com.asen.android.lib.base.tool.util.AppUtil;
import com.asen.android.lib.base.tool.util.LogUtil;
import com.asen.android.lib.base.tool.util.Version;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * GPS定位实现类（纯Android原生）
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class GpsLocationMain extends GpsLocation implements IMaxStack.IGoodCompareListener<TmpGpsInfo> {

    private static final String TAG = GpsLocation.class.getSimpleName();

    private static final long MIN_TIME = 500; // 更新时间间隔

    private static final int MIN_DISTANCE = 0; // 更新最小距离间隔

    private static final long STAY_TIME_INTERVAL = 60 * 1000; // 停留时间间隔

//    public static final int EXTENSION_BUFFER_DISTANCE = 10; // 第三方定位缓冲距离，超过这个距离与两点精度的和，则更新点位

//    public static final int EXTENSION_BUFFER_TIME = 60 * 1000; // 第三方定位缓冲时间，超过这个时间，则更新点位

    private static final int MAX_STACK_SIZE = 5; // 判断最优点的集合的数据量上限

    private Context mContext;

    private MapPoint mMapPoint = null;

    private GpsPoint mGpsPoint = null;

    private boolean isNotFirst = false;

    private LocationManager locationManager;

    private List<OnLocationChangedListener> mOnLocationChangedListeners = null;

    private OnSatelliteChangedListener mOnSatelliteChangedListener = null;

    private List<OnAddressChangedListener> mOnAddressChangedListeners = null;

    private SenMaxListStack<TmpGpsInfo> senMaxListStack;

    private GeocodeReverse mGeocodeReverse;

    private GpsLocationTimingTask mTimingTask;

    private boolean isStarted = false;

    private IExtensionLocation mExtensionLocation; // 扩展性定位

    GpsLocationMain(Context context) {
        mContext = context;
        mOnLocationChangedListeners = new ArrayList<>();
        mOnAddressChangedListeners = new ArrayList<>();
        senMaxListStack = new SenMaxListStack<>(MAX_STACK_SIZE);
        setGeocodeReverse(new TianDTGeocodeReverse()); // 设置天地图的逆地理编码
        mTimingTask = new GpsLocationTimingTask(this);
    }

    @Override
    public boolean hasGpsPoint() {
        return mGpsPoint != null;
    }

    @Override
    public boolean isNotFirst() {
        return isNotFirst;
    }

    @Override
    public MapPoint getMapPoint() {
        return mMapPoint;
    }

    @Override
    public GpsPoint getGpsPoint() {
        return mGpsPoint;
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }

    @Override
    public LocationInfo getLocationInfo() {
        return mGeocodeReverse == null ? null : mGeocodeReverse.getLocationInfo();
    }

    public void setExtensionLocation(ExtensionASingleLocation extensionLocation) {
        extensionLocation.setGpsLocation(this);
        mExtensionLocation = extensionLocation;
    }

    public void setExtensionLocation(ExtensionContinuousLocation extensionLocation) {
        extensionLocation.setGpsLocation(this);
        mExtensionLocation = extensionLocation;
    }

    @Override
    IExtensionLocation getExtensionLocation() {
        return mExtensionLocation;
    }

    @Override
    public void addOnLocationChangedListener(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListeners.add(onLocationChangedListener);
    }

    @Override
    public void removeOnLocationChangedListener(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListeners.remove(onLocationChangedListener);
    }

    @Override
    public void addOnAddressChangedListener(OnAddressChangedListener onAddressChangedListener) {
        mOnAddressChangedListeners.add(onAddressChangedListener);
    }

    @Override
    public void removeOnAddressChangedListener(OnAddressChangedListener onAddressChangedListener) {
        mOnAddressChangedListeners.remove(onAddressChangedListener);
    }

    @Override
    public void setOnSatelliteChangedListener(OnSatelliteChangedListener onSatelliteChangedListener) {
        mOnSatelliteChangedListener = onSatelliteChangedListener;
    }

    LocationManager getLocationManager() {
        return locationManager;
    }

    private ICoordinateTransform mICoordinateTransform = null;

    private GpsLocationListener mGpsLocationListener = null;

    private GpsLocationListener mNetworkLocationListener = null;

    private GpsStatusListener mGpsStatusListener = null;

    @Override
    public boolean start() {
        return start(null);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public boolean start(@Nullable ICoordinateTransform coordinateTransform) {
        if (isStarted()) return true;
        isStarted = true;
        mICoordinateTransform = coordinateTransform;

        if (Version.hasMarshmallow() && mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogUtil.e(TAG, "no Manifest.permission.ACCESS_FINE_LOCATION !!!!");
            return false;
        }

        try {
            if (mExtensionLocation != null && mExtensionLocation instanceof ExtensionContinuousLocation) {
                try {
                    mExtensionLocation.start(); // 开启第三方定位
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (locationManager == null) {
                locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

                mGpsStatusListener = new GpsStatusListener(this);
                locationManager.addGpsStatusListener(mGpsStatusListener);// 侦听GPS状态

                Location location = null;
                if (AppUtil.hasGPSDevice(mContext)) { // 设备存在GPS定位的硬件
                    mGpsLocationListener = new GpsLocationListener(this, GpsInfoType.TYPE_GPS);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, mGpsLocationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if (AppUtil.hasNetworkDevice(mContext)) {
                    mNetworkLocationListener = new GpsLocationListener(this, GpsInfoType.TYPE_NETWORK);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, mNetworkLocationListener);
                    if (location == null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    } else {
                        Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null && location.getTime() < loc.getTime()) {
                            location = loc;
                        }
                    }
                }

                refreshLocation(GpsInfoType.TYPE_FIRST, location);
            }

            mTimingTask.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public boolean stop() {
        if (!isStarted()) return true;

        mTimingTask.stop();

        if (mExtensionLocation != null && mExtensionLocation instanceof ExtensionContinuousLocation) {
            mExtensionLocation.stop(); // 结束第三方定位
        }
        mExtensionLocation = null;

        if (Version.hasMarshmallow() && mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        try {
            if (locationManager != null) {
                if (mGpsLocationListener != null) {
                    locationManager.removeUpdates(mGpsLocationListener);
                    mGpsLocationListener = null;
                }
                if (mNetworkLocationListener != null) {
                    locationManager.removeUpdates(mNetworkLocationListener);
                    mNetworkLocationListener = null;
                }
                if (mGpsStatusListener != null) {
                    locationManager.removeGpsStatusListener(mGpsStatusListener);
                    mGpsStatusListener = null;
                }
                locationManager = null;
            }

            isStarted = false;

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setGeocodeReverse(GeocodeReverse geocodeReverse) {
        mGeocodeReverse = geocodeReverse;
        mGeocodeReverse.setAddressChangedListeners(mOnAddressChangedListeners);
    }

    public void refreshLocation(GpsInfoType type, Location location) {
        if (GpsInfoType.TYPE_FIRST == type) { // 此处为第一个获得的GPS点位（上回记录的GPS点）
            TmpGpsInfo tmpGpsInfo = location(type, location);
            valueToPointInfo(tmpGpsInfo);
            sendLocationListener();
        } else {
            boolean flag = !isNotFirst;
            isNotFirst = true; // 设为非首次定位状态
            if (!hasGpsPoint() || flag) { // 如果没有获得过GPS位置，或者位置为首次定位的点，则重新定位
                TmpGpsInfo tmpGpsInfo = location(type, location);
                add2MaxListStack(tmpGpsInfo);
                valueToPointInfo(tmpGpsInfo);
                sendLocationListener();
            } else {
                TmpGpsInfo tmpGpsInfo = location(type, location);
                add2MaxListStack(tmpGpsInfo); // 将点位信息加到集合中，并计算与上次点位的距离
                tmpGpsInfo = senMaxListStack.getGood(this); // 获取最优点位
                valueToPointInfo(tmpGpsInfo);
                sendLocationListener();
            }
        }
    }

    // 刷新定位位置信息
    public void refreshLocationInfo(double lon, double lat, LocationInfo locationInfo) {
        if (mGeocodeReverse != null) mGeocodeReverse.refreshLocationInfo(lon, lat, locationInfo);
    }

    // 赋值到正式的定位信息上
    private void valueToPointInfo(TmpGpsInfo tmpGpsInfo) {
        if (tmpGpsInfo == null) return;
        mMapPoint = tmpGpsInfo.getMapPoint();
        mGpsPoint = tmpGpsInfo.getGpsPoint();
    }

    @Override
    public TmpGpsInfo getGood(List<TmpGpsInfo> dataList) { // 跳点处理关键代码
        int size = dataList.size();

        if (size == 0) {
            return null;
        } else if (size == 1) {
            return dataList.get(0);
        } else if (size == 2) { // 只有两条有效数据，第一条数据为初始点
            return dataList.get(size - 1);
        } else {
            // 获取一组点中 最有效的点位
            TmpGpsInfo tmpGpsInfo = null;
            double minDistance = -1;

            synchronized (this) {
                for (TmpGpsInfo info1 : dataList) {
                    double distance = 0;
                    GpsPoint gpsPoint1 = info1.getGpsPoint();
                    for (TmpGpsInfo info2 : dataList) {
                        if (info1 == info2) continue;
                        GpsPoint gpsPoint2 = info2.getGpsPoint();
                        distance += distance(gpsPoint1.getLongitude(), gpsPoint1.getLatitude(), gpsPoint2.getLongitude(), gpsPoint2.getLatitude());
                    }
                    if (minDistance == -1) {
                        minDistance = distance;
                        tmpGpsInfo = info1;
                    } else if (distance < minDistance) {
                        minDistance = distance;
                        tmpGpsInfo = info1;
                    }
                }
            }

            if (tmpGpsInfo == null) return null;
            GpsPoint tmpPoint = tmpGpsInfo.getGpsPoint();
            if ((distance(mGpsPoint.getLongitude(), mGpsPoint.getLatitude(), tmpPoint.getLongitude(), tmpPoint.getLatitude()) > tmpPoint.getAccuracy() * 2)) { // accuracy < mGpsPoint.getAccuracy()
                return tmpGpsInfo;
            } else { // 不需要刷新点位
                mGpsPoint.setSpeed(tmpPoint.getTime() - mGpsPoint.getTime() > STAY_TIME_INTERVAL ? 0 : tmpPoint.getSpeed());
                return null;
            }
        }
    }

    // 将最新点位加到集合中
    private void add2MaxListStack(TmpGpsInfo tmpGpsInfo) {
        if (tmpGpsInfo != null) {
            senMaxListStack.push(tmpGpsInfo);
        }
    }

    // 计算经纬度之间的距离
    private double distance(double lon1, double lat1, double lon2, double lat2) { // 6371004.0
        return 6378137.000 * Math.acos(1 - (Math.pow((Math.sin((90 - lat1) * Math.PI / 180) * Math.cos(lon1 * Math.PI / 180) - Math.sin((90 - lat2) * Math.PI / 180) * Math.cos(lon2 * Math.PI / 180)), 2) + Math.pow((Math.sin((90 - lat1) * Math.PI / 180) * Math.sin(lon1 * Math.PI / 180) - Math.sin((90 - lat2) * Math.PI / 180) * Math.sin(lon2 * Math.PI / 180)), 2) + Math.pow((Math.cos((90 - lat1) * Math.PI / 180) - Math.cos((90 - lat2) * Math.PI / 180)), 2)) / 2);
    }

    private TmpGpsInfo location(GpsInfoType type, Location location) {
        if (location == null) return null;

        double longitude = location.getLongitude(); // 去掉过小的点位
        double latitude = location.getLatitude();
        if (Math.abs(longitude) < 0.1 && Math.abs(latitude) < 0.1) {
            return null;
        }

        GpsPoint gpsPoint = new GpsPoint();
        gpsPoint.setAccuracy(location.getAccuracy());
        gpsPoint.setAltitude(location.getAltitude());
        gpsPoint.setBearing(location.getBearing());
        gpsPoint.setLatitude(location.getLatitude());
        gpsPoint.setLongitude(location.getLongitude());
        gpsPoint.setSpeed(location.getSpeed());
        gpsPoint.setTime(location.getTime());
        gpsPoint.setGpsInfoType(type); // 是否为GPS点位

        MapPoint mapPoint = mICoordinateTransform != null ? mICoordinateTransform.gpsPoint2MapPoint(gpsPoint) : new MapPoint(location.getLongitude(), location.getLatitude(), location.getAltitude());

        try {
            if (mGeocodeReverse != null) mGeocodeReverse.reverseGeocode(gpsPoint);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new TmpGpsInfo(gpsPoint, mapPoint);
    }

    // 发送处理定位监听
    void sendLocationListener() {
        if (mGpsPoint.getSpeed() != 0 && new Date().getTime() - mGpsPoint.getTime() > STAY_TIME_INTERVAL) {
            mGpsPoint.setSpeed(0);
        }
        for (OnLocationChangedListener listener : mOnLocationChangedListeners) {
            listener.locationChanged(mGpsPoint, mMapPoint);
        }
    }

    void refreshGpsStatus(int gpsStatus, List<GpsSatellite> gpsSatellites) {
        if (mOnSatelliteChangedListener != null)
            mOnSatelliteChangedListener.satelliteChanged(gpsStatus, gpsSatellites);
    }

}