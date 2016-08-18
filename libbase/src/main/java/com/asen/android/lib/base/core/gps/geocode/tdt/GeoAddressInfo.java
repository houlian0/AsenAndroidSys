package com.asen.android.lib.base.core.gps.geocode.tdt;

/**
 * 天地图地址信息对象
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class GeoAddressInfo {

    /**
     * 格式化的地址
     */
    private String formattedAddress;

    /**
     * 最近的路名
     */
    private String road;

    /**
     * 与最近路名的距离
     */
    private int roadDistance;

    /**
     * 最近的POI点
     */
    private String poi;

    /**
     * 与最近的POI点的距离
     */
    private int poiDistance;

    /**
     * 与最近POI点的方位信息
     */
    private String poiPosition;

    /**
     * 最近的地址
     */
    private String address;

    /**
     * 与最近地址的方位信息
     */
    private String addressPosition;

    /**
     * 与最近地址的距离
     */
    private int addressDistance;

    /**
     * 所在的城市信息
     */
    private String city;

    /**
     * 构造函数
     *
     * @param formattedAddress 格式化的地址
     * @param road             最近的路名
     * @param roadDistance     与最近路名的距离
     * @param poi              最近的POI点
     * @param poiDistance      与最近的POI点的距离
     * @param poiPosition      与最近POI点的方位信息
     * @param address          最近的地址
     * @param addressPosition  与最近地址的方位信息
     * @param addressDistance  与最近地址的距离
     * @param city             所在的城市信息
     */
    public GeoAddressInfo(String formattedAddress, String road, int roadDistance, String poi, int poiDistance, String poiPosition, String address, String addressPosition, int addressDistance, String city) {
        this.formattedAddress = formattedAddress;
        this.road = road;
        this.roadDistance = roadDistance;
        this.poi = poi;
        this.poiDistance = poiDistance;
        this.poiPosition = poiPosition;
        this.address = address;
        this.addressPosition = addressPosition;
        this.addressDistance = addressDistance;
        this.city = city;
    }

    /**
     * 获取格式化的地址
     *
     * @return 格式化的地址
     */
    public String getFormattedAddress() {
        return this.formattedAddress;
    }

    /**
     * 设置格式化的地址
     *
     * @param formattedAddress 格式化的地址
     */
    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    /**
     * 获取最近的路名
     *
     * @return 最近的路名
     */
    public String getRoad() {
        return this.road;
    }

    /**
     * 设置最近的路名
     *
     * @param road 最近的路名
     */
    public void setRoad(String road) {
        this.road = road;
    }

    /**
     * 获取与最近路名的距离
     *
     * @return 与最近路名的距离
     */
    public int getRoadDistance() {
        return this.roadDistance;
    }

    /**
     * 设置与最近路名的距离
     *
     * @param roadDistance 与最近路名的距离
     */
    public void setRoadDistance(int roadDistance) {
        this.roadDistance = roadDistance;
    }

    /**
     * 获取最近的POI点
     *
     * @return 最近的POI点
     */
    public String getPoi() {
        return this.poi;
    }

    /**
     * 设置最近的POI点
     *
     * @param poi 最近的POI点
     */
    public void setPoi(String poi) {
        this.poi = poi;
    }

    /**
     * 获取与最近的POI点的距离
     *
     * @return 与最近的POI点的距离
     */
    public int getPoiDistance() {
        return this.poiDistance;
    }

    /**
     * 设置与最近的POI点的距离
     *
     * @param poiDistance 与最近的POI点的距离
     */
    public void setPoiDistance(int poiDistance) {
        this.poiDistance = poiDistance;
    }

    /**
     * 获取与最近POI点的方位信息
     *
     * @return 与最近POI点的方位信息
     */
    public String getPoiPosition() {
        return this.poiPosition;
    }

    /**
     * 设置与最近POI点的方位信息
     *
     * @param poiPosition 与最近POI点的方位信息
     */
    public void setPoiPosition(String poiPosition) {
        this.poiPosition = poiPosition;
    }

    /**
     * 获取最近的地址
     *
     * @return 最近的地址
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * 设置最近的地址
     *
     * @param address 最近的地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取与最近地址的方位信息
     *
     * @return 与最近地址的方位信息
     */
    public String getAddressPosition() {
        return this.addressPosition;
    }

    /**
     * 设置与最近地址的方位信息
     *
     * @param addressPosition 与最近地址的方位信息
     */
    public void setAddressPosition(String addressPosition) {
        this.addressPosition = addressPosition;
    }

    /**
     * 获取与最近地址的距离
     *
     * @return 与最近地址的距离
     */
    public int getAddressDistance() {
        return this.addressDistance;
    }

    /**
     * 设置与最近地址的距离
     *
     * @param addressDistance 与最近地址的距离
     */
    public void setAddressDistance(int addressDistance) {
        this.addressDistance = addressDistance;
    }

    /**
     * 获取所在的城市信息
     *
     * @return 所在的城市信息
     */
    public String getCity() {
        return this.city;
    }

    /**
     * 设置所在的城市信息
     *
     * @param city 所在的城市信息
     */
    public void setCity(String city) {
        this.city = city;
    }
}