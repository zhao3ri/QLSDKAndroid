package com.qinglan.sdk.android.channel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.game.sdk.HuosdkManager;
import com.game.sdk.domain.CustomPayParam;
import com.game.sdk.domain.LoginErrorMsg;
import com.game.sdk.domain.LogincallBack;
import com.game.sdk.domain.PaymentCallbackInfo;
import com.game.sdk.domain.PaymentErrorMsg;
import com.game.sdk.domain.RoleInfo;
import com.game.sdk.domain.SubmitRoleInfoCallBack;
import com.game.sdk.listener.OnInitSdkListener;
import com.game.sdk.listener.OnLoginListener;
import com.game.sdk.listener.OnLogoutListener;
import com.game.sdk.listener.OnPaymentListener;
import com.google.gson.reflect.TypeToken;
import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.channel.entity.HuoVerifyRequest;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.OnResponseListener;

import java.util.Map;

public class HuoSdkChannel extends BaseChannel {
    private static final int RESULT_STATUS_SUCCESS = 1;
    private static final String RESULT_STATUS = "status";
    private static final String RESULT_MSG = "msg";
    private HuosdkManager sdkManager;

    @Override
    public void load(ChannelParamsReader.ChannelParam p, Config config) {
        super.load(p, config);
        sdkManager = HuosdkManager.getInstance();
        sdkManager.setDirectLogin(false);
//        sdkManager.setFloatInitXY(500, 200);
    }

    @Override
    public void init(Activity activity, final Callback.OnInitConnectedListener listener) {
        sdkManager.initSdk(activity, new OnInitSdkListener() {
            @Override
            public void initSuccess(String code, String msg) {
                if (listener != null) {
                    listener.onSuccess(null);
                }
            }

            @Override
            public void initError(String code, String msg) {
                if (listener != null) {
                    listener.onFailed(getErrorMsg(code, msg));
                }
            }
        });
    }

    @Override
    public void login(Activity activity, final Callback.OnLoginListener listener) {
        sdkManager.addLoginListener(new OnLoginListener() {
            @Override
            public void loginSuccess(LogincallBack logincallBack) {
                verifyLogin(logincallBack, listener);
            }

            @Override
            public void loginError(LoginErrorMsg loginErrorMsg) {
                if (listener != null) {
                    listener.onFailed(getErrorMsg(String.valueOf(loginErrorMsg.code), loginErrorMsg.msg));
                }
            }
        });
        sdkManager.showLogin(true);
    }

    private void verifyLogin(final LogincallBack logincallBack, final Callback.OnLoginListener listener) {
        HuoVerifyRequest request = new HuoVerifyRequest();
        request.gameId = gameConfig.getGameId();
        request.channelId = getId();
        request.appId = gameConfig.getAppID();
        request.memId = logincallBack.mem_id;
        request.userToken = logincallBack.user_token;
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {

            @Override
            public void onResponse(boolean success, String result) {
                if (success && !TextUtils.isEmpty(result)) {
                    Map<String, String> resMap = Utils.json2Object(result, new TypeToken<Map<String, String>>() {
                    }.getType());
                    String status = resMap.get(RESULT_STATUS);
                    String msg = resMap.get(RESULT_MSG);
                    if (TextUtils.isEmpty(status)) {
                        if (listener != null)
                            listener.onFailed(result);
                        return;
                    }
                    if (Integer.valueOf(status) == RESULT_STATUS_SUCCESS) {
                        if (listener != null) {
                            listener.onSuccess(getUser(logincallBack));
                        }
                    } else {
                        if (listener != null)
                            listener.onFailed(getErrorMsg(status, msg));
                    }
                } else {
                    if (listener != null)
                        listener.onFailed(result);
                }
            }
        }).execute(request);
    }

    private UserInfo getUser(LogincallBack logincallBack) {
        UserInfo user = new UserInfo();
        user.setId(logincallBack.mem_id);
        user.setUdToken(logincallBack.user_token);
        return user;
    }

    @Override
    public void logout(Activity activity, GameRole role, final Callback.OnLogoutResponseListener listener) {
        sdkManager.addLogoutListener(new OnLogoutListener() {
            @Override
            public void logoutSuccess(int type, String code, String msg) {
                Log.d("logout, type=" + type + " code=" + code + " msg=" + msg);
                if (listener != null)
                    listener.onSuccess();
                if (type == OnLogoutListener.TYPE_NORMAL_LOGOUT) {//正常退出成功

                } else if (type == OnLogoutListener.TYPE_SWITCH_ACCOUNT) {//切换账号退出成功
                    //游戏此时可跳转到登陆页面，让用户进行切换账号
                    sdkManager.showLogin(true);

                } else if (type == OnLogoutListener.TYPE_TOKEN_INVALID) {//登陆过期退出成功
                    //游戏此时可跳转到登陆页面，让用户进行重新登陆
                    sdkManager.showLogin(true);
                }
            }

            @Override
            public void logoutError(int type, String code, String msg) {
                if (listener != null)
                    listener.onFailed(getErrorMsg(code, msg));
            }
        });
        sdkManager.logout();
    }

    @Override
    public void showWinFloat(Activity activity) {
        sdkManager.showFloatView();
    }

    @Override
    public void hideWinFloat(Activity activity) {
        sdkManager.removeFloatView();
    }

    @Override
    public void exit(Activity activity, GameRole role, Callback.OnExitListener listener) {
        if (listener != null)
            listener.onFinished(true, "");
    }

    @Override
    public void pay(Activity activity, GameRole role, final GamePay pay, Map<String, Object> result, final Callback.OnPayRequestListener listener) {
        final String orderId = result.get(HttpConstants.RESPONSE_ORDER_ID).toString();
        final CustomPayParam param = getCustomPayParam(orderId, role, pay);
        sdkManager.showPay(param, new OnPaymentListener() {
            @Override
            public void paymentSuccess(PaymentCallbackInfo paymentCallbackInfo) {
                if (paymentCallbackInfo != null && paymentCallbackInfo.money == param.getProduct_price()) {
                    if (listener != null)
                        listener.onSuccess(orderId);
                } else {
                    if (listener != null)
                        listener.onFailed(paymentCallbackInfo.msg);
                }
            }

            @Override
            public void paymentError(PaymentErrorMsg paymentErrorMsg) {
                if (listener != null)
                    listener.onFailed(getErrorMsg(String.valueOf(paymentErrorMsg.code), paymentErrorMsg.msg));
            }
        });
    }

    private CustomPayParam getCustomPayParam(String orderId, GameRole role, GamePay pay) {
        CustomPayParam param = new CustomPayParam();
        param.setCp_order_id(orderId);
        param.setProduct_count(pay.getGoodsCount());
        param.setProduct_price(Float.valueOf(pay.getAmount() / 100));
        param.setProduct_id(pay.getGoodsId());
        param.setProduct_desc(pay.getGoodsName());
        param.setExchange_rate(0);
        param.setCurrency_name("");
        param.setExt(pay.getExtInfo());
        param.setRoleinfo(getRoleInfo(role, 0));
        return param;
    }

    @Override
    public void createRole(Activity activity, final GameRole role, long createTime, final Callback.OnGameRoleRequestListener listener) {
        selectRole(activity, false, role, createTime, listener);
    }

    @Override
    public void selectRole(Activity activity, boolean showFloat, final GameRole role, long createTime, final Callback.OnGameRoleRequestListener listener) {
        sdkManager.setRoleInfo(getRoleInfo(role, createTime), new SubmitRoleInfoCallBack() {
            @Override
            public void submitSuccess() {
                if (listener != null)
                    listener.onSuccess(role);
            }

            @Override
            public void submitFail(String msg) {
                if (listener != null)
                    listener.onFailed(msg);
            }
        });
    }

    private RoleInfo getRoleInfo(GameRole role, long createTime) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRole_id(role.getRoleId());
        roleInfo.setRole_level(Integer.valueOf(role.getRoleLevel()));
        roleInfo.setRole_vip(0);
        if (createTime != 0)
            roleInfo.setRolelevel_ctime(String.valueOf(createTime));
        return roleInfo;
    }

    @Override
    public void levelUpdate(Activity activity, GameRole role, long createTime, Callback.OnLevelUpListener listener) {

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
        sdkManager.recycle();
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
