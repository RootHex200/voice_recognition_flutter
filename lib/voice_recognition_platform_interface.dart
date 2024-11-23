import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:voice_recognition_flutter/model/voice_recognition_model.dart';

import 'voice_recognition_method_channel.dart';

abstract class VoiceRecognitionPlatform extends PlatformInterface {
  /// Constructs a VoiceRecognitionPlatform.
  VoiceRecognitionPlatform() : super(token: _token);

  static final Object _token = Object();

  static VoiceRecognitionPlatform _instance = MethodChannelVoiceRecognition();

  /// The default instance of [VoiceRecognitionPlatform] to use.
  ///
  /// Defaults to [MethodChannelVoiceRecognition].
  static VoiceRecognitionPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [VoiceRecognitionPlatform] when
  /// they register themselves.
  static set instance(VoiceRecognitionPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }


  Future setLang(String name);
  Future startVoice();

  Future stopVoice();

  Future getAllLocal();

  Stream<VoiceRecognitionModel> listenResult();
}
