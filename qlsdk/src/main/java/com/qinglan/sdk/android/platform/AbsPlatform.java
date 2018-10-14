package com.qinglan.sdk.android.platform;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.R;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.net.OnResponseListener;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.impl.InitRequestInfo;
import com.qinglan.sdk.android.net.impl.TokenRequestInfo;
import com.qinglan.sdk.android.utils.PermissionUtils;
import com.qinglan.sdk.android.utils.SDKUtils;
import com.qinglan.sdk.android.utils.ToastUtils;


public abstract class AbsPlatform implements IPlatform {
    protected Config gameConfig;

    @Override
    public final void setGameConfig(Config config) {
        gameConfig = config;
    }

    @Override
    public void init(Activity activity, final Callback.OnInitCompletedListener listener) {
        InitRequestInfo request = new InitRequestInfo();
        request.appId = getAppId(activity);
        request.platformId = getId();
        request.deviceId = Utils.getDeviceId(activity);
        request.imsi = Utils.getIMSI(activity);
        request.latitude = "";
        request.longitude = "";
        request.location = "";
        request.manufacturer = Build.BRAND;
        request.model = Build.MODEL;
        request.networkCountryIso = Utils.getSimCountryIso(activity);
        request.phoneType = Utils.getPhoneType(activity);
        request.networkType = Utils.getNetworkClass(activity);
        request.os = "Android " + Build.VERSION.RELEASE;
        request.resolution = Utils.getDeviceHeight(activity) + "x" + Utils.getDeviceWidth(activity);
        request.simOperatorName = Utils.getSimOperatorName(activity);
        request.systemVersion = String.valueOf(Build.VERSION.SDK_INT);
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                Log.d("init: " + success);
                if (listener != null) {
                    listener.onCompleted(success, result);
                }
            }
        }).execute(request);
    }

    protected final String getAppId(Context ctx) {
        String id = SDKUtils.getAppId(ctx);
        if (TextUtils.isEmpty(id)) {
            id = gameConfig.getGameId();
        }
        return id;
    }

    /**
     * 请求token
     */
    protected void getToken(final Context context, String extend) {
        TokenRequestInfo request = new TokenRequestInfo();
        request.appId = getAppId(context);
        request.platformId = getId();
        request.extend = extend;
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                if (success) {//获取token 成功

                } else {//获取token失败
                    ToastUtils.showToast(context, R.string.qlsdk_get_token_error);
//                    ToastUtils.showToast(context, ResContainer.get(context).string("qlsdk_get_token_error"));
                }
            }
        }).execute(request);
    }

}
