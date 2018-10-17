package com.qinglan.sdk.android;

import com.qinglan.sdk.android.model.UserInfo;

/**
 * Created by tyland on 2018/10/13.
 */
public class Callback {

    public interface OnInitCompletedListener {
        void onCompleted(boolean success, String result);
    }

    public interface OnLoginResponseListener {
        void onSuccess(UserInfo user);

        void onFailed(String error);
    }

    public interface GetTokenListener {
        void onFinished(boolean success, String result);
    }

    public interface OnCreateRoleFinishedListener {
        void onFinished(boolean success, String result);
    }

    public interface OnGameStartedListener {
        void onGameStarted(long timestamp);

        void onFailed(String result);
    }

    public interface OnRefreshSessionListener {
        void onRefreshed(boolean success, String result);
    }

    public interface HeartBeanRequestListener {
        void onResponse(boolean success, String result);
    }

    public interface OnLogoutResponseListener {
        void onSuccess();

        void onFailed(String error);
    }

    public interface OnExitListener {
        void onCompleted(boolean success, String msg);
    }

    public interface OnPayRequestListener {
        void onSuccess(String orderId);

        void onFailed(String msg);
    }

    public interface GenerateOrderListener {
        void onSuccess(String orderId, String notifyUrl);

        void onFailed(String msg);
    }

}
