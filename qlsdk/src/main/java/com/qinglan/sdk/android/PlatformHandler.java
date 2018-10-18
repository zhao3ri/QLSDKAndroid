package com.qinglan.sdk.android;

import android.content.Context;
import android.support.annotation.NonNull;

import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.platform.DefaultPlatform;
import com.qinglan.sdk.android.platform.IPlatform;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
final class PlatformHandler {
    private static PlatformHandler platformHandler = null;
    private Context mContext;
    private IPresenter presenter;
    private IPlatform platform;

    static PlatformHandler create(@NonNull Config config) {
        if (platformHandler == null) {
            platformHandler = new PlatformHandler(config);
        }
        return platformHandler;
    }

    private PlatformHandler() {
    }

    private PlatformHandler(Config config) {
        mContext = config.context;
        if (platform == null) {
            Class cls = config.platformClass;
            if (cls == null) {
                throw new IllegalArgumentException("未配置平台class参数");
            }
            try {
                platform = (IPlatform) cls.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("create platform failed! new instance default platform.");
                platform = new DefaultPlatform();
            } finally {
                presenter = new SDKPresenter(mContext, config.getGameId(), platform);
            }
        }
    }

    public IPresenter getPresenter() {
        return presenter;
    }

}
