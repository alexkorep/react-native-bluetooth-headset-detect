#import "RNBluetoothHeadsetDetect.h"
#import <AVFoundation/AVFoundation.h>

@implementation RNBluetoothHeadsetDetect

RCT_EXPORT_MODULE()

// Called when this module's first listener is added.
-(void)startObserving {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(audioHardwareRouteChanged:) name:AVAudioSessionRouteChangeNotification object:nil];
    [self updateConnectedDevice];
}

// Called when this module's last listener is removed, or on dealloc.
-(void)stopObserving {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (NSArray<NSString *> *)supportedEvents {
    return @[@"onChange"];
}

- (void)updateConnectedDevice {
    NSMutableArray *devices = [NSMutableArray array];

    AVAudioSessionRouteDescription* route = [[AVAudioSession sharedInstance] currentRoute];
    for (AVAudioSessionPortDescription* desc in [route outputs]) {
        if ([[desc portType] isEqualToString:AVAudioSessionPortBluetoothHFP] ||
            [[desc portType] isEqualToString:AVAudioSessionPortBluetoothA2DP] ||
            [[desc portType] isEqualToString:AVAudioSessionPortBluetoothLE]) {
            [devices addObject:[desc portName]];
        }
    }
    [self sendEventWithName:@"onChange" body:@{@"devices": devices}];
}

- (void)audioHardwareRouteChanged:(NSNotification *)notification {
    NSNumber *reason = [notification.userInfo objectForKey:AVAudioSessionRouteChangeReasonKey];
    if ([reason unsignedIntegerValue] == AVAudioSessionRouteChangeReasonNewDeviceAvailable ||
        [reason unsignedIntegerValue] == AVAudioSessionRouteChangeReasonOldDeviceUnavailable) {
        [self updateConnectedDevice];
    }
}

@end
