package com.qinglan.sdk;

import com.qinglan.sdk.platform.DefaultPlatform;
import com.qinglan.sdk.platform.IPlatform;
import com.qinglan.sdk.common.Log;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
public class PlatformHandler {
    private IPlatform platform;

    public PlatformHandler(Config config) {
        if (platform == null) {
            try {
                Class cls = null;
                switch (config.getPlatformInfo()) {
                    case DEFAULT:
                    default:
                        cls = Class.forName("com.qinglan.sdk.platform.DefaultPlatform");
                        break;
                }
                platform = (IPlatform) cls.newInstance();
            } catch (Exception e) {
                Log.d("未配置平台参数:" + config.getPlatformInfo());
                platform = new DefaultPlatform();
            } finally {
                platform.init();
            }
        }
    }

    public IPlatform getPlatform() {
        return platform;
    }
}
