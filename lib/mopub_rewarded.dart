import 'package:flutter/services.dart';

import 'mopub_constants.dart';

enum RewardedVideoAdResult {
  /// Rewarded video error.
  ERROR,

  /// Rewarded video loaded successfully.
  LOADED,

  /// Rewarded video clicked.
  CLICKED,

  /// Rewarded video played till the end. Use it to reward the user.
  VIDEO_COMPLETE,

  /// Rewarded video closed.
  VIDEO_CLOSED,
}

class MoPubRewardedVideoAd {
  static void Function(RewardedVideoAdResult, dynamic) _listener;

  static const _channel = const MethodChannel(REWARDED_VIDEO_CHANNEL);

  static Future<bool> loadRewardedVideoAd(String adUnitId,
      {Function(RewardedVideoAdResult, dynamic) listener}) async {
    try {
      final args = <String, dynamic>{
        "adUnitId": adUnitId,
      };

      final result = await _channel.invokeMethod(
        LOAD_REWARDED_VIDEO_METHOD,
        args,
      );
      _channel.setMethodCallHandler(_rewardedMethodCall);
      _listener = listener;

      return result;
    } on PlatformException {
      return false;
    }
  }

  static Future<bool> showRewardedVideoAd(String adUnitId) async {
    try {
      final args = <String, dynamic>{
        "adUnitId": adUnitId,
      };

      final result = await _channel.invokeMethod(
        SHOW_REWARDED_VIDEO_METHOD,
        args,
      );

      return result;
    } on PlatformException {
      return false;
    }
  }

  static Future<dynamic> _rewardedMethodCall(MethodCall call) {
    switch (call.method) {
      case REWARDED_VIDEO_COMPLETED_METHOD:
        if (_listener != null)
          _listener(RewardedVideoAdResult.VIDEO_COMPLETE, call.arguments);
        break;
      case REWARDED_VIDEO_CLOSED_METHOD:
        if (_listener != null)
          _listener(RewardedVideoAdResult.VIDEO_CLOSED, call.arguments);
        break;
      case ERROR_METHOD:
        if (_listener != null)
          _listener(RewardedVideoAdResult.ERROR, call.arguments);
        break;
      case LOADED_METHOD:
        if (_listener != null)
          _listener(RewardedVideoAdResult.LOADED, call.arguments);
        break;
      case CLICKED_METHOD:
        if (_listener != null)
          _listener(RewardedVideoAdResult.CLICKED, call.arguments);
        break;
    }
    return Future.value(true);
  }
}
