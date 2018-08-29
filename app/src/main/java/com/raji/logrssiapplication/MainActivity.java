package com.raji.logrssiapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.livinglifetechway.quickpermissions.annotations.WithPermissions;
import com.opencsv.CSVWriter;
import com.raji.logrssiapplication.app.LogRssiApp;
import com.raji.logrssiapplication.db.Distance;
import com.raji.logrssiapplication.db.Result;
import com.raji.logrssiapplication.model.NetWorkData;
import com.raji.logrssiapplication.model.WifiStateObject;
import com.raji.logrssiapplication.service.WifiInfoService;
import com.raji.logrssiapplication.utils.WifiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView networkName, macAddress, rssiAddress;
    private EditText idTxt;
    private Button startBtn;

    private boolean isServiceStarted = false;

    private static final int SERVICE_RUNTIME = 2000;

    private List<NetWorkData> wifiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        setupViews();
        setupControllers();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void setupViews() {
        idTxt = findViewById(R.id.id);
        networkName = findViewById(R.id.networkName);
        macAddress = findViewById(R.id.macAddress);
        rssiAddress = findViewById(R.id.rssiTxt);
        startBtn = findViewById(R.id.startBtn);
        if (!isServiceStarted) {
            startBtn.setText(R.string.start);
        } else {
            startBtn.setText(R.string.stop);
        }
    }

    private void setupControllers() {
        init();
        addToWifiList();
        startBtn.setOnClickListener(v -> {
            onValuesReceived();
        });
    }

    private void onValuesReceived() {
        if (!isServiceStarted) {
            startBtn.setText(R.string.stop);
            switchWifiAndCalculate(0);

            isServiceStarted = !isServiceStarted;
            startBtn.setEnabled(false);
        }
    }

    private void addToWifiList() {
        wifiList = new ArrayList<>();
        wifiList.add(new NetWorkData("SSID", 55d, 3d, 8d));
        wifiList.add(new NetWorkData("P1", 45d, 6.5d, 0d));
        wifiList.add(new NetWorkData("sweet home", 54d, 8d, 6d));
    }


    private void switchWifiAndCalculate(int wifiIndex) {
        Intent intent = new Intent(this, WifiInfoService.class);
        Log.i("TAGG", "wifi network " + wifiList.get(wifiIndex));
        switchWifi(wifiList.get(wifiIndex).getNetworkname());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startWifiService(intent);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            stopWifiService(intent);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (wifiIndex + 1 < 3) {
                switchWifiAndCalculate(wifiIndex + 1);
            } else {
                startBtn.setText(R.string.start);
                isServiceStarted = !isServiceStarted;
                startBtn.setEnabled(true);
                calculate();
            }
        }, SERVICE_RUNTIME);
    }

    private void calculate() {
        LogRssiApp.getWifiDb().daoAccess().fetch()
                .map(this::calculateDandABCandXY)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    private List<Distance> calculateDandABCandXY(List<Result> results) {
        List<Distance> distanceList = new ArrayList<>();
        for (NetWorkData netWorkData : wifiList) {
            Pair<Integer, Integer> avgRssi = getMinMaxRssiValue(results, netWorkData.getNetworkname());


            Distance distance = new Distance();
            distance.ssid = netWorkData.getNetworkname();
            distance.bssid = netWorkData.getNetworkname();
            distance.minRssi = avgRssi.first;
            distance.maxRssi = avgRssi.second;
            distanceList.add(distance);
            Log.i("DISTANCE TAG", " min/max " + avgRssi);
        }
        export(idTxt.getText().toString(), distanceList);
        return distanceList;
    }

    private void export(String blockId, List<Distance> distanceList) {
        String csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/RssiTable.csv");
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(csv));

            List<String[]> data = new ArrayList<>();
            for (Distance distance : distanceList) {
                data.add(new String[]{blockId, distance.bssid, String.valueOf(distance.getMinRssi()), String.valueOf(distance.getMaxRssi())});
            }
            writer.writeAll(data);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pair<Integer, Integer> getMinMaxRssiValue(List<Result> results, String networkName) {
        int minValue = 0, maxValue = 0;
        for (Result result : results) {
            if (result.ssid.contains(networkName) && result.getRssiId() != -127) {
                minValue = result.getRssiId();
                maxValue = result.getRssiId();
                break;
            }
        }
        for (Result result : results) {
            if (result.ssid.contains(networkName) && result.getRssiId() != -127) {
                if (result.getRssiId() > minValue) {
                    minValue = result.getRssiId();
                }
                if (result.getRssiId() < minValue) {
                    maxValue = result.getRssiId();
                }
            }
        }

        return new Pair<>(minValue * -1, maxValue * -1);
    }

    private void switchWifi(String networkSSID) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equalsIgnoreCase("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                getWifiDetails();
                break;
            }
        }
    }

    @WithPermissions(
            permissions = {Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
            rationaleMessage = "Custom rational message",
            permanentlyDeniedMessage = "Custom permanently denied message"
    )
    private void init() {
        getWifiDetails();
    }

    private void getWifiDetails() {
        WifiUtils utils = new WifiUtils();
        WifiStateObject wifiStateObject = utils.getWifiState(getApplicationContext());
        setEvent(wifiStateObject);

    }

    private void setEvent(WifiStateObject event) {
        if (event != null) {
            rssiAddress.setText(String.format(Locale.getDefault(), "%d dBm", event.getRssiValue()));
            networkName.setText(event.getSsid());
            macAddress.setText(event.getBssid());
        }
    }


    @WithPermissions(
            permissions = {Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}
    )
    private void startWifiService(Intent intent) {
        startService(intent);
    }

    private void stopWifiService(Intent intent) {
        stopService(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWifiStateReceived(WifiStateObject event) {
        if (event != null) {
            setEvent(event);
        }
    }
}
