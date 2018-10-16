package com.qinglan.sdk.android;

import android.app.Activity;

import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.impl.SDKConnector;
import com.qinglan.sdk.android.platform.IPlatform;

/**
 * Created by tyland on 2018/10/17.
 */
class SDKPresenter implements IPresenter {
    private IConnector iConnector;
    private IPlatform iPlatform;
    private String gameId;
    private boolean isLogged = false;
    private boolean isHeartBeating = false;
    private long loginTime = 0;

    public SDKPresenter(String id, IPlatform platform) {
        gameId = id;
        iPlatform = platform;
        iConnector = new SDKConnector(this);
    }

    @Override
    public void init(final Activity activity, final Callback.OnInitCompletedListener listener) {
        iPlatform.init(activity, new IPlatform.OnInitConnectedListener() {
            @Override
            public void initSuccess(UserInfo user) {
                if (user != null) {
                    saveUserInfo(user);
                }
                iConnector.initSdk(activity, listener);
            }

            @Override
            public void initFailed(String msg) {

            }
        });
    }

    @Override
    public void login(Activity activity, Callback.OnLoginResponseListener listener) {

    }

    @Override
    public void enterGame(Activity activity, boolean showFloat, GameRole game, Callback.OnGameStartedListener listener) {

    }

    @Override
    public void createRole(Activity activity, GameRole role, Callback.OnCreateRoleFinishedListener listener) {

    }

    @Override
    public void logout(Activity activity, GameRole role, Callback.OnLogoutResponseListener listener) {

    }

    @Override
    public void pay() {

    }

    @Override
    public void exit() {

    }

    @Override
    public String getGameId() {
        return null;
    }

    @Override
    public IPlatform getPlatform() {
        return null;
    }

    @Override
    public void setLogged(boolean logged) {

    }

    @Override
    public void setHeartBeating(boolean heartBeating) {

    }

    @Override
    public boolean isLogged() {
        return false;
    }

    @Override
    public boolean isHeartBeating() {
        return false;
    }

    @Override
    public long getLoginTime() {
        return 0;
    }

    @Override
    public void setLoginTime(long loginTime) {

    }

    @Override
    public void saveUserInfo(UserInfo user) {

    }

    @Override
    public UserInfo getUserInfo() {
        return null;
    }

    @Override
    public String getUid() {
        return null;
    }

    @Override
    public void setSession(String session) {

    }

    @Override
    public void clear() {

    }
}
