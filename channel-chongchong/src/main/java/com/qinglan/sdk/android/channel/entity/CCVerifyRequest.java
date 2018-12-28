package com.qinglan.sdk.android.channel.entity;

import com.qinglan.sdk.android.net.AbsRequestInfo;

import java.util.Map;

public class CCVerifyRequest extends AbsRequestInfo {
    private static final String REQUEST_PARAM_USER_ID = "userId";
    private static final String REQUEST_PARAM_TOKEN = "token";

    public String userId;
    public String token;

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put(REQUEST_PARAM_USER_ID, userId);
        params.put(REQUEST_PARAM_TOKEN, token);
        return params;
    }
}
