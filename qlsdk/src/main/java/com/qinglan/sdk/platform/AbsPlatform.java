package com.qinglan.sdk.platform;


import android.content.Context;

import com.qinglan.sdk.http.OnRequestFinishedListener;
import com.qinglan.sdk.http.RequestTask;
import com.qinglan.sdk.http.TokenRequest;
import com.qinglan.sdk.utils.SDKUtils;

public abstract class AbsPlatform implements IPlatform {

    public void getToken(Context context, String extend) {
        TokenRequest request = new TokenRequest();
        request.appId = SDKUtils.getAppId(context);
        request.extend = extend;
        request.platformId = getPlatformInfo().getId() + "";
        new RequestTask().setFinishedListener(new OnRequestFinishedListener() {
            @Override
            public void onFinished(boolean success, String result) {

            }
        }).execute(request);
    }

}
