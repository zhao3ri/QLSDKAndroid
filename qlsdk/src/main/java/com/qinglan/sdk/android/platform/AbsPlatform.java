package com.qinglan.sdk.android.platform;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.qinglan.sdk.android.http.OnRequestFinishedListener;
import com.qinglan.sdk.android.http.RequestTask;
import com.qinglan.sdk.android.http.impl.TokenRequest;
import com.qinglan.sdk.android.utils.PermissionUtils;
import com.qinglan.sdk.android.utils.SDKUtils;


public abstract class AbsPlatform implements IPlatform {

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Activity activity) {
        if (PermissionUtils.checkSelfPermission(activity, PermissionUtils.CODE_READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            onPermissionGranted(PermissionUtils.CODE_READ_EXTERNAL_STORAGE);
        } else {
            requestPermission(activity, PermissionUtils.CODE_READ_EXTERNAL_STORAGE);
        }
    }

    protected int requestPermission(Activity activity, int requestCode) {
        return PermissionUtils.requestPermission(activity, requestCode, null);
    }

    public void getToken(Context context, String extend) {
        TokenRequest request = new TokenRequest();
        request.appId = SDKUtils.getAppId(context);
        request.extend = extend;
        request.platformId = "";
        new RequestTask().setFinishedListener(new OnRequestFinishedListener() {
            @Override
            public void onFinished(boolean success, String result) {

            }
        }).execute(request);
    }

}
