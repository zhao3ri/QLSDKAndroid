package com.qinglan.sdk.android.net.impl;

import com.qinglan.sdk.android.net.AbsRequestInfo;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.Url;

import java.util.Map;

/**
 * 获取token
 */
public class TokenRequestInfo extends AbsRequestInfo {
    public String extend;

    @Override
    public String getPath() {
        return Url.TOKEN_URL;
    }

    @Override
    public Map<String, Object> getParams() {
        Map params = super.getParams();
        params.put(HttpConstants.REQUEST_PARAM_TOKEN_EXTEND, extend);
        return params;
    }
}
