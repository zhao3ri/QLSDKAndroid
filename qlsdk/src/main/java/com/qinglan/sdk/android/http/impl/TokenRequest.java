package com.qinglan.sdk.android.http.impl;

import com.qinglan.sdk.android.http.AbsRequest;
import com.qinglan.sdk.android.http.RequestParamKey;
import com.qinglan.sdk.android.http.Url;

import java.util.HashMap;
import java.util.Map;

public class TokenRequest extends AbsRequest {
    public String appId;
    public String platformId;
    public String extend;

    @Override
    public String getPath() {
        return Url.TOKEN_URL;
    }

    @Override
    public Map<String, Object> getParams() {
        Map params = new HashMap();
        params.put(RequestParamKey.REQUEST_PARAM_TOKEN_APP_ID, appId);
        params.put(RequestParamKey.REQUEST_PARAM_TOKEN_PLATFORM_ID, platformId);
        params.put(RequestParamKey.REQUEST_PARAM_TOKEN_EXTEND, extend);
        return params;
    }
}
