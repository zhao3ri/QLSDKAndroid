package com.qinglan.sdk.android.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;

import java.util.Map;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 * 平台功能接口
 */
public interface IPlatform {

    void load(PlatformParamsReader.PlatformParam p, Config config);

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
    void init(Activity activity, Callback.OnInitConnectedListener listener);

    /**
     * 登录
     */
    void login(Activity activity, Callback.OnLoginListener listener);

    /**
     * 注销
     */
    void logout(Activity activity, GameRole role, Callback.OnLogoutResponseListener listener);

    /**
     * 显示浮窗
     */
    void showWinFloat(Activity activity);

    /**
     * 隐藏浮窗
     */
    void hideWinFloat(Activity activity);

    /**
     * 退出
     */
    void exit(Activity activity, GameRole role, Callback.OnExitListener listener);

    /**
     * 支付
     */
    void pay(Activity activity, GameRole role, GamePay pay, Map<String, Object> result, Callback.OnPayRequestListener listener);

    /**
     * 创建角色
     */
    void createRole(Activity activity, GameRole role, long createTime, Callback.OnGameRoleRequestListener listener);

    /**
     * 设置角色
     */
    void selectRole(Activity activity, boolean showFloat, GameRole role, long createTime, Callback.OnGameRoleRequestListener listener);

    /**
     * 升级
     */
    void levelUpdate(Activity activity, GameRole role, long createTime, Callback.OnLevelUpListener listener);

    /**
     * 是否有自定义退出UI
     */
    boolean isCustomLogoutUI();

    void onCreate(Activity activity);

    void onStart(Activity activity);

    void onResume(Activity activity);

    void onPause(Activity activity);

    void onStop(Activity activity);

    void onDestroy(Activity activity);

    void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);

    void onNewIntent(Intent intent);

    void onBackPressed();

    void attachBaseContext(Context newBase);

    void onConfigurationChanged(Configuration newConfig);

    void setUser(UserInfo user);
}
