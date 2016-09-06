package com.example.administrator.mytestdemo.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.ble.bean.UserInfoEntity;
import com.example.administrator.mytestdemo.util.KLog;
import com.example.administrator.mytestdemo.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchBleActivity extends AppCompatActivity {

    public static final int STATE_DISCONNECTING = 3;
    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");


    private BluetoothGatt mGatt;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "action_gatt_connected";
    public final static String ACTION_GATT_DISCONNECTED = "action_gatt_disconnected";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "action_gatt_services_discovered";
    public final static String ACTION_DATA_AVAILABLE = "action_data_available";
    public final static String EXTRA_DATA = "extra_data";
    public final static String ACTION_DEVICE_DOES_NOT_SUPPORT = "action_device_does_not_support";
    public static final String ACTION_UPDATE_REAL_TIME_HB = "action_update_real_time_hb";
    public static final String ACTION_SEND_M4_BIN_FILE = "action_send_m4_bin_file";
    public static final String ACTION_STATIC_HEART_BEATS = "action_static_heart_beats";
    public static final String SPEND_KCAL = "spend_kcal";
    public static final String COMPLETE_DEGREE = "complete_degree";
    public static final String COMPLETE_COUNTS = "complete_counts";
    public static final String ACTION_STOP_REALTIME_FINISH_COURSE = "action_stop_realtime_finish_course";
    public static final String ACTION_SET_CLOCK = "action_set_clock";

    public static final String ACTION_GET_SOURCE_LENGTH = "action_get_source_length";
    public static final String ACTION_GET_SOURCE_DATA = "action_get_source_data";
    public static final String ACTION_SYNC_STEP_DATA_START = "action_sync_step_data_start";
    public static final String ACTION_SYNC_STEP_DATA_FINISH = "action_sync_step_data_finish";
    public static final String ACTION_SYNC_STEP_DATA_FAILURE = "action_sync_step_data_failure";

    public static final String SENDM4FAILURE = "sendM4Failure";
    public static final String SENDM4SUCCESS = "sendM4Success";
    public static final String FIRST_PACKAGE_FAILED = "first_package_failed";

    public static final String CONFIG_USER_INFO_SUCCESS = "config_user_info_success";
    public static final String CONFIG_USER_INFO_FAILED = "config_user_info_failed";
    public static final String START_ACTION_IDENTITY_SUCCESS = "start_action_identity_success";
    public static final String START_ACTION_IDENTITY_FAILURE = "start_action_identity_failure";


    public static final String ACTION_SURPLUS_CAPATITY = "action_surplus_capatity";
    public static final String ACTION_RUN_MODE_HB = "action_run_mode_hb";
    public static final String ACTION_RUN_MODE_STEP = "action_run_mode_step";
    public static final String ACTION_RUN_MODE_KCAL = "action_run_mode_kcal";
    public static final String ACTION_CHECK_USER_INFO = "action_check_user_info";
    public static final String ACTION_CHECK_DFU_VERSION = "action_check_dfu_version";
    public static final String ACTION_CHECK_M4_VERSION = "action_check_m4_version";
    public static final String ACTION_CHECK_DEVICE_ID = "action_check_device_id";

    private Button bt_search;
    private ProgressBar pb_progress;
    private ListView lv_devices;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;

    private int mConnectionState;
    private List<String> mDevices;
    private ArrayAdapter mArrayAdapter;
    private BluetoothChatService mChatService;
    private String mConnectedDeviceName;
    private BluetoothGattService mRXService;
    private BluetoothGattCharacteristic mTXGattCharacteristic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ble);
        bt_search = (Button) findViewById(R.id.bt_search);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
        lv_devices = (ListView) findViewById(R.id.lv_devices);
        mDevices = new ArrayList<>();

        mChatService = new BluetoothChatService(this, mHandler);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBluetooth();
            }


        });
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDevices);
        lv_devices.setAdapter(mArrayAdapter);
        lv_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBluetoothAdapter.cancelDiscovery();
                connect(mDevices.get(position).split("-")[1]);
//                connectDevice(mDevices.get(position).split("-")[1],true);
            }
        });
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            KLog.i("-----STATE_CONNECTED" + STATE_CONNECTED);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            KLog.i("-----STATE_CONNECTING" + STATE_CONNECTING);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                            KLog.i("-----STATE_LISTEN" + BluetoothChatService.STATE_LISTEN);
                            break;
                        case BluetoothChatService.STATE_NONE:
                            KLog.i("-----STATE_NONE" + BluetoothChatService.STATE_NONE);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    KLog.i("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    KLog.i(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_CONNECTED_DEVICE_NAME:
                    KLog.i("-----" + mConnectedDeviceName);
                    break;
                case Constants.MESSAGE_TOAST:
                    KLog.i("-----" + msg.getData().getString(Constants.TOAST));
                    break;
            }
        }
    };

    private void openBluetooth() {
        if (!BleUtils.isBleSupport(this)) {
            ToastUtils.showToast("该设备不支持BLE");
            return;
        }
        bluetoothManager = BleUtils.getBluetoothManager(this);
        if (bluetoothManager != null) {
            mBluetoothAdapter = BleUtils.getBluetoothAdapter(this);
        }
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                //开启蓝牙
//                mBluetoothAdapter.enable();
                mDevices.clear();
                mArrayAdapter.notifyDataSetChanged();
                Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enabler, 0x1);
            } else {
                registerBluetoothScan();
            }

        }
    }

    /**
     * 确保蓝牙可被发现
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0x1:
                if (mBluetoothAdapter.isEnabled()) {
                    registerBluetoothScan();
//                    mBluetoothAdapter.startLeScan(mLeScanCallback);
                }
                break;
        }
    }

    private void registerBluetoothScan() {
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                KLog.i("-------发现设备：NAME:" + device.getName() + "---MAC:" + device.getAddress());

                if (pb_progress.getVisibility() != View.VISIBLE) {
                    pb_progress.setVisibility(View.VISIBLE);
                }
                if (!mDevices.contains(device.getName() + "-" + device.getAddress())) {
                    mDevices.add(device.getName() + "-" + device.getAddress());
                    mArrayAdapter.notifyDataSetChanged();
                }
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    //未配对设备
                } else {
                    KLog.i("-------已配对设备：NAME:" + device.getName() + "---MAC:" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                KLog.i("------------发现结束");
                pb_progress.setVisibility(View.INVISIBLE);
            }
        }
    };

    BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            KLog.i("-------发现设备：" + device.getAddress());
            if (!TextUtils.isEmpty(device.getAddress()) && !mDevices.contains(device.getAddress())) {
                mDevices.add(device.getAddress());
            }
//            connect(device.getAddress());
        }
    };


    private void connectDevice(String address, boolean secure) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */


    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            KLog.i("-------BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        // Previously connected device. Try to reconnect. (先前连接的设备。 尝试重新连接)  
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            KLog.i("-------Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            KLog.i("-------Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the  
        // autoConnect  
        // parameter to false.  
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback); //创建GATT服务器的本地代理实例，用于与连接的蓝牙设备通信
        KLog.i("-------Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    private BluetoothGattCharacteristic mRXGattCharacteristic;
    @SuppressLint("NewApi")
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override  //当连接上设备或者失去连接时会回调该函数  
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) { //连接成功  
                mBluetoothGatt.discoverServices(); //连接成功后就去找出该设备中的服务 private BluetoothGatt mBluetoothGatt;
                KLog.i("-------Trying to discoverServices.");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //连接失败  
            }
        }

        @Override  //当设备是否找到服务时，会回调该函数
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {   //找到服务了  
                //在这里可以对服务进行解析，寻找到你需要的服务
                List<BluetoothGattService> bluetoothGattServices = gatt.getServices();
                for (BluetoothGattService service : bluetoothGattServices) {
                    KLog.i("-------service:" + service.getUuid() + "--" + service.getType());
                }

                mRXService = gatt.getService(RX_SERVICE_UUID);
                if (mRXService != null) {
                    KLog.i("-------mRXService_uuid:" + mRXService.getUuid());
                    mTXGattCharacteristic = mRXService.getCharacteristic(TX_CHAR_UUID);
                    if (mTXGattCharacteristic != null) {
                        KLog.i("-------mTXGattCharacteristic_uuid:" + mTXGattCharacteristic.getUuid());

                        gatt.setCharacteristicNotification(mTXGattCharacteristic, true);
                        BluetoothGattDescriptor descriptor = mTXGattCharacteristic.getDescriptor(CCCD);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        gatt.writeDescriptor(descriptor);
                        mRXGattCharacteristic = mRXService.getCharacteristic(RX_CHAR_UUID);
                        mRXGattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);

                    }
                }

            } else {
                KLog.i("-------onServicesDiscovered received: " + status);
            }
        }

        @Override  //当读取设备时会回调该函数
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            KLog.i("-------onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //读取到的数据存在characteristic当中，可以通过characteristic.getValue();函数取出。然后再进行解析操作。  
                //int charaProp = characteristic.getProperties();if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)表示可发出通知。  判断该Characteristic属性  
            }
        }

        @Override //当向设备Descriptor中写数据时，会回调该函数  
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            KLog.i("-------onDescriptorWriteonDescriptorWrite = " + status + ", descriptor =" + descriptor.getUuid().toString());
        }

        @Override //设备发出通知时会调用到该接口  
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic.getValue() != null) {
                KLog.i("--------" + characteristic.getStringValue(0));
            }
            KLog.i("--------onCharacteristicChanged-----");

            onReadcharCallback(characteristic);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            KLog.i("-------rssi = " + rssi);
        }

        @Override //当向Characteristic写数据时会回调该函数
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            KLog.i("--------write success----- status:" + status);
        }
    };


    @Override
    public void onBackPressed() {
        mBluetoothAdapter.disable();
        this.unregisterReceiver(mReceiver);
        super.onBackPressed();
    }


    private void sendLocalBroadcast(final String action) {
        final Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastIntentUpdate(Intent intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendLocalBroadcast(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (TX_CHAR_UUID.equals(characteristic.getUuid())) {
            // KLog.d(String.format("Received TX: %d",characteristic.getValue() ));
            intent.putExtra(EXTRA_DATA, characteristic.getValue());
        } else {

        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    byte[] STM4_BinFileData;
    int M4BinFilelength;
    int rxOKcount;
    int rxFailcount;
    Intent sendM4Bin = new Intent(ACTION_SEND_M4_BIN_FILE);
    int sendM4BinFilenumber;
    boolean isSendingM4 = false;
    //    private String fileName = "allCommand".concat(DataCreator.getFileTime()).concat(".txt")  ;
//    public String sourceData = "sourceData".concat(DataCreator.getFileTime()).concat(".txt")  ;
//    public String sourceHeartBeat = "sourceHeartBeat".concat(DataCreator.getFileTime()).concat(".txt")  ;
//    public String sourcePluse = "sourcePluse".concat(DataCreator.getFileTime()).concat(".txt")  ;
    boolean isAlreadyInitDb = false;
//    EquipDatabase database = null ;

    private void onReadcharCallback(BluetoothGattCharacteristic characteristic) {

        sendLocalBroadcast(ACTION_DATA_AVAILABLE, characteristic);
        byte[] data = characteristic.getValue();
        KLog.e("-----onReadcharCallback---Read data:[" + data + "]-------------");
        KLog.e("-----onReadcharCallback---Read data:[" + refreshWRContent(data) + "]-------------");


        if (data[0] == 0x1f) { //3.2.25.同步步数数据协议

            if (!isAlreadyInitDb) {
//                database = new EquipDatabase(getApplicationContext());
                isAlreadyInitDb = true;
                broadcastIntentUpdate(new Intent(ACTION_SYNC_STEP_DATA_START)); // 数据同步开始
            }

            int equipUserId = DataUtils.byteArrayToInt(new byte[]{data[1], data[2], data[3], data[4]});
            int testTime = DataUtils.byteArrayToInt(new byte[]{data[5], data[6], data[7], data[8]}); // 测量时间
            int steps = DataUtils.byteArrayToInt(new byte[]{data[9], data[10]}); // 步数
            int kcal = DataUtils.byteArrayToInt(new byte[]{data[11], data[12]}); // 卡路里
            int distance = DataUtils.byteArrayToInt(new byte[]{data[13], data[14]});// 距离

            KLog.i("KKK", "同步数据 result = " + "userId : "  + " ; testTime = " + testTime + " ; steps = " + steps + " ; kcal = " + kcal + " ; distance = " + distance);

//            if (testTime == 0 && steps == 0 && kcal == 0 && distance == 0) { // 表示数据已全部返回完毕
////                database = null; // 释放
//                isAlreadyInitDb = false;
//                KLog.i("KKK", "数据同步成功...");
////                currentUserid = 0;
//                broadcastIntentUpdate(new Intent(ACTION_SYNC_STEP_DATA_FINISH)); // 数据全部同步完成
//            } else {
////                EquipSyncDataModel esdm = new EquipSyncDataModel();
////                esdm.setUserId(currentUserid + "");
//                if (testTime != 0) {
//                    SimpleDateFormat SDF_SERVER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    try {
//                        String tempTime = String.valueOf(testTime);
//                        Long timestamp = Long.parseLong(tempTime) * 1000;
//                        String date = SDF_SERVER.format(new Date(timestamp));
//                        esdm.setDate(date);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        esdm.setDate(testTime + "");
//                    }
//                } else {
//                    esdm.setDate(testTime + "");
//                }
//                esdm.setSteps(steps + "");
//                esdm.setDistance(distance + "");
//                esdm.setKcal(kcal + "");
//                database.insertEquipRecord(esdm);
//            }
        }


        if (data[0] == 0x1b) { // deviceId返回
            int deviceId = DataUtils.byteArrayToInt(new byte[]{data[1], data[2], data[3], data[4]});
            KLog.i("-----收到0x1b——deviceId：" + deviceId);
            Intent intent = new Intent(ACTION_CHECK_DEVICE_ID);
            intent.putExtra(ACTION_CHECK_DEVICE_ID, deviceId);
            broadcastIntentUpdate(intent);
        }

        if (data[0] == 0x10) { // M4版本返回
            int m4Start = DataUtils.byteArrayToInt(new byte[]{data[1]});
            int m4End = DataUtils.byteArrayToInt(new byte[]{data[2]});

            StringBuilder m4Sb = new StringBuilder();
            m4Sb.append(m4Start);
            m4Sb.append(".");
            m4Sb.append(m4End);

            KLog.i("-----收到0x10——M4版本返回：" + m4Sb.toString());
            Intent intent = new Intent(ACTION_CHECK_M4_VERSION);
            intent.putExtra(ACTION_CHECK_M4_VERSION, m4Sb.toString());
            broadcastIntentUpdate(intent);
            KLog.i("KKK", "腕表返回M4版本号为" + m4Sb.toString());
        }

        if (data[0] == 0x17) { // 查询DFU版本号
            int dfuStart = DataUtils.byteArrayToInt(new byte[]{data[1]});
            int dfuEnd = DataUtils.byteArrayToInt(new byte[]{data[2]});

            StringBuilder dfuSb = new StringBuilder();
            dfuSb.append(dfuStart);
            dfuSb.append(".");
            dfuSb.append(dfuEnd);
            Intent intent = new Intent(ACTION_CHECK_DFU_VERSION);
            intent.putExtra(ACTION_CHECK_DFU_VERSION, dfuSb.toString());
            broadcastIntentUpdate(intent);
            KLog.i("----收到0x17——腕表返回Nordic版本号为：" + dfuSb.toString());
        }


        if (data[0] == 0x14) { // 查询用户信息

            int userId = DataUtils.byteArrayToInt(new byte[]{data[1], data[2], data[3], data[4]});
            int height = DataUtils.byteArrayToInt(new byte[]{data[5]});

            // 传输时两个byte分别小时小数点前和小数点后数据
            float weightStart = DataUtils.byteArrayToInt(new byte[]{data[6]});
            float weightEnd = (DataUtils.byteArrayToInt(new byte[]{data[7]})) / 100;

            float finalweight = weightStart + weightEnd;
            int age = DataUtils.byteArrayToInt(new byte[]{data[8]});
            int sex = DataUtils.byteArrayToInt(new byte[]{data[9]}); // 分别用1表示男，2表示女
            int staticHb = DataUtils.byteArrayToInt(new byte[]{data[10]});

            UserInfoEntity uie = new UserInfoEntity();
            uie.setUser_id(userId);

            if (userId != 0) { //只在设置过用户信息时携带相关信息
                uie.setAge(age);
                uie.setHeight(height);
                uie.setSex(sex);
                uie.setWeight(finalweight);
                uie.setStaticHb(staticHb);
            }
            KLog.i("----收到0x14——查询用户信息：" + uie.toString());
            Intent intent = new Intent(ACTION_CHECK_USER_INFO);
            intent.putExtra(ACTION_CHECK_USER_INFO, uie);
            broadcastIntentUpdate(intent);
        }


        if (data[0] == 0x0d) { // 获取静止心率数据

        }

        if (data[0] == 0x06) { // 获取运动心率数据
            if (data[1] == 0x00) { // 代表获取心率段数

            }

            if (data[1] == 0x01) { //查询电量


            }
        }

        if (data[0] == 0x1c) { // 查询剩余电量
            int capatity = DataUtils.byteArrayToInt(new byte[]{data[1]});
            KLog.i("KKK", "当前电量为：" + capatity + "%");
            Intent intent = new Intent(ACTION_SURPLUS_CAPATITY);
            intent.putExtra(ACTION_SURPLUS_CAPATITY, capatity);
            broadcastIntentUpdate(intent);
        }


        if (data[0] == 0x11) { //  获取动作原始数据长度
            Intent intent = new Intent(ACTION_GET_SOURCE_LENGTH);
            StringBuilder sb = new StringBuilder();
            sb.append(DataUtils.byteArrayToInt(new byte[]{data[1], data[2]}));
            intent.putExtra(ACTION_GET_SOURCE_LENGTH, sb.toString());
            broadcastIntentUpdate(intent);
        }

        if (data[0] == 0x12) { // 获取动作原始数据命令
            Intent intent = new Intent(ACTION_GET_SOURCE_DATA);
            StringBuilder sb = new StringBuilder();
//            sb.append(" 动作原始数据： ").append(DataCreator.getCurrentTime()+ "\n");
            for (int i = 1; i < data.length - 1; i += 2) { // 只取出2-19总共18个字节就好了
                int temp = DataUtils.byteArrayToInt(new byte[]{data[i], data[i + 1]});
                short y = (short) temp;
                sb.append(y).append("\n");
            }

//            FileUtils.saveAllDataToFile(sourceData,sb.toString());
            intent.putExtra(ACTION_GET_SOURCE_DATA, sb.toString());
            broadcastIntentUpdate(intent);
        }


        if (data[0] == 0x18) { // 查询跑步模式下的心率

            //0 : 退出跑步模式0x00 , 1: 进入跑步模式，开启心率模块0x01 , 2 . 获取实时步数、实时心率0x02
            if (data[1] == 0x01) { // 进入跑步模式，开启心率模块
                int step = DataUtils.byteArrayToInt(new byte[]{data[2], data[3], data[4]});
                int temp = DataUtils.byteArrayToInt(new byte[]{data[5]});
                int kcal = DataUtils.byteArrayToInt(new byte[]{data[6], data[7]});
                Intent intent = new Intent(ACTION_RUN_MODE_HB);
                intent.putExtra(ACTION_RUN_MODE_HB, temp);
                intent.putExtra(ACTION_RUN_MODE_STEP, step);
                intent.putExtra(ACTION_RUN_MODE_KCAL, kcal);
                broadcastIntentUpdate(intent);
            } else if (data[1] == 0x02) { //获取实时步数、实时心率
                int step = DataUtils.byteArrayToInt(new byte[]{data[2], data[3], data[4]});
                int temp = DataUtils.byteArrayToInt(new byte[]{data[5]});
                int kcal = DataUtils.byteArrayToInt(new byte[]{data[6], data[7]});
                Intent intent = new Intent(ACTION_RUN_MODE_HB);
                intent.putExtra(ACTION_RUN_MODE_HB, temp);
                intent.putExtra(ACTION_RUN_MODE_STEP, step);
                intent.putExtra(ACTION_RUN_MODE_KCAL, kcal);
                broadcastIntentUpdate(intent);
            } else if (data[1] == 0x00) { // 退出跑步模式0x00

            }
        }

        if (data[0] == 0x09) { // 配置用户锻炼信息
            broadcastIntentUpdate(new Intent(data[5] == 0x01 ? CONFIG_USER_INFO_SUCCESS : CONFIG_USER_INFO_FAILED));
        } else if (data[0] == 0x0e) {  // 动作识别
            broadcastIntentUpdate(new Intent(data[5] == 0x01 ? START_ACTION_IDENTITY_SUCCESS : START_ACTION_IDENTITY_FAILURE));
        } else if (data[0] == 0x0a) { // 动作完成度 时时心率
            StringBuilder sb = new StringBuilder();
            sb.append("心率-")
                    .append(DataUtils.byteArrayToInt(new byte[]{data[5]}))
                    .append(" ");
            sb.append("原始脉冲-")
                    .append(DataUtils.byteArrayToInt(new byte[]{data[6]}))
                    .append(" ")
                    .append(DataUtils.byteArrayToInt(new byte[]{data[7]}))
                    .append(" ")
                    .append(DataUtils.byteArrayToInt(new byte[]{data[8]}))
                    .append(" ")
                    .append(DataUtils.byteArrayToInt(new byte[]{data[9]}))
                    .append(" ");
            sb.append("消耗-")
                    .append(DataUtils.byteArrayToInt(new byte[]{data[10], data[11]}))
                    .append(" ");
            sb.append("编码-")
                    .append(" ")
                    .append(DataUtils.byteArrayToInt(new byte[]{data[12], data[13]}))
                    .append(" ");
            sb.append("完成数-")
                    .append(" ")
                    .append(DataUtils.byteArrayToInt(new byte[]{data[14]}))
                    .append(" ");
            sb.append("完成程度-")
                    .append(" ")
                    .append(DataUtils.byteArrayToInt(new byte[]{data[15]/*, data[16],data[17], data[18]*/}))
                    .append(" ");
            sb.append("静止标识-")
                    .append(" ")
                    .append(DataUtils.byteTohex(data[19]))
                    .append(" ");
            Intent intent = new Intent(ACTION_UPDATE_REAL_TIME_HB);
            intent.putExtra(ACTION_UPDATE_REAL_TIME_HB, sb.toString());
            broadcastIntentUpdate(intent);
//            FileUtils.writeToLocalFile(data,sourceHeartBeat,sourcePluse);

            String heartBeats = String.valueOf(DataUtils.byteArrayToInt(new byte[]{data[5]}));
            String spendKcal = String.valueOf(DataUtils.byteArrayToInt(new byte[]{data[10], data[11]}));
            int completeDegree = DataUtils.byteArrayToInt(new byte[]{data[15]/*, data[16],data[17], data[18]*/});
            int completeCounts = DataUtils.byteArrayToInt(new byte[]{data[14]});

            Intent sendData = new Intent(ACTION_STATIC_HEART_BEATS);
            sendData.putExtra(ACTION_STATIC_HEART_BEATS, heartBeats);
            sendData.putExtra(SPEND_KCAL, spendKcal);
            sendData.putExtra(COMPLETE_DEGREE, completeDegree);
            sendData.putExtra(COMPLETE_COUNTS, completeCounts);
            broadcastIntentUpdate(sendData);
            KLog.i("-----0x0a:" + heartBeats);

        } else if (data[0] == 0x0b) { // 快快减肥APP向快快心率腕带发送停止发送心率协议0x0b；快快心率腕带停止发送心率协议，并返回停止成功
            sendLocalBroadcast(ACTION_STOP_REALTIME_FINISH_COURSE);
//            fileName = "allCommand".concat(DataCreator.getFileTime()).concat(".txt") ;
//            sourceData = "sourceData".concat(DataCreator.getFileTime()).concat(".txt") ;
//            sourceHeartBeat = "sourceHeartBeat".concat(DataCreator.getFileTime()).concat(".txt") ;
//            sourcePluse = "sourcePluse".concat(DataCreator.getFileTime()).concat(".txt") ;
        } else if (data[0] == 0x0d) {
            String heartBeats = String.valueOf(DataUtils.byteArrayToInt(new byte[]{data[5]}));
            KLog.i("-----0x0d:" + heartBeats);
            broadcastIntentUpdate(new Intent(ACTION_STATIC_HEART_BEATS).putExtra(ACTION_STATIC_HEART_BEATS, heartBeats));
        } else if (data[0] == 0x13) {
            KLog.e("--------send real data -------------");
            if (data[5] == 0x01) {
                isSendingM4 = true;
                sendM4BinFilenumber = 0;
//                sendm4BinFile(sendM4BinFilenumber);
                sendM4BinFilenumber++;
            } else { // 表示未准备就绪,本次操作失败
                broadcastIntentUpdate(new Intent(SENDM4FAILURE));
            }
        } else if (data[0] == 0x08) { // 闹钟设置反馈数据
            sendLocalBroadcast(ACTION_SET_CLOCK);

            KLog.i("-----0x08");
        }
    }

    private String refreshWRContent(byte[] data) {
        StringBuilder sb = new StringBuilder();
        sb.append("RX:");
        sb.append(" 时间： ").append(DataCreator.getCurrentTime() + " ");
        for (int i = 0; i < data.length; i++) {
            sb.append(DataUtils.byteTohex(data[i])).append(" ");
        }

        sb.append("\n");
        KLog.i("KKK", "反馈 : " + sb.toString());

//        FileUtils.saveAllDataToFile(fileName,sb.toString());

        return sb.toString();
    }
}
