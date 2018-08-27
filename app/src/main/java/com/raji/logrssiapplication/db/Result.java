package com.raji.logrssiapplication.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Result {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String bssid;
    public String ssid;
    public int rssiId;

    public Result() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getRssiId() {
        return rssiId;
    }

    public void setRssiId(int rssiId) {
        this.rssiId = rssiId;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", bssid='" + bssid + '\'' +
                ", ssid='" + ssid + '\'' +
                ", rssiId=" + rssiId +
                '}';
    }
}
