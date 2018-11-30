package com.qinglan.sdk.android.platform.entity;

import com.qinglan.sdk.android.BuildConfig;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.HttpMethod;
import com.qinglan.sdk.android.net.IRequestInfo;

import java.util.HashMap;
import java.util.Map;

public class UCSessionRequest implements IRequestInfo {
    private static final String REQUEST_PARAM_SID = "sid";
    private static final String REQUEST_PARAM_APP_ID = "appID";

    public long gameId;
    public int platformId;
    public String sid;
    public String appID;

    @Override
    public String getUrl() {
        return BuildConfig.DOMAIN_HOST + "ucgame/session";
    }

    @Override
    public int getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, Object> getParams() {
        Map params = new HashMap();
        params.put(HttpConstants.REQUEST_PARAM_GAME_ID, gameId);
        params.put(HttpConstants.REQUEST_PARAM_PLATFORM_ID, platformId);
        params.put(REQUEST_PARAM_SID, sid);
        params.put(REQUEST_PARAM_APP_ID, appID);
        return params;
    }
}
