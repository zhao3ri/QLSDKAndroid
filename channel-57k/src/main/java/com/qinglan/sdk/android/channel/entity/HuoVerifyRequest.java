package com.qinglan.sdk.android.channel.entity;

import com.qinglan.sdk.android.net.AbsRequestInfo;

import java.util.Map;

public class HuoVerifyRequest extends AbsRequestInfo {
    private static final String REQUEST_PARAM_APP_ID = "appId";
    private static final String REQUEST_PARAM_MEM_ID = "memId";
    private static final String REQUEST_PARAM_USER_TOKEN = "userToken";

    public String appId;
    public String memId;
    public String userToken;

    @Override
    public String getPath() {
        return "channel/57k/session";
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put(REQUEST_PARAM_APP_ID, appId);
        params.put(REQUEST_PARAM_MEM_ID, memId);
        params.put(REQUEST_PARAM_USER_TOKEN, userToken);
        return params;
    }
}
