package com.qinglan.sdk.android.channel.entity;

import com.qinglan.sdk.android.BuildConfig;
import com.qinglan.sdk.android.net.AbsRequestInfo;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.HttpMethod;
import com.qinglan.sdk.android.net.IRequestInfo;

import java.util.HashMap;
import java.util.Map;

public class UCSessionRequest extends AbsRequestInfo {
    private static final String REQUEST_PARAM_SID = "sid";
    private static final String REQUEST_PARAM_APP_ID = "appID";

    public long gameId;
    public int platformId;
    public String sid;
    public String appID;

    @Override
    public String getPath() {
        return "channel/ucgame/session";
    }

    @Override
    public int getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, Object> getParams() {
        Map params = super.getParams();
        params.put(REQUEST_PARAM_SID, sid);
        params.put(REQUEST_PARAM_APP_ID, appID);
        return params;
    }
}
