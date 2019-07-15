#import "RNBluetoothHeadsetDetect.h"
#import <AVFoundation/AVFoundation.h>

@implementation RNBluetoothHeadsetDetect

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(getBluetoothHeadset:(RCTResponseSenderBlock)callback)
{
    NSString* name = @"";
    AVAudioSessionRouteDescription* route = [[AVAudioSession sharedInstance] currentRoute];
    for (AVAudioSessionPortDescription* desc in [route outputs]) {
        if ([[desc portType] isEqualToString:AVAudioSessionPortHeadphones] ||
            [[desc portType] isEqualToString:AVAudioSessionPortBluetoothHFP] ||
            [[desc portType] isEqualToString:AVAudioSessionPortBluetoothA2DP] ||
            [[desc portType] isEqualToString:AVAudioSessionPortBluetoothLE]) {
            name = [desc portName];
        }
    }
    callback(@[name]);
}

@end
