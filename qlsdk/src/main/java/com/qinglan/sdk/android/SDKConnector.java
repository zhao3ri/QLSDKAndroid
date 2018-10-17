package com.qinglan.sdk.android;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.IConnector;
import com.qinglan.sdk.android.IPresenter;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.OnResponseListener;
import com.qinglan.sdk.android.net.impl.CleanSessionRequestInfo;
import com.qinglan.sdk.android.net.impl.ExitRequestInfo;
import com.qinglan.sdk.android.net.impl.GameRoleRequestInfo;
import com.qinglan.sdk.android.net.impl.HeartBeatRequestInfo;
import com.qinglan.sdk.android.net.impl.InitRequestInfo;
import com.qinglan.sdk.android.net.impl.OrderRequestInfo;
import com.qinglan.sdk.android.net.impl.RefreshSessionRequestInfo;
import com.qinglan.sdk.android.net.impl.TokenRequestInfo;
import com.qinglan.sdk.android.utils.SDKUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhaoj on 2018/10/17.
 */
public class SDKConnector implements IConnector {
    private IPresenter iPresenter;

    public SDKConnector(IPresenter presenter) {
        iPresenter = presenter;
    }

    @Override
    public void initSdk(Context context, final Callback.OnInitCompletedListener listener) {
        InitRequestInfo request = new InitRequestInfo();
        request.appId = iPresenter.getGameId();
        request.platformId = iPresenter.getPlatformId();
        request.deviceId = iPresenter.getDeviceId();
        request.imsi = Utils.getIMSI(context);
        request.latitude = "";
        request.longitude = "";
        request.location = "";
        request.manufacturer = Build.BRAND;
        request.model = Build.MODEL;
        request.os = "Android " + Build.VERSION.RELEASE;
        request.networkCountryIso = Utils.getSimCountryIso(context);
        request.phoneType = Utils.getPhoneType(context);
        request.networkType = Utils.getNetworkClass(context);
        request.resolution = Utils.getResolution(context);
        request.simOperatorName = Utils.getSimOperatorName(context);
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
                    Log.e("initSdk error, The jason was not properly formatted." + result);
                    isSuccess = false;
                } finally {
                    if (listener != null)
                        listener.onCompleted(isSuccess, result);
                }
            }
        }).execute(request);
    }

    @Override
    public void getToken(String uid, final Callback.GetTokenListener listener) {
        TokenRequestInfo request = new TokenRequestInfo();
        request.appId = iPresenter.getGameId();
        request.platformId = iPresenter.getPlatformId();
        request.extend = uid;
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                try {
                    if (success && getResponseCode(result) == HttpConstants.RESPONSE_SUCCESS_CODE) {//获取token 成功
                        JSONObject jsonObject = new JSONObject(result);
                        String token = jsonObject.optString(HttpConstants.RESPONSE_TOKEN);
                        if (listener != null)
                            listener.onFinished(success, token);
                    } else {//获取token失败
                        Log.e("Get token failed!");
                        if (listener != null)
                            listener.onFinished(false, result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("getToken error, The jason was not properly formatted." + result);
                    if (listener != null)
                        listener.onFinished(false, result);
                }
            }
        }).execute(request);
    }

    @Override
    public void refreshSession(final Context context, final GameRole role, final Callback.OnRefreshSessionListener listener) {
        RefreshSessionRequestInfo request = new RefreshSessionRequestInfo();
        request.appId = iPresenter.getGameId();
        request.platformId = iPresenter.getPlatformId();
        request.uid = iPresenter.getUid();
        request.zoneId = role.getZoneId();
        request.zoneName = role.getZoneName();
        request.roleId = role.getRoleId();
        request.roleName = role.getRoleName();
        request.roleLevel = role.getRoleLevel();
        request.deviceId = iPresenter.getDeviceId();
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                try {
                    if (success && getResponseCode(result) == HttpConstants.RESPONSE_SUCCESS_CODE) {
                        JSONObject jsonObject = new JSONObject(result);
                        String timestamp = jsonObject.optString(HttpConstants.RESPONSE_LOGIN_TIMESTAMP);
                        if (listener != null)
                            listener.onRefreshed(true, timestamp);
                    } else {
                        if (listener != null)
                            listener.onRefreshed(false, result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("refreshSession error, The jason was not properly formatted." + result);
                    if (listener != null)
                        listener.onRefreshed(false, result);
                }
            }
        }).execute(request);
    }

    @Override
    public void startHeartBeat(Context context, GameRole role, String time, final Callback.HeartBeanRequestListener listener) {
        HeartBeatRequestInfo request = new HeartBeatRequestInfo();
        request.appId = iPresenter.getGameId();
        request.platformId = iPresenter.getPlatformId();
        request.loginTime = time;
        request.uid = iPresenter.getUid();
        request.zoneId = role.getZoneId();
        request.roleId = role.getRoleId();
        request.deviceId = iPresenter.getDeviceId();
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                try {
                    if (success && getResponseCode(result) == HttpConstants.RESPONSE_SUCCESS_CODE) {
                        if (listener != null)
                            listener.onResponse(true, result);
                    } else {
                        if (listener != null) {
                            listener.onResponse(false, result);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("startHeartBeat error, The jason was not properly formatted." + result);
                    if (listener != null)
                        listener.onResponse(false, result);
                }
            }
        }).execute(request);
    }

    @Override
    public void cleanSession(Context context, GameRole role, final Callback.OnLogoutResponseListener listener) {
        CleanSessionRequestInfo request = new CleanSessionRequestInfo();
        request.appId = iPresenter.getGameId();
        request.platformId = iPresenter.getPlatformId();
        request.uid = iPresenter.getUid();
        if (role != null) {
            request.zoneId = role.getZoneId();
            request.roleId = role.getRoleId();
        }
        request.deviceId = iPresenter.getDeviceId();
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                try {
                    if (success && getResponseCode(result) == HttpConstants.RESPONSE_SUCCESS_CODE) {
                        if (listener != null)
                            listener.onSuccess();

                    } else {
                        if (listener != null)
                            listener.onFailed(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("cleanSession error, The jason was not properly formatted." + result);
                    if (listener != null)
                        listener.onFailed(result);
                }
            }
        }).execute(request);
    }

    @Override
    public void exit(Context context, GameRole role, final Callback.OnExitListener listener) {
        ExitRequestInfo request = new ExitRequestInfo();
        request.appId = iPresenter.getGameId();
        request.platformId = iPresenter.getPlatformId();
        request.uid = iPresenter.getUid();
        request.zoneId = role.getZoneId();
        request.roleId = role.getRoleId();
        request.deviceId = iPresenter.getDeviceId();
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                try {
                    if (success && getResponseCode(result) == HttpConstants.RESPONSE_SUCCESS_CODE) {
                        if (listener != null)
                            listener.onCompleted(true, result);
                    } else {
                        if (listener != null)
                            listener.onCompleted(false, result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exit error, The jason was not properly formatted." + result);
                    if (listener != null)
                        listener.onCompleted(false, result);
                }
            }
        }).execute(request);
    }

    @Override
    public void createRole(Context context, GameRole role, final Callback.OnCreateRoleFinishedListener listener) {
        GameRoleRequestInfo request = new GameRoleRequestInfo();
        request.appId = iPresenter.getGameId();
        request.platformId = iPresenter.getPlatformId();
        request.uid = iPresenter.getUid();
        request.zoneId = role.getZoneId();
        request.zoneName = role.getZoneName();
        request.roleId = role.getRoleId();
        request.roleName = role.getRoleName();
        request.roleLevel = role.getRoleLevel();
        request.deviceId = iPresenter.getDeviceId();
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                try {
                    if (success && getResponseCode(result) == HttpConstants.RESPONSE_SUCCESS_CODE) {
                        if (listener != null)
                            listener.onFinished(true, result);
                    } else {
                        if (listener != null)
                            listener.onFinished(false, result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("createRole error, The jason was not properly formatted." + result);
                    if (listener != null)
                        listener.onFinished(false, result);
                }
            }
        }).execute(request);
    }

    @Override
    public void generateOrder(Context context, GameRole game, GamePay pay, int fixed, String loginTime, final Callback.GenerateOrderListener listener) {
        OrderRequestInfo request = new OrderRequestInfo();
        request.appId = iPresenter.getGameId();
        request.platformId = iPresenter.getPlatformId();
        request.uid = iPresenter.getUid();
        request.zoneId = game.getZoneId();
        request.roleId = game.getRoleId();
        request.roleName = game.getRoleName();
        request.cpOrderId = pay.getCpOrderId();
        request.extInfo = pay.getExtInfo();
        request.amount = String.valueOf(pay.getMoney());
        request.notifyUrl = pay.getNotifyUrl();
        request.fixed = String.valueOf(fixed);
        request.loginTime = loginTime;
        request.deviceId = iPresenter.getDeviceId();
        request.gold = String.valueOf(pay.getProductCount());
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                try {
                    if (success && getResponseCode(result) == HttpConstants.RESPONSE_SUCCESS_CODE) {
                        JSONObject jsonObject = new JSONObject(result);
                        String orderId = jsonObject.optString(HttpConstants.RESPONSE_ORDER_ID);
                        String notifyUrl = jsonObject.optString(HttpConstants.REQUEST_PARAM_NOTIFY_URL);
                        if (listener != null)
                            listener.onSuccess(orderId, notifyUrl);
                    } else {
                        if (listener != null)
                            listener.onFailed(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("generateOrder error, The jason was not properly formatted." + result);
                    if (listener != null)
                        listener.onFailed(result);
                }
            }
        }).execute(request);

    }

    private int getResponseCode(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        int code = jsonObject.getInt(HttpConstants.RESPONSE_CODE);
        return code;
    }
}
