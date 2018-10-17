package com.qinglan.sdk.android.platform;

import android.app.Activity;
import android.content.Intent;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.PlatformHandler;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.OnResponseListener;
import com.qinglan.sdk.android.net.impl.GameRoleRequestInfo;

/**
 * 默认平台（测试功能使用）
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
public class DefaultPlatform implements IPlatform  {
    public static final String DEFAULT_PLATFORM_NAME = "DEFAULT";
    public static final int DEFAULT_PLATFORM_ID = 0;
    public static final String DEFAULT_GAME_ID = "";

    @Override
    public void setHandler(PlatformHandler handler) {

    }

    @Override
    public int getId() {
        return DEFAULT_PLATFORM_ID;
    }

    @Override
    public String getName() {
        return DEFAULT_PLATFORM_NAME;
    }

    @Override
    public void init(Activity activity, OnInitConnectedListener listener) {

    }

    @Override
    public void login(Activity activity, OnLoginListener listener) {

    }

    @Override
    public void logout(Activity activity, GameRole role, Callback.OnLogoutResponseListener listener) {

    }

    @Override
    public void showWinFloat(Activity activity) {

    }

    @Override
    public void hideWinFloat(Activity activity) {

    }

    @Override
    public void exit(Activity activity, GameRole role, Callback.OnExitListener listener) {

    }

    @Override
    public void pay(Activity activity, GameRole role, GamePay pay, String orderId, String notifyUrl, Callback.OnPayRequestListener listener) {

    }

    @Override
    public void createRole(Activity activity, GameRole role, OnGameRoleRequestListener listener) {

    }

    @Override
    public void selectRole(Activity activity, boolean showFloat, GameRole role, OnGameRoleRequestListener listener) {

    }

    @Override
    public void levelUpdate(Activity activity, GameRole role, OnLevelUpListener listener) {

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void attachBaseContext(Activity activity) {

    }

    @Override
    public void onConfigurationChanged() {

    }


}
