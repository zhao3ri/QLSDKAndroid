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

    public interface OnCreateRoleFinishedListener {
        void onFinished(boolean success, String result);
    }

    public interface OnGameStartedListener {
        void onGameStarted(long timestamp);

        void onFailed(String result);
    }

    public interface OnLogoutResponseListener{
        void onSuccess();

        void onFailed(String error);
    }
}
