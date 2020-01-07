package com.flutter_mopub;

import androidx.annotation.NonNull;

import com.mopub.common.MoPubReward;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideos;

import java.util.HashMap;
import java.util.Set;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

class MopubRewardedVideoAdPlugin implements MethodChannel.MethodCallHandler, MoPubRewardedVideoListener {

    private MethodChannel channel;
//    private Activity activity;

    MopubRewardedVideoAdPlugin(MethodChannel channel) {
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
            case MopubConstants.SHOW_REWARDED_VIDEO_METHOD:
                result.success(showAd((HashMap) call.arguments));
                break;
            case MopubConstants.LOAD_REWARDED_VIDEO_METHOD:
                result.success(loadAd((HashMap) call.arguments));
                break;
//            case MopubConstants.DESTROY_REWARDED_VIDEO_METHOD:
//                result.success(destroyAd());
//                break;
            case MopubConstants.HAS_REWARDED_VIDEO_METHOD:
                result.success(hasRewardedVideo((HashMap) call.arguments));
                break;
            default:
                result.notImplemented();
        }
    }

    private boolean hasRewardedVideo(HashMap args) {
        final String adUnitId = (String) args.get("adUnitId");
        return MoPubRewardedVideos.hasRewardedVideo(adUnitId);
    }

    private boolean loadAd(HashMap args) {
        final String adUnitId = (String) args.get("adUnitId");

        if (MoPubRewardedVideos.hasRewardedVideo(adUnitId))
            return true;

        MoPubRewardedVideos.setRewardedVideoListener(this);
        MoPubRewardedVideos.loadRewardedVideo(adUnitId);

        return true;
    }

    private boolean showAd(HashMap args) {
        final String adUnitId = (String) args.get("adUnitId");

        if (MoPubRewardedVideos.hasRewardedVideo(adUnitId))
            MoPubRewardedVideos.showRewardedVideo(adUnitId);

        return true;
    }

    @Override
    public void onRewardedVideoLoadSuccess(@NonNull String adUnitId) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("adUnitId", adUnitId);

        channel.invokeMethod(MopubConstants.LOADED_METHOD, args);
    }

    @Override
    public void onRewardedVideoLoadFailure(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("adUnitId", adUnitId);
        args.put("errorCode", errorCode.toString());

        channel.invokeMethod(MopubConstants.ERROR_METHOD, args);
    }

    @Override
    public void onRewardedVideoStarted(@NonNull String adUnitId) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("adUnitId", adUnitId);

        channel.invokeMethod(MopubConstants.REWARDED_PLAYBACK_STARTED, args);
    }

    @Override
    public void onRewardedVideoPlaybackError(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("adUnitId", adUnitId);
        args.put("errorCode", errorCode.toString());

        channel.invokeMethod(MopubConstants.REWARDED_PLAYBACK_ERROR, args);
    }

    @Override
    public void onRewardedVideoClicked(@NonNull String adUnitId) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("adUnitId", adUnitId);

        channel.invokeMethod(MopubConstants.CLICKED_METHOD, args);
    }

    @Override
    public void onRewardedVideoClosed(@NonNull String adUnitId) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("adUnitId", adUnitId);

        channel.invokeMethod(MopubConstants.REWARDED_VIDEO_CLOSED_METHOD, args);
    }

    @Override
    public void onRewardedVideoCompleted(@NonNull Set<String> adUnitIds, @NonNull MoPubReward reward) {
        HashMap<String, Object> args = new HashMap<>();
//        args.put("adUnitIds", adUnitIds);
        args.put("success", reward.isSuccessful());
        args.put("reward", reward.isSuccessful() ? reward.getAmount() : 0);

        channel.invokeMethod(MopubConstants.REWARDED_VIDEO_COMPLETED_METHOD, args);
    }
}
