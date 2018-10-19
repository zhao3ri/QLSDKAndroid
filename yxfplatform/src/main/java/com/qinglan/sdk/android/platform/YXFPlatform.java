package com.qinglan.sdk.android.platform;


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

public class YXFPlatform implements IPlatform {
    private YXFSDKManager manager;

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public String getName() {
        return "YXF";
    }

    @Override
    public void init(Activity activity, Callback.OnInitConnectedListener onInitConnectedListener) {
        Log.d(getName() + " Platform init");
        //yxf平台不需要初始化，直接回调成功
        YXFSDKManager.getInstance(activity);
        if (onInitConnectedListener != null) {
            onInitConnectedListener.initSuccess(null);
        }
    }

    @Override
    public void login(Activity activity, final Callback.OnLoginListener onLoginListener) {
        Log.d(getName() + " Platform login");
        YXFSDKManager.getInstance(activity).showLogin(activity, true, new OnLoginListener() {
            @Override
            public void loginSuccess(LogincallBack logincallBack) {
                Log.d(getName() + " loginSuccess");
                if (onLoginListener != null) {
                    UserInfo user = getUser(logincallBack);
                    onLoginListener.loginSuccess(user);
                }
            }

            @Override
            public void loginError(LoginErrorMsg loginErrorMsg) {
                Log.d(getName() + " loginError");
                if (onLoginListener != null)
                    onLoginListener.initFailed(loginErrorMsg.msg);
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

    private RoleInfo getRoleInfo(GameRole game) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleLevel(game.getRoleLevel());
        roleInfo.setRoleName(game.getRoleName());
        roleInfo.setServerID(game.getServerId());
        roleInfo.setRoleID(game.getRoleId());
        return roleInfo;
    }

    @Override
    public void logout(Activity activity, GameRole gameRole, Callback.OnLogoutResponseListener onLogoutResponseListener) {
        Log.d(getName() + " Platform logout");
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
        Log.d(getName() + " Platform exit");
        YXFSDKManager.getInstance(activity).LoginOut(true);
        if (onExitListener != null)
            onExitListener.onCompleted(true, "");
    }

    @Override
    public void pay(Activity activity, GameRole gameRole, GamePay gamePay, String orderId, String notifyUrl, Callback.OnPayRequestListener onPayRequestListener) {
        Log.d(getName() + " Platform pay");
        YXFSDKManager.getInstance(activity)
                .showPay(activity, gameRole.getRoleId()
                        , String.valueOf(Integer.valueOf(gamePay.getMoney()) / 100), gameRole.getServerId(), gamePay.getProductName(), gamePay.getProductId(), orderId);
    }

    @Override
    public void createRole(Activity activity, final GameRole gameRole, final Callback.OnGameRoleRequestListener onGameRoleRequestListener) {
        Log.d(getName() + " Platform createRole");
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
    public void selectRole(Activity activity, boolean b, final GameRole gameRole, final Callback.OnGameRoleRequestListener onGameRoleRequestListener) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleName(gameRole.getRoleName());
        roleInfo.setRoleVIP("1");
        roleInfo.setRoleLevel(gameRole.getRoleLevel());
        roleInfo.setServerID(gameRole.getServerId());
        roleInfo.setServerName("1");
        roleInfo.setRoleID(gameRole.getRoleId());
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

    @Override
    public void levelUpdate(Activity activity, GameRole gameRole, Callback.OnLevelUpListener onLevelUpListener) {
        if (onLevelUpListener != null)
            onLevelUpListener.onCompleted(true, "");
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
    public void onActivityResult(int i, int i1, Intent intent) {

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
