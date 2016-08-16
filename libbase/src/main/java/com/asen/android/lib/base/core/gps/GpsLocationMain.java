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
 * GPS��λʵ���ࣨ��Androidԭ����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class GpsLocationMain extends GpsLocation implements IMaxStack.IGoodCompareListener<TmpGpsInfo> {

    private static final String TAG = GpsLocation.class.getSimpleName();

    private static final long MIN_TIME = 500; // ����ʱ����

    private static final int MIN_DISTANCE = 0; // ������С������

    private static final long STAY_TIME_INTERVAL = 60 * 1000; // ͣ��ʱ����

    public static final int EXTENSION_BUFFER_DISTANCE = 25; // ��������λ������룬����������������㾫�ȵĺͣ�����µ�λ

    public static final int EXTENSION_BUFFER_TIME = 60 * 1000; // ��������λ����ʱ�䣬�������ʱ�䣬����µ�λ

    private static final int MAX_STACK_SIZE = 5; // �ж����ŵ�ļ��ϵ�����������

    private LocationType mLocationType = LocationType.ORIGINAL_EXTENSION; // ��λ״̬

    private Context mContext;

    private MapPoint mMapPoint = null;

    private GpsPoint mGpsPoint = null;

//    private TmpGpsInfo tmpGpsInfo = null; // ��ʱ��λ��Ϣ

    private boolean isNotFirst = false;

    private LocationManager locationManager;

    private List<OnLocationChangedListener> mOnLocationChangedListeners = null;

    private OnSatelliteChangedListener mOnSatelliteChangedListener = null;

    private List<OnAddressChangedListener> mOnAddressChangedListeners = null;

    private GeocodeReverse mGeocodeReverse;

    private GpsLocationTimingTask mTimingTask;

    private boolean isStarted = false;

    private IExtensionLocation mExtensionLocation; // ��չ�Զ�λ

    private SenMaxListStack<TmpGpsInfo> senMaxListStack;

    private List<GpsSatellite> mGpsSatellites;

    GpsLocationMain(Context context) {
        mContext = context;
        mOnLocationChangedListeners = new ArrayList<>();
        mOnAddressChangedListeners = new ArrayList<>();
        setGeocodeReverse(new TianDTGeocodeReverse()); // �������ͼ����������
        mTimingTask = new GpsLocationTimingTask(this);
        senMaxListStack = new SenMaxListStack<>(MAX_STACK_SIZE);
    }

    @Override
    public void setLocationType(LocationType locationType) {
        if (locationType != null) mLocationType = locationType;
    }

    /**
     * �Ƿ�����GPS��λ
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
     * ������һ��ת����õ�ͼ�ϵĵ�
     *
     * @return if mGpsPoint is null, return null
     */
    @Override
    public MapPoint getMapPoint() {
        return mMapPoint;
    }

    /**
     * ������һ��GPS��λ
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
     * ��õ�ַ��λ��Ϣ
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
     * ���õ�������չ�Զ�λ�����ζ�λ
     *
     * @param extensionLocation
     */
    public void setExtensionLocation(ExtensionASingleLocation extensionLocation) {
        extensionLocation.setGpsLocation(this);
        mExtensionLocation = extensionLocation;
    }

    /**
     * ���õ�������չ�Զ�λ��������λ
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
     * ����GPS�ı��������add�����
     *
     * @param onLocationChangedListener
     */
    @Override
    public void addOnLocationChangedListener(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListeners.add(onLocationChangedListener);
    }

    /**
     * �Ƴ�GPS�ı����
     *
     * @param onLocationChangedListener
     */
    @Override
    public void removeOnLocationChangedListener(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListeners.remove(onLocationChangedListener);
    }

    /**
     * ���ӵ�ַ�ı�ʱ����
     *
     * @param onAddressChangedListener
     */
    @Override
    public void addOnAddressChangedListener(OnAddressChangedListener onAddressChangedListener) {
        mOnAddressChangedListeners.add(onAddressChangedListener);
    }

    /**
     * �Ƴ���ַ�ı�ʱ����
     *
     * @param onAddressChangedListener
     */
    @Override
    public void removeOnAddressChangedListener(OnAddressChangedListener onAddressChangedListener) {
        mOnAddressChangedListeners.remove(onAddressChangedListener);
    }

    /**
     * ����GPS״̬����
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
     * ����GPS��λ����Androidԭ����
     *
     * @return
     */
    @Override
    public boolean start() {
        return start(null);
    }

    /**
     * ����GPS��λ����Androidԭ����
     *
     * @param {@Nullable} coordinateTransform ����ת���ӿڣ�����ִֻ����gpsPoint2MapPoint����
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
                    mExtensionLocation.start(); // ������������λ
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (mLocationType != LocationType.EXTENSION && locationManager == null) {
                locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

                mGpsStatusListener = new GpsStatusListener(this);
                locationManager.addGpsStatusListener(mGpsStatusListener);// ����GPS״̬

                Location location = null;
                if (AppUtil.hasGPSDevice(mContext)) { // �豸����GPS��λ��Ӳ��
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
     * ����GPS��λ����Androidԭ����
     *
     * @return
     */
    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public boolean stop() {
        if (!isStarted()) return true;

        mTimingTask.stop();

        if (mExtensionLocation != null && mExtensionLocation instanceof ExtensionContinuousLocation) {
            mExtensionLocation.stop(); // ������������λ
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
//        if (GpsInfoType.TYPE_FIRST == type) { // �˴�Ϊ��һ����õ�GPS��λ���ϻؼ�¼��GPS�㣩
//            location(type, location);
//            sendLocationListener();
//        } else {
//            boolean flag = !isNotFirst;
//            isNotFirst = true; // ��Ϊ���״ζ�λ״̬
//            if (!hasGpsPoint() || flag) { // ���û�л�ù�GPSλ�ã�����λ��Ϊ�״ζ�λ�ĵ㣬�����¶�λ
//                location(type, location);
//                sendLocationListener();
//            } else {
//                double longitude = location.getLongitude();
//                double latitude = location.getLatitude();
//                float accuracy = location.getAccuracy();
//                int accuracyScale = type == GpsInfoType.TYPE_EXTENSION ? 3 : 2; // ��չ��λ�����ϸ��ƫ�ƺ��ȥ�ı�λ��
//                if ((distance(longitude, latitude, mGpsPoint.getLongitude(), mGpsPoint.getLatitude()) > accuracy * accuracyScale)) { // accuracy < mGpsPoint.getAccuracy()
//                    // ���������γ�ȼ�ľ�����ھ��Ⱥ�
//                    location(type, location);
//                } else {
//                    mGpsPoint.setSpeed(location.getTime() - mGpsPoint.getTime() > STAY_TIME_INTERVAL ? 0 : location.getSpeed());
//                }
//                sendLocationListener();
//            }
//        }
//    }

    public void refreshLocation(GpsInfoType type, Location location) {
        if (GpsInfoType.TYPE_FIRST == type) { // �˴�Ϊ��һ����õ�GPS��λ���ϻؼ�¼��GPS�㣩
            TmpGpsInfo tmpGpsInfo = location(type, location);
            valueToPointInfo(tmpGpsInfo);
            sendLocationListener();
        } else {
            boolean flag = !isNotFirst;
            isNotFirst = true; // ��Ϊ���״ζ�λ״̬
            if (!hasGpsPoint() || flag) { // ���û�л�ù�GPSλ�ã�����λ��Ϊ�״ζ�λ�ĵ㣬�����¶�λ
                TmpGpsInfo tmpGpsInfo = location(type, location);
                add2MaxListStack(tmpGpsInfo);
                valueToPointInfo(tmpGpsInfo);
                sendLocationListener();
            } else {
                TmpGpsInfo tmpGpsInfo = location(type, location);
                add2MaxListStack(tmpGpsInfo); // ����λ��Ϣ�ӵ������У����������ϴε�λ�ľ���
                tmpGpsInfo = senMaxListStack.getGood(this); // ��ȡ���ŵ�λ
                valueToPointInfo(tmpGpsInfo);
                sendLocationListener();
            }
        }
    }

    // ��ֵ����ʽ�Ķ�λ��Ϣ��
    private void valueToPointInfo(TmpGpsInfo tmpGpsInfo) {
        if (tmpGpsInfo == null) return;
        mMapPoint = tmpGpsInfo.getMapPoint();
        mGpsPoint = tmpGpsInfo.getGpsPoint();
    }

    @Override
    public TmpGpsInfo getGood(List<TmpGpsInfo> dataList) { // ���㴦��ؼ�����
        int size = dataList.size();

        if (size == 0) {
            return null;
        } else if (size == 1) {
            return dataList.get(0);
        } else if (size == 2) { // ֻ��������Ч���ݣ���һ������Ϊ��ʼ��
            return dataList.get(size - 1);
        } else {
            // ��ȡһ����� ����Ч�ĵ�λ
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
            } else { // ����Ҫˢ�µ�λ
                mGpsPoint.setSpeed(tmpPoint.getTime() - mGpsPoint.getTime() > STAY_TIME_INTERVAL ? 0 : tmpPoint.getSpeed());
                return null;
            }
        }
    }

    // �����µ�λ�ӵ�������
    private void add2MaxListStack(TmpGpsInfo tmpGpsInfo) {
        if (tmpGpsInfo != null) {
//            GpsPoint currentGps = tmpGpsInfo.getGpsPoint();
//            TmpGpsInfo lastData = senMaxListStack.getLastData();
//            if (lastData != null) {
//                GpsPoint prevGps = lastData.getGpsPoint();
//                // ���㵱ǰ������һ����λ�ľ���
//                mGpsPoint.setPrevDistance(distance(prevGps.getLongitude(), prevGps.getLatitude(), currentGps.getLongitude(), currentGps.getLatitude()));
//            } else {
//                mGpsPoint.setPrevDistance(-1);
//            }
            senMaxListStack.push(tmpGpsInfo);
        }
    }

    // ���㾭γ��֮��ľ���
    private double distance(double lon1, double lat1, double lon2, double lat2) { // 6371004.0
        return 6378137.000 * Math.acos(1 - (Math.pow((Math.sin((90 - lat1) * Math.PI / 180) * Math.cos(lon1 * Math.PI / 180) - Math.sin((90 - lat2) * Math.PI / 180) * Math.cos(lon2 * Math.PI / 180)), 2) + Math.pow((Math.sin((90 - lat1) * Math.PI / 180) * Math.sin(lon1 * Math.PI / 180) - Math.sin((90 - lat2) * Math.PI / 180) * Math.sin(lon2 * Math.PI / 180)), 2) + Math.pow((Math.cos((90 - lat1) * Math.PI / 180) - Math.cos((90 - lat2) * Math.PI / 180)), 2)) / 2);
    }

    private TmpGpsInfo location(GpsInfoType type, Location location) {
        if (location == null) return null;

        double longitude = location.getLongitude(); // ȥ����С�ĵ�λ
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
        gpsPoint.setGpsInfoType(type); // �Ƿ�ΪGPS��λ

        MapPoint mapPoint = mICoordinateTransform != null ? mICoordinateTransform.gpsPoint2MapPoint(gpsPoint) : new MapPoint(location.getLongitude(), location.getLatitude(), location.getAltitude());

        try {
            if (mGeocodeReverse != null) mGeocodeReverse.reverseGeocode(gpsPoint);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new TmpGpsInfo(gpsPoint, mapPoint);
    }

    // ���ʹ���λ����
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
     * @param gpsSatellites ����Ϊnull
     */
    void refreshGpsStatus(int gpsStatus, List<GpsSatellite> gpsSatellites) {
        this.mGpsSatellites = gpsSatellites;
        if (mOnSatelliteChangedListener != null)
            mOnSatelliteChangedListener.satelliteChanged(gpsStatus, gpsSatellites);
        if (mExtensionLocation == null || !(mExtensionLocation instanceof ExtensionContinuousLocation)) {
            return;  // ������ʵʱ��λû������ʱ����������
        }
        if (gpsSatellites != null && gpsSatellites.size() > 3 && hasGpsPoint()) {
            mExtensionLocation.stop();  // �ѵ�������������������ʱ�������Ƕ�λ
        } else if (mLocationType != LocationType.ORIGINAL) {
            mExtensionLocation.start();
        }
    }


}