# voice_recognition

A Flutter plugin that provides voice recognition with support for multiple languages. This plugin allows you to implement real-time speech-to-text functionality in your Flutter applications.


## Features

- Real-time voice recognition
- Support for multiple languages
- Continuous recognition mode
- Speech recognition events handling
- Get list of available locales

## Getting Started

### Installation

Add this to your package's `pubspec.yaml` file:

```yaml
dependencies:
  voice_recognition: ^1.0.0
```

### Platform Specific Setup

#### Android

Add the following permission to your Android Manifest (`android/app/src/main/AndroidManifest.xml`):

```xml
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.INTERNET" />
    <queries>
        <intent>
            <action android:name="android.speech.RecognitionService" />
        </intent>
    </queries>
```

## Usage

### Basic Implementation

```dart
import 'package:voice_recognition/voice_recognition.dart';

// Create an instance of VoiceRecognition
final voiceRecognition = VoiceRecognition();

// Set preferred language
voiceRecognition.setLanguages("bn-BD"); // Example for Bengali language

// Start listening
await voiceRecognition.startVoice();

// Stop listening
await voiceRecognition.stopVoice();

// Get all available locales
final locales = await voiceRecognition.getAllLocal();
```

### Setting Up Listeners

```dart
voiceRecognition.listenResult(
  onReadyForSpeech: (ready) {
    // Called when the recognizer is ready to listen
  },
  onBeginningOfSpeech: (beginning) {
    // Called when the user starts speaking
  },
  onRmsChanged: (rmsDb) {
    // Called when the audio level changes
    print("Audio level: $rmsDb");
  },
  onResults: (results) async {
    // Called when recognition results are ready
    print("Recognition result: $results");
    
    // For continuous recognition, start listening again
    await voiceRecognition.startVoice();
  },
  onError: (error) {
    // Handle any errors
    print("Error: $error");
  },
  onEndOfSpeech: (end) {
    // Called when the user stops speaking
  },
  onPartialResults: (partial) {
    // Called when partial recognition results are available
  },
  onEvent: (event) {
    // Handle other recognition events
  },
);
```


### Methods

- `setLanguages(String locale)`: Set the recognition language
- `startVoice()`: Start voice recognition
- `stopVoice()`: Stop voice recognition
- `getAllLocal()`: Get list of available locales

### Callbacks

- `onReadyForSpeech`: Called when the recognizer is ready
- `onBeginningOfSpeech`: Called when speech input starts
- `onRmsChanged`: Audio level changes
- `onBufferReceived`: Raw audio buffer updates
- `onEndOfSpeech`: Called when speech input ends
- `onError`: Error handling
- `onResults`: Final recognition results
- `onPartialResults`: Intermediate recognition results
- `onEvent`: Other recognition events

## Supported Languages

The plugin supports multiple languages. Use the `getAllLocal()` method to get a list of available locales on the device.

Example locales:
- English (US): "en-US"
- Bengali: "bn-BD"
- Hindi: "hi-IN"
- And many more...


## License

This project is licensed under the MIT License - see the LICENSE file for details.
