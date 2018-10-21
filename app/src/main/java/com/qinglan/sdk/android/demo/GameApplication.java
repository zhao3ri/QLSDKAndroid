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
                .setPlatformClass(YXFPlatform.class)//必填，当前平台的class
                .setGameId(GAME_ID)//可选，SDK服务端给游戏分配的游戏id，也可在manifest中配置
                                    // 选择其中一种配置方式即可，若两者都配置了则以代码配置优先
                .setDebug(true));//可选，设置为true时开启log，便于调试
    }
}
