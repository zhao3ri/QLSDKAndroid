package com.qinglan.sdk.android.demo;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.QLSDK;

public class GameApplication extends Application {

    private static final String GAME_ID = "181018126361";
//    private static final String APP_ID = "你的AppId";
//    private static final String PUBLIC_KEY = "你的pulicKey";
//    private static final String SECRET_KEY = "你的secretKey";
//    private static final String CP_ID = "你的CPId";

    @Override
    public void onCreate() {
        super.onCreate();
        QLSDK.create(new Config(this)
                .setGameId(GAME_ID)//可选，SDK服务端给游戏分配的游戏id，也可在manifest中配置
                // 选择其中一种配置方式即可，若两者都配置了则以代码配置优先
//                .setAppID(APP_ID)//第三方渠道平台的appID
//                .setPublicKey(PUBLIC_KEY)//第三方渠道平台的publicKey\
//                .setSecretKey(SECRET_KEY)//第三方渠道的secretKey（appSecret）
//                .setCpID(CP_ID)//某些渠道例如华为，分配的CPId
                .setScreenOrientation(Config.SCREEN_ORIENTATION_LANDSCAPE)//屏幕方向，SCREEN_ORIENTATION_LANDSCAPE为横屏
                //SCREEN_ORIENTATION_PORTRAIT为竖屏，默认为竖屏
                .setDebug(true));//可选，设置为true时开启log，便于调试
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
