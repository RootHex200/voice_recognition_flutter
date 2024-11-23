

class VoiceRecognitionModel {
  final String event;
  final dynamic data;
  VoiceRecognitionModel({
    required this.event,
    required this.data,
  });

  factory VoiceRecognitionModel.fromMap(map) {
    return VoiceRecognitionModel(
      event: map['event'] as String,
      data: map['data'],
    );
  }
}
