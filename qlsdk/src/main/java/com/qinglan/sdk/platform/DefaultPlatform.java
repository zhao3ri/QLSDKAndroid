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
public class DefaultPlatform extends AbsPlatform {
    @Override
    public void init() {

    }

    @Override
    public PlatformInfo getPlatformInfo() {
        return PlatformInfo.DEFAULT;
    }

    @Override
    public void login() {

    }

    @Override
    public void logout() {

    }

    @Override
    public void showFloat() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void pay() {

    }

    @Override
    public void createRole(GameRoleInfo role) {

    }

    @Override
    public void setRole(GameRoleInfo role) {

    }

    @Override
    public void onCreate(Activity activity) {

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
}
