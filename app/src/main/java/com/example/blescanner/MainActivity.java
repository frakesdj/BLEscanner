package com.example.blescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Btd> btdsNSorted = new ArrayList<Btd>();
    private ArrayList<Btd> btdsSorted = new ArrayList<Btd>();
    private HashMap<BluetoothDevice, Btd> btdHashMap = new HashMap<BluetoothDevice, Btd>();
    private boolean toggled;

    private BtdScanner scanner;
    private ListAdapter_BTD adapter;
    private Button scan;
    private ToggleButton sort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.scanner = new BtdScanner(this);
        scan = (Button) findViewById(R.id.scanner);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!scanner.isScanning()) {
                    scanner.start();
                }

            }
        });

        sort = (ToggleButton) findViewById(R.id.toggle);
        toggled = false;
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggled == false) {
                    toggled = true;
                    Collections.sort(btdsSorted);
                    adapter.setBtds(btdsSorted);
                }
                else {
                    toggled = false;
                    adapter.setBtds(btdsNSorted);
                }
                adapter.notifyDataSetChanged();
            }
        });

        RecyclerView recycler = findViewById(R.id.btdList);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter_BTD(this, btdsNSorted, recycler, scanner);
        recycler.setAdapter(adapter);
        adapter.setBtds(btdsNSorted);
    }

    public void addBtdList(BluetoothDevice dev, int rssi, boolean connectable) {
        if (btdHashMap.containsKey(dev)) {
            Btd holder = btdHashMap.get(dev);
            holder.setConnectable(connectable);
            holder.setRssi(rssi);
            Collections.sort(btdsSorted);
        } else {
            Btd holder = new Btd(dev, this, rssi, connectable);
            btdsNSorted.add(holder);
            btdsSorted.add(holder);
            btdHashMap.put(dev, holder);
            Collections.sort(btdsSorted);
        }
        adapter.notifyDataSetChanged();

    }

    public void clearLists() {
        btdsNSorted.clear();
        btdsSorted.clear();
        btdHashMap.clear();
        adapter.notifyDataSetChanged();
    }


}