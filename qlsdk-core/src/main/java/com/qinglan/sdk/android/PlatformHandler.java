package com.qinglan.sdk.android;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.platform.DefaultPlatform;
import com.qinglan.sdk.android.platform.IPlatform;
import com.qinglan.sdk.android.platform.PlatformParamsReader;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
final class PlatformHandler implements PlatformParamsReader.OnReadEndListener {
    private static PlatformHandler platformHandler = null;
    private Context mContext;
    private Config mConfig;
    private IPresenter presenter;
    private IPlatform platform;
    private int platformId;
    private static final String PLATFORM_PACKAGE_NAME = "com.qinglan.sdk.android.platform";
    private static final String PLATFORM_PREFIX = "qlsdk_";

    static PlatformHandler create(@NonNull Config config) {
        synchronized (PlatformHandler.class) {
            if (platformHandler == null) {
                platformHandler = new PlatformHandler(config);
            }
        }
        return platformHandler;
    }

    private PlatformHandler() {
    }

    private PlatformHandler(Config config) {
        mConfig = config;
        mContext = config.context;
        if (presenter == null) {
            AssetManager am = mContext.getAssets();
            try {
                String[] files = am.list("");
                for (String file : files) {
                    if (file.startsWith(PLATFORM_PREFIX)) {
                        platformId = Integer.valueOf(file.substring(file.indexOf(PLATFORM_PREFIX) + PLATFORM_PREFIX.length()));
                        break;
                    }
                }
                PlatformParamsReader reader = new PlatformParamsReader(am.open("platform_list.xml"));
                reader.setOnReadEndListener(this);
                reader.parser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public IPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onEnd(Map<Integer, PlatformParamsReader.PlatformParam> p) {
        if (p != null && p.get(platformId) != null) {
            PlatformParamsReader.PlatformParam param = p.get(platformId);
            String clsName = String.format("%s.%s", PLATFORM_PACKAGE_NAME, param.getClazz());
            try {
                Class cls = Class.forName(clsName);
                platform = (IPlatform) cls.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof ClassNotFoundException)
                    Log.e("未配置平台class参数");
                if (e instanceof IllegalAccessException
                        || e instanceof InstantiationException) {
                    Log.e("平台class参数有误");
                }
                platform = new DefaultPlatform();
            } finally {
                platform.load(param, mConfig);
                Log.d("id===" + platform.getId() + ",name====" + platform.getName());
                presenter = new SDKPresenter(mContext, mConfig.getGameId(), platform);
            }
        }
    }
}
