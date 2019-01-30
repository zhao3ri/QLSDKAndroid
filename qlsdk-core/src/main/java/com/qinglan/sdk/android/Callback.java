package com.qinglan.sdk.android;

import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;

import java.util.Map;

/**
 * Created by tyland on 2018/10/13.
 */
public class Callback {
    public interface OnResultListener {
        void onFinished(boolean success, String result);
    }

    public interface DefaultResponseListener {
        void onFailed(String msg);
    }

    public interface OnInitCompletedListener extends OnResultListener {
    }

    public interface OnLoginResponseListener extends DefaultResponseListener {
        void onSuccess(UserInfo user);
    }

    public interface GetTokenListener extends OnResultListener {
    }

    public interface OnCreateRoleListener extends OnResultListener  {
    }

    public interface OnGameStartedListener extends DefaultResponseListener {
        void onSuccess(long timestamp);
    }

    public interface OnGameStartResponseListener {
        void onResult(boolean success, long loginTimestamp, long createTimestamp, String result);
    }

    public interface HeartBeanResponseListener {
        void onResponse(boolean success, String result);
    }

    public interface OnLogoutResponseListener extends DefaultResponseListener {
        void onSuccess();
    }

    public interface OnExitListener extends OnResultListener {
    }

    public interface OnPayRequestListener extends DefaultResponseListener {
        void onSuccess(String orderId);
    }

    public interface GenerateOrderListener extends DefaultResponseListener {
        //        void onSuccess(String orderId, String notifyUrl);
        void onSuccess(Map<String, Object> result);
    }


    public interface OnInitConnectedListener extends DefaultResponseListener {
        void onSuccess(UserInfo user);
    }

    public interface OnLoginListener extends DefaultResponseListener {
        void onSuccess(UserInfo userInfo);
    }

    public interface OnGameRoleRequestListener extends DefaultResponseListener {
        void onSuccess(GameRole role);
    }

    public interface OnLevelUpListener extends OnResultListener {
    }

}
