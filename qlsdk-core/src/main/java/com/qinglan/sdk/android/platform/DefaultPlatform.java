package com.qinglan.sdk.android.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;

/**
 * 默认平台（测试功能使用）
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
public class DefaultPlatform extends BasePlatform {
    private static final String TAG = "DefaultPlatform";
    public static final String DEFAULT_PLATFORM_NAME = "DEFAULT";
    public static final int DEFAULT_PLATFORM_ID = 41;

    @Override
    public int getId() {
        return DEFAULT_PLATFORM_ID;
    }

    @Override
    public String getName() {
        return DEFAULT_PLATFORM_NAME;
    }

    @Override
    public void init(Activity activity, Callback.OnInitConnectedListener listener) {
        Log.d(TAG, "platform init");
        listener.initSuccess(null);
    }

    @Override
    public void login(Activity activity, Callback.OnLoginListener listener) {
        Log.d(TAG, "platform login");
        listener.loginSuccess(null);
    }

    @Override
    public void logout(Activity activity, GameRole role, Callback.OnLogoutResponseListener listener) {
        Log.d(TAG, "platform logout");
        listener.onSuccess();
    }

    @Override
    public void showWinFloat(Activity activity) {
        Log.d(TAG, "showWinFloat");
    }

    @Override
    public void hideWinFloat(Activity activity) {
        Log.d(TAG, "hideWinFloat");
    }

    @Override
    public void exit(Activity activity, GameRole role, Callback.OnExitListener listener) {
        Log.d(TAG, "exit");
        listener.onCompleted(true, "");
    }

    @Override
    public void pay(Activity activity, GameRole role, GamePay pay, String orderId, String notifyUrl, Callback.OnPayRequestListener listener) {
        Log.d(TAG, "pay");
    }

    @Override
    public void createRole(Activity activity, GameRole role, Callback.OnGameRoleRequestListener listener) {
        Log.d(TAG, "createRole");
        listener.onSuccess(role);
    }

    @Override
    public void selectRole(Activity activity, boolean showFloat, GameRole role, Callback.OnGameRoleRequestListener listener) {
        Log.d(TAG, "selectRole");
        listener.onSuccess(role);
    }

    @Override
    public void levelUpdate(Activity activity, GameRole role, Callback.OnLevelUpListener listener) {
        Log.d(TAG, "levelUpdate");
        listener.onCompleted(true, "");
    }

    @Override
    public boolean isCustomLogoutUI() {
        return false;
    }

    @Override
    public void onCreate(Activity activity) {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onStart(Activity activity) {
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume(Activity activity) {
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause(Activity activity) {
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop(Activity activity) {
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy(Activity activity) {
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
    }

    @Override
    public void attachBaseContext(Context newBase) {
        Log.d(TAG, "attachBaseContext");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
    }


}
