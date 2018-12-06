package com.qinglan.sdk.android.net.impl;

import com.qinglan.sdk.android.net.AbsRequestInfo;
import com.qinglan.sdk.android.net.HttpConstants;

import java.util.Map;

public class QueryOrderRequestInfo extends AbsRequestInfo {
    public String orderId;

    @Override
    public String getPath() {
        return "account/order/query";
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put(HttpConstants.REQUEST_PARAM_ORDER_ID, orderId);
        return params;
    }
}
