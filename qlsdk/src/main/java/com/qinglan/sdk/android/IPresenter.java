package com.qinglan.sdk.android;

import android.app.Activity;

import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.platform.IPlatform;

/**
 * Created by zhaoj on 2018/10/17.
 * 业务逻辑相关接口
 */
public interface IPresenter {
    void init(Activity activity, Callback.OnInitCompletedListener listener);

    void login(Activity activity, Callback.OnLoginResponseListener listener);

    void enterGame(Activity activity, boolean showFloat, GameRole game, Callback.OnGameStartedListener listener);

    void createRole(Activity activity, GameRole role, Callback.OnCreateRoleFinishedListener listener);

    void logout(Activity activity, GameRole role, Callback.OnLogoutResponseListener listener);

    void pay();

    void exit();

    String getGameId();

    IPlatform getPlatform();

    void setLogged(boolean logged);

    void setHeartBeating(boolean heartBeating);

    boolean isLogged();

    boolean isHeartBeating();

    long getLoginTime();

    void setLoginTime(long loginTime);

    void saveUserInfo(UserInfo user);

    UserInfo getUserInfo();

    String getUid();

    void setSession(String session);

    void clear();

}
