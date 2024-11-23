import 'package:flutter_test/flutter_test.dart';
import 'package:voice_recognition/model/voice_recognition_model.dart';
import 'package:voice_recognition/voice_recognition_platform_interface.dart';
import 'package:voice_recognition/voice_recognition_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockVoiceRecognitionPlatform
    with MockPlatformInterfaceMixin
    implements VoiceRecognitionPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future getAllLocal() {
    // TODO: implement getAllLocal
    throw UnimplementedError();
  }

  @override
  Stream<VoiceRecognitionModel> listenResult() {
    // TODO: implement listenResult
    throw UnimplementedError();
  }

  @override
  Future setLang(String name) {
    // TODO: implement setLang
    throw UnimplementedError();
  }

  @override
  Future startVoice() {
    // TODO: implement startVoice
    throw UnimplementedError();
  }

  @override
  Future stopVoice() {
    // TODO: implement stopVoice
    throw UnimplementedError();
  }
}

void main() {
  final VoiceRecognitionPlatform initialPlatform = VoiceRecognitionPlatform.instance;

  test('$MethodChannelVoiceRecognition is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelVoiceRecognition>());
  });

}
