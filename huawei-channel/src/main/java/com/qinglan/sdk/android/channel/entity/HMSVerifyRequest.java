package com.qinglan.sdk.android.channel.entity;

import com.qinglan.sdk.android.net.AbsRequestInfo;

import java.util.Map;

public class HMSVerifyRequest extends AbsRequestInfo {
    private static final String REQUEST_PARAM_APPID = "appID";
    private static final String REQUEST_PARAM_CPID = "cpID";
    private static final String REQUEST_PARAM_TS = "ts";
    private static final String REQUEST_PARAM_PLAYER_ID = "playerId";
    private static final String REQUEST_PARAM_PLAYER_LEVEL = "playerLevel";
    private static final String REQUEST_PARAM_PLAYER_SIGN = "playerSSign";

    public String appID;
    public String cpID;
    public String ts;
    public String playerId;
    public String playerLevel;
    public String playerSSign;

    @Override
    public String getPath() {
        return "channel2/hms/session";
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put(REQUEST_PARAM_APPID, appID);
        params.put(REQUEST_PARAM_CPID, cpID);
        params.put(REQUEST_PARAM_TS, ts);
        params.put(REQUEST_PARAM_PLAYER_ID, playerId);
        params.put(REQUEST_PARAM_PLAYER_LEVEL, playerLevel);
        params.put(REQUEST_PARAM_PLAYER_SIGN, playerSSign);
        return params;
    }
}
