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
import com.asen.android.lib.base.core.gps.extension.ExtensionASingleLocation;
import com.asen.android.lib.base.core.gps.extension.ExtensionContinuousLocation;
import com.asen.android.lib.base.core.gps.extension.IExtensionLocation;
import com.asen.android.lib.base.core.gps.geocode.GeocodeReverse;
import com.asen.android.lib.base.core.gps.geocode.tdt.TianDTGeocodeReverse;
import com.asen.android.lib.base.core.gps.listener.OnAddressChangedListener;
import com.asen.android.lib.base.core.gps.listener.OnLocationChangedListener;
import com.asen.android.lib.base.core.gps.listener.OnSatelliteChangedListener;
import com.asen.android.lib.base.tool.util.AppUtil;
import com.asen.android.lib.base.tool.util.LogUtil;
import com.asen.android.lib.base.tool.util.Version;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Simple to Introduction  GPS定位类（纯Android原生）
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class GpsLocationMain extends GpsLocation {

    private static final String TAG = GpsLocation.class.getSimpleName();

    private static final long MIN_TIME = 500; // 更新时间间隔

    private static final int MIN_DISTANCE = 0; // 更新最小距离间隔

    private static final long STAY_TIME_INTERVAL = 60 * 1000; // 停留时间间隔

    public static final int EXTENSION_BUFFER_DISTANCE = 10; // 第三方定位缓冲距离，超过这个距离与两点精度的和，则更新点位

    public static final int EXTENSION_BUFFER_TIME = 60 * 1000; // 第三方定位缓冲时间，超过这个时间，则更新点位

    private Context mContext;

    private MapPoint mMapPoint = null;

    private GpsPoint mGpsPoint = null;

    private boolean isNotFirst = false;

    private LocationManager locationManager;

    private List<OnLocationChangedListener> mOnLocationChangedListeners = null;

    private OnSatelliteChangedListener mOnSatelliteChangedListener = null;

    private List<OnAddressChangedListener> mOnAddressChangedListeners = null;

    private GeocodeReverse mGeocodeReverse;

    private GpsLocationTimingTask mTimingTask;

    private boolean isStarted = false;

    private IExtensionLocation mExtensionLocation; // 扩展性定位

    GpsLocationMain(Context context) {
        mContext = context;
        mOnLocationChangedListeners = new ArrayList<>();
        mOnAddressChangedListeners = new ArrayList<>();
        setGeocodeReverse(new TianDTGeocodeReverse()); // 设置天地图的逆地理编码
        mTimingTask = new GpsLocationTimingTask(this);
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
                    mGpsLocationListener = new GpsLocationListener(this, LocationType.TYPE_GPS);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, mGpsLocationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if (AppUtil.hasNetworkDevice(mContext)) {
                    mNetworkLocationListener = new GpsLocationListener(this, LocationType.TYPE_NETWORK);
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

                refreshLocation(LocationType.TYPE_FIRST, location);
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

//  	public void refreshLocation(LocationType type, Location location) {
//		if (LocationType.TYPE_FIRST == type) { // 此处为第一个获得的GPS点位（上回记录的GPS点）
//			location(type, location);
//			sendLocationListener();
//		} else {
//			boolean flag = !isNotFirst;
//			isNotFirst = true; // 设为非首次定位状态
//			if (!hasGpsPoint() || flag) { // 如果没有获得过GPS位置，或者位置为首次定位的点，则重新定位
//				location(type, location);
//				sendLocationListener();
//			} else {
//				double longitude = location.getLongitude();
//				double latitude = location.getLatitude();
//				float accuracy = location.getAccuracy();
//				if (LocationType.TYPE_GPS == type) { // Android自带的网络定位 或者 GPS定位
//					if (mGpsPoint.getLocationType() == LocationType.TYPE_EXTENSION || LocationType.TYPE_NETWORK == mGpsPoint.getLocationType() || accuracy < mGpsPoint.getAccuracy() || (distance(longitude, latitude, mGpsPoint.getLongitude(), mGpsPoint.getLatitude()) > accuracy * 2 && location.getSpeed() != 0)) {
//						// 如果两个经纬度间的距离大于精度和 或者  前一个点为第三方定位获得或网络定位获得，则重新定位
//						location(type, location);
//					} else {
//						mGpsPoint.setSpeed(location.getTime() - mGpsPoint.getTime() > STAY_TIME_INTERVAL ? 0 : location.getSpeed());
//					}
//				} else if (LocationType.TYPE_NETWORK == type) {
//					if (mGpsPoint.getLocationType() == LocationType.TYPE_EXTENSION || accuracy < mGpsPoint.getAccuracy() || (distance(longitude, latitude, mGpsPoint.getLongitude(), mGpsPoint.getLatitude()) > accuracy * 2 && location.getSpeed() != 0)) {
//						// 如果两个经纬度间的距离大于精度和 或者  前一个点为第三方定位获得，则重新定位
//						location(type, location);
//					} else {
//						mGpsPoint.setSpeed(location.getTime() - mGpsPoint.getTime() > STAY_TIME_INTERVAL ? 0 : location.getSpeed());
//					}
//				} else if (LocationType.TYPE_EXTENSION == type) { // 第三方扩展的定位
//					if (mGpsPoint.getLocationType() == LocationType.TYPE_EXTENSION) { // 上次定位是第三方定位，则直接比较
//						if (accuracy < mGpsPoint.getAccuracy() ||(distance(longitude, latitude, mGpsPoint.getLongitude(), mGpsPoint.getLatitude()) > accuracy * 2 && location.getSpeed() != 0)) {
//							location(type, location); // 如果两个经纬度间的距离大于精度和，则重新定位
//						} else {
//							mGpsPoint.setSpeed(location.getTime() - mGpsPoint.getTime() > STAY_TIME_INTERVAL ? 0 : location.getSpeed());
//						}
//					} else { // 上次定位是GPS点位或者是Android自带的网络定位的，本次更改GPS位置信息的条件就比较严格，超过精度和+缓冲距离，且超过指定时间
//						if (distance(longitude, latitude, mGpsPoint.getLongitude(), mGpsPoint.getLatitude()) > accuracy * 2 + EXTENSION_BUFFER_DISTANCE && location.getTime() - mGpsPoint.getTime() > EXTENSION_BUFFER_TIME) {
//							if (location.getSpeed() != 0) {
//								location(type, location); // 如果两个经纬度间的距离大于精度和，则重新定位
//							} else {
//								mGpsPoint.setSpeed(location.getTime() - mGpsPoint.getTime() > STAY_TIME_INTERVAL ? 0 : location.getSpeed());
//							}
//						}
//					}
//				}
//			}
//		}
//	}

    public void refreshLocation(LocationType type, Location location) {
        if (LocationType.TYPE_FIRST == type) { // 此处为第一个获得的GPS点位（上回记录的GPS点）
            location(type, location);
            sendLocationListener();
        } else {
            boolean flag = !isNotFirst;
            isNotFirst = true; // 设为非首次定位状态
            if (!hasGpsPoint() || flag) { // 如果没有获得过GPS位置，或者位置为首次定位的点，则重新定位
                location(type, location);
                sendLocationListener();
            } else {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                float accuracy = location.getAccuracy();
                int accuracyScale = type == LocationType.TYPE_EXTENSION ? 3 : 2; // 扩展定位，更严格的偏移后才去改变位置
                if (accuracy < mGpsPoint.getAccuracy() || (distance(longitude, latitude, mGpsPoint.getLongitude(), mGpsPoint.getLatitude()) > accuracy * accuracyScale)) {
                    // 如果两个经纬度间的距离大于精度和
                    location(type, location);
                } else {
                    mGpsPoint.setSpeed(location.getTime() - mGpsPoint.getTime() > STAY_TIME_INTERVAL ? 0 : location.getSpeed());
                }
            }

        }
    }

    // 计算经纬度之间的距离
    private double distance(double lon1, double lat1, double lon2, double lat2) { // 6371004.0
        return 6378137.000 * Math.acos(1 - (Math.pow((Math.sin((90 - lat1) * Math.PI / 180) * Math.cos(lon1 * Math.PI / 180) - Math.sin((90 - lat2) * Math.PI / 180) * Math.cos(lon2 * Math.PI / 180)), 2) + Math.pow((Math.sin((90 - lat1) * Math.PI / 180) * Math.sin(lon1 * Math.PI / 180) - Math.sin((90 - lat2) * Math.PI / 180) * Math.sin(lon2 * Math.PI / 180)), 2) + Math.pow((Math.cos((90 - lat1) * Math.PI / 180) - Math.cos((90 - lat2) * Math.PI / 180)), 2)) / 2);
    }

    private void location(LocationType type, Location location) {
        if (location == null) return;

        mGpsPoint = new GpsPoint();
        mGpsPoint.setAccuracy(location.getAccuracy());
        mGpsPoint.setAltitude(location.getAltitude());
        mGpsPoint.setBearing(location.getBearing());
        mGpsPoint.setLatitude(location.getLatitude());
        mGpsPoint.setLongitude(location.getLongitude());
        mGpsPoint.setSpeed(location.getSpeed());
        mGpsPoint.setTime(location.getTime());
        mGpsPoint.setLocationType(type); // 是否为GPS点位

        if (mICoordinateTransform != null) {
            mMapPoint = mICoordinateTransform.gpsPoint2MapPoint(mGpsPoint);
        } else {
            if (mMapPoint == null)
                mMapPoint = new MapPoint(location.getLongitude(), location.getLatitude(), location.getAltitude());
            else {
                mMapPoint.setX(location.getLongitude());
                mMapPoint.setY(location.getLatitude());
                mMapPoint.setZ(location.getAltitude());
            }
        }

        try {
            if (mGeocodeReverse != null) mGeocodeReverse.reverseGeocode(mGpsPoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * When this method is called, the client should call
     * {@link LocationManager#getGpsStatus} to get additional
     * status information.
     *
     * @param gpsStatus
     * @param gpsSatellites 可能为null
     */
    void refreshGpsStatus(int gpsStatus, List<GpsSatellite> gpsSatellites) {
        if (mOnSatelliteChangedListener != null)
            mOnSatelliteChangedListener.satelliteChanged(gpsStatus, gpsSatellites);
    }

}