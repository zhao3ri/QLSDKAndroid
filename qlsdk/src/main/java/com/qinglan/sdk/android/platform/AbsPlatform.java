package com.qinglan.sdk.android.platform;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.PlatformHandler;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.OnResponseListener;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.impl.HeartBeatRequestInfo;
import com.qinglan.sdk.android.net.impl.InitRequestInfo;
import com.qinglan.sdk.android.net.impl.SubmitRequestInfo;
import com.qinglan.sdk.android.net.impl.TokenRequestInfo;
import com.qinglan.sdk.android.utils.SDKUtils;
import com.qinglan.sdk.android.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class AbsPlatform implements IPlatform {
    protected PlatformHandler handler;

    @Override
    public final void setHandler(PlatformHandler handler) {
        this.handler = handler;
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
            id = handler.getGameId();
        }
        return id;
    }

    @Override
    public final void setRole(final Activity activity, final boolean showFloat, final GameRole role, final Callback.OnGameStartedListener listener) {
        selectRole(activity, showFloat, role, new Callback.OnGameStartedListener() {
            @Override
            public void onGameStarted(long timestamp) {
                //渠道平台设置游戏角色成功后，SDK将游戏相关数据提交到服务器
                submit(activity, showFloat, role, listener);
            }

            @Override
            public void onFailed(String result) {
                listener.onFailed(result);
            }
        });
    }

    public abstract void selectRole(Activity activity, boolean showFloat, GameRole role, Callback.OnGameStartedListener listener);

    private final void submit(final Activity activity, final boolean showFloat, final GameRole role, final Callback.OnGameStartedListener listener) {
        SubmitRequestInfo request = new SubmitRequestInfo();
        request.appId = getAppId(activity);
        request.platformId = getId();
        request.uid = UserPreferences.get(activity, UserPreferences.KEY_UID, "");
        request.zoneId = role.getZoneId();
        request.zoneName = role.getZoneName();
        request.roleId = role.getRoleId();
        request.roleName = role.getRoleName();
        request.roleLevel = role.getRoleLevel();
        request.deviceId = Utils.getDeviceId(activity);
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                if (success) {
                    handleResult(activity, result, role, showFloat, listener);
                } else {
                    listener.onFailed(result);
                }
            }
        }).execute(request);
    }

    private void handleResult(Activity activity, String result, GameRole role, boolean showFloat, Callback.OnGameStartedListener listener) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int code = jsonObject.optInt(HttpConstants.RESPONSE_CODE);
            if (code == HttpConstants.RESPONSE_SUCCESS_CODE) {
                long timestamp = Long.parseLong(jsonObject.optString(HttpConstants.RESPONSE_LOGIN_TIMESTAMP));
                if (!handler.isHeartBeating()) {//心跳未跳动
                    //启动心跳
                    requestHeart(activity, role);
                    handler.setHeartBeating(true);
                    handler.setLoginTime(timestamp);
                }
                listener.onGameStarted(timestamp);
                if (showFloat) {
                    showFloat(activity);
                }
            } else {
                listener.onFailed(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFailed(result);
        }
    }

    private void requestHeart(final Activity activity, final GameRole role) {
        handler.startBeat(activity, new PlatformHandler.OnHeartBeatListener() {
            @Override
            public void onBeat() {
                if (!handler.isLogged()) {
                    return;
                }
                HeartBeatRequestInfo request = new HeartBeatRequestInfo();
                request.appId = getAppId(activity);
                request.platformId = getId();
                request.loginTime = String.valueOf(handler.getLoginTime());
                request.uid = UserPreferences.get(activity, UserPreferences.KEY_UID, "");
                request.zoneId = role.getZoneId();
                request.roleId = role.getRoleId();
                request.deviceId = Utils.getDeviceId(activity);
                new HttpConnectionTask().setResponseListener(new OnResponseListener() {
                    @Override
                    public void onResponse(boolean success, String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int code = jsonObject.optInt(HttpConstants.RESPONSE_CODE);
                            if (code == HttpConstants.RESPONSE_SUCCESS_CODE) {
                                Log.i("heart beat result:" + success);
                                return;
                            }
                            Log.e("heart beat result:" + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).execute(request);
            }
        });
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
                    ToastUtils.showToast(context, "get token failed!");
//                    ToastUtils.showToast(context, ResContainer.get(context).string("qlsdk_get_token_error"));
                }
            }
        }).execute(request);
    }

}
