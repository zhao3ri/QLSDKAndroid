package com.qinglan.sdk.android.platform;

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
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;

public class GioneePlatform extends BasePlatform {

    @Override
    public void load(PlatformParamsReader.PlatformParam p, Config config) {
        super.load(p, config);
        GamePlatform.init((Application) config.getContext().getApplicationContext(), config.getAppKey());
    }

    @Override
    public void init(Activity activity, Callback.OnInitConnectedListener listener) {
        //直接回调成功
        if (listener != null) {
            listener.initSuccess(null);
        }
    }

    @Override
    public void login(Activity activity, final Callback.OnLoginListener listener) {
        GamePlatform.loginAccount(activity, false, new GamePlatform.LoginListener() {
            @Override
            public void onError(Object o) {
                if (listener != null) {
                    listener.loginFailed(o.toString());
                }
            }

            @Override
            public void onSuccess(AccountInfo accountInfo) {
                if (listener != null) {
                    listener.loginSuccess(getUser(accountInfo));
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
                    listener.onCompleted(true, "");
            }

            @Override
            public void onCancel() {
                if (listener != null)
                    listener.onCompleted(false, "");
            }
        });
    }

    @Override
    public void pay(Activity activity, GameRole role, GamePay pay, final String orderId, String notifyUrl, final Callback.OnPayRequestListener listener) {
        GameOrder gameOrder = new GameOrder();
        gameOrder.mOutOrderNo = pay.getCpOrderId();
        gameOrder.mSubmitTime = "";
        gameOrder.mSubject = pay.getProductName();
        gameOrder.mTotalFee = String.valueOf(pay.getMoney() / 100);
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
        GamePayManager.getInstance().pay(activity, gameOrder, gameConfig.getPrivateKey());
    }

    @Override
    public void createRole(Activity activity, GameRole role, Callback.OnGameRoleRequestListener listener) {
        if (listener != null) {
            listener.onSuccess(role);
        }
    }

    @Override
    public void selectRole(Activity activity, boolean showFloat, GameRole role, Callback.OnGameRoleRequestListener listener) {
        if (listener != null) {
            listener.onSuccess(role);
        }
    }

    @Override
    public void levelUpdate(Activity activity, GameRole role, Callback.OnLevelUpListener listener) {
        if (listener != null) {
            listener.onCompleted(true, "");
        }
    }

    @Override
    public boolean isCustomLogoutUI() {
        return true;
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
        GamePlatform.onActivityResult(activity, requestCode, resultCode, data);
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
