package com.qinglan.sdk.android;

import android.content.Context;
import android.support.annotation.NonNull;

import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.platform.DefaultPlatform;

/**
 * Created by zhaoj on 2018/9/20
 * SDK初始化需要配置的参数
 *
 * @author zhaoj
 */
public final class Config {
    private String gameId;
    private String appKey;
    private String privateKey;
    protected Context context;

    public Config(@NonNull Context ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        context = ctx;
    }

    public static Config createDefaultConfig(@NonNull Context ctx) {
        return new Config(ctx).setDebug(false);
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

    String getGameId() {
        return gameId;
    }

    public Config setAppKey(String key) {
        appKey = key;
        return this;
    }

    public Config setPrivateKey(String key) {
        privateKey = key;
        return this;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public Context getContext() {
        return context;
    }
}
