package com.example.administrator.mytestdemo.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.elvishew.xlog.KLog;

import java.io.IOException;

/**
 * This thread runs while attempting to make an outgoing connection
 * with a device. It runs straight through; the connection either
 * succeeds or fails.
 * Created by Administrator on 9/3/2016.
 */
public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private String mSocketType;
    private int mState;
    private BluetoothAdapter mAdapter;
    private AcceptCallback mAcceptCallback;

    public ConnectThread(BluetoothDevice device, boolean secure, int state, BluetoothAdapter adapter, AcceptCallback acceptCallback) {
        mmDevice = device;
        BluetoothSocket tmp = null;
        mSocketType = secure ? "Secure" : "Insecure";
        mState = state;
        mAdapter = adapter;
        mAcceptCallback=acceptCallback;

        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            if (secure) {
                tmp = device.createRfcommSocketToServiceRecord(Constants.MY_UUID_SECURE);
            } else {
                tmp = device.createInsecureRfcommSocketToServiceRecord(Constants.MY_UUID_INSECURE);
            }
        } catch (IOException e) {
            KLog.i("Socket Type: " + mSocketType + "create() failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        KLog.i("BEGIN mConnectThread SocketType:" + mSocketType);
        setName("ConnectThread" + mSocketType);

        // Always cancel discovery because it will slow down a connection
        mAdapter.cancelDiscovery();

        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mmSocket.connect();
        } catch (IOException e) {
            // Close the socket
            try {
                mmSocket.close();
            } catch (IOException e2) {
                KLog.i("unable to close() " + mSocketType +
                        " socket during connection failure", e2);
            }
            mAcceptCallback.onConnectionFailed();
            return;
        }

        // Reset the ConnectThread because we're done
        synchronized (this) {
            mAcceptCallback.onResetConnection();
        }

        // Start the connected thread
        mAcceptCallback.onConneted(mmSocket, mmDevice, mSocketType);
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            KLog.i("close() of connect " + mSocketType + " socket failed", e);
        }
    }
}
