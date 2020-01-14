import { NativeEventEmitter, NativeModules } from 'react-native';
import { useEffect, useState } from 'react';

const BluetoothHeadsetDetectModule = NativeModules.RNBluetoothHeadsetDetect;
const bluetoothHeadsetDetectEmitter = new NativeEventEmitter(
  BluetoothHeadsetDetectModule
);

let device = null;
const listeners = [];

bluetoothHeadsetDetectEmitter.addListener('onChange', ({ devices }) => {
  device = devices.length ? devices[0] : null;
  listeners.forEach(listener => {
    listener(device);
  });
});

// Events
export const getHeadset = () => device;
export const addListener = listener => {
  listeners.push(listener);
};
export const removeListener = listener => {
  const idx = listeners.indexOf(listener);
  if (idx === -1) {
    return;
  }
  listeners.splice(idx, 1);
};

// React hook
export const useBluetoothHeadsetDetection = () => {
  const [headset, setHeadset] = useState(null);
  useEffect(() => {
    setHeadset(device);
    addListener(setHeadset);
    return () => {
      removeListener(setHeadset);
    };
  }, []);

  return headset;
};
