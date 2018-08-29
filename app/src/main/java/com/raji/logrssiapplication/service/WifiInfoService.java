package com.raji.logrssiapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.raji.logrssiapplication.app.LogRssiApp;
import com.raji.logrssiapplication.db.Result;
import com.raji.logrssiapplication.model.WifiStateObject;
import com.raji.logrssiapplication.utils.WifiUtils;

import org.greenrobot.eventbus.EventBus;

public class WifiInfoService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        handleInit();
        return START_STICKY;
    }

    Handler handler = new Handler();
    private Runnable runnable = null;
    private long runTime = 200L;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void handleInit() {
        handler = new Handler();
        runnable = () -> {
            init();
            handler.postDelayed(runnable, runTime);
        };
        handler.post(runnable);
    }

    private void init() {
        WifiUtils utils = new WifiUtils();
        WifiStateObject wifiStateObject = utils.getWifiState(getApplicationContext());
        Result result = new Result();
        result.setSsid(wifiStateObject.getSsid());
        result.setBssid(wifiStateObject.getMasAddress());
        result.setRssiId(wifiStateObject.getRssiValue());
        new Thread(() -> LogRssiApp.getWifiDb().daoAccess().insert(result)).start();
        EventBus.getDefault().post(wifiStateObject);
    }

    @Override
    public void onDestroy() {
        Log.i("TAG", "onDestroy");
        handler.removeCallbacks(runnable);
        super.onDestroy();

    }


}
