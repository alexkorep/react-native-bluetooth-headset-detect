package io.github.alexkorep;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.bluetooth.BluetoothA2dp;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class RNBluetoothHeadsetDetectModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    BroadcastReceiver receiver;

    private void onChange(final String deviceName) {
        // Report device name (if not empty) to the host
        WritableMap payload = Arguments.createMap();
        WritableArray deviceList = Arguments.createArray();
        if (!deviceName.isEmpty()) {
            deviceList.pushString(deviceName);
        }
        payload.putArray("devices", deviceList);
        this.getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("onChange", payload);
    }

    public RNBluetoothHeadsetDetectModule(ReactApplicationContext reactContext) {
        super(reactContext);
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter intentFilter = new IntentFilter(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.receiver = new BroadcastReceiver() {
            private static final String LOG_TAG = "BluetoothHeadsetDetect";

            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action.equals(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)) {
                    // Bluetooth headset connection state has changed
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    final int state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, BluetoothProfile.STATE_DISCONNECTED);
                    if (state == BluetoothProfile.STATE_CONNECTED) {
                        // Device has connected, report it
                        onChange(device.getName());
                    } else if (state == BluetoothProfile.STATE_DISCONNECTED) {
                        // Device has disconnected, report it
                        onChange("");
                    }
                } else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE,
                            BluetoothProfile.STATE_DISCONNECTED);
                    if (state == BluetoothProfile.STATE_DISCONNECTED) {
                        // Bluetooth is disabled
                        onChange("");
                    }
                }
            }
        };

        // Subscribe for intents
        reactContext.registerReceiver(this.receiver, intentFilter);
        // Subscribe for lifecycle
        reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return "RNBluetoothHeadsetDetect";
    }

    @Override
    public void onHostResume() {
        final Activity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }
        final AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        try {
            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo device : devices) {
                final int type = device.getType();
                if (type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP || type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                    // Device is found
                    final String deviceName = device.getProductName().toString();
                    onChange(deviceName);
                    return;
                }
            }
        } catch(NoSuchMethodError e) {
            //ignore in case of error
        }
        // No devices found
        onChange("");
    }

    @Override
    public void onHostPause() {
    }

    @Override
    public void onHostDestroy() {
    }
}
