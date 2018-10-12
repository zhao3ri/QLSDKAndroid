package com.qinglan.sdk.http;

import java.util.Map;

public interface IRequest {
    String getUrl();

    int getMethod();

    Map<String, Object> getParams();
}
