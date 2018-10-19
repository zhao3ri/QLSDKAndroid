package com.qinglan.sdk.android.demo;

import android.app.Application;

import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.QLSDK;
import com.qinglan.sdk.android.platform.DefaultPlatform;
import com.qinglan.sdk.android.platform.YXFPlatform;

public class GameApplication extends Application {

    private static final String GAME_ID = "181018126361";

    @Override
    public void onCreate() {
        super.onCreate();
        QLSDK.create(new Config(this)
                .setGameId(GAME_ID)
                .setPlatformClass(YXFPlatform.class)).setDebug(true);
    }
}
