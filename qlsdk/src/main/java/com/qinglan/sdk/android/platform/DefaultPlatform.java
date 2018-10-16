package com.qinglan.sdk.android.platform;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.OnResponseListener;
import com.qinglan.sdk.android.net.impl.GameRoleRequestInfo;

/**
 * 默认平台
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
public class DefaultPlatform extends AbsPlatform {
    public static final String DEFAULT_PLATFORM_NAME = "DEFAULT";
    public static final int DEFAULT_PLATFORM_ID = 0;
    public static final String DEFAULT_GAME_ID = "";

    @Override
    public int getId() {
        return DEFAULT_PLATFORM_ID;
    }

    @Override
    public String getName() {
        return DEFAULT_PLATFORM_NAME;
    }

    @Override
    public void login(Activity activity, Callback.OnLoginResponseListener listener) {
//        LoginRequestInfo request = new LoginRequestInfo();
//        request.appId = getAppId(activity);
//        request.platformId = getId();
//        request.uid = UserPreferences.get(activity, UserPreferences.KEY_UID, "");
//        request.zoneId = role.getZoneId();
//        request.zoneName = role.getZoneName();
//        request.roleId = role.getRoleId();
//        request.roleName = role.getRoleName();
//        request.roleLevel = role.getRoleLevel();
//        request.deviceId = Utils.getDeviceId(activity);
//        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
//            @Override
//            public void onResponse(boolean success, String result) {
//                if (listener != null) {
//                    listener.onFinished(success, result);
//                }
//            }
//        }).execute(request);
    }

    @Override
    public void logout(Activity activity, GameRole role, Callback.OnLogoutResponseListener listener) {

    }

    @Override
    public void showFloat(Activity activity) {

    }

    @Override
    public void hideFloat(Activity activity) {

    }

    @Override
    public void exit(Activity activity) {

    }

    @Override
    public void pay(Activity activity) {

    }

    @Override
    public void createRole(Activity activity, GameRole role, final Callback.OnCreateRoleFinishedListener listener) {
        if (role == null) {
            Log.e("role is null!!!!");
            if (listener != null) {
                listener.onFinished(false, "Create role failed! The role is null.");
            }
            return;
        }
        GameRoleRequestInfo request = new GameRoleRequestInfo();
        request.appId = getAppId(activity);
        request.platformId = getId();
        request.uid = handler.getUid();
        request.zoneId = role.getZoneId();
        request.zoneName = role.getZoneName();
        request.roleId = role.getRoleId();
        request.roleName = role.getRoleName();
        request.roleLevel = role.getRoleLevel();
        request.deviceId = Utils.getDeviceId(activity);
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                if (listener != null) {
                    listener.onFinished(success, result);
                }
            }
        }).execute(request);
    }

    @Override
    public void levelUpdate(Activity activity) {

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

    @Override
    public void selectRole(Activity activity, boolean showFloat, GameRole role, Callback.OnGameStartedListener listener) {

    }
}
