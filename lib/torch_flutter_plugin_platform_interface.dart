import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'torch_flutter_plugin_method_channel.dart';

abstract class TorchFlutterPluginPlatform extends PlatformInterface {
  /// Constructs a TorchFlutterPluginPlatform.
  TorchFlutterPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static TorchFlutterPluginPlatform _instance = MethodChannelTorchFlutterPlugin();

  /// The default instance of [TorchFlutterPluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelTorchFlutterPlugin].
  static TorchFlutterPluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [TorchFlutterPluginPlatform] when
  /// they register themselves.
  static set instance(TorchFlutterPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
