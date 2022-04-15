package com.example.blescanner;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import androidx.annotation.MainThread;

import java.util.List;

public class Btd implements Comparable {

    private BluetoothDevice device;
    private String name;
    private int rssi;
    private MainActivity main;
    private boolean connectable;
    private BluetoothGatt gatt;


    public Btd(BluetoothDevice bluetoothDevice, MainActivity main, int rssi, boolean connectable) {
        this.device = bluetoothDevice;
        this.name = bluetoothDevice.getName();
        this.main = main;
        this.rssi = rssi;
        this.connectable = connectable;
        if (this.name == null) {
            this.name = bluetoothDevice.getAddress();
        }
    }

    @Override
    public int compareTo(Object o) {
        Btd holder = (Btd) o;
        if (this.getRssi() < (holder.getRssi())) {
            return 1;
        } else if (this.getRssi() == (holder.getRssi())) {
            return 0;
        } else {
            return -1;
        }
    }

    //Getter and Setters
    //GetConnection uses bluetoothManager to get devices that the phone is connected to.
    public String getConnection() {
        BluetoothManager bluetoothManager = (BluetoothManager) main.getSystemService(Context.BLUETOOTH_SERVICE);
        List < BluetoothDevice > listOfDevices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
        for (BluetoothDevice d: listOfDevices) {
            if (d.getAddress().equals(device.getAddress())) {
                return "connected";
            }
        }
        return "disconnected";
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setConnectable(boolean connectable) {
        this.connectable = connectable;
    }
    public boolean isConnectable() {
        return connectable;
    }

    public String getName() {
        return name;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
    public int getRssi() {
        return rssi;
    }

    public void setGatt(BluetoothGatt g) {
        gatt = g;
    }
    public BluetoothGatt getGatt() {
        return gatt;
    }


}

