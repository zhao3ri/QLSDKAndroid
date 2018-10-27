package com.qinglan.sdk.android.platform;

public abstract class BasePlatform implements IPlatform {
    private int platformId;
    private String platformName;

    @Override
    public final void load(PlatformParamsReader.PlatformParam p) {
        platformId = p.id;
        platformName = p.name;
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
