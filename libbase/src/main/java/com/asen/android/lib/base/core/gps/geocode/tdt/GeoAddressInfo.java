package com.asen.android.lib.base.core.gps.geocode.tdt;

/**
 * Simple to Introduction
 * 天地图地址信息
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class GeoAddressInfo {

    private String formattedAddress;

    private String road;

    private int roadDistance;

    private String poi;

    private int poiDistance;

    private String poiPosition;

    private String address;

    private String addressPosition;

    private int addressDistance;

    private String city;

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

    public String getFormattedAddress() {
        return this.formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getRoad() {
        return this.road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public int getRoadDistance() {
        return this.roadDistance;
    }

    public void setRoadDistance(int roadDistance) {
        this.roadDistance = roadDistance;
    }

    public String getPoi() {
        return this.poi;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }

    public int getPoiDistance() {
        return this.poiDistance;
    }

    public void setPoiDistance(int poiDistance) {
        this.poiDistance = poiDistance;
    }

    public String getPoiPosition() {
        return this.poiPosition;
    }

    public void setPoiPosition(String poiPosition) {
        this.poiPosition = poiPosition;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressPosition() {
        return this.addressPosition;
    }

    public void setAddressPosition(String addressPosition) {
        this.addressPosition = addressPosition;
    }

    public int getAddressDistance() {
        return this.addressDistance;
    }

    public void setAddressDistance(int addressDistance) {
        this.addressDistance = addressDistance;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}