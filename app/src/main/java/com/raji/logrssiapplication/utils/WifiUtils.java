package com.raji.logrssiapplication.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.raji.logrssiapplication.model.WifiStateObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class WifiUtils {

    public WifiStateObject getWifiState(Context context) {
        //To prevent memory leaks on devices prior to Android N,
        //retrieve WifiManager with
        //getApplicationContext().getSystemService(Context.WIFI_SERVICE),
        //instead of getSystemService(Context.WIFI_SERVICE)
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {

                String ipAddress = getMacAddr();
                return new WifiStateObject(ipAddress, wifiInfo.getSSID(), wifiInfo.getBSSID(), wifiInfo.getRssi());


            }
        }
        return null;
    }

    public String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    //res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    //    private val resultsData = ArrayList<ResultData>()
//
//    private fun newFun() {
//        val wifi = applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
//
//        val results = wifi.scanResults
//        Log.i("onWifiStateReceived", "new Fun $results")
//        for (i in results.indices) {
//            val ssid0 = results[i].SSID
//            val bssid = results[i].BSSID
//
//            val rssi0 = results[i].level
//            var found = false
//            for (pos in 0 until resultsData.size) {
//                Log.i("onWifiStateReceived", "new Fun ${resultsData[pos].router.bssid}")
//                if (resultsData[pos].router.bssid == bssid) {
//                    found = true
//                    resultsData[pos].values.add(rssi0)
//                    Log.i("onWifiStateReceived", "new Fun found ${bssid}")
//                    break
//                }
//            }
//            if (!found) {
//
//                val data = ResultData(Router(ssid0, bssid))
//                data.values.add(rssi0)
//                resultsData.add(data)
//                Log.i("onWifiStateReceived", "new Fun added ${rssi0}")
//            }
//            // String rssiString0 = String.valueOf(rssi0);
//            // textStatus = textStatus.concat("\n" + ssid0 + "   " +
//            // rssiString0);
//            // System.out.println("ajsdhks"+textStatus);
//        }
//    }
}
