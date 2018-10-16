package com.qinglan.sdk.android.platform;

import android.app.Activity;
import android.content.Intent;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.PlatformHandler;
import com.qinglan.sdk.android.model.GameRole;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
public interface IPlatform {

    void setHandler(PlatformHandler handler);

    /**
     * 获取平台id
     */
    int getId();

    /**
     * 获取平台名称
     */
    String getName();

    /***
     * 平台初始化
     */
    void init(Activity activity, Callback.OnInitCompletedListener listener);

    /**
     * 登录
     */
    void login(Activity activity, Callback.OnLoginResponseListener listener);

    /**
     * 注销
     */
    void logout(Activity activity);

    /**
     * 显示浮窗
     */
    void showFloat(Activity activity);

    /**
     * 隐藏浮窗
     */
    void hideFloat(Activity activity);

    /**
     * 退出
     */
    void exit(Activity activity);

    /**
     * 支付
     */
    void pay(Activity activity);

    /**
     * 创建角色
     */
    void createRole(Activity activity, GameRole role, Callback.OnCreateRoleFinishedListener listener);

    /**
     * 设置进入游戏的角色信息
     */
    void setRole(Activity activity, boolean showFloat, GameRole role, Callback.OnGameStartedListener listener);

    /**
     * 升级
     */
    void levelUpdate(Activity activity);

    /**
     * 是否定制退出UI
     */
    boolean isCustomLogoutUI();

    void onCreate(Activity activity);

    void onStart(Activity activity);

    void onResume(Activity activity);

    void onPause(Activity activity);

    void onStop(Activity activity);

    void onDestroy(Activity activity);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onNewIntent(Intent intent);

    void onBackPressed();

    void attachBaseContext(Activity activity);

    void onConfigurationChanged();
}
