package com.qinglan.sdk.android;

import android.app.Activity;
import android.content.Intent;

import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.platform.IPlatform;

/**
 * Created by zhaoj on 2018/9/19
 *
 * @author zhaoj
 */
public class QLSDK {
    private static QLSDK singleton = null;
    private IPlatform mPlatform;

    private QLSDK(Config config) {
        PlatformHandler mHandler = PlatformHandler.create(config);
        mPlatform = mHandler.getPlatform();
    }

    /**
     * 初始化SDK
     */
    public static QLSDK create(Config config) {
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

    public void setDebug(boolean debug) {
        if (debug) {
            Log.setLevel(Log.VERBOSE);
        } else {
            Log.setLevel(Log.ERROR);
        }
    }

    /**
     * 设置游戏角色进入游戏
     */
    public void enterGame(Activity activity, boolean showFloat, GameRole game, Callback.OnGameStartedListener listener) {
        mPlatform.setRole(activity, showFloat, game, listener);
    }

    /**
     * 平台初始化
     */
    public void initPlatform(Activity activity, Callback.OnInitCompletedListener listener) {
        mPlatform.init(activity, listener);
    }

    /**
     * 显示登录页面
     */
    public void login(Activity activity, Callback.OnLoginResponseListener listener) {
        mPlatform.login(activity, listener);
    }

    /**
     * 创建角色
     */
    public void createGameRole(Activity activity, GameRole role, Callback.OnCreateRoleFinishedListener listener) {
        mPlatform.createRole(activity, role, listener);
    }

    /**
     * 获取平台id
     */
    public int getPlatformId() {
        return mPlatform.getId();
    }

    /**
     * 获取平台名称
     */
    public String getPlatformName() {
        return mPlatform.getName();
    }

    /**
     * 注销
     */
    public void logout(Activity activity, GameRole role, Callback.OnLogoutResponseListener listener) {
        mPlatform.logout(activity, role, listener);
    }

    /**
     * 退出
     */
    public void exit(Activity activity) {
        mPlatform.exit(activity);
    }

    /**
     * 支付
     */
    public void doPay(Activity activity) {
        mPlatform.pay(activity);
    }

    /**
     * 升级
     */
    public void levelUpdate(Activity activity) {
        mPlatform.levelUpdate(activity);
    }

    /**
     * 显示浮窗
     */
    public void showWinFloat(Activity activity) {
        mPlatform.showFloat(activity);
    }

    /**
     * 隐藏浮窗
     */
    public void hideWinFloat(Activity activity) {
        mPlatform.hideFloat(activity);
    }

    public void onCreate(Activity activity) {
        mPlatform.onCreate(activity);
    }

    public void onStart(Activity activity) {
        mPlatform.onStart(activity);
    }

    public void onResume(Activity activity) {
        mPlatform.onResume(activity);
    }

    public void onPause(Activity activity) {
        mPlatform.onPause(activity);
    }

    public void onStop(Activity activity) {
        mPlatform.onStop(activity);
    }

    public void onDestroy(Activity activity) {
        mPlatform.onDestroy(activity);
    }

    public void onNewIntent(Intent intent) {
        mPlatform.onNewIntent(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPlatform.onActivityResult(requestCode, resultCode, data);
    }

}
