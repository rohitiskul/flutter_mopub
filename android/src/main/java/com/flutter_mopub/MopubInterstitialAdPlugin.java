package com.flutter_mopub;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

class MopubInterstitialAdPlugin implements MethodChannel.MethodCallHandler, MoPubInterstitial.InterstitialAdListener {
    private MoPubInterstitial interstitialAd = null;

    private Activity activity;
    private MethodChannel channel;

    MopubInterstitialAdPlugin(Activity activity, MethodChannel channel) {
        this.activity = activity;
        this.channel = channel;
    }

//    void dispose() {
//        methodChannel.setMethodCallHandler(null);
//        methodChannel = null;
//    }

//    void setActivity(Activity activity) {
//        this.activity = activity;
//    }


    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case MopubConstants.SHOW_INTERSTITIAL_METHOD:
                result.success(showAd());
                break;
            case MopubConstants.LOAD_INTERSTITIAL_METHOD:
                result.success(loadAd((HashMap) call.arguments));
                break;
            case MopubConstants.DESTROY_INTERSTITIAL_METHOD:
                result.success(destroyAd());
                break;
            default:
                result.notImplemented();
        }
    }

    private boolean loadAd(HashMap args) {
        final String adUnitId = (String) args.get("adUnitId");
        if (interstitialAd == null) {
            interstitialAd = new MoPubInterstitial(activity, adUnitId);
            interstitialAd.setInterstitialAdListener(this);
        }
        try {
            if (!interstitialAd.isReady())
                interstitialAd.load();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private boolean showAd() {
        if (interstitialAd == null || !interstitialAd.isReady())
            return false;
        interstitialAd.show();
        return true;
    }

    private boolean destroyAd() {
        if (interstitialAd == null) {
            return false;
        } else {
            interstitialAd.setInterstitialAdListener(null);
            interstitialAd.destroy();
            interstitialAd = null;
        }
        return true;
    }

    @Override
    public void onInterstitialLoaded(MoPubInterstitial ad) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("keywords", ad.getKeywords());

        channel.invokeMethod(MopubConstants.LOADED_METHOD, args);
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial ad, MoPubErrorCode errorCode) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("keywords", ad.getKeywords());
        args.put("errorCode", errorCode.toString());

        channel.invokeMethod(MopubConstants.ERROR_METHOD, args);
    }

    @Override
    public void onInterstitialShown(MoPubInterstitial ad) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("keywords", ad.getKeywords());

        channel.invokeMethod(MopubConstants.DISPLAYED_METHOD, args);
    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial ad) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("keywords", ad.getKeywords());

        channel.invokeMethod(MopubConstants.CLICKED_METHOD, args);
    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial ad) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("keywords", ad.getKeywords());

        channel.invokeMethod(MopubConstants.DISMISSED_METHOD, args);
    }
}
