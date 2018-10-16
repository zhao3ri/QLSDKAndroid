package com.qinglan.sdk.android;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.NonNull;

import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.platform.DefaultPlatform;
import com.qinglan.sdk.android.platform.IPlatform;
import com.qinglan.sdk.android.platform.UserPreferences;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
public final class PlatformHandler {
    private static PlatformHandler platformHandler = null;
    private IPlatform platform;
    private String gameId;
    private boolean isLogged = false;
    private boolean isHeartBeating = false;
    private long loginTime = 0;

    private static final long HEART_RATE = 1000 * 60 * 20;//20分钟更新一次心跳
    private static final String HEART_BEAT_ACTION = "com.qinglan.sdk.android.HEART_BEAT_ACTION";
    private AlarmManager heartBeatManager;
    private OnHeartBeatListener heartBeatListener;

    private BroadcastReceiver heartBeatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(HEART_BEAT_ACTION) && heartBeatListener != null) {
                heartBeatListener.onBeat();
            }
        }
    };

    static PlatformHandler create(@NonNull Config config) {
        if (platformHandler == null) {

            platformHandler = new PlatformHandler(config);
        }
        return platformHandler;
    }

    private PlatformHandler() {
    }

    private PlatformHandler(Config config) {
        gameId = config.getGameId();
        heartBeatManager = (AlarmManager) config.context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

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
                platform.setHandler(platformHandler);
            }
        }
    }

    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(HEART_BEAT_ACTION + getGameId());
        PendingIntent pi = PendingIntent
                .getBroadcast(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }

    public String getGameId() {
        return gameId;
    }

    public IPlatform getPlatform() {
        return platform;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public void setHeartBeating(boolean heartBeating) {
        isHeartBeating = heartBeating;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public boolean isHeartBeating() {
        return isHeartBeating;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public void saveUserInfo(Context context, UserInfo user) {
        UserPreferences.saveUserInfo(context, user);
    }

    public UserInfo getUserInfo(Context context) {
        UserInfo user = UserPreferences.getUserInfo(context);
        return user;
    }

    public void clear() {
        isLogged = false;
        isHeartBeating = false;
        loginTime = 0;
    }

    public void register(Activity activity) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HEART_BEAT_ACTION);
        activity.registerReceiver(heartBeatReceiver, intentFilter);
    }

    public void unregister(Activity activity) {
        activity.unregisterReceiver(heartBeatReceiver);
    }

    public void startBeat(Context context, OnHeartBeatListener listener) {
        heartBeatListener = listener;
        PendingIntent pi = getPendingIntent(context);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            heartBeatManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), HEART_RATE, pi);
        } else {
            heartBeatManager.setWindow(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), HEART_RATE, pi);
        }
    }

    public void cancel(Context context) {
        heartBeatManager.cancel(getPendingIntent(context));
    }

    public interface OnHeartBeatListener {
        void onBeat();
    }
}
