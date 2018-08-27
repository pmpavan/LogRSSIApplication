package com.raji.logrssiapplication.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Distance {

    @PrimaryKey
    @NotNull
    public String bssid;
    public String ssid;
    public double minRssi;
    public double maxRssi;
    public Distance() {
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public double getMinRssi() {
        return minRssi;
    }

    public void setMinRssi(double minRssi) {
        this.minRssi = minRssi;
    }

    public double getMaxRssi() {
        return maxRssi;
    }

    public void setMaxRssi(double maxRssi) {
        this.maxRssi = maxRssi;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "bssid='" + bssid + '\'' +
                ", ssid='" + ssid + '\'' +
                ", minRssi=" + minRssi +
                ", maxRssi=" + maxRssi +
                '}';
    }
}
