package io.github.alexkorep;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNBluetoothHeadsetDetectModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNBluetoothHeadsetDetectModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNBluetoothHeadsetDetect";
    }

    @ReactMethod
    public void getBluetoothHeadset(Callback callback) {
        final AudioManager audioManager = (AudioManager) reactContext.getCurrentActivity().getSystemService(Context.AUDIO_SERVICE);

        String deviceName = "";
        AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
        for (AudioDeviceInfo device : devices) {
            final int type = device.getType();
            if (type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP || type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                deviceName = device.getProductName().toString();
            }
        }

        callback.invoke(deviceName);
    }
}
