package com.qinglan.sdk.android;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import com.qinglan.sdk.android.base.IPresenter;
import com.qinglan.sdk.android.channel.DefaultChannel;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.channel.IChannel;
import com.qinglan.sdk.android.channel.ChannelParamsReader;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
final class ChannelHandler implements ChannelParamsReader.OnReadEndListener {
    private static ChannelHandler channelHandler = null;
    private Context mContext;
    private Config mConfig;
    private IPresenter presenter;
    private IChannel channel;
    private int channelId;
    private static final String PLATFORM_PACKAGE_NAME = "com.qinglan.sdk.android.channel";
    private static final String PLATFORM_PREFIX = "qlsdk_";

    static ChannelHandler create(@NonNull Config config) {
        synchronized (ChannelHandler.class) {
            if (channelHandler == null) {
                channelHandler = new ChannelHandler(config);
            }
        }
        return channelHandler;
    }

    private ChannelHandler() {
    }

    private ChannelHandler(Config config) {
        mConfig = config;
        mContext = config.context;
        if (presenter == null) {
            AssetManager am = mContext.getAssets();
            try {
                String[] files = am.list("");
                for (String file : files) {
                    if (file.startsWith(PLATFORM_PREFIX)) {
                        channelId = Integer.valueOf(file.substring(file.indexOf(PLATFORM_PREFIX) + PLATFORM_PREFIX.length()));
                        break;
                    }
                }
                ChannelParamsReader reader = new ChannelParamsReader(am.open("channel_list.xml"));
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
    public void onEnd(Map<Integer, ChannelParamsReader.ChannelParam> p) {
        if (p != null && p.get(channelId) != null) {
            ChannelParamsReader.ChannelParam param = p.get(channelId);
            String clsName = String.format("%s.%s", PLATFORM_PACKAGE_NAME, param.getClazz());
            try {
                Class cls = Class.forName(clsName);
                channel = (IChannel) cls.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof ClassNotFoundException)
                    Log.e("未配置平台class参数");
                if (e instanceof IllegalAccessException
                        || e instanceof InstantiationException) {
                    Log.e("平台class参数有误");
                }
                channel = new DefaultChannel();
            } finally {
                channel.load(param, mConfig);
                Log.d("id===" + channel.getId() + ",name====" + channel.getName());
                presenter = new SDKPresenter(mContext, mConfig.getGameId(), channel);
            }
        }
    }
}
