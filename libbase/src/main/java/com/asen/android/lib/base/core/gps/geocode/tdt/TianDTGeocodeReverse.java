package com.asen.android.lib.base.core.gps.geocode.tdt;

import com.asen.android.lib.base.core.gps.bean.GpsPoint;
import com.asen.android.lib.base.core.gps.bean.LocationInfo;
import com.asen.android.lib.base.core.gps.geocode.GeocodeReverse;
import com.asen.android.lib.base.core.network.task.SenAsyncTask;
import com.asen.android.lib.base.core.network.urlconn.HttpUtil;
import com.asen.android.lib.base.tool.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * ���ͼ��������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class TianDTGeocodeReverse extends GeocodeReverse {

    private static final double MIN_DISTANCE = 5.0; // ��Сˢ��λ����Ϣ�ľ���

    private static final String TAG = TianDTGeocodeReverse.class.getName();

    private SenAsyncTask<Double, Void, GeoAddressInfo> mSenAsyncTask; // �첽������

    @Override
    public void reverseGeocode(GpsPoint gpsPoint) {
        // ���ڻ�ȡλ����Ϣʱ�����ظ�ִ��
        if (mSenAsyncTask != null) return;

        mSenAsyncTask = new SenAsyncTask<Double, Void, GeoAddressInfo>() {
            @Override
            protected GeoAddressInfo doInBackground(Double... params) throws RuntimeException {
                // ����Ƿ���Ҫ���»�ȡλ����Ϣ
                if (!checkNeadRequest(params[0], params[1])) return null;

                String result;
                try {
                    result = get(params[0], params[1]); // get���󣬻�ȡ���
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("network connection is failed");
                }

                if (result == null) {
                    throw new RuntimeException("network connection is failed");
                }

                try {
                    return result2AddressInfo(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new RuntimeException("json error");
                }
            }

            @Override
            protected void onResultOK(GeoAddressInfo result) {
                if (result == null) return;

                mSenAsyncTask = null;
                if (mLocationInfo == null) {
                    mLocationInfo = new LocationInfo(result.getAddress(), result.getCity());
                } else {
                    mLocationInfo.setAddress(result.getAddress());
                    mLocationInfo.setCity(result.getCity());
                }
                refreshAddressChangedListener();
            }

            @Override
            protected void onResultError(String msg) {
                mSenAsyncTask = null;
                LogUtil.e(TAG, msg);
            }
        };
        mSenAsyncTask.execute(gpsPoint.getLongitude(), gpsPoint.getLatitude());
    }

    /**
     * �ж��Ƿ���Ҫ���������ַ��Ϣ���ǳ���ʱ����false
     *
     * @return �Ƿ���Ҫ���������ַ��Ϣ
     */
    private boolean checkNeadRequest(double lon, double lat) {
        if (Double.isNaN(mLon) || Double.isNaN(mLat)) {
            mLon = lon;
            mLat = lat;
            return true;
        }

        double distance = distance(mLon, mLat, lon, lat);
        if (distance < MIN_DISTANCE) { // ����С��5m����Լ���룩
            return false;
        } else {
            mLon = lon;
            mLat = lat;
            return true;
        }
    }

    // ���㾭γ��֮��ľ���
    private double distance(double lon1, double lat1, double lon2, double lat2) { // 6371004.0
        return 6378137.000 * Math.acos(1 - (Math.pow((Math.sin((90 - lat1) * Math.PI / 180) * Math.cos(lon1 * Math.PI / 180) - Math.sin((90 - lat2) * Math.PI / 180) * Math.cos(lon2 * Math.PI / 180)), 2) + Math.pow((Math.sin((90 - lat1) * Math.PI / 180) * Math.sin(lon1 * Math.PI / 180) - Math.sin((90 - lat2) * Math.PI / 180) * Math.sin(lon2 * Math.PI / 180)), 2) + Math.pow((Math.cos((90 - lat1) * Math.PI / 180) - Math.cos((90 - lat2) * Math.PI / 180)), 2)) / 2);
    }

    /**
     * get����������������
     *
     * @param lon ����
     * @param lat γ��
     * @return ��������������
     * @throws Exception
     */
    private String get(double lon, double lat) throws Exception {
        String json = "{\"lon\":\"" + lon + "\",\"lat\":\"" + lat + "\",\"appkey\":\"5d46aa412a3480e09771e3797003565b\",\"ver\":1}";
        String encode = URLEncoder.encode(json, "UTF-8");
        return HttpUtil.get("http://www.tianditu.com/query.shtml?type=geocode&postStr=" + encode);
    }

    /**
     * ����JSON��
     *
     * @param result ����������
     * @return ���ؽ������
     * @throws JSONException
     */
    private GeoAddressInfo result2AddressInfo(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result).getJSONObject("result");

        String formattedAddress = jsonObject.optString("formatted_address");
        jsonObject = jsonObject.optJSONObject("addressComponent");
        String road = jsonObject.optString("road");
        int roadDistance = jsonObject.optInt("road_distance", 0);

        String poi = jsonObject.optString("poi");
        int poiDistance = jsonObject.optInt("poi_distance");
        String poiPosition = jsonObject.optString("poi_position");

        String address = jsonObject.optString("address");
        String addressPosition = jsonObject.optString("address_position");
        int addressDistance = jsonObject.optInt("address_distance");

        String city = jsonObject.optString("city");

        return new GeoAddressInfo(formattedAddress, road, roadDistance, poi, poiDistance, poiPosition, address, addressPosition, addressDistance, city);
    }

}