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
    protected Context context;
    protected Class platformClass;

    public Config(@NonNull Context ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        context = ctx;
    }

//    public static Config createDefaultConfig(@NonNull Context ctx) {
//        return new Config(ctx).setGameId(DefaultPlatform.DEFAULT_GAME_ID)
//                .setPlatformClass(DefaultPlatform.class);
//    }

    public Config setGameId(String id) {
        gameId = id;
        return this;
    }

    public Config setPlatformClass(Class cls) {
        platformClass = cls;
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

    Class getPlatformClass() {
        return platformClass;
    }

    String getGameId() {
        return gameId;
    }
}
