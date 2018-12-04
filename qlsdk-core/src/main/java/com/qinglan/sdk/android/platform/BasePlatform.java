package com.qinglan.sdk.android.platform;

import android.text.TextUtils;

import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.common.ResContainer;
import com.qinglan.sdk.android.model.UserInfo;

public abstract class BasePlatform implements IPlatform {
    private int platformId;
    private String platformName;
    protected Config gameConfig;
    protected UserInfo user;
    private static final String RES_NAME_APP_ID = "qlsdk_third_party_appid";
    private static final String RES_NAME_APP_KEY = "qlsdk_third_party_appkey";
    private static final String RES_NAME_PRIVATE_KEY = "qlsdk_third_party_private";


    @Override
    public void load(PlatformParamsReader.PlatformParam p, Config config) {
        platformId = p.id;
        platformName = p.name;
        gameConfig = config;
        if (TextUtils.isEmpty(gameConfig.getAppID())) {
            gameConfig.setAppID(ResContainer.getString(config.getContext(), RES_NAME_APP_ID));
        }
        if (TextUtils.isEmpty(gameConfig.getAppKey())) {
            gameConfig.setAppKey(ResContainer.getString(config.getContext(), RES_NAME_APP_KEY));
        }
        if (TextUtils.isEmpty(gameConfig.getPrivateKey())) {
            gameConfig.setPrivateKey(ResContainer.getString(config.getContext(), RES_NAME_PRIVATE_KEY));
        }
    }

    @Override
    public int getId() {
        return platformId;
    }

    @Override
    public String getName() {
        return platformName;
    }

    @Override
    public void setUser(UserInfo info) {
        user = info;
    }
}
