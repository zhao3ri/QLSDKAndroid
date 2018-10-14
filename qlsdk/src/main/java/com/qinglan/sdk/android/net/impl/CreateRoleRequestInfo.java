package com.qinglan.sdk.android.net.impl;

import com.qinglan.sdk.android.BuildConfig;
import com.qinglan.sdk.android.net.AbsRequestInfo;
import com.qinglan.sdk.android.net.RequestParamKey;
import com.qinglan.sdk.android.net.Url;

import java.util.Map;

/**
 * Created by tyland on 2018/10/13.
 */
public class CreateRoleRequestInfo extends AbsRequestInfo {
    public String uid;
    public String zoneId;
    public String zoneName;
    public String roleId;
    public String roleName;
    public String roleLevel;
    public String deviceId;

    @Override
    public String getPath() {
        return BuildConfig.DOMAIN_HOST + Url.ROLE_ESTABLISH_URL;
    }

    @Override
    public Map<String, Object> getParams() {
        Map paramMap = super.getParams();
        paramMap.put(RequestParamKey.REQUEST_PARAM_UID, uid);
        paramMap.put(RequestParamKey.REQUEST_PARAM_ZONE_ID, zoneId);
        paramMap.put(RequestParamKey.REQUEST_PARAM_ZONE_NAME, zoneName);
        paramMap.put(RequestParamKey.REQUEST_PARAM_ROLE_ID, roleId);
        paramMap.put(RequestParamKey.REQUEST_PARAM_ROLE_NAME, roleName);
        paramMap.put(RequestParamKey.REQUEST_PARAM_ROLE_LEV, roleLevel);
        paramMap.put(RequestParamKey.REQUEST_PARAM_DEVICE_ID, deviceId);
        paramMap.put(RequestParamKey.REQUEST_PARAM_CLIENT_TYPE, CLIENT_TYPE);
        return paramMap;
    }
}
