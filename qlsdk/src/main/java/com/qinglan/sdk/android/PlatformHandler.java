package com.qinglan.sdk.android;

import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.platform.DefaultPlatform;
import com.qinglan.sdk.android.platform.IPlatform;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
class PlatformHandler {
    private static PlatformHandler platformHandler = null;
    private IPlatform platform;

    public static PlatformHandler create(Config config) {
        if (platformHandler == null) {
            platformHandler = new PlatformHandler(config);
        }
        return platformHandler;
    }

    private PlatformHandler() {
    }

    private PlatformHandler(Config config) {
        if (platform == null) {
            Class cls = config.platformClass;
            if (cls == null) {
                throw new IllegalArgumentException("未配置平台class参数:" + config.platformName);
            }
            try {
                platform = (IPlatform) cls.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("create " + config.platformName + " failed! new instance default platform.");
                platform = new DefaultPlatform();
            } finally {
                platform.setGameConfig(config);
            }
        }
    }

    public IPlatform getPlatform() {
        return platform;
    }
}