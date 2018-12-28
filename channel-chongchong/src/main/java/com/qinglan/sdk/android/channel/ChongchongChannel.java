package com.qinglan.sdk.android.channel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.lion.ccpay.bean.PlayUserInfo;
import com.lion.ccpay.sdk.CCPaySdk;
import com.lion.ccsdk.SdkExitAppListener;
import com.lion.ccsdk.SdkLoginListener;
import com.lion.ccsdk.SdkLogoutListener;
import com.lion.ccsdk.SdkPayListener;
import com.lion.ccsdk.SdkUser;
import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.channel.entity.CCVerifyRequest;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.OnResponseListener;

import java.util.Map;

public class ChongchongChannel extends BaseChannel {
    private static final String VERIFY_SUCCESS = "success";

    @Override
    public void load(ChannelParamsReader.ChannelParam p, Config config) {
        super.load(p, config);
        CCPaySdk.getInstance().onCreate((Application) config.getContext().getApplicationContext());
    }

    @Override
    public void init(Activity activity, Callback.OnInitConnectedListener listener) {
        CCPaySdk.getInstance().init(activity);
        if (listener != null)
            listener.onSuccess(null);
    }

    @Override
    public void login(Activity activity, final Callback.OnLoginListener listener) {
        CCPaySdk.getInstance().login(activity, true, new SdkLoginListener() {
            @Override
            public void onLoginSuccess(SdkUser sdkUser) {
                if (sdkUser == null) {
                    if (listener != null)
                        listener.onFailed("");
                    return;
                }
                verify(sdkUser, listener);
            }

            @Override
            public void onLoginCancel() {
                if (listener != null)
                    listener.onFailed("login cancel");
            }

            @Override
            public void onLoginFail(String message) {
                if (listener != null)
                    listener.onFailed(message);
            }
        });
    }

    private void verify(final SdkUser sdkUser, final Callback.OnLoginListener listener) {
        CCVerifyRequest request = new CCVerifyRequest();
        request.channelId = getId();
        request.gameId = gameConfig.getGameId();
        request.userId = sdkUser.uid;
        request.token = sdkUser.token;
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                if (success && !TextUtils.isEmpty(result) && result.equals(VERIFY_SUCCESS)) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserName(sdkUser.userName);
                    userInfo.setId(sdkUser.uid);
                    userInfo.setUdToken(sdkUser.token);
                    if (listener != null) {
                        listener.onSuccess(userInfo);
                    }
                } else {
                    if (listener != null) {
                        listener.onFailed(result);
                    }
                }
            }
        }).execute(request);
    }

    @Override
    public void logout(Activity activity, GameRole role, final Callback.OnLogoutResponseListener listener) {
        CCPaySdk.getInstance().setOnLoginOutListener(new SdkLogoutListener() {
            @Override
            public void onLoginOut() {
                if (listener != null)
                    listener.onSuccess();
            }
        });
        CCPaySdk.getInstance().logout(activity);
        CCPaySdk.getInstance().submitExtraData(getPlayUser(role, 0, PlayUserInfo.TYPE_EXIT_GAME));
    }

    @Override
    public void showWinFloat(Activity activity) {

    }

    @Override
    public void hideWinFloat(Activity activity) {

    }

    @Override
    public void exit(final Activity activity, GameRole role, final Callback.OnExitListener listener) {
        CCPaySdk.getInstance().exitApp(activity, true, new SdkExitAppListener() {
            @Override
            public void onExitApp() {
                CCPaySdk.getInstance().killApp(activity);
                if (listener != null)
                    listener.onFinished(true, "");
            }
        });
    }

    @Override
    public void pay(Activity activity, GameRole role, GamePay pay, Map<String, Object> result, final Callback.OnPayRequestListener listener) {
        final String orderId = result.get(HttpConstants.RESPONSE_ORDER_ID).toString();
        PlayUserInfo playUser = getPlayUser(role, 0, 0);
        CCPaySdk.getInstance().pay4OLGame(activity, orderId, pay.getGoodsId(), pay.getGoodsName(), String.valueOf(pay.getAmount() / 100), pay.getExtInfo(), playUser, new SdkPayListener() {
            @Override
            public void onPayResult(int status, String tn, String money) {
                switch (status) {
                    case SdkPayListener.CODE_FAIL://支付失败
                    case SdkPayListener.CODE_CANCEL://支付取消
                        if (listener != null)
                            listener.onFailed(getErrorMsg(status + "", tn));
                        break;
                    case SdkPayListener.CODE_SUCCESS://支付成功
                    case SdkPayListener.CODE_UNKNOWN://支付结果未知
                        //查询订单状态
                        queryOrderStatus(orderId, listener);
                        break;
                }
            }
        });
    }

    private PlayUserInfo getPlayUser(GameRole role, long createTime, int type) {
        PlayUserInfo playUser = new PlayUserInfo();
        if (type != 0) {
            playUser.setDataType(type);
        }
        playUser.setServerID(Integer.valueOf(role.getZoneId()));
        playUser.setServerName(role.getZoneName());
        if (PlayUserInfo.TYPE_SELECT_SERVER != type) {
            playUser.setRoleID(role.getRoleId());
            playUser.setRoleLevel(Integer.valueOf(role.getRoleLevel()));
            if (createTime != 0)
                playUser.setRoleCreateTime(createTime);
            playUser.setRoleName(role.getRoleId());
            playUser.setVip("1");
        }
        return playUser;
    }

    @Override
    public void createRole(Activity activity, GameRole role, long createTime, Callback.OnGameRoleRequestListener listener) {
        CCPaySdk.getInstance().submitExtraData(getPlayUser(role, createTime, PlayUserInfo.TYPE_CREATE_ROLE));
        if (listener != null)
            listener.onSuccess(role);
    }

    @Override
    public void selectRole(Activity activity, boolean showFloat, GameRole role, long createTime, Callback.OnGameRoleRequestListener listener) {
        CCPaySdk.getInstance().submitExtraData(getPlayUser(role, createTime, PlayUserInfo.TYPE_ENTER_GAME));
        if (listener != null)
            listener.onSuccess(role);
    }

    @Override
    public void levelUpdate(Activity activity, GameRole role, long createTime, Callback.OnLevelUpListener listener) {
        CCPaySdk.getInstance().submitExtraData(getPlayUser(role, createTime, PlayUserInfo.TYPE_LEVEL_UP));
        if (listener != null)
            listener.onFinished(true, "");
    }

    @Override
    public boolean isCustomLogoutUI() {
        return true;
    }

    @Override
    public void onApplicationAttachBaseContext(Application app, Context base) {
        CCPaySdk.getInstance().attachBaseContext(app, base);
    }

    @Override
    public void onApplicationConfiguration(Application app, Configuration newConfig) {
        CCPaySdk.getInstance().onConfigurationChanged(app, newConfig);
    }

    @Override
    public void onApplicationTerminate(Application app) {
        CCPaySdk.getInstance().onTerminate();
    }

    @Override
    public void onCreate(Activity activity) {
        CCPaySdk.getInstance().onCreate(activity);
    }

    @Override
    public void onResume(Activity activity) {
        CCPaySdk.getInstance().onResume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        CCPaySdk.getInstance().onPause(activity);
    }

    @Override
    public void onStop(Activity activity) {
        CCPaySdk.getInstance().onStop(activity);
    }

    @Override
    public void onDestroy(Activity activity) {
        CCPaySdk.getInstance().onDestroy(activity);
    }

    @Override
    public void onRestart(Activity activity) {
        CCPaySdk.getInstance().onRestart(activity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        CCPaySdk.getInstance().onActivityResult(activity, requestCode, resultCode, data);
    }
}
