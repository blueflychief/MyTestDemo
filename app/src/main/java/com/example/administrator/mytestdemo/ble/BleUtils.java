package com.example.administrator.mytestdemo.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by Administrator on 9/3/2016.
 */
public class BleUtils {

    public static boolean isBleSupport(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return false;
        }
        if (!context.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }
        return ((BluetoothManager) (context.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE))).getAdapter() != null;
    }

    /**
     * 获取BluetoothManager
     *
     * @param context
     * @return
     */
    public static BluetoothManager getBluetoothManager(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return (BluetoothManager) (context.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE));
        }
        return null;
    }

    /**
     * 获取BluetoothAdapter
     *
     * @param context
     * @return
     */
    public static BluetoothAdapter getBluetoothAdapter(Context context) {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) && (getBluetoothManager(context) != null)) {
            return getBluetoothManager(context).getAdapter();
        }
        return null;
    }
}
