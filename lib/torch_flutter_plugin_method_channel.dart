import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'torch_flutter_plugin_platform_interface.dart';

/// An implementation of [TorchFlutterPluginPlatform] that uses method channels.
class MethodChannelTorchFlutterPlugin extends TorchFlutterPluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('torch_flutter_plugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
