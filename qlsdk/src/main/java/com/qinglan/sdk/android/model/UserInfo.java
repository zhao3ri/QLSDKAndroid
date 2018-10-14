package com.qinglan.sdk.android.model;

/**
 * Created by zhaoj on 2018/9/20
 * 用户信息
 *
 * @author zhaoj
 */
public final class UserInfo {
    private String id;
    private String sessionId;
    private String userName;
    private String udToken;
//    private String appId;
//    private String platformId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUdToken() {
        return udToken;
    }

    public void setUdToken(String udToken) {
        this.udToken = udToken;
    }

//    public String getAppId() {
//        return appId;
//    }
//
//    public void setAppId(String appId) {
//        this.appId = appId;
//    }
//
//    public String getPlatformId() {
//        return platformId;
//    }
//
//    public void setPlatformId(String platformId) {
//        this.platformId = platformId;
//    }
}
