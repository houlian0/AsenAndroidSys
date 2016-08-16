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

    public static final int EXTENSION_BUFFER_DISTANCE = 25; // 第三方定位缓冲距离，超过这个距离与两点精度的和，则更新点位

    public static final int EXTENSION_BUFFER_TIME = 60 * 1000; // 第三方定位缓冲时间，超过这个时间，则更新点位

    private static final int MAX_STACK_SIZE = 5; // 判断最优点的集合的数据量上限

    private LocationType mLocationType = LocationType.ORIGINAL_EXTENSION; // 定位状态

    private Context mContext;

    private MapPoint mMapPoint = null;

    private GpsPoint mGpsPoint = null;

//    private TmpGpsInfo tmpGpsInfo = null; // 临时定位信息

    private boolean isNotFirst = false;

    private LocationManager locationManager;

    private List<OnLocationChangedListener> mOnLocationChangedListeners = null;

    private OnSatelliteChangedListener mOnSatelliteChangedListener = null;

    private List<OnAddressChangedListener> mOnAddressChangedListeners = null;

    private GeocodeReverse mGeocodeReverse;

    private GpsLocationTimingTask mTimingTask;

    private boolean isStarted = false;

    private IExtensionLocation mExtensionLocation; // 扩展性定位

    private SenMaxListStack<TmpGpsInfo> senMaxListStack;

    private List<GpsSatellite> mGpsSatellites;

    GpsLocationMain(Context context) {
        mContext = context;
        mOnLocationChangedListeners = new ArrayList<>();
        mOnAddressChangedListeners = new ArrayList<>();
        setGeocodeReverse(new TianDTGeocodeReverse()); // 设置天地图的逆地理编码
        mTimingTask = new GpsLocationTimingTask(this);
        senMaxListStack = new SenMaxListStack<>(MAX_STACK_SIZE);
    }

    @Override
    public void setLocationType(LocationType locationType) {
        if (locationType != null) mLocationType = locationType;
    }

    /**
     * 是否获得了GPS点位
     *
     * @return
     */
    @Override
    public boolean hasGpsPoint() {
        return mGpsPoint != null;
    }

    @Override
    public boolean isNotFirst() {
        return isNotFirst;
    }

    /**
     * 获得最后一个转换后得地图上的点
     *
     * @return if mGpsPoint is null, return null
     */
    @Override
    public MapPoint getMapPoint() {
        return mMapPoint;
    }

    /**
     * 获得最后一个GPS点位
     *
     * @return
     */
    @Override
    public GpsPoint getGpsPoint() {
        return mGpsPoint;
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * 获得地址定位信息
     *
     * @return
     */
    @Override
    public LocationInfo getLocationInfo() {
        return mGeocodeReverse == null ? null : mGeocodeReverse.getLocationInfo();
    }

    @Override
    public List<GpsSatellite> getGpsSatellites() {
        return mGpsSatellites;
    }

    /**
     * 设置第三方扩展性定位，单次定位
     *
     * @param extensionLocation
     */
    public void setExtensionLocation(ExtensionASingleLocation extensionLocation) {
        extensionLocation.setGpsLocation(this);
        mExtensionLocation = extensionLocation;
    }

    /**
     * 设置第三方扩展性定位，持续定位
     *
     * @param extensionLocation
     */
    public void setExtensionLocation(ExtensionContinuousLocation extensionLocation) {
        extensionLocation.setGpsLocation(this);
        mExtensionLocation = extensionLocation;
    }

    @Override
    IExtensionLocation getExtensionLocation() {
        return mExtensionLocation;
    }

    /**
     * 增加GPS改变监听（可add多个）
     *
     * @param onLocationChangedListener
     */
    @Override
    public void addOnLocationChangedListener(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListeners.add(onLocationChangedListener);
    }

    /**
     * 移除GPS改变监听
     *
     * @param onLocationChangedListener
     */
    @Override
    public void removeOnLocationChangedListener(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListeners.remove(onLocationChangedListener);
    }

    /**
     * 增加地址改变时监听
     *
     * @param onAddressChangedListener
     */
    @Override
    public void addOnAddressChangedListener(OnAddressChangedListener onAddressChangedListener) {
        mOnAddressChangedListeners.add(onAddressChangedListener);
    }

    /**
     * 移除地址改变时监听
     *
     * @param onAddressChangedListener
     */
    @Override
    public void removeOnAddressChangedListener(OnAddressChangedListener onAddressChangedListener) {
        mOnAddressChangedListeners.remove(onAddressChangedListener);
    }

    /**
     * 设置GPS状态监听
     *
     * @param onSatelliteChangedListener
     */
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

    /**
     * 开启GPS定位（纯Android原生）
     *
     * @return
     */
    @Override
    public boolean start() {
        return start(null);
    }

    /**
     * 开启GPS定位（纯Android原生）
     *
     * @param {@Nullable} coordinateTransform 坐标转换接口，该类只执行了gpsPoint2MapPoint方法
     * @return
     */
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

            if (mLocationType != LocationType.ORIGINAL && mExtensionLocation != null && mExtensionLocation instanceof ExtensionContinuousLocation) {
                try {
                    mExtensionLocation.start(); // 开启第三方定位
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (mLocationType != LocationType.EXTENSION && locationManager == null) {
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

    /**
     * 结束GPS定位（纯Android原生）
     *
     * @return
     */
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

//    public void refreshLocation(GpsInfoType type, Location location) {
//        if (GpsInfoType.TYPE_FIRST == type) { // 此处为第一个获得的GPS点位（上回记录的GPS点）
//            location(type, location);
//            sendLocationListener();
//        } else {
//            boolean flag = !isNotFirst;
//            isNotFirst = true; // 设为非首次定位状态
//            if (!hasGpsPoint() || flag) { // 如果没有获得过GPS位置，或者位置为首次定位的点，则重新定位
//                location(type, location);
//                sendLocationListener();
//            } else {
//                double longitude = location.getLongitude();
//                double latitude = location.getLatitude();
//                float accuracy = location.getAccuracy();
//                int accuracyScale = type == GpsInfoType.TYPE_EXTENSION ? 3 : 2; // 扩展定位，更严格的偏移后才去改变位置
//                if ((distance(longitude, latitude, mGpsPoint.getLongitude(), mGpsPoint.getLatitude()) > accuracy * accuracyScale)) { // accuracy < mGpsPoint.getAccuracy()
//                    // 如果两个经纬度间的距离大于精度和
//                    location(type, location);
//                } else {
//                    mGpsPoint.setSpeed(location.getTime() - mGpsPoint.getTime() > STAY_TIME_INTERVAL ? 0 : location.getSpeed());
//                }
//                sendLocationListener();
//            }
//        }
//    }

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
            double distance = distance(mGpsPoint.getLongitude(), mGpsPoint.getLatitude(), tmpPoint.getLongitude(), tmpPoint.getLatitude());
            if (tmpPoint.getGpsInfoType() == GpsInfoType.TYPE_EXTENSION ? (tmpPoint.getTime() - mGpsPoint.getTime() > EXTENSION_BUFFER_TIME && distance > tmpPoint.getAccuracy() * 3 + EXTENSION_BUFFER_DISTANCE) : (distance > tmpPoint.getAccuracy() * 2)) {
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
//            GpsPoint currentGps = tmpGpsInfo.getGpsPoint();
//            TmpGpsInfo lastData = senMaxListStack.getLastData();
//            if (lastData != null) {
//                GpsPoint prevGps = lastData.getGpsPoint();
//                // 计算当前点与上一个点位的距离
//                mGpsPoint.setPrevDistance(distance(prevGps.getLongitude(), prevGps.getLatitude(), currentGps.getLongitude(), currentGps.getLatitude()));
//            } else {
//                mGpsPoint.setPrevDistance(-1);
//            }
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
    synchronized void sendLocationListener() {
        if (hasGpsPoint()) {
            if (mGpsPoint.getSpeed() != 0 && new Date().getTime() - mGpsPoint.getTime() > STAY_TIME_INTERVAL) {
                mGpsPoint.setSpeed(0);
            }
            for (OnLocationChangedListener listener : mOnLocationChangedListeners) {
                listener.locationChanged(mGpsPoint, mMapPoint);
            }
        }
    }

    /**
     * When this method is called, the client should call
     * {@link LocationManager#getGpsStatus} to get additional
     * status information.
     *
     * @param gpsStatus
     * @param gpsSatellites 可能为null
     */
    void refreshGpsStatus(int gpsStatus, List<GpsSatellite> gpsSatellites) {
        this.mGpsSatellites = gpsSatellites;
        if (mOnSatelliteChangedListener != null)
            mOnSatelliteChangedListener.satelliteChanged(gpsStatus, gpsSatellites);
        if (mExtensionLocation == null || !(mExtensionLocation instanceof ExtensionContinuousLocation)) {
            return;  // 第三方实时定位没有设置时，不做操作
        }
        if (gpsSatellites != null && gpsSatellites.size() > 3 && hasGpsPoint()) {
            mExtensionLocation.stop();  // 搜到的卫星数量大于三个时才能卫星定位
        } else if (mLocationType != LocationType.ORIGINAL) {
            mExtensionLocation.start();
        }
    }


}