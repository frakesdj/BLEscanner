package com.example.blescanner;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter_BTD extends RecyclerView.Adapter < ListAdapter_BTD.ViewHolder > {

    private Context context;
    private ArrayList < Btd > btds = new ArrayList < Btd > ();
    private RecyclerView rvProgram;
    private boolean isConnecting = false;
    private BtdScanner scanner;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView rssi;
        TextView connectable;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameId);
            rssi = itemView.findViewById(R.id.rssiID);
            connectable = itemView.findViewById(R.id.connectableID);
        }
    }
    public ListAdapter_BTD(@NonNull Context context, ArrayList < Btd > btds, RecyclerView rvProgram, BtdScanner scanner) {
        this.context = context;
        this.btds = btds;
        this.rvProgram = rvProgram;
        this.scanner = scanner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.btd_item, parent, false);
        view.setOnClickListener(new MyOnClickListener());
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Btd item = btds.get(position);
        holder.name.setText(item.getName());
        holder.rssi.setText(Integer.toString(item.getRssi()));
        String con;
        if (item.isConnectable()) {
            con = "Connectable";
        } else {
            con = "Not Connectable";
        }
        holder.connectable.setText(con);
    }

    public void setBtds(ArrayList < Btd > devices) {
        btds = devices;
    }

    @Override
    public int getItemCount() {
        return btds.size();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            int itemPos = rvProgram.getChildLayoutPosition(v);
            Btd item = btds.get(itemPos);
            if (item.isConnectable() && item.getConnection().equals("disconnected") && !isConnecting && !scanner.isScanning()) {
                isConnecting = true;
                Toast toast = Toast.makeText(context, "connecting", Toast.LENGTH_LONG);
                toast.show();
                item.setGatt(item.getDevice().connectGatt(context, false, bluetoothGattCallback));
            } else if (item.getConnection().equals("connected") && !isConnecting && !scanner.isScanning()) {
                isConnecting = true;
                item.getGatt().disconnect();
            }
        }
    }

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("success", "Connected to GATT Server");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("failure", "disconnected from the GATT Server");
                gatt.close();
            } else {
                gatt.close();
            }
            isConnecting = false;
        }
    };

}