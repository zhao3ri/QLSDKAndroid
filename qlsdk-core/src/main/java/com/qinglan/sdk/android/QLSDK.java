package com.qinglan.sdk.android;

/**
 * **#*#*  *#                          **#*
 * #*      *#    #**#                 #*     *#
 * #*  @  @  *#         **#*#        *#      #*       *#**
 * #*     *#                *#**   ##     *#      #*     *#
 * **#*#*            * *       #***    #*     #*      #*
 * #*           *  @  *          #**     #*     #*
 * #*          * *       * *      #*#     *#
 * *#   #*              *  @  *      *# *
 * #*   *#                 * *          #*
 * #*     #*                              #*
 * *#      *#             #*      *#*      #*
 * #*        *#  *#  **       **    **    #*
 * #*                        **    **   #*
 * *#*                       *#*     #*
 * *#*                         *#*
 * *#*                   *#*
 * *#   #  *#   #*
 * <p>
 * 小猪佩奇保佑代码永无bug！！
 *
 * @author 三日
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.qinglan.sdk.android.base.IPresenter;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;

/**
 * Created by zhaoj on 2018/9/19
 *
 * @author zhaoj
 */
public class QLSDK {
    private static QLSDK singleton = null;
    private IPresenter mPresenter;

    private QLSDK(@NonNull Config config) {
        ChannelHandler mHandler = ChannelHandler.create(config);
        mPresenter = mHandler.getPresenter();
    }

    /**
     * 初始化SDK
     */
    public static QLSDK create(@NonNull Config config) {
        synchronized (QLSDK.class) {
            if (singleton == null) {
                singleton = new QLSDK(config);
            }
        }
        return singleton;
    }

    /**
     * 获取SDK实例
     */
    public static QLSDK getInstance() {
        if (singleton == null) {
            throw new IllegalArgumentException("QLSDK must be create with configuration before using");
        }
        return singleton;
    }

    /**
     * 设置游戏角色进入游戏
     */
    public void enterGame(Activity activity, boolean showFloat, GameRole game, Callback.OnGameStartedListener listener) {
        mPresenter.enterGame(activity, showFloat, game, listener);
    }

    /**
     * 平台初始化
     */
    public void initPlatform(Activity activity, Callback.OnInitCompletedListener listener) {
        mPresenter.init(activity, listener);
    }

    /**
     * 显示登录页面
     */
    public void login(Activity activity, Callback.OnLoginResponseListener listener) {
        mPresenter.login(activity, listener);
    }

    /**
     * 创建角色
     */
    public void createGameRole(Activity activity, GameRole role, Callback.OnCreateRoleListener listener) {
        mPresenter.createRole(activity, role, listener);
    }

    /**
     * 获取平台id
     */
    public int getChannelId() {
        return mPresenter.getChannelId();
    }

    /**
     * 获取平台名称
     */
    public String getChannelName() {
        return mPresenter.getChannelName();
    }

    /**
     * 注销
     */
    public void logout(Activity activity, Callback.OnLogoutResponseListener listener) {
        mPresenter.logout(activity, listener);
    }

    /**
     * 退出
     */
    public void exit(Activity activity, Callback.OnExitListener listener) {
        mPresenter.exitGame(activity, listener);
    }

    /**
     * 支付
     */
    public void doPay(Activity activity, GamePay pay, Callback.OnPayRequestListener listener) {
        mPresenter.doPay(activity, pay, listener);
    }

    /**
     * 升级
     */
    public void levelUpdate(Activity activity, GameRole role) {
        mPresenter.levelUpdate(activity, role);
    }

    /**
     * 显示浮窗
     */
    public void showWinFloat(Activity activity) {
        mPresenter.showFloat(activity);
    }

    /**
     * 隐藏浮窗
     */
    public void hideWinFloat(Activity activity) {
        mPresenter.hideFloat(activity);
    }

    public void onCreate(Activity activity) {
        mPresenter.onCreate(activity);
    }

    public void onStart(Activity activity) {
        mPresenter.onStart(activity);
    }

    public void onRestart(Activity activity) {
        mPresenter.onRestart(activity);
    }

    public void onResume(Activity activity) {
        mPresenter.onResume(activity);
    }

    public void onPause(Activity activity) {
        mPresenter.onPause(activity);
    }

    public void onStop(Activity activity) {
        mPresenter.onStop(activity);
    }

    public void onDestroy(Activity activity) {
        mPresenter.onDestroy(activity);
    }

    public void onNewIntent(Activity activity, Intent intent) {
        mPresenter.onNewIntent(activity, intent);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(activity, requestCode, resultCode, data);
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        mPresenter.onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
    }

    public void onWindowFocusChanged(Activity activity, boolean hasFocus) {
        mPresenter.onWindowFocusChanged(activity, hasFocus);
    }

    public void onApplicationAttachBaseContext(Context base) {
        mPresenter.onApplicationAttachBaseContext(base);
    }

    public void onApplicationConfiguration(Context base, Configuration newConfig) {
        mPresenter.onApplicationConfiguration(base, newConfig);
    }

    public void onApplicationTerminate(Context base) {
        mPresenter.onApplicationTerminate(base);
    }

    public void onBackPressed(Activity activity) {
        mPresenter.onBackPressed(activity);
    }

    public void attachBaseContext(Context newBase) {
        mPresenter.attachBaseContext(newBase);
    }

    public void onConfigurationChanged(Activity activity, Configuration newConfig) {
        mPresenter.onConfigurationChanged(activity, newConfig);
    }
}
