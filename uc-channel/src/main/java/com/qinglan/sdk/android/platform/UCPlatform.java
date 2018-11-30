package com.qinglan.sdk.android.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.OnResponseListener;
import com.qinglan.sdk.android.platform.entity.UCResponse;
import com.qinglan.sdk.android.platform.entity.UCSessionRequest;

import java.util.Map;

import cn.gundam.sdk.shell.even.SDKEventKey;
import cn.gundam.sdk.shell.even.SDKEventReceiver;
import cn.gundam.sdk.shell.even.Subscribe;
import cn.gundam.sdk.shell.exception.AliLackActivityException;
import cn.gundam.sdk.shell.exception.AliNotInitException;
import cn.gundam.sdk.shell.open.OrderInfo;
import cn.gundam.sdk.shell.open.ParamInfo;
import cn.gundam.sdk.shell.open.UCOrientation;
import cn.gundam.sdk.shell.param.SDKParamKey;
import cn.gundam.sdk.shell.param.SDKParams;
import cn.uc.gamesdk.UCGameSdk;

public class UCPlatform extends BasePlatform {
    private static final String PARAM_PAY_SIGN = "sign";
    private static final String SIGN_TYPE_MD5 = "MD5";

    @Override
    public void init(Activity activity, final Callback.OnInitConnectedListener listener) {
        ParamInfo pi = new ParamInfo();
        pi.setGameId(Integer.valueOf(gameConfig.getAppID()));
        pi.setOrientation(gameConfig.getScreenOrientation() == Config.SCREEN_ORIENTATION_LANDSCAPE
                ? UCOrientation.LANDSCAPE : UCOrientation.PORTRAIT);
        SDKParams sdkParams = new SDKParams();
        sdkParams.put(SDKParamKey.GAME_PARAMS, pi);
        try {
            UCGameSdk.defaultSdk().initSdk(activity, sdkParams);
            SDKEventReceiver receiver = new SDKEventReceiver() {
                @Subscribe(event = SDKEventKey.ON_INIT_SUCC)
                private void onInitSucc() {
                    if (listener != null)
                        listener.initSuccess(null);
                    UCGameSdk.defaultSdk().unregisterSDKEventReceiver(this);
                }

                @Subscribe(event = SDKEventKey.ON_INIT_FAILED)
                private void onInitFailed(String desc) {
                    if (listener != null)
                        listener.initFailed(desc);
                    UCGameSdk.defaultSdk().unregisterSDKEventReceiver(this);
                }
            };
            UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);
        } catch (AliLackActivityException e) {
            e.printStackTrace();
            if (listener != null)
                listener.initFailed(e.getMessage());
        }
    }

    @Override
    public void login(Activity activity, final Callback.OnLoginListener listener) {
        try {
            UCGameSdk.defaultSdk().login(activity, null);
            SDKEventReceiver receiver = new SDKEventReceiver() {
                @Subscribe(event = SDKEventKey.ON_LOGIN_SUCC)
                private void onLoginSucc(String sid) {
                    if (!TextUtils.isEmpty(sid)) {
                        Log.d("sid===" + sid);
                        //登录成功后认证session，若认证成功返回userInfo
                        verifySession(sid, listener);
                    }
                }

                @Subscribe(event = SDKEventKey.ON_LOGIN_FAILED)
                private void onLoginFailed(String desc) {
                    if (listener != null)
                        listener.loginFailed(desc);
                }
            };
            UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);
        } catch (AliNotInitException | AliLackActivityException e) {
            if (listener != null)
                listener.loginFailed(e.getMessage());
        }
    }

    private void verifySession(String sid, final Callback.OnLoginListener listener) {
        final UCSessionRequest request = new UCSessionRequest();
        request.gameId = Long.valueOf(gameConfig.getGameId());
        request.platformId = getId();
        request.sid = sid;
        request.appID = gameConfig.getAppID();
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {

            @Override
            public void onResponse(boolean success, String result) {
                if (!success) {
                    if (listener != null)
                        listener.loginFailed(result);
                    return;
                }
                UCResponse response = Utils.json2Object(result, UCResponse.class);
                if (null == response
                        || Integer.valueOf(response.state.get(UCResponse.RESPONSE_KEY_CODE).toString()) != UCResponse.RESPONSE_SUCCESS_CODE) {
                    if (listener != null)
                        listener.loginFailed(result);
                    return;
                }
                UserInfo userInfo = new UserInfo();
                userInfo.setId(response.data.get(UCResponse.RESPONSE_KEY_ACCOUNT_ID).toString());
                userInfo.setUserName(response.data.get(UCResponse.RESPONSE_KEY_NICKNAME).toString());
                if (listener != null) {
                    listener.loginSuccess(userInfo);
                }
            }
        }).execute(request);
    }

    @Override
    public void logout(Activity activity, GameRole role, final Callback.OnLogoutResponseListener listener) {
        try {
            UCGameSdk.defaultSdk().logout(activity, null);
            SDKEventReceiver receiver = new SDKEventReceiver() {
                @Subscribe(event = SDKEventKey.ON_LOGOUT_SUCC)
                private void onLogoutSucc() {
                    if (listener != null)
                        listener.onSuccess();
                }

                @Subscribe(event = SDKEventKey.ON_LOGOUT_FAILED)
                private void onLogoutFailed() {
                    if (listener != null)
                        listener.onFailed("logout failed!");
                }
            };
            UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);
        } catch (AliLackActivityException | AliNotInitException e) {
            e.printStackTrace();
            if (listener != null)
                listener.onFailed(e.getMessage());
        }
    }

    @Override
    public void showWinFloat(Activity activity) {

    }

    @Override
    public void hideWinFloat(Activity activity) {

    }

    @Override
    public void exit(Activity activity, GameRole role, final Callback.OnExitListener listener) {
        try {
            UCGameSdk.defaultSdk().exit(activity, null);
            SDKEventReceiver receiver = new SDKEventReceiver() {
                @Subscribe(event = SDKEventKey.ON_EXIT_SUCC)
                private void onExitSucc() {
                    if (listener != null)
                        listener.onCompleted(true, "");
                    UCGameSdk.defaultSdk().unregisterSDKEventReceiver(this);
                }

                @Subscribe(event = SDKEventKey.ON_EXIT_CANCELED)
                private void onExitCanceled() {
                    if (listener != null)
                        listener.onCompleted(false, "exit error");
                    UCGameSdk.defaultSdk().unregisterSDKEventReceiver(this);
                }
            };
            UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);
        } catch (AliNotInitException | AliLackActivityException e) {
            if (listener != null)
                listener.onCompleted(false, e.getMessage());
        }
    }

    @Override
    public void pay(Activity activity, GameRole role, GamePay
            pay, final Map<String, Object> result, final Callback.OnPayRequestListener listener) {
        SDKParams params = new SDKParams();
        params.put(SDKParamKey.CALLBACK_INFO, pay.getExtInfo());
        params.put(SDKParamKey.NOTIFY_URL, pay.getNotifyUrl());
        params.put(SDKParamKey.AMOUNT, pay.getAmount() / 100);
        params.put(SDKParamKey.CP_ORDER_ID, pay.getCpOrderId());
        params.put(SDKParamKey.ACCOUNT_ID, user.getId());
        params.put(SDKParamKey.SIGN_TYPE, SIGN_TYPE_MD5);
        params.put(SDKParamKey.SIGN, result.get(PARAM_PAY_SIGN).toString());
        // 以上字段的值都需要由游戏服务器生成,各字段详细说明，见SDKParamKey参数表
        try {
            UCGameSdk.defaultSdk().pay(activity, params);
            SDKEventReceiver receiver = new SDKEventReceiver() {
                @Subscribe(event = SDKEventKey.ON_CREATE_ORDER_SUCC)
                private void onCreateOrderSucc(OrderInfo orderInfo) {
                    if (orderInfo != null) {
                        String txt = orderInfo.getOrderAmount() + "," + orderInfo.getOrderId() + "," + orderInfo.getPayWay();
                        Log.d("create order success:" + txt);
                    }
                    if (listener != null)
                        listener.onSuccess(result.get(HttpConstants.RESPONSE_ORDER_ID).toString());
                    UCGameSdk.defaultSdk().unregisterSDKEventReceiver(this);
                }

                @Subscribe(event = SDKEventKey.ON_PAY_USER_EXIT)
                private void onPayUserExit(OrderInfo orderInfo) {
                    if (orderInfo != null) {
                        String txt = orderInfo.getOrderAmount() + "," + orderInfo.getOrderId() + "," + orderInfo.getPayWay();
                        Log.d("create order failed:" + txt);
                    }
                    if (listener != null)
                        listener.onFailed("pay failed!");
                    UCGameSdk.defaultSdk().unregisterSDKEventReceiver(this);
                }
            };
            UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);
        } catch (IllegalArgumentException | AliNotInitException | AliLackActivityException e) {
            //传入参数错误异常处理
        }
    }

    @Override
    public void createRole(Activity activity, GameRole role, long createTime, Callback.OnGameRoleRequestListener listener) {
        selectRole(activity, false, role, createTime, listener);
    }

    @Override
    public void selectRole(Activity activity, boolean showFloat, GameRole role, long createTime, Callback.OnGameRoleRequestListener listener) {
        try {
            submitRoleData(activity, role, createTime);
            if (listener != null)
                listener.onSuccess(role);
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null)
                listener.onFailed(e.getMessage());
        }
    }

    @Override
    public void levelUpdate(Activity activity, GameRole role, long createTime, Callback.OnLevelUpListener listener) {
        try {
            submitRoleData(activity, role, createTime);
            if (listener != null)
                listener.onCompleted(true, "");
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null)
                listener.onCompleted(false, e.getMessage());
        }
    }

    private void submitRoleData(Activity activity, GameRole role, long createTime) throws AliNotInitException, AliLackActivityException {
        SDKParams sdkParams = new SDKParams();
        sdkParams.put(SDKParamKey.STRING_ROLE_ID, role.getRoleId());
        sdkParams.put(SDKParamKey.STRING_ROLE_NAME, role.getRoleName());
        sdkParams.put(SDKParamKey.LONG_ROLE_LEVEL, Long.valueOf(role.getRoleLevel()));
        sdkParams.put(SDKParamKey.LONG_ROLE_CTIME, createTime);
        sdkParams.put(SDKParamKey.STRING_ZONE_ID, role.getZoneId());
        sdkParams.put(SDKParamKey.STRING_ZONE_NAME, role.getZoneName());
        UCGameSdk.defaultSdk().submitRoleData(activity, sdkParams);
        Log.d("submitData = " + sdkParams);
    }

    @Override
    public boolean isCustomLogoutUI() {
        return false;
    }

    @Override
    public void onCreate(Activity activity) {
    }

    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onPause(Activity activity) {

    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void attachBaseContext(Context newBase) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }
}
