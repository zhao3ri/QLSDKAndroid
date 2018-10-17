package com.qinglan.sdk.android.net.impl;

import com.qinglan.sdk.android.BuildConfig;
import com.qinglan.sdk.android.net.AbsRequestInfo;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.Url;

import java.util.Map;

/**
 * Created by zhaoj on 2018/10/13.
 */
public class OrderRequestInfo extends AbsRequestInfo {
    public String uid;
    public String zoneId;
    public String roleId;
    public String roleName;
    public String cpOrderId;
    public String extInfo;
    public String amount;
    public String notifyUrl;
    public String fixed;
    public String loginTime;
    public String deviceId;
    public String gold;

    @Override
    public String getPath() {
        return BuildConfig.DOMAIN_HOST + Url.ROLE_ESTABLISH_URL;
    }

    @Override
    public Map<String, Object> getParams() {
        Map params = super.getParams();
        params.put(HttpConstants.REQUEST_PARAM_UID, uid);
        params.put(HttpConstants.REQUEST_PARAM_ZONE_ID, zoneId);
        params.put(HttpConstants.REQUEST_PARAM_ROLE_ID, roleId);
        params.put(HttpConstants.REQUEST_PARAM_ROLE_NAME, roleName);
        params.put(HttpConstants.REQUEST_PARAM_ORDER_ID, cpOrderId);
        params.put(HttpConstants.REQUEST_PARAM_EXT_INFO, extInfo);
        params.put(HttpConstants.REQUEST_PARAM_AMOUNT, amount);
        params.put(HttpConstants.REQUEST_PARAM_NOTIFY_URL, notifyUrl);
        params.put(HttpConstants.REQUEST_PARAM_FIXED, fixed);
        params.put(HttpConstants.REQUEST_PARAM_TIMESTAMP, loginTime);
        params.put(HttpConstants.REQUEST_PARAM_DEVICE_ID, deviceId);
        params.put(HttpConstants.REQUEST_PARAM_GOLD, gold);
        params.put(HttpConstants.REQUEST_PARAM_CLIENT_TYPE, CLIENT_TYPE);
        return params;
    }
}
