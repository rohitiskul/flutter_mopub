import 'package:flutter/material.dart';

import 'package:flutter/services.dart';
import 'package:flutter_mopub/mopub.dart';
import 'package:flutter_mopub/mopub_banner.dart';
import 'package:flutter_mopub/mopub_interstitial.dart';
import 'package:flutter_mopub/mopub_rewarded.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    try {
      MoPub.init('ad_unit_id', testMode: true).then((_) {
        _loadRewardedAd();
        _loadInterstitialAd();
      });
    } on PlatformException {}
  }

  void _loadRewardedAd() {
    MoPubRewardedVideoAd.loadRewardedVideoAd(
      'REWARDED_AD_ID',
      listener: (result, args) {
        if (result == RewardedVideoAdResult.VIDEO_COMPLETE) {
          print('Rewarded args: $args');
        }
        if (result == RewardedVideoAdResult.VIDEO_CLOSED) {
          _loadRewardedAd();
        }
      },
    );
  }

  void _loadInterstitialAd() {
    MoPubInterstitialAd.loadInterstitialAd(
      'ad_unit_id',
      listener: (result, args) {
        if (result == InterstitialAdResult.DISMISSED) {
          _loadInterstitialAd();
        }
        print('Interstitial $result');
      },
    );
  }

  @override
  void dispose() {
    super.dispose();
    MoPubInterstitialAd.destroyInterstitialAd();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            RaisedButton(
              onPressed: () {
                MoPubInterstitialAd.showInterstitialAd();
                // MoPubRewardedVideoAd.showRewardedVideoAd(
                //     'REWARDED_AD_ID');
              },
              child: Text('Show interstitial'),
            ),
            MoPubBannerAd(
              adUnitId: 'ad_unit_id',
              bannerSize: BannerSize.STANDARD,
              keepAlive: true,
              listener: (result, dynamic) {
                print('$result');
              },
            ),
          ],
        ),
      ),
    );
  }
}
