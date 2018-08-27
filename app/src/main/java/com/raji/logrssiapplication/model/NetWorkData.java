package com.raji.logrssiapplication.model;

import java.io.Serializable;

public class NetWorkData implements Serializable {
    private String networkname;
    private double txpower;
    private double a;
    private double b;

    public NetWorkData(String networkname, double txpower, double a, double b) {
        this.networkname = networkname;
        this.txpower = txpower;
        this.a = a;
        this.b = b;
    }

    public String getNetworkname() {
        return networkname;
    }

    public void setNetworkname(String networkname) {
        this.networkname = networkname;
    }

    public double getTxpower() {
        return txpower;
    }

    public void setTxpower(double txpower) {
        this.txpower = txpower;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "NetWorkData{" +
                "networkname='" + networkname + '\'' +
                ", txpower=" + txpower +
                ", a=" + a +
                ", b=" + b +
                '}';
    }


}