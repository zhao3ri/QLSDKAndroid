package com.qinglan.sdk.android.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;

/**
 * Created by zhaoj on 2018/10/17.
 * 业务逻辑相关接口
 */
public interface IPresenter {
    /**
     * 初始化
     */
    void init(@NonNull Activity activity, Callback.OnInitCompletedListener listener);

    /**
     * 登录
     */
    void login(@NonNull Activity activity, Callback.OnLoginResponseListener listener);

    /**
     * 开始游戏
     */
    void enterGame(@NonNull Activity activity, boolean showFloat, @NonNull GameRole game, Callback.OnGameStartedListener listener);

    /**
     * 创建角色
     */
    void createRole(@NonNull Activity activity, @NonNull GameRole role, Callback.OnCreateRoleListener listener);

    /**
     * 注销
     */
    void logout(@NonNull Activity activity, Callback.OnLogoutResponseListener listener);

    /**
     * 退出
     */
    void exitGame(@NonNull Activity activity, Callback.OnExitListener listener);

    /**
     * 退出游戏，可自定义退出的提示
     */
    void exitGameWithTips(@NonNull Activity activity, Callback.OnExitListener listener
            , String title, String msg, String negativeButtonText, String positiveButtonText);

    /**
     * 支付
     */
    void doPay(@NonNull Activity activity, GamePay pay, Callback.OnPayRequestListener listener);

    /**
     * 升级
     */
    void levelUpdate(Activity activity, GameRole role);

    /**
     * 获取当前游戏id
     */
    String getGameId();

    /**
     * 获取平台id
     */
    int getPlatformId();

    /**
     * 获取平台名称
     */
    String getPlatformName();

    /**
     * 显示浮窗
     */
    void showFloat(@NonNull Activity activity);

    /**
     * 隐藏浮窗
     */
    void hideFloat(@NonNull Activity activity);

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

    void saveUserInfo(UserInfo user);

    UserInfo getUserInfo();

    String getUid();

    void setSession(String session);

    void clear();

    String getDeviceId();

}
