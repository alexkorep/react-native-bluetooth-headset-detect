# react-native-bluetooth-headset-detect

Bluetooth Headset Detection for React Native

## Getting started

`$ npm install react-native-bluetooth-headset-detect --save`

### If you use react-native < 0.60

`$ react-native link react-native-bluetooth-headset-detect`

## Usage
### With React hooks:
```javascript
import { useBluetoothHeadsetDetection } from 'react-native-bluetooth-headset-detect';

const MyComponent = () => {
  const device = useBluetoothHeadsetDetection();
  return (
    <Text>Connected headset: {device}</Text>
  );
};
```
### Without React hooks:
```javascript
import {
  getHeadset,
  addListener,
  removeListener,
} from 'react-native-bluetooth-headset-detect';

console.log('Connected device:', getHeadset());
addListener((device) => {
  console.log('Connected device:', device);
});
```
## Legacy (not recommended):
```javascript
import { NativeEventEmitter, NativeModules } from "react-native";

const BluetoothHeadsetDetectModule = NativeModules.RNBluetoothHeadsetDetect;
const bluetoothHeadsetDetectEmitter = new NativeEventEmitter(
  BluetoothHeadsetDetectModule
);
bluetoothHeadsetDetectEmitter.addListener("onChange", ({ devices }) => {
  if (devices.length) {
    console.log("Connected device:", devices[0]);
  } else {
    console.log("No devices connected");
  }
});
```

## Demo project
[https://github.com/alexkorep/react-native-bluetooth-headset-detect-demo](https://github.com/alexkorep/react-native-bluetooth-headset-detect-demo)
