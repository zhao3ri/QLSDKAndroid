package com.qinglan.sdk.android.net.impl;

import com.qinglan.sdk.android.BuildConfig;
import com.qinglan.sdk.android.net.AbsRequestInfo;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.Url;

import java.util.Map;

/**
 * Created by tyland on 2018/10/17
 */
public class ExitRequestInfo extends AbsRequestInfo {
    public String uid;
    public String zoneId;
    public String roleId;
    public String deviceId;

    @Override
    public String getPath() {
        return BuildConfig.DOMAIN_HOST + Url.QUIT_URL;
    }

    @Override
    public Map<String, Object> getParams() {
        Map params = super.getParams();
        params.put(HttpConstants.REQUEST_PARAM_CLIENT_TYPE, ANDROID_CLIENT_TYPE);
        params.put(HttpConstants.REQUEST_PARAM_UID, uid);
        params.put(HttpConstants.REQUEST_PARAM_ZONE_ID, zoneId);
        params.put(HttpConstants.REQUEST_PARAM_ROLE_ID, roleId);
        params.put(HttpConstants.REQUEST_PARAM_DEVICE_ID, deviceId);
        return params;
    }
}
