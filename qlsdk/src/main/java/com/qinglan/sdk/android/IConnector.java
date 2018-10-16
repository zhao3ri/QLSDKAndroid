package com.qinglan.sdk.android;

import android.app.Activity;

import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;

/**
 * Created by tyland on 2018/10/17.
 */
public interface IConnector {

    void initSdk(Activity activity, Callback.OnInitCompletedListener listener);

    void getToken(Activity activity);

    void login(Activity activity);

    void enterGame(Activity activity, GameRole game);

    void createRole(Activity activity, GameRole role);

    void logout(Activity activity, GameRole role);
}
