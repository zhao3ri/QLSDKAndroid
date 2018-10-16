package com.qinglan.sdk.android;

import android.content.Context;
import android.support.annotation.NonNull;

import com.qinglan.sdk.android.platform.DefaultPlatform;

/**
 * Created by zhaoj on 2018/9/20
 * SDK初始化需要配置的参数
 *
 * @author zhaoj
 */
public final class Config {
    private int platformId;
    protected String platformName;
    private String gameId;
    protected Context context;
    protected Class platformClass;

    public Config(@NonNull Context ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        context = ctx;
    }

    public static Config createDefaultConfig(Context ctx) {
        return new Config(ctx).setGameId(DefaultPlatform.DEFAULT_GAME_ID)
                .setPlatformName(DefaultPlatform.DEFAULT_PLATFORM_NAME)
                .setPlatformId(DefaultPlatform.DEFAULT_PLATFORM_ID)
                .setPlatformClass(DefaultPlatform.class);
    }

    public Config setGameId(String id) {
        gameId = id;
        return this;
    }

    public Config setPlatformId(int id) {
        platformId = id;
        return this;
    }

    public Config setPlatformName(String name) {
        platformName = name;
        return this;
    }

    public Config setPlatformClass(Class cls) {
        platformClass = cls;
        return this;
    }

//    public Context getContext() {
//        return context;
//    }

    public int getPlatformId() {
        return platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public Class getPlatformClass() {
        return platformClass;
    }

    public String getGameId() {
        return gameId;
    }
}
