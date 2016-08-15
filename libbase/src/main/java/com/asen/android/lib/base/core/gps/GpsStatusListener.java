package com.asen.android.lib.base.core.gps;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;

import com.asen.android.lib.base.tool.util.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * GPS ״̬����
 *
 * @author ASEN
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
                LogUtil.d(TAG, "GPS ��һ��������ʱ  " + i);
                mGpsLocation.refreshGpsStatus(event, null);
                break;
            }
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS: {// ���ڵı�������״̬
                // �õ������յ������ǵ���Ϣ������ ���ǵĸ߶Ƚǡ���λ�ǡ�����ȡ���α����ţ������Ǳ�ţ�
                Iterable<GpsSatellite> allSatellites = gpsStatus.getSatellites();
                Iterator<GpsSatellite> iterator = allSatellites.iterator();

                mGpsSatellites.clear();
                while (iterator.hasNext()) {
                    mGpsSatellites.add(iterator.next());
                }

                LogUtil.d(TAG, "�ܹ�������" + mGpsSatellites.size() + "������");
                mGpsLocation.refreshGpsStatus(event, mGpsSatellites);

                // ͨ��������������ΪArrayList
//                if (GpsLocation.DEBUG) {
//                    // ���������Ϣ
//                    for (int i = 0; i < mGpsSatellites.size(); i++) {
//                        GpsSatellite satellite = mGpsSatellites.get(i);
//                        // ���ǵķ�λ�ǣ�����������
//                        mGpsLocation.logD("���ǵķ�λ��" + satellite.getAzimuth());
//                        // ���ǵĸ߶ȣ�����������
//                        mGpsLocation.logD("���ǵĸ߶�" + satellite.getElevation());
//                        // ���ǵ�α��������룬��������
//                        mGpsLocation.logD("���ǵ�α���������" + satellite.getPrn());
//                        // ���ǵ�����ȣ�����������
//                        mGpsLocation.logD("���ǵ������" + satellite.getSnr());
//                        // �����Ƿ�������������������
//                        mGpsLocation.logD("�����Ƿ���������" + satellite.hasAlmanac());
//                        // �����Ƿ�������������������
//                        mGpsLocation.logD("�����Ƿ���������" + satellite.hasEphemeris());
//                        // �����Ƿ����ڽ��ڵ�GPS��������
//                        mGpsLocation.logD("�����Ƿ����ڽ��ڵ�GPS��������" + satellite.hasAlmanac());
//                        // �ָ���
//                        mGpsLocation.logD("*******************************");
//                    }
//                }
                break;
            }
            case GpsStatus.GPS_EVENT_STARTED: {
                LogUtil.d(TAG, "��λ����");
                mGpsLocation.refreshGpsStatus(event, null);
                break;
            }
            case GpsStatus.GPS_EVENT_STOPPED: {
                LogUtil.d(TAG, "��λ����");
                mGpsLocation.refreshGpsStatus(event, null);
                break;
            }
            default:
                break;
        }


    }

}