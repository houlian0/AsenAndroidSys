package com.asen.android.lib.base.tool.coordtransform;

/**
 * 各地图API坐标系统比较与转换;
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
 * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
 * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。
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
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System 转 Mars Geodetic System
     *
     * @param lat 纬度
     * @param lon 经度
     * @return 转换后的经纬度信息
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
     * 火星坐标系 (GCJ-02) to 84
     *
     * @param lat 纬度
     * @param lon 经度
     * @return 转换后的经纬度信息
     */
    public static PositionGps gcj02_To_Gps84(double lat, double lon) {
        PositionGps gps = transform(lat, lon);
        double longitude = lon * 2 - gps.getWgLon();
        double latitude = lat * 2 - gps.getWgLat();
        return new PositionGps(latitude, longitude);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param gg_lat 纬度
     * @param gg_lon 经度
     * @return 转换后的经纬度信息
     */
    public static PositionGps gcj02_To_Bd09(double gg_lat, double gg_lon) {
        double z = Math.sqrt(gg_lon * gg_lon + gg_lat * gg_lat) + 0.00002 * Math.sin(gg_lat * pi);
        double theta = Math.atan2(gg_lat, gg_lon) + 0.000003 * Math.cos(gg_lon * pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new PositionGps(bd_lat, bd_lon);
    }

    /**
     * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 *
     *
     * @param bd_lat 纬度
     * @param bd_lon 经度
     * @return 转换后的经纬度信息
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
     * (BD-09) 转 84
     *
     * @param bd_lat 纬度
     * @param bd_lon 经度
     * @return 转换后的经纬度信息
     */
    public static PositionGps bd09_To_Gps84(double bd_lat, double bd_lon) {
        PositionGps gcj02 = PositionUtil.bd09_To_Gcj02(bd_lat, bd_lon);
        return PositionUtil.gcj02_To_Gps84(gcj02.getWgLat(), gcj02.getWgLon());
    }

    /**
     * 判断是否在中国
     *
     * @param lat 纬度
     * @param lon 经度
     * @return true，在中国范围内；false，不在中国
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
//        // 北斗芯片获取的经纬度为WGS84地理坐标 31.426896,119.496145
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
