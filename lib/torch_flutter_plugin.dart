import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'torch_flutter_plugin_platform_interface.dart';

class TorchFlutterPlugin {
  Future<String?> getPlatformVersion() {
    return TorchFlutterPluginPlatform.instance.getPlatformVersion();
  }

  static const _methodChannel = MethodChannel('torch_flutter_plugin');

  static Future<bool> checkTorchAvailable() async {
    return await _methodChannel.invokeMethod('checkTorchAvailable');
  }

  static Future<void> torchOn() async {
    try {
      await _methodChannel.invokeMethod('turnTorchOn');
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print("Failed to turn on the torch: '${e.message}'.");
      }
    }
  }

  static Future<void> torchOff() async {
    try {
      await _methodChannel.invokeMethod('turnTorchOff');
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print("Failed to turn off the torch: '${e.message}'.");
      }
    }
  }
}
