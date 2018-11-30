package com.qinglan.sdk.android.demo;

import android.app.Application;

import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.QLSDK;

public class GameApplication extends Application {

    private static final String GAME_ID = "180928139602";
    private static final String APP_ID = "你的AppId";
    private static final String PRIVATE_KEY = "你的privateKey";

    @Override
    public void onCreate() {
        super.onCreate();
        QLSDK.create(new Config(this)
                .setGameId(GAME_ID)//可选，SDK服务端给游戏分配的游戏id，也可在manifest中配置
                                    // 选择其中一种配置方式即可，若两者都配置了则以代码配置优先
                .setAppID(APP_ID)//第三方渠道平台的appID
                .setPrivateKey(PRIVATE_KEY)//第三方渠道平台的privateKey
                .setScreenOrientation(Config.SCREEN_ORIENTATION_LANDSCAPE)//屏幕方向，SCREEN_ORIENTATION_LANDSCAPE为横屏
                                                                            //SCREEN_ORIENTATION_PORTRAIT为竖屏，默认为竖屏
                .setDebug(true));//可选，设置为true时开启log，便于调试
    }
}
