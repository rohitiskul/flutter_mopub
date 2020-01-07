#import "FlutterMopubPlugin.h"
#import <flutter_mopub/flutter_mopub-Swift.h>

@implementation FlutterMopubPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterMopubPlugin registerWithRegistrar:registrar];
}
@end
