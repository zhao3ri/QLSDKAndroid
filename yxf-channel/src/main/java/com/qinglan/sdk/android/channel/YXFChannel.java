package com.qinglan.sdk.android.channel;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.game.sdk.YXFSDKManager;
import com.game.sdk.domain.LoginErrorMsg;
import com.game.sdk.domain.LogincallBack;
import com.game.sdk.domain.OnLoginListener;
import com.game.sdk.domain.RoleInfo;
import com.game.sdk.domain.RolecallBack;
import com.game.sdk.domain.onRoleListener;
import com.game.sdk.util.Constants;
import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.HttpConstants;

import java.util.Map;

public class YXFChannel extends BaseChannel {

    @Override
    public void init(Activity activity, Callback.OnInitConnectedListener onInitConnectedListener) {
        Log.d(getName() + " ChannelParam init");
        //yxf平台不需要初始化，直接回调成功
        YXFSDKManager.getInstance(activity);
        if (onInitConnectedListener != null) {
            onInitConnectedListener.onSuccess(null);
        }
    }

    @Override
    public void login(Activity activity, final Callback.OnLoginListener onLoginListener) {
        Log.d(getName() + " ChannelParam login");
        YXFSDKManager.getInstance(activity).showLogin(activity, true, new OnLoginListener() {
            @Override
            public void loginSuccess(LogincallBack logincallBack) {
                Log.d(getName() + " loginSuccess");
                if (onLoginListener != null) {
                    UserInfo user = getUser(logincallBack);
                    onLoginListener.onSuccess(user);
                }
            }

            @Override
            public void loginError(LoginErrorMsg loginErrorMsg) {
                Log.d(getName() + " loginError");
                if (onLoginListener != null)
                    onLoginListener.onFailed(loginErrorMsg.msg);
            }
        });
    }

    private UserInfo getUser(LogincallBack logincallBack) {
        UserInfo user = new UserInfo();
        user.setId(logincallBack.userId);
        user.setUdToken(logincallBack.udtoken);
        user.setUserName(logincallBack.username);
        return user;
    }

    @Override
    public void logout(Activity activity, GameRole gameRole, Callback.OnLogoutResponseListener onLogoutResponseListener) {
        Log.d(getName() + " ChannelParam logout");
        YXFSDKManager.getInstance(activity).LoginOut(false);
        onLogoutResponseListener.onSuccess();
    }

    @Override
    public void showWinFloat(Activity activity) {
        YXFSDKManager.getInstance(activity).showFloatView();
    }

    @Override
    public void hideWinFloat(Activity activity) {
        YXFSDKManager.getInstance(activity).removeFloatView();
    }

    @Override
    public void exit(Activity activity, GameRole gameRole, Callback.OnExitListener onExitListener) {
        Log.d(getName() + " ChannelParam exit");
        YXFSDKManager.getInstance(activity).LoginOut(true);
        if (onExitListener != null)
            onExitListener.onFinished(true, "");
    }

    @Override
    public void pay(Activity activity, GameRole role, GamePay pay, Map<String, Object> result, Callback.OnPayRequestListener listener) {
        Log.d(getName() + " ChannelParam pay");
        String orderId = result.get(HttpConstants.RESPONSE_ORDER_ID).toString();
        YXFSDKManager.getInstance(activity)
                .showPay(activity, role.getRoleId()
                        , String.valueOf(Long.valueOf(pay.getAmount()) / 100), role.getServerId(), pay.getGoodsName(), pay.getGoodsId(), orderId);
    }

    @Override
    public void createRole(Activity activity, final GameRole gameRole, long createTime, final Callback.OnGameRoleRequestListener onGameRoleRequestListener) {
        Log.d(getName() + " ChannelParam createRole");
        RoleInfo roleInfo = getRoleInfo(gameRole);
        YXFSDKManager.getInstance(activity).getRoleInfo(activity, roleInfo, Constants.TYPE_CREATE_ROLE, new onRoleListener() {
            public void onSuccess(RolecallBack rolecallBack) {
                if (onGameRoleRequestListener != null)
                    onGameRoleRequestListener.onSuccess(gameRole);
            }

            public void onError(RolecallBack rolecallBack) {
                if (onGameRoleRequestListener != null)
                    onGameRoleRequestListener.onFailed(rolecallBack.msg);
            }
        });
    }

    @Override
    public void selectRole(Activity activity, boolean show, final GameRole gameRole, long createTime, final Callback.OnGameRoleRequestListener onGameRoleRequestListener) {
        RoleInfo roleInfo = getRoleInfo(gameRole);
        Log.d("setGameInfo RoleInfo : " + gameRole.getRoleLevel() + " " + gameRole.getRoleName() + " " + gameRole.getServerId());
        YXFSDKManager.getInstance(activity).getRoleInfo(activity, roleInfo, Constants.TYPE_ENTER_GAME, new onRoleListener() {
            public void onSuccess(RolecallBack rolecallBack) {
                Log.d(getName() + " createRole Success");
                if (onGameRoleRequestListener != null)
                    onGameRoleRequestListener.onSuccess(gameRole);
            }

            public void onError(RolecallBack rolecallBack) {
                Log.d(getName() + " createRole Failed");
                if (onGameRoleRequestListener != null)
                    onGameRoleRequestListener.onFailed(rolecallBack.msg);
            }
        });
    }

    private RoleInfo getRoleInfo(GameRole game) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleLevel(game.getRoleLevel());
        roleInfo.setRoleVIP("1");
        roleInfo.setRoleName(game.getRoleName());
        roleInfo.setServerID(game.getServerId());
        roleInfo.setRoleID(game.getRoleId());
        roleInfo.setServerName("1");
        return roleInfo;
    }

    @Override
    public void levelUpdate(Activity activity, GameRole gameRole, long createTime, Callback.OnLevelUpListener onLevelUpListener) {
        if (onLevelUpListener != null)
            onLevelUpListener.onFinished(true, "");
    }

    @Override
    public boolean isCustomLogoutUI() {
        return false;
    }

    @Override
    public void onCreate(Activity activity) {
        YXFSDKManager.getInstance(activity);
    }

    @Override
    public void onStart(Activity activity) {
    }

    @Override
    public void onResume(Activity activity) {
        if (activity.hasWindowFocus())
            YXFSDKManager.getInstance(activity).showFloatView();
    }

    @Override
    public void onPause(Activity activity) {
    }

    @Override
    public void onStop(Activity activity) {
        YXFSDKManager.getInstance(activity).removeFloatView();
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
