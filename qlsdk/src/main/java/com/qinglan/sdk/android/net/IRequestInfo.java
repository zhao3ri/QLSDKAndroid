package com.qinglan.sdk.android.net;

import java.util.Map;

public interface IRequestInfo {
    String getUrl();

    int getMethod();

    Map<String, Object> getParams();
}
