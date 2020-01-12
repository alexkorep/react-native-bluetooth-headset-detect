import {NativeEventEmitter, NativeModules} from 'react-native';
import {useEffect, useState} from 'react';
import EventEmitter from 'events';

const BluetoothHeadsetDetectModule = NativeModules.RNBluetoothHeadsetDetect;
const bluetoothHeadsetDetectEmitter = new NativeEventEmitter(
  BluetoothHeadsetDetectModule,
);

const ee = new EventEmitter();
const MSG = 'onChange';
let device = null;

bluetoothHeadsetDetectEmitter.addListener('onChange', ({devices}) => {
  console.log('call listener with', devices);
  device = devices.length ? devices[0] : null;
  ee.emit(MSG, device);
});

// Events
export const getHeadset = () => device;
export const addListener = ee.addListener;
export const removeListener = ee.removeListener;

// React hook
export const useBluetoothHeadsetDetection = () => {
  const [headset, setHeadset] = useState(null);
  useEffect(() => {
    setHeadset(device);
    ee.addListener(MSG, setHeadset);
    return () => {
      ee.removeListener(MSG, setHeadset);
    };
  }, []);

  return headset;
};
