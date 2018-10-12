package com.qinglan.sdk;

import android.app.Activity;

/**
 * Created by zhaoj on 2018/9/19
 *
 * @author zhaoj
 */
public class QLSDK {
    private static QLSDK singleton = null;
    private PlatformHandler mHandler;

    private QLSDK(Config config) {
        mHandler = new PlatformHandler(config);
    }

    /**
     * 初始化SDK
     */
    public static QLSDK init(Config config) {
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
            throw new IllegalArgumentException("QLSDK must be init with configuration before using");
        }
        return singleton;
    }

    public void setDebug(boolean debug) {

    }

    /**
     * 显示登录页面
     */
    public void showLogin(Activity activity) {

    }

    /**
     * 提交角色信息
     */
    public void updateGameRole() {

    }

    /**
     * 创建角色
     */
    public void createGameRole() {

    }

    /**
     * 获取平台id
     */
    public int getPlatformId() {
        return mHandler.getPlatform().getPlatformInfo().getId();
    }

    /**
     * 获取平台名称
     */
    public String getPlatform() {
        return mHandler.getPlatform().getPlatformInfo().getName();
    }

    /**
     * 注销
     */
    public void logout() {

    }

    /**
     * 退出
     */
    public void exit() {

    }

    /**
     * 支付
     */
    public void doPay() {

    }

}
