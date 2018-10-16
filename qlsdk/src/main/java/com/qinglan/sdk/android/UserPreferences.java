package com.qinglan.sdk.android;

import android.content.Context;
import android.content.SharedPreferences;

import com.qinglan.sdk.android.common.CachePreferences;
import com.qinglan.sdk.android.model.UserInfo;

/**
 * Created by zhaoj on 2018/10/13.
 */
final class UserPreferences extends CachePreferences {
    private static final String USER_INFO = "user_info";
    public static final String KEY_UID = "uid";
    public static final String KEY_SESSION_ID = "sessionId";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_UD_TOKEN = "udToken";

    /**
     * 保存用户信息
     */
    public static void saveUserInfo(Context context, UserInfo userInfo) {
        if (userInfo != null) {
            SharedPreferences.Editor editor = getEditor(context, USER_INFO);
            editor.putString(KEY_UID, userInfo.getId());
            editor.putString(KEY_SESSION_ID, userInfo.getSessionId());
            editor.putString(KEY_USER_NAME, userInfo.getUserName());
            editor.putString(KEY_UD_TOKEN, userInfo.getUdToken());
            editor.commit();
        }
    }

    /**
     * 获取用户信息
     */
    public static UserInfo getUserInfo(Context context) {
        SharedPreferences sharedPreferences = getPreferences(context, USER_INFO);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(sharedPreferences.getString(KEY_UID, ""));
        userInfo.setSessionId(sharedPreferences.getString(KEY_SESSION_ID, ""));
        userInfo.setUserName(sharedPreferences.getString(KEY_USER_NAME, ""));
        userInfo.setUdToken(sharedPreferences.getString(KEY_UD_TOKEN, ""));
        return userInfo;
    }

    public static void put(Context context, String key, Object value) {
        put(context, USER_INFO, key, value);
    }

    public static <T extends Object> T get(Context context, String key, Object defValue) {
        return get(context, USER_INFO, key, defValue);
    }

    public static void clear(Context context) {
        clear(context, USER_INFO);
    }
}
