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
 * GPS��λ�ࣨ��Androidԭ����
 * GPS��λ��Ҫ�ࣨ����ࣩ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public abstract class GpsLocation {

    private static volatile GpsLocation g = null;

    /**
     * ���GPS��λʵ��
     *
     * @param context Android ������
     * @return GPS��λʵ��
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
     * �Ƿ�����GPS��λ
     *
     * @return �Ƿ�����GPS��λ
     */
    public abstract boolean hasGpsPoint();

    /**
     * �����׸�GPS��λ���׸�GPS��λ�������õ��ϴιر�ϵͳʱ�ĵ�λ��Ϣ��
     *
     * @return �Ƿ����׸�GPS��λ
     */
    public abstract boolean isNotFirst();

    /**
     * �Ƿ�ʼ��λ
     *
     * @return �ж��Ƿ�ʼ��λ
     */
    public abstract boolean isStarted();

    /**
     * ������һ��ת����õ�ͼ�ϵĵ�
     *
     * @return if mGpsPoint is null, return null
     */
    public abstract MapPoint getMapPoint();

    /**
     * ������һ��GPS��λ
     *
     * @return mGpsPoint
     */
    public abstract GpsPoint getGpsPoint();

    /**
     * ��õ�ַ��λ��Ϣ
     *
     * @return ��ַ��λ��Ϣ
     */
    public abstract LocationInfo getLocationInfo();

    /**
     * ���õ�������չ�Զ�λ�����ζ�λ
     *
     * @param extensionLocation ���ζ�λ
     */
    public abstract void setExtensionLocation(ExtensionASingleLocation extensionLocation);

    /**
     * ���õ�������չ�Զ�λ��������λ
     *
     * @param extensionLocation ������λ
     */
    public abstract void setExtensionLocation(ExtensionContinuousLocation extensionLocation);

    /**
     * ��õ�������չ�Զ�λ
     */
    abstract IExtensionLocation getExtensionLocation();

    /**
     * ����GPS�ı��������add�����
     *
     * @param onLocationChangedListener ��λ����
     */
    public abstract void addOnLocationChangedListener(OnLocationChangedListener onLocationChangedListener);

    /**
     * �Ƴ�GPS�ı����
     *
     * @param onLocationChangedListener ��λ����
     */
    public abstract void removeOnLocationChangedListener(OnLocationChangedListener onLocationChangedListener);

    /**
     * ���ӵ�ַ�ı�ʱ����
     *
     * @param onAddressChangedListener ��ַ����
     */
    public abstract void addOnAddressChangedListener(OnAddressChangedListener onAddressChangedListener);

    /**
     * �Ƴ���ַ�ı�ʱ����
     *
     * @param onAddressChangedListener ��ַ����
     */
    public abstract void removeOnAddressChangedListener(OnAddressChangedListener onAddressChangedListener);

    /**
     * ����GPS״̬����
     *
     * @param onSatelliteChangedListener GPS״̬����
     */
    public abstract void setOnSatelliteChangedListener(OnSatelliteChangedListener onSatelliteChangedListener);

    /**
     * ����GPS��λ����Androidԭ����
     *
     * @return �ɹ����
     */
    public abstract boolean start();

    /**
     * ����GPS��λ����Androidԭ����
     *
     * @param coordinateTransform ����ת���ӿڣ�����ִֻ����gpsPoint2MapPoint���� {@link ICoordinateTransform#gpsPoint2MapPoint(GpsPoint)}
     * @return �ɹ����
     */
    public abstract boolean start(@Nullable ICoordinateTransform coordinateTransform);

    /**
     * ����GPS��λ����Androidԭ����
     *
     * @return �ɹ����
     */
    public abstract boolean stop();

    /**
     * �����Զ������������ʵ�֣�Ĭ�ϲ���{@link TianDTGeocodeReverse} ���ͼ�������룩
     *
     * @param geocodeReverse ������������� �� ʵ��
     */
    public abstract void setGeocodeReverse(GeocodeReverse geocodeReverse);

}