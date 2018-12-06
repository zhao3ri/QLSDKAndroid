package com.qinglan.sdk.android;

import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;

import java.util.Map;

/**
 * Created by tyland on 2018/10/13.
 */
public class Callback {
    public interface OnResultListener {
        void onCompleted(boolean success, String result);
    }

    public interface OnInitCompletedListener extends OnResultListener {
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
        void onRefreshed(boolean success, long loginTimestamp, long createTimestamp, String result);
    }

    public interface HeartBeanRequestListener {
        void onResponse(boolean success, String result);
    }

    public interface OnLogoutResponseListener {
        void onSuccess();

        void onFailed(String error);
    }

    public interface OnExitListener extends OnResultListener {
    }

    public interface OnPayRequestListener {
        void onSuccess(String orderId);

        void onFailed(String msg);
    }

    public interface GenerateOrderListener {
        //        void onSuccess(String orderId, String notifyUrl);
        void onSuccess(Map<String, Object> result);

        void onFailed(String msg);
    }


    public interface OnInitConnectedListener {
        void initSuccess(UserInfo user);

        void initFailed(String msg);
    }

    public interface OnLoginListener {
        void loginSuccess(UserInfo userInfo);

        void loginFailed(String msg);
    }

    public interface OnGameRoleRequestListener {
        void onSuccess(GameRole role);

        void onFailed(String msg);
    }

    public interface OnLevelUpListener extends OnResultListener {
    }

}
