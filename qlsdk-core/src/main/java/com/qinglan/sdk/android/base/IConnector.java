package com.qinglan.sdk.android.base;

import android.content.Context;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;

/**
 * Created by zhaoj on 2018/10/17.
 * SDK相关网络请求接口
 */
public interface IConnector {

    /**
     * 初始化SDK
     */
    void initSdk(Context context, Callback.OnInitCompletedListener listener);

    /**
     * 获取token
     */
    void getToken(String uid, Callback.GetTokenListener listener);

    void refreshSession(Context context, GameRole role, Callback.OnRefreshSessionListener listener);

    void startHeartBeat(Context context, GameRole role, String time, Callback.HeartBeanRequestListener listener);

    void cleanSession(Context context, GameRole role, Callback.OnLogoutResponseListener listener);

    void exit(Context context, GameRole game, Callback.OnExitListener listener);

    void createRole(Context context, GameRole role, Callback.OnCreateRoleListener listener);

    void generateOrder(Context context, GameRole game, GamePay pay, int fixed, String loginTime, Callback.GenerateOrderListener listener);
}
