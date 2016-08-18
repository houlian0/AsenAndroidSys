package com.asen.android.lib.base.tool.coordtransform;

/**
 * ����ͼAPI����ϵͳ�Ƚ���ת��;
 * WGS84����ϵ������������ϵ��������ͨ�õ�����ϵ���豸һ�����GPSоƬ���߱���оƬ��ȡ�ľ�γ��ΪWGS84��������ϵ,
 * �ȸ��ͼ���õ���WGS84��������ϵ���й���Χ���⣩;
 * GCJ02����ϵ������������ϵ�������й����Ҳ����ƶ��ĵ�����Ϣϵͳ������ϵͳ����WGS84����ϵ�����ܺ������ϵ��
 * �ȸ��й���ͼ�������й���ͼ���õ���GCJ02��������ϵ; BD09����ϵ�����ٶ�����ϵ��GCJ02����ϵ�����ܺ������ϵ;
 * �ѹ�����ϵ��ͼ������ϵ�ȣ�����Ҳ����GCJ02�����ϼ��ܶ��ɵġ�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public class PositionUtil {

//    public static final String BAIDU_LBS_TYPE = "bd09ll";

    public static double pi = 3.1415926535897932384626;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;

    /**
     * 84 to ��������ϵ (GCJ-02) World Geodetic System ת Mars Geodetic System
     *
     * @param lat γ��
     * @param lon ����
     * @return ת����ľ�γ����Ϣ
     */
    public static PositionGps gps84_To_Gcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new PositionGps(lat, lon);
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new PositionGps(mgLat, mgLon);
    }

    /**
     * ��������ϵ (GCJ-02) to 84
     *
     * @param lat γ��
     * @param lon ����
     * @return ת����ľ�γ����Ϣ
     */
    public static PositionGps gcj02_To_Gps84(double lat, double lon) {
        PositionGps gps = transform(lat, lon);
        double longitude = lon * 2 - gps.getWgLon();
        double latitude = lat * 2 - gps.getWgLat();
        return new PositionGps(latitude, longitude);
    }

    /**
     * ��������ϵ (GCJ-02) ��ٶ�����ϵ (BD-09) ��ת���㷨 �� GCJ-02 ����ת���� BD-09 ����
     *
     * @param gg_lat γ��
     * @param gg_lon ����
     * @return ת����ľ�γ����Ϣ
     */
    public static PositionGps gcj02_To_Bd09(double gg_lat, double gg_lon) {
        double z = Math.sqrt(gg_lon * gg_lon + gg_lat * gg_lat) + 0.00002 * Math.sin(gg_lat * pi);
        double theta = Math.atan2(gg_lat, gg_lon) + 0.000003 * Math.cos(gg_lon * pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new PositionGps(bd_lat, bd_lon);
    }

    /**
     * * ��������ϵ (GCJ-02) ��ٶ�����ϵ (BD-09) ��ת���㷨 * * �� BD-09 ����ת����GCJ-02 ���� *
     *
     * @param bd_lat γ��
     * @param bd_lon ����
     * @return ת����ľ�γ����Ϣ
     */
    public static PositionGps bd09_To_Gcj02(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new PositionGps(gg_lat, gg_lon);
    }

    /**
     * (BD-09) ת 84
     *
     * @param bd_lat γ��
     * @param bd_lon ����
     * @return ת����ľ�γ����Ϣ
     */
    public static PositionGps bd09_To_Gps84(double bd_lat, double bd_lon) {
        PositionGps gcj02 = PositionUtil.bd09_To_Gcj02(bd_lat, bd_lon);
        return PositionUtil.gcj02_To_Gps84(gcj02.getWgLat(), gcj02.getWgLon());
    }

    /**
     * �ж��Ƿ����й�
     *
     * @param lat γ��
     * @param lon ����
     * @return true�����й���Χ�ڣ�false�������й�
     */
    public static boolean outOfChina(double lat, double lon) {
        return lon < 72.004 || lon > 137.8347 || lat < 0.8293 || lat > 55.8271;
    }

    static PositionGps transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new PositionGps(lat, lon);
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new PositionGps(mgLat, mgLon);
    }

    static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }

//    public static void main(String[] args) {
//
//        // ����оƬ��ȡ�ľ�γ��ΪWGS84�������� 31.426896,119.496145
//        PositionGps gps = new PositionGps(31.426896, 119.496145);
//        System.out.println("gps :" + gps);
//        PositionGps gcj = gps84_To_Gcj02(gps.getWgLat(), gps.getWgLon());
//        System.out.println("gcj :" + gcj);
//        PositionGps star = gcj02_To_Gps84(gcj.getWgLat(), gcj.getWgLon());
//        System.out.println("star:" + star);
//        PositionGps bd = gcj02_To_Bd09(gcj.getWgLat(), gcj.getWgLon());
//        System.out.println("bd  :" + bd);
//        PositionGps gcj2 = bd09_To_Gcj02(bd.getWgLat(), bd.getWgLon());
//        System.out.println("gcj :" + gcj2);
//    }

}
