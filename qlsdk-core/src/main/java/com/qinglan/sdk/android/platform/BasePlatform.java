package com.qinglan.sdk.android.platform;

import com.qinglan.sdk.android.Config;

public abstract class BasePlatform implements IPlatform {
    private int platformId;
    private String platformName;
    protected Config gameConfig;

    @Override
    public void load(PlatformParamsReader.PlatformParam p, Config config) {
        platformId = p.id;
        platformName = p.name;
        gameConfig = config;
    }

    @Override
    public int getId() {
        return platformId;
    }

    @Override
    public String getName() {
        return platformName;
    }
}
