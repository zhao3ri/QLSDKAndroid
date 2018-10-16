package com.qinglan.sdk.android.net.impl;

import com.qinglan.sdk.android.BuildConfig;
import com.qinglan.sdk.android.net.AbsRequestInfo;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.Url;

import java.util.Map;

/**
 * Created by tyland on 2018/10/14.
 * SDK提交登录成功的用户数据请求参数
 */
public class RefreshSessionRequestInfo extends AbsRequestInfo {
    public String uid;
    public String zoneId;
    public String zoneName;
    public String roleId;
    public String roleName;
    public String roleLevel;
    public String deviceId;

    @Override
    public String getPath() {
        return BuildConfig.DOMAIN_HOST + Url.LOGIN_URL;
    }

    @Override
    public Map<String, Object> getParams() {
        Map params = super.getParams();
        params.put(HttpConstants.REQUEST_PARAM_CLIENT_TYPE, CLIENT_TYPE);
        params.put(HttpConstants.REQUEST_PARAM_UID, uid);
        params.put(HttpConstants.REQUEST_PARAM_ZONE_ID, zoneId);
        params.put(HttpConstants.REQUEST_PARAM_ZONE_NAME, zoneName);
        params.put(HttpConstants.REQUEST_PARAM_ROLE_ID, roleId);
        params.put(HttpConstants.REQUEST_PARAM_ROLE_NAME, roleName);
        params.put(HttpConstants.REQUEST_PARAM_ROLE_LEV, roleLevel);
        params.put(HttpConstants.REQUEST_PARAM_DEVICE_ID, deviceId);
        return params;
    }
}
