package com.flutter_mopub;

import android.content.Context;
import android.view.View;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.util.HashMap;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

class MopubBannerAdPlugin extends PlatformViewFactory {

    private final BinaryMessenger messenger;
//    private Activity activity;

    MopubBannerAdPlugin(BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
    }

//    void setActivity(Activity activity) {
//        this.activity = activity;
//    }

    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        return new MopubBannerAdView(context, viewId, (HashMap) args, messenger);
    }
}

class MopubBannerAdView implements MoPubView.BannerAdListener, PlatformView {
    private MoPubView adView;
    private MethodChannel channel;

//    private final int id;
//    private final Context context;
//    private final HashMap args;
//    private final BinaryMessenger messenger;

    MopubBannerAdView(Context context, int id, HashMap args, BinaryMessenger messenger) {
//        this.id = id;
//        this.context = context;
//        this.args = args;
//        this.messenger = messenger;

        channel = new MethodChannel(messenger, MopubConstants.BANNER_AD_CHANNEL + "_" + id);
        final String adUnitId = (String) args.get("adUnitId");
        final boolean autoRefresh = (boolean) args.get("autoRefresh");
        final int height = (int) args.get("height");

        adView = new MoPubView(context);
        adView.setAdUnitId(adUnitId);
        adView.setAutorefreshEnabled(autoRefresh);
        adView.setAdSize(getBannerAdSize(height));
        adView.setBannerAdListener(this);
        adView.loadAd();
    }

//    private void createAdView() {
//        final String adUnitId = (String) args.get("adUnitId");
//        final boolean autoRefresh = (boolean) args.get("autoRefresh");
//        final int height = (int) args.get("height");

//        adView = new View(context);
//        adView.setAdUnitId(adUnitId);
//        adView.setAutorefreshEnabled(autoRefresh);
//        adView.setAdSize(getBannerAdSize(height));
//    }

    private MoPubView.MoPubAdSize getBannerAdSize(double height) {
        if (height >= 280) {
            return MoPubView.MoPubAdSize.HEIGHT_280;
        } else if (height >= 250) {
            return MoPubView.MoPubAdSize.HEIGHT_250;
        } else if (height >= 90) {
            return MoPubView.MoPubAdSize.HEIGHT_90;
        } else if (height >= 50) {
            return MoPubView.MoPubAdSize.HEIGHT_50;
        } else {
            return MoPubView.MoPubAdSize.MATCH_VIEW;
        }
    }

    @Override
    public void onBannerLoaded(MoPubView banner) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("adUnitId", banner.getAdUnitId());

        channel.invokeMethod(MopubConstants.LOADED_METHOD, args);
    }

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("adUnitId", banner.getAdUnitId());
        args.put("errorCode", errorCode.toString());

        channel.invokeMethod(MopubConstants.ERROR_METHOD, args);
    }

    @Override
    public void onBannerClicked(MoPubView banner) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("adUnitId", banner.getAdUnitId());

        channel.invokeMethod(MopubConstants.CLICKED_METHOD, args);
    }

    @Override
    public void onBannerExpanded(MoPubView banner) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("adUnitId", banner.getAdUnitId());

        channel.invokeMethod(MopubConstants.EXPANDED_METHOD, args);
    }

    @Override
    public void onBannerCollapsed(MoPubView banner) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("adUnitId", banner.getAdUnitId());

        channel.invokeMethod(MopubConstants.DISMISSED_METHOD, args);
    }

    @Override
    public View getView() {
        return adView;
    }

    @Override
    public void dispose() {
//        channel.setMethodCallHandler(null);
        if (adView != null) {
            adView.setAutorefreshEnabled(false);
            adView.destroy();
        }
//        disposed = true;
    }

    @Override
    public void onInputConnectionLocked() {
    }

    @Override
    public void onInputConnectionUnlocked() {
    }

//    @Override
//    public void onFlutterViewAttached(View view) {
//        Log.d("###FLUTTER### Mopub", "View attached " + id + " disposed?" + disposed);
//        channel = new MethodChannel(messenger, MopubConstants.BANNER_AD_CHANNEL + "_" + id);
//        if (disposed) {
//            createAdView();
//        }
//        adView.setAutorefreshEnabled((boolean) args.get("autoRefresh"));
//        adView.setBannerAdListener(this);
//        adView.loadAd();
//    }

    //    @Override
//    public void onFlutterViewDetached() {
//        Log.d("###FLUTTER### Mopub", "View detached " + id);
//        adView.setX(100);
//        adView.setAutorefreshEnabled(false);
//        adView.destroy();
//        dispose();
//    }
}
