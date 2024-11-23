import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:voice_recognition/model/voice_recognition_model.dart';

import 'voice_recognition_platform_interface.dart';

class MethodChannelVoiceRecognition extends VoiceRecognitionPlatform {
  @visibleForTesting
  final methodChannel = const MethodChannel('voice_recognition/method');
  final eventChannel = const EventChannel("voice_recognition/event");


  @override
  Future startVoice() async {
    await methodChannel.invokeListMethod("startListening");
  }

  @override
  Future stopVoice() async {
    await methodChannel.invokeListMethod("stopListening");
  }

  @override
  Stream<VoiceRecognitionModel> listenResult() {
    return eventChannel.receiveBroadcastStream().map((e) {
      Map<String, dynamic> tmpmap = Map<String, dynamic>.from(e);
      return VoiceRecognitionModel.fromMap(tmpmap);
    });
  }

  @override
  Future getAllLocal() async {
    return await methodChannel.invokeListMethod("getAllLocal");
  }

  @override
  Future setLang(String name) async {
    await methodChannel.invokeListMethod("setLng", name);
  }
}
