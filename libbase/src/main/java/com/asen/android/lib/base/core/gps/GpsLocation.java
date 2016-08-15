package com.asen.android.lib.base.core.gps;

import android.content.Context;
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

/**
 * GPS定位类（纯Android原生）
 * GPS定位主要类（入口类）
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public abstract class GpsLocation {

    private static volatile GpsLocation g = null;

    /**
     * 获得GPS定位实例
     *
     * @param context Android 上下文
     * @return GPS定位实例
     */
    public static GpsLocation getInstance(Context context) {
        if (null == g) {
            synchronized (GpsLocation.class) {
                if (null == g) {
                    g = new GpsLocationMain(context);
                }
            }
        }
        return g;
    }


    /**
     * 是否获得了GPS点位
     *
     * @return 是否获得了GPS点位
     */
    public abstract boolean hasGpsPoint();

    /**
     * 不是首个GPS点位（首个GPS点位可能是拿的上次关闭系统时的点位信息）
     *
     * @return 是否不是首个GPS点位
     */
    public abstract boolean isNotFirst();

    /**
     * 是否开始定位
     *
     * @return 判断是否开始定位
     */
    public abstract boolean isStarted();

    /**
     * 获得最后一个转换后得地图上的点
     *
     * @return if mGpsPoint is null, return null
     */
    public abstract MapPoint getMapPoint();

    /**
     * 获得最后一个GPS点位
     *
     * @return mGpsPoint
     */
    public abstract GpsPoint getGpsPoint();

    /**
     * 获得地址定位信息
     *
     * @return 地址定位信息
     */
    public abstract LocationInfo getLocationInfo();

    /**
     * 设置第三方扩展性定位，单次定位
     *
     * @param extensionLocation 单次定位
     */
    public abstract void setExtensionLocation(ExtensionASingleLocation extensionLocation);

    /**
     * 设置第三方扩展性定位，持续定位
     *
     * @param extensionLocation 持续定位
     */
    public abstract void setExtensionLocation(ExtensionContinuousLocation extensionLocation);

    /**
     * 获得第三方扩展性定位
     */
    abstract IExtensionLocation getExtensionLocation();

    /**
     * 增加GPS改变监听（可add多个）
     *
     * @param onLocationChangedListener 定位监听
     */
    public abstract void addOnLocationChangedListener(OnLocationChangedListener onLocationChangedListener);

    /**
     * 移除GPS改变监听
     *
     * @param onLocationChangedListener 定位监听
     */
    public abstract void removeOnLocationChangedListener(OnLocationChangedListener onLocationChangedListener);

    /**
     * 增加地址改变时监听
     *
     * @param onAddressChangedListener 地址监听
     */
    public abstract void addOnAddressChangedListener(OnAddressChangedListener onAddressChangedListener);

    /**
     * 移除地址改变时监听
     *
     * @param onAddressChangedListener 地址监听
     */
    public abstract void removeOnAddressChangedListener(OnAddressChangedListener onAddressChangedListener);

    /**
     * 设置GPS状态监听
     *
     * @param onSatelliteChangedListener GPS状态监听
     */
    public abstract void setOnSatelliteChangedListener(OnSatelliteChangedListener onSatelliteChangedListener);

    /**
     * 开启GPS定位（纯Android原生）
     *
     * @return 成功与否
     */
    public abstract boolean start();

    /**
     * 开启GPS定位（纯Android原生）
     *
     * @param coordinateTransform 坐标转换接口，该类只执行了gpsPoint2MapPoint方法 {@link ICoordinateTransform#gpsPoint2MapPoint(GpsPoint)}
     * @return 成功与否
     */
    public abstract boolean start(@Nullable ICoordinateTransform coordinateTransform);

    /**
     * 结束GPS定位（纯Android原生）
     *
     * @return 成功与否
     */
    public abstract boolean stop();

    /**
     * 设置自定义逆地理编码的实现（默认采用{@link TianDTGeocodeReverse} 天地图逆地理编码）
     *
     * @param geocodeReverse 逆地理编码抽象类 的 实现
     */
    public abstract void setGeocodeReverse(GeocodeReverse geocodeReverse);

}