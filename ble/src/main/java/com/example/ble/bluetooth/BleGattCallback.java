
package com.example.ble.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;

import com.example.ble.exception.BleException;


public abstract class BleGattCallback extends BluetoothGattCallback {

    public abstract void onConnectSuccess(BluetoothGatt gatt, int status);

    @Override
    public abstract void onServicesDiscovered(BluetoothGatt gatt, int status);

    public abstract void onConnectFailure(BleException exception);

}