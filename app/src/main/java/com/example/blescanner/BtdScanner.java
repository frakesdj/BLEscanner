package com.example.blescanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class BtdScanner {
    private MainActivity main;

    private BluetoothAdapter bluetoothAdapter;
    private boolean scanning;
    private Handler handler = new Handler();
    private static final long SCAN_PERIOD = 10000;

    private BluetoothLeScanner bluetoothLeScanner;

    public BtdScanner(MainActivity main) {
        this.main = main;
        scanning = false;
        BluetoothManager bluetoothManager = (BluetoothManager) main.getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    //Point at which to start scan
    public void start() {
        scanLeDevice();
    }

    private void scanLeDevice() {
        if (!isScanning()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                }
            }, SCAN_PERIOD);
            scanning = true;
            main.clearLists();
            bluetoothLeScanner.startScan(leScanCallback);
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
        }
    }

    private ScanCallback leScanCallback = new ScanCallback() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            main.addBtdList(result.getDevice(), result.getRssi(), result.isConnectable());
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBatchScanResults(List < ScanResult > results) {
            super.onBatchScanResults(results);
            for (ScanResult result: results) {
                main.addBtdList(result.getDevice(), result.getRssi(), result.isConnectable());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    public boolean isScanning() {
        return scanning;
    }

}