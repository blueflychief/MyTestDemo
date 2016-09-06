package com.example.administrator.mytestdemo.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * Created by Administrator on 9/3/2016.
 */
public interface AcceptCallback {
    void onConneted(BluetoothSocket socket, BluetoothDevice device, final String socketType);

    void onConnectionFailed();

    void onResetConnection();

    void onConnectionLost();
}
