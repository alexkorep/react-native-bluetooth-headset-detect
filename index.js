import {NativeEventEmitter, NativeModules} from 'react-native';
import {useEffect, useState} from 'react';

const BluetoothHeadsetDetectModule = NativeModules.RNBluetoothHeadsetDetect;
const bluetoothHeadsetDetectEmitter = new NativeEventEmitter(
  BluetoothHeadsetDetectModule,
);

var EventEmitter = require('events');
var ee = new EventEmitter();
const MSG = 'message';
let device = null;

bluetoothHeadsetDetectEmitter.addListener('onChange', ({devices}) => {
  console.log('call listener with', devices);
  device = devices.length ? devices[0] : null;
  ee.emit(MSG, device);
});

const useBluetoothHeadsetDetection = () => {
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

export default useBluetoothHeadsetDetection;
