
import 'package:flutter/foundation.dart';

import 'voice_recognition_platform_interface.dart';


//enums event name

enum RecgonitionEvent {
  onReadyForSpeechEvent,
  onBeginningOfSpeechEvent,
  onRmsChangedEvent,
  onBufferReceivedEvent,
  onEndOfSpeechEvent,
  onErrorEvent,
  onResultsEvent,
  onPartialResultsEvent,
  onEvent
}



class VoiceRecognition {

  Future startVoice() async {
    await VoiceRecognitionPlatform.instance.startVoice();
  }

  Future stopVoice() async {
    await VoiceRecognitionPlatform.instance.stopVoice();
  }

  Future getAllLocal() async {
    return await VoiceRecognitionPlatform.instance.getAllLocal();
  }

  Future setLanguages(String lgnName) async {
    await VoiceRecognitionPlatform.instance.setLang(lgnName);
  }


    void listenResult(
      {required ValueChanged onReadyForSpeech,
      required ValueChanged? onBeginningOfSpeech,
      required ValueChanged<double?> onRmsChanged,
      required ValueChanged<dynamic> onBufferReceived,
      required ValueChanged onEndOfSpeech,
      required ValueChanged<String?> onError,
      required ValueChanged<String?> onResults,
      required ValueChanged? onPartialResults,
      required ValueChanged? onEvent}) {
    VoiceRecognitionPlatform.instance.listenResult().listen((result) {
      if (RecgonitionEvent.onBeginningOfSpeechEvent.name == result.event) {
        onReadyForSpeech;
      }
      if (RecgonitionEvent.onBeginningOfSpeechEvent.name == result.event) {
        onBeginningOfSpeech;
      }
      if (RecgonitionEvent.onRmsChangedEvent.name == result.event) {
        onRmsChanged(result.data as double);
      }
      if (RecgonitionEvent.onBufferReceivedEvent.name == result.event) {
        onBufferReceived(result.data);
      }
      if (RecgonitionEvent.onEndOfSpeechEvent.name == result.event) {
        onEndOfSpeech;
      }
      if (RecgonitionEvent.onErrorEvent.name == result.event) {
        onError(result.data);
      }
      if (RecgonitionEvent.onResultsEvent.name == result.event) {
        onResults(result.data);
      }
    });
  }
}
