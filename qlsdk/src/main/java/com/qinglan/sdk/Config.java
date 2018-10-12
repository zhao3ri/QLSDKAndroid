package com.qinglan.sdk;

import android.content.Context;

/**
 * Created by zhaoj on 2018/9/20
 * SDK初始化需要配置的参数
 *
 * @author zhaoj
 */
public final class Config {
    private Context mContext;
    private PlatformInfo platformInfo;

    public Config(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        mContext = context;
    }

    public static Config createDefaultConfig(Context context) {
        return new Config(context).setPlatformId(PlatformInfo.DEFAULT.getId());
    }

    public Config setPlatformId(int id) {
        PlatformInfo p = PlatformInfo.getPlatformById(id);
        platformInfo = p == null ? PlatformInfo.DEFAULT : p;
        return this;
    }

    protected PlatformInfo getPlatformInfo() {
        return platformInfo;
    }

    public Context getContext() {
        return mContext;
    }
}
