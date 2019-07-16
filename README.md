# react-native-bluetooth-headset-detect

Bluetooth Headset Detection for React Native

## Getting started

`$ npm install react-native-bluetooth-headset-detect --save`

### Mostly automatic installation

`$ react-native link react-native-bluetooth-headset-detect`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-bluetooth-headset-detect` and add `RNBluetoothHeadsetDetect.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNBluetoothHeadsetDetect.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import io.github.alexkorep.RNBluetoothHeadsetDetectPackage;` to the imports at the top of the file
  - Add `new RNBluetoothHeadsetDetectPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-bluetooth-headset-detect'
  	project(':react-native-bluetooth-headset-detect').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-bluetooth-headset-detect/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-bluetooth-headset-detect')
  	```


## Usage
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
