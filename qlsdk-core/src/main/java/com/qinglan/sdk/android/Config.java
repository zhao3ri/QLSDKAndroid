package com.qinglan.sdk.android;

import android.content.Context;
import android.support.annotation.NonNull;

import com.qinglan.sdk.android.common.Log;

/**
 * Created by zhaoj on 2018/9/20
 * SDK初始化需要配置的参数
 *
 * @author zhaoj
 */
public final class Config {
    private String gameId;
    private String appID;
    private String appKey;
    private String publicKey;
    private String secretKey;
    private String cpID;
    private String cpKey;
    private int screenOrientation;
    protected Context context;
    //屏幕方向
    public static final int SCREEN_ORIENTATION_PORTRAIT = 0;//竖屏
    public static final int SCREEN_ORIENTATION_LANDSCAPE = 1;//横屏

    public Config(@NonNull Context ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        context = ctx;
    }

    public static Config createDefaultConfig(@NonNull Context ctx) {
        return new Config(ctx).setDebug(false).setScreenOrientation(SCREEN_ORIENTATION_PORTRAIT);
    }

    public Config setGameId(String id) {
        gameId = id;
        return this;
    }

    public Config setDebug(boolean debug) {
        if (debug) {
            Log.setLevel(Log.VERBOSE);
        } else {
            Log.setLevel(Log.ERROR);
        }
        return this;
    }

    public String getGameId() {
        return gameId;
    }

    public Config setAppKey(String key) {
        appKey = key;
        return this;
    }

    public Config setAppID(String id) {
        appID = id;
        return this;
    }

    public Config setPublicKey(String key) {
        publicKey = key;
        return this;
    }

    public Config setCpID(String id) {
        cpID = id;
        return this;
    }

    public Config setCpKey(String key) {
        cpKey = key;
        return this;
    }

    public Config setSecretKey(String key) {
        secretKey = key;
        return this;
    }

    public Config setScreenOrientation(int orientation) {
        screenOrientation = orientation;
        return this;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getAppID() {
        return appID;
    }

    public String getCpID() {
        return cpID;
    }

    public String getCpKey() {
        return cpKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public Context getContext() {
        return context;
    }

    public int getScreenOrientation() {
        return screenOrientation;
    }
}
