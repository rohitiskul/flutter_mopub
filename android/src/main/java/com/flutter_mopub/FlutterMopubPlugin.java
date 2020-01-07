package com.flutter_mopub;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;

import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

import static com.mopub.common.logging.MoPubLog.SdkLogEvent.CUSTOM;

/**
 * FlutterMopubPlugin
 */
public class FlutterMopubPlugin implements MethodCallHandler {

    private Activity activity;
//    private MethodChannel mainChannel;
//
//    private MopubBannerAdPlugin bannerAdPlugin;
//    private MopubInterstitialAdPlugin interstitialAdPlugin;
//    private MopubRewardedVideoAdPlugin rewardedVideoAdPlugin;

    private FlutterMopubPlugin(Activity activity) {
        this.activity = activity;
    }

//    @Override
//    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
//        setupMethodChannel(binding);
//    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    public static void registerWith(PluginRegistry.Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), MopubConstants.MAIN_CHANNEL);
        channel.setMethodCallHandler(new FlutterMopubPlugin(registrar.activity()));

        // Interstitial Ad channel
        final MethodChannel interstitialAdChannel = new MethodChannel(registrar.messenger(),
                MopubConstants.INTERSTITIAL_AD_CHANNEL);
        interstitialAdChannel.setMethodCallHandler(new MopubInterstitialAdPlugin(registrar.activity(),
                interstitialAdChannel));

        // Rewarded video Ad channel
        final MethodChannel rewardedAdChannel = new MethodChannel(registrar.messenger(),
                MopubConstants.REWARDED_VIDEO_CHANNEL);
        rewardedAdChannel.setMethodCallHandler(new MopubRewardedVideoAdPlugin(rewardedAdChannel));

        // Banner Ad PlatformView channel
        registrar.platformViewRegistry().registerViewFactory(MopubConstants.BANNER_AD_CHANNEL,
                new MopubBannerAdPlugin(registrar.messenger()));
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals(MopubConstants.INIT_METHOD))
            result.success(init((HashMap) call.arguments));
        else
            result.notImplemented();
    }

//    @Override
//    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
//        teardownMethodChannel();
//    }
//
//    @Override
//    public void onAttachedToActivity(ActivityPluginBinding binding) {
//        attachActivity(binding.getActivity());
//    }

//    @Override
//    public void onDetachedFromActivityForConfigChanges() {
//        onDetachedFromActivity();
//    }
//
//    @Override
//    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
//        onAttachedToActivity(binding);
//    }

//    @Override
//    public void onDetachedFromActivity() {
//        attachActivity(null);
//    }

//    private void attachActivity(Activity activity) {
//        this.activity = activity;
//        bannerAdPlugin.setActivity(activity);
//        interstitialAdPlugin.setActivity(activity);
//        rewardedVideoAdPlugin.setActivity(activity);
//    }

//    private void setupMethodChannel(FlutterPluginBinding binding) {
//
//        //Main channel for init
//        mainChannel = new MethodChannel(binding.getBinaryMessenger(),
//                MopubConstants.MAIN_CHANNEL);
//        mainChannel.setMethodCallHandler(this);
//
//        //Rewarded channel
//        rewardedVideoAdPlugin = new MopubRewardedVideoAdPlugin(binding.getBinaryMessenger());
//
//        //Interstitial channel
//        interstitialAdPlugin = new MopubInterstitialAdPlugin(binding.getBinaryMessenger());
//
//        // Banner ads
//        bannerAdPlugin = new MoPubBannerAdPlugin(binding.getBinaryMessenger());
//        binding.getPlatformViewRegistry().registerViewFactory(MopubConstants.BANNER_AD_CHANNEL, bannerAdPlugin);
//    }

//    private void teardownMethodChannel() {
////        mainChannel.setMethodCallHandler(null);
//        mainChannel = null;
////        rewardedVideoAdPlugin.dispose();
//        rewardedVideoAdPlugin = null;
////        interstitialAdPlugin.dispose();
//        interstitialAdPlugin = null;
////        bannerAdPlugin = null;
//    }

    private boolean init(HashMap initValues) {
        final boolean testMode = (boolean) initValues.get("testMode");
        final String adUnitId = (String) initValues.get("adUnitId");

        final SdkConfiguration.Builder configBuilder = new SdkConfiguration.Builder(adUnitId)
                .withLogLevel(testMode ? MoPubLog.LogLevel.DEBUG : MoPubLog.LogLevel.NONE);

        MoPub.initializeSdk(activity, configBuilder.build(), new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                MoPubLog.log(CUSTOM, "##Flutter## MoPub SDK initialized." +
                        " AdUnitId: " + adUnitId + " TestMode:" + testMode);
            }
        });

        return true;
    }


}
