package com.qinglan.sdk.android.platform.entity;

import com.qinglan.sdk.android.net.AbsRequestInfo;

import java.util.Map;

public class HMSPaySignRequest extends AbsRequestInfo {
    private static final String REQUEST_PARAM_CONTENT = "content";

    public String content;

    @Override
    public String getPath() {
        return "platform2/hms/pay/sign";
    }

    @Override
    public Map<String, Object> getParams() {
        Map params = super.getParams();
        params.put(REQUEST_PARAM_CONTENT, content);
        return params;
    }
}
