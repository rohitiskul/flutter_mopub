import 'package:flutter/services.dart';

import 'mopub_constants.dart';

enum InterstitialAdResult {
  /// Interstitial Ad displayed to the user
  DISPLAYED,

  /// Interstitial Ad dismissed by the user
  DISMISSED,

  /// Interstitial Ad error
  ERROR,

  /// Interstitial Ad loaded
  LOADED,

  /// Interstitial Ad clicked
  CLICKED,
}

class MoPubInterstitialAd {
  static void Function(InterstitialAdResult, dynamic) _listener;

  static const _channel = const MethodChannel(INTERSTITIAL_AD_CHANNEL);

  static Future<bool> loadInterstitialAd(String adUnitId,
      {Function(InterstitialAdResult, dynamic) listener}) async {
    try {
      final args = <String, dynamic>{
        "adUnitId": adUnitId,
      };

      final result = await _channel.invokeMethod(
        LOAD_INTERSTITIAL_METHOD,
        args,
      );
      _channel.setMethodCallHandler(_interstitialMethodCall);
      _listener = listener;

      return result;
    } on PlatformException {
      return false;
    }
  }

  static Future<bool> showInterstitialAd() async {
    try {
      final result = await _channel.invokeMethod(SHOW_INTERSTITIAL_METHOD);
      return result;
    } on PlatformException {
      return false;
    }
  }

  /// Removes the Ad.
  static Future<bool> destroyInterstitialAd() async {
    try {
      final result = await _channel.invokeMethod(DESTROY_INTERSTITIAL_METHOD);
      return result;
    } on PlatformException {
      return false;
    }
  }

  static Future<dynamic> _interstitialMethodCall(MethodCall call) {
    switch (call.method) {
      case DISPLAYED_METHOD:
        if (_listener != null)
          _listener(InterstitialAdResult.DISPLAYED, call.arguments);
        break;
      case DISMISSED_METHOD:
        if (_listener != null)
          _listener(InterstitialAdResult.DISMISSED, call.arguments);
        break;
      case ERROR_METHOD:
        if (_listener != null)
          _listener(InterstitialAdResult.ERROR, call.arguments);
        break;
      case LOADED_METHOD:
        if (_listener != null)
          _listener(InterstitialAdResult.LOADED, call.arguments);
        break;
      case CLICKED_METHOD:
        if (_listener != null)
          _listener(InterstitialAdResult.CLICKED, call.arguments);
        break;
    }
    return Future.value(true);
  }
}
