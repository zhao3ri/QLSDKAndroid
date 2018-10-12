package com.qinglan.sdk.android;

import android.content.Context;

import com.qinglan.sdk.android.platform.DefaultPlatform;
import com.qinglan.sdk.android.platform.IPlatform;

/**
 * Created by zhaoj on 2018/9/20
 * SDK初始化需要配置的参数
 *
 * @author zhaoj
 */
public final class Config {
    protected Context mContext;
    protected int platformId;
    protected String platformName;
    protected Class platformClass;

    public Config(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        mContext = context;
    }

    public static Config createDefaultConfig(Context context) {
        return new Config(context).setPlatformName(DefaultPlatform.DEFAULT_PLATFORM_NAME)
                .setPlatformId(DefaultPlatform.DEFAULT_PLATFORM_ID)
                .setPlatformClass(DefaultPlatform.class);
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

}
