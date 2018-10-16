package com.qinglan.sdk.android.net.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.IConnector;
import com.qinglan.sdk.android.IPresenter;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.OnResponseListener;
import com.qinglan.sdk.android.utils.SDKUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tyland on 2018/10/17.
 */
public class SDKConnector implements IConnector {
    //    private OnConnectedListener mListener;
    private IPresenter iPresenter;

    public SDKConnector(/*OnConnectedListener listener,*/ IPresenter presenter) {
//        mListener = listener;
        iPresenter = presenter;
    }

    @Override
    public void initSdk(Activity activity, final Callback.OnInitCompletedListener listener) {
        InitRequestInfo request = new InitRequestInfo();
        request.appId = getAppId(activity);
        request.platformId = iPresenter.getPlatform().getId();
        request.deviceId = Utils.getDeviceId(activity);
        request.imsi = Utils.getIMSI(activity);
        request.latitude = "";
        request.longitude = "";
        request.location = "";
        request.manufacturer = Build.BRAND;
        request.model = Build.MODEL;
        request.os = "Android " + Build.VERSION.RELEASE;
        request.networkCountryIso = Utils.getSimCountryIso(activity);
        request.phoneType = Utils.getPhoneType(activity);
        request.networkType = Utils.getNetworkClass(activity);
        request.resolution = Utils.getResolution(activity);
        request.simOperatorName = Utils.getSimOperatorName(activity);
        request.systemVersion = String.valueOf(Build.VERSION.SDK_INT);
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                boolean isSuccess = false;
                try {
                    if (success && getResponseCode(result) == HttpConstants.RESPONSE_SUCCESS_CODE) {
                        isSuccess = true;
                    } else {
                        isSuccess = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isSuccess = false;
                } finally {
                    listener.onCompleted(isSuccess, result);
                }
            }
        }).execute(request);
    }

    @Override
    public void getToken(Activity activity) {

    }

    @Override
    public void login(Activity activity) {

    }

    @Override
    public void enterGame(Activity activity, GameRole game) {

    }

    @Override
    public void createRole(Activity activity, GameRole role) {

    }

    @Override
    public void logout(Activity activity, GameRole role) {

    }

    private int getResponseCode(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        int code = jsonObject.getInt(HttpConstants.RESPONSE_CODE);
        return code;
    }

    private final String getAppId(Context ctx) {
        String id = SDKUtils.getAppId(ctx);
        if (TextUtils.isEmpty(id)) {
            id = iPresenter.getGameId();
        }
        return id;
    }
}
