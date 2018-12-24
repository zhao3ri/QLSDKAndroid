package com.qinglan.sdk.android.channel.entity;

import com.qinglan.sdk.android.net.AbsRequestInfo;

import java.util.Map;

public class YeshenVerifyRequest extends AbsRequestInfo {
    private static final String REQUEST_PARAM_APP_ID = "appID";
    private static final String REQUEST_PARAM_TOKEN = "accessToken";
    private static final String REQUEST_PARAM_UID = "uid";

    public String appID;
    public String accessToken;
    public String uid;

    @Override
    public String getPath() {
        return "channel2/yeshen/session";
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put(REQUEST_PARAM_APP_ID, appID);
        params.put(REQUEST_PARAM_TOKEN, accessToken);
        params.put(REQUEST_PARAM_UID, uid);
        return params;
    }
}
