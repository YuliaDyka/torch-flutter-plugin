import 'package:flutter_test/flutter_test.dart';
import 'package:torch_flutter_plugin/torch_flutter_plugin.dart';
import 'package:torch_flutter_plugin/torch_flutter_plugin_platform_interface.dart';
import 'package:torch_flutter_plugin/torch_flutter_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockTorchFlutterPluginPlatform
    with MockPlatformInterfaceMixin
    implements TorchFlutterPluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final TorchFlutterPluginPlatform initialPlatform = TorchFlutterPluginPlatform.instance;

  test('$MethodChannelTorchFlutterPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelTorchFlutterPlugin>());
  });

  test('getPlatformVersion', () async {
    TorchFlutterPlugin torchFlutterPlugin = TorchFlutterPlugin();
    MockTorchFlutterPluginPlatform fakePlatform = MockTorchFlutterPluginPlatform();
    TorchFlutterPluginPlatform.instance = fakePlatform;

    expect(await torchFlutterPlugin.getPlatformVersion(), '42');
  });
}
