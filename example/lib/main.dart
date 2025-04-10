import 'dart:math';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:voice_recognition_flutter/voice_recognition.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _voicerecognitionPlugin = VoiceRecognition();
  var tmpdata = "";

  @override
  void initState() {
    super.initState();
    _voicerecognitionPlugin.setLanguages("bn-BD");
    //print(_voicetestPlugin.getAllLocal());
    _voicerecognitionPlugin.listenResult(
        onReadyForSpeech: (onReadyForSpeech) {},
        onBeginningOfSpeech: (onBeginningOfSpeech) {},
        onRmsChanged: (onRmsChanged) {
          print("onRmsChanged $onRmsChanged");
        },
        onBufferReceived: (onBufferReceived) {},
        onEndOfSpeech: (onEndOfSpeech) {},
        onError: (onError) {
          print("Flutter:- $onError");
        },
        onResults: (onResults) async {
          setState(() {
            tmpdata = onResults!;
          });

          //call again for continouse recognition
          await _voicerecognitionPlugin.startVoice();
        },
        onPartialResults: (onPartialResults) {},
        onEvent: (onEvent) {});
  }


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            Center(
              child: Text('Running on: $tmpdata\n'),
            ),
            ElevatedButton(
                onPressed: () async {
                  await _voicerecognitionPlugin.startVoice();
                },
                child: const Text("Start Voice")),
            ElevatedButton(
                onPressed: () async {
                  await _voicerecognitionPlugin.stopVoice();
                },
                child: const Text("Stop Voice")),
            ElevatedButton(
                onPressed: () async {
                  final data = await _voicerecognitionPlugin.getAllLocal();
                  print(data);
                  print(data.runtimeType);
                  for(var element in data) {
                    if(element.startsWith("a")){
                      print(element);
                    }
                  }
                },
                child: const Text("Local"))
          ],
        ),
      ),
    );
  }
}
