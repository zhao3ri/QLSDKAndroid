package com.qinglan.sdk.platform;

import android.app.Activity;
import android.content.Intent;

import com.qinglan.sdk.PlatformInfo;
import com.qinglan.sdk.model.GameRoleInfo;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
public interface IPlatform {
    void init();

    PlatformInfo getPlatformInfo();

    void login();

    void logout();

    void showFloat();

    void exit();

    void pay();

    void createRole(GameRoleInfo role);

    void setRole(GameRoleInfo role);

    void onCreate(Activity activity);

    void onResume(Activity activity);

    void onPause(Activity activity);

    void onStop(Activity activity);

    void onDestroy(Activity activity);

    void onActivityResult(int requestCode, int resultCode, Intent data);

}
