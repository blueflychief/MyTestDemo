package com.example.administrator.mytestdemo.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import com.example.administrator.mytestdemo.util.KLog;

import java.io.IOException;

/**
 * /**
 * This thread runs while listening for incoming connections. It behaves
 * like a server-side client. It runs until a connection is accepted
 * (or until cancelled).
 * Created by Administrator on 9/3/2016.
 */
public class AcceptThread extends Thread {
    // The local server socket
    private final BluetoothServerSocket mmServerSocket;
    private String mSocketType;
    private int mState;
    private BluetoothAdapter mAdapter;
    private AcceptCallback mAcceptCallback;

    public AcceptThread(boolean secure, int state, BluetoothAdapter adapter, AcceptCallback acceptCallback) {
        BluetoothServerSocket tmp = null;
        mSocketType = secure ? "Secure" : "Insecure";
        mState = state;
        mAdapter = adapter;
        mAcceptCallback = acceptCallback;
        // Create a new listening server socket
        try {
            if (secure) {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(Constants.NAME_SECURE, Constants.MY_UUID_SECURE);
            } else {
                tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
                        Constants.NAME_INSECURE, Constants.MY_UUID_INSECURE);
            }
        } catch (IOException e) {
            KLog.i("Socket Type: " + mSocketType + "listen() failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        KLog.i("Socket Type: " + mSocketType + "BEGIN mAcceptThread" + this);
        setName("AcceptThread" + mSocketType);
        BluetoothSocket socket = null;
        // Listen to the server socket if we're not connected
        while (mState != Constants.STATE_CONNECTED) {
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                KLog.i("Socket Type: " + mSocketType + "accept() failed", e);
                break;
            }

            // If a connection was accepted
            if (socket != null) {
                synchronized (this) {
                    switch (mState) {
                        case Constants.STATE_LISTEN:
                        case Constants.STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
                            mAcceptCallback.onConneted(socket, socket.getRemoteDevice(), mSocketType);
                            break;
                        case Constants.STATE_NONE:
                        case Constants.STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                                KLog.i("Could not close unwanted socket", e);
                            }
                            break;
                    }
                }
            }
        }
        KLog.i("END mAcceptThread, socket Type: " + mSocketType);

    }

    public void cancel() {
        KLog.i("Socket Type" + mSocketType + "cancel " + this);
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            KLog.i("Socket Type" + mSocketType + "close() of server failed", e);
        }
    }
}
