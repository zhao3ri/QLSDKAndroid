package com.qinglan.sdk.android.channel.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.qinglan.sdk.android.net.AbsRequestInfo;

import java.util.Map;

public class HanfengVerifyRequest extends AbsRequestInfo {
    private static final String REQUEST_PARAM_SID = "sid";
    private static final String REQUEST_PARAM_USER_ID = "userId";
    private static final String REQUEST_PARAM_APPID = "appId";
    private static final String REQUEST_PARAM_VERSION = "version";
    private static final String REQUEST_PARAM_CHANNEL = "channel";
    private static final String REQUEST_PARAM_EXT = "ext";

    @Expose
    public String sid;
    @Expose
    public String userId;
    @Expose
    @SerializedName(value = "gameId")
    public String appId;
    @Expose
    public String version;
    @Expose
    public String channel;
    @Expose
    public String ext;

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put(REQUEST_PARAM_SID, sid);
        params.put(REQUEST_PARAM_USER_ID, userId);
        params.put(REQUEST_PARAM_APPID, appId);
        params.put(REQUEST_PARAM_VERSION, version);
        params.put(REQUEST_PARAM_CHANNEL, channel);
        params.put(REQUEST_PARAM_EXT, ext);
        return params;
    }
}
