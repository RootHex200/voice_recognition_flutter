package com.sabitur.voice_recognition.voice_recognition
//VoiceRecognitionPlugin

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry.RequestPermissionsResultListener
import java.util.Locale

class VoiceRecognitionPlugin : FlutterPlugin, MethodChannel.MethodCallHandler, EventChannel.StreamHandler, ActivityAware,RequestPermissionsResultListener {

  private lateinit var methodChannel: MethodChannel
  private lateinit var eventChannel: EventChannel
  private var eventSink: EventChannel.EventSink? = null
  private var speechRecognizer: SpeechRecognizer? = null
  private var recognizerIntent: Intent? = null
  private var textToSpeech: TextToSpeech? = null
  private var selectedLanguage = "bn" // Default language

  private lateinit var context: Context
  private lateinit var activity: Activity
  private val PERMISSIONS_REQUEST_RECORD_AUDIO = 100
  private val RESULTS_LIMIT = 1

  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel = MethodChannel(binding.binaryMessenger, "voice_recognition/method")
    eventChannel = EventChannel(binding.binaryMessenger, "voice_recognition/event")
    methodChannel.setMethodCallHandler(this)
    eventChannel.setStreamHandler(this)
    context = binding.applicationContext
    Log.d("locals",getAllLanguages().toString())
  }

  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
    when (call.method) {
      "startListening" -> {
        startListening()
        result.success(null)
      }
      "stopListening" -> {
        stopListening()
        result.success(null)
      }
      "getAllLocal"->{
        var tmp=  getAllLanguages()
        result.success(tmp)
      }
      "setLng"->{
        val lngname=call.arguments
        //Log.d("setLng",lngname.toString())
        setLanguages(lngname.toString())
        //Log.d("setLng",selectedLanguage);
      }
      else -> result.notImplemented()
    }
  }

  private fun setLanguages(countryCode: String){
    selectedLanguage=countryCode
  }
  override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
    eventSink = events
  }

  override fun onCancel(arguments: Any?) {
    eventSink = null
  }

  private fun getAllLanguages():List<String>{
    var list= mutableListOf("en")
    for ( tmp in Locale.getAvailableLocales()){
      list.add(tmp.toString())
    }
    return list
  }

  private fun startListening() {
    if(checkPermissions()==false){
      requestPermission();
    }else{
      resetSpeechRecognizer()
      setRecogniserIntent()
      speechRecognizer!!.startListening(recognizerIntent)
    }
  }

  private fun stopListening() {
    speechRecognizer!!.stopListening()
    speechRecognizer!!.destroy()
  }
  private fun setRecogniserIntent() {
    recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    recognizerIntent!!.putExtra(
      RecognizerIntent.EXTRA_LANGUAGE,
      selectedLanguage
    )
    recognizerIntent!!.putExtra(
      RecognizerIntent.EXTRA_LANGUAGE_MODEL,
      RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
    )
    recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, RESULTS_LIMIT)
  }
  private fun resetSpeechRecognizer() {
    if (speechRecognizer != null) speechRecognizer!!.destroy()
    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    if (SpeechRecognizer.isRecognitionAvailable(context)) {
      speechRecognizer!!.setRecognitionListener(mRecognitionListener)
    }
  }

  private val mRecognitionListener =object : RecognitionListener {

    // Handle other RecognitionListener methods as needed
    override fun onReadyForSpeech(p0: Bundle?) {
      //Log.d("onEndOfSpeech","onReadyForSpeech")
      eventSink!!.success(mapOf("event" to "onReadyForSpeechEvent","data" to "onReadyForSpeech is call"))
    }

    override fun onBeginningOfSpeech() {
      //Log.d("onEndOfSpeech","onBeginningOfSpeech")
      eventSink!!.success(mapOf("event" to "onBeginningOfSpeechEvent","data" to "onBeginningOfSpeech is call"))
    }

    override fun onRmsChanged(p0: Float) {
      eventSink!!.success(mapOf("event" to "onRmsChangedEvent","data" to p0))
    }

    override fun onBufferReceived(p0: ByteArray?) {
      eventSink!!.success(mapOf("event" to "onBufferReceivedEvent","data" to p0))
    }

    override fun onEndOfSpeech() {
      speechRecognizer!!.stopListening();
      //Log.d("onEndOfSpeechEvent","onEndOfSpeech")
      eventSink!!.success(mapOf("event" to "onEndOfSpeechEvent","data" to "onEndOfSpeechEvent is call"))
    }

    override fun onError(p0: Int) {
      eventSink!!.success( mapOf("event" to "onErrorEvent","data" to getErrorText(p0)))
      // eventSink!!.success(getErrorText(p0));
      //Log.d("onEndOfSpeech","Error : ${getErrorText(p0)}")
    }

    override fun onResults(results: Bundle?) {
      val matches: ArrayList<String>? = results!!
        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
      var text = ""
      for (result in matches!!) text += result
      eventSink!!.success(mapOf("event" to "onResultsEvent","data" to text))
      //eventSink!!.success(text)
      Log.d("onEndOfSpeech",text)

    }

    override fun onPartialResults(p0: Bundle?) {
      eventSink!!.success(mapOf("event" to "onPartialResultsEvent","data" to p0))

    }

    override fun onEvent(p0: Int, p1: Bundle?) {
      eventSink!!.success(mapOf("event" to "onEvent","data" to mapOf("value1" to p0,"value2" to p1)))
    }
  }

  private fun checkPermissions():Boolean {
    Log.d("onEndOfSpeech","CheckPermission")
    return (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
  }

  private fun requestPermission(){
    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSIONS_REQUEST_RECORD_AUDIO)
  }
  fun getErrorText(errorCode: Int): String {
    val message: String = when (errorCode) {
      SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
      SpeechRecognizer.ERROR_CLIENT -> "Client side error"
      SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
      SpeechRecognizer.ERROR_NETWORK -> "Network error"
      SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
      SpeechRecognizer.ERROR_NO_MATCH -> "No match"
      SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
      SpeechRecognizer.ERROR_SERVER -> "error from server"
      SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
      SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED -> "Language Not supported"
      SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> "Language Unavailable"
      else -> "Didn't understand, please try again."
    }
    return message
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel.setMethodCallHandler(null)
    eventChannel.setStreamHandler(null)
    speechRecognizer!!.stopListening()
    speechRecognizer!!.destroy()
  }

  // Implement ActivityAware methods to attach and detach from activity
  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
  }
  override fun onDetachedFromActivityForConfigChanges() {}
  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {}
  override fun onDetachedFromActivity() {}
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ): Boolean {
    if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        //startListening()
        return true;
      } else {
        Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
        return false
      }
    }
    return false
  }


}
