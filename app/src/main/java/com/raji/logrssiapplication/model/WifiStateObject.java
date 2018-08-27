package com.raji.logrssiapplication.model;

public class WifiStateObject {
    private String masAddress;
    private String ssid;
    private String bssid;
    private int rssiValue;


    public WifiStateObject(String masAddress, String ssid, String bssid, int rssiValue) {
        this.masAddress = masAddress;
        this.ssid = ssid;
        this.bssid = bssid;
        this.rssiValue = rssiValue;
    }

    public int getRssiValue() {
        return rssiValue;
    }

    public void setRssiValue(int rssiValue) {
        this.rssiValue = rssiValue;
    }

    public String getMasAddress() {
        return masAddress;
    }

    public void setMasAddress(String masAddress) {
        this.masAddress = masAddress;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }
}
