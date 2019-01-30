package com.qinglan.sdk.android.channel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.gionee.gamesdk.floatwindow.AccountInfo;
import com.gionee.gamesdk.floatwindow.GameOrder;
import com.gionee.gamesdk.floatwindow.GamePayCallBack;
import com.gionee.gamesdk.floatwindow.GamePayManager;
import com.gionee.gamesdk.floatwindow.GamePlatform;
import com.gionee.gamesdk.floatwindow.QuitGameCallback;
import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.HttpConstants;

import java.util.Map;

public class GioneeChannel extends BaseChannel {

    @Override
    public void load(ChannelParamsReader.ChannelParam p, Config config) {
        super.load(p, config);
        GamePlatform.init((Application) config.getContext().getApplicationContext(), config.getAppKey());
    }

    @Override
    public void init(Activity activity, Callback.OnInitConnectedListener listener) {
        //直接回调成功
        if (listener != null) {
            listener.onSuccess(null);
        }
    }

    @Override
    public void login(Activity activity, final Callback.OnLoginListener listener) {
        GamePlatform.loginAccount(activity, false, new GamePlatform.LoginListener() {
            @Override
            public void onError(Object o) {
                if (listener != null) {
                    listener.onFailed(o.toString());
                }
            }

            @Override
            public void onSuccess(AccountInfo accountInfo) {
                if (listener != null) {
                    listener.onSuccess(getUser(accountInfo));
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private UserInfo getUser(AccountInfo accountInfo) {
        UserInfo user = new UserInfo();
        user.setId(accountInfo.mUserId);
        user.setUdToken(accountInfo.mToken);
        user.setUserName(accountInfo.mNickName);
        user.setSessionId(accountInfo.mPlayerId);
        return user;
    }

    @Override
    public void logout(Activity activity, GameRole role, final Callback.OnLogoutResponseListener listener) {
        GamePlatform.quitGame(activity, new QuitGameCallback() {
            @Override
            public void onQuit() {
                if (listener != null)
                    listener.onSuccess();
            }

            @Override
            public void onCancel() {
                if (listener != null)
                    listener.onFailed("");
            }
        });
    }

    @Override
    public void showWinFloat(Activity activity) {
        GamePlatform.requestFloatWindowsPermission(activity);
    }

    @Override
    public void hideWinFloat(Activity activity) {

    }

    @Override
    public void exit(Activity activity, GameRole role, final Callback.OnExitListener listener) {
        GamePlatform.quitGame(activity, new QuitGameCallback() {
            @Override
            public void onQuit() {
                if (listener != null)
                    listener.onFinished(true, "");
            }

            @Override
            public void onCancel() {
                if (listener != null)
                    listener.onFinished(false, "");
            }
        });
    }

    @Override
    public void pay(Activity activity, GameRole role, GamePay pay, Map<String, Object> result, final Callback.OnPayRequestListener listener) {
        final String orderId = result.get(HttpConstants.RESPONSE_ORDER_ID).toString();
        GameOrder gameOrder = new GameOrder();
        gameOrder.mOutOrderNo = orderId;
        gameOrder.mSubmitTime = Utils.toTimeString(System.currentTimeMillis());
        gameOrder.mSubject = pay.getGoodsName();
        gameOrder.mTotalFee = String.valueOf(pay.getAmount() / 100);
        gameOrder.mNotifyURL = pay.getNotifyUrl();
        gameOrder.mGamePayCallback = new GamePayCallBack() {
            @Override
            public void onCreateOrderSuccess(String s) {
                Log.d("order==" + s);
            }

            @Override
            public void onPaySuccess() {
                if (listener != null) {
                    listener.onSuccess(orderId);
                }
            }

            @Override
            public void onPayFail(Exception e) {
                if (listener != null) {
                    listener.onFailed(e.getMessage());
                }
            }
        };
        GamePayManager.getInstance().pay(activity, gameOrder, gameConfig.getPublicKey());
    }

    @Override
    public void createRole(Activity activity, GameRole role, long createTime, Callback.OnGameRoleRequestListener listener) {
        if (listener != null) {
            listener.onSuccess(role);
        }
    }

    @Override
    public void selectRole(Activity activity, GameRole role, long createTime, Callback.OnGameRoleRequestListener listener) {
        if (listener != null) {
            listener.onSuccess(role);
        }
    }

    @Override
    public void levelUpdate(Activity activity, GameRole role, long createTime, Callback.OnLevelUpListener listener) {
        if (listener != null) {
            listener.onFinished(true, "");
        }
    }

    @Override
    public boolean isCustomLogoutUI() {
        return true;
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        GamePlatform.onActivityResult(activity, requestCode, resultCode, data);
    }

}
