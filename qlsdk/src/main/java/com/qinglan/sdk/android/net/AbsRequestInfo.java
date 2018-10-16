package com.qinglan.sdk.android.net;

import com.qinglan.sdk.android.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
public abstract class AbsRequestInfo implements IRequestInfo {
    public String appId;
    public int platformId;
    public static final String CLIENT_TYPE = "1";
    private static final int DEFAULT_METHOD = HttpMethod.POST;//默认为post方法

    public abstract String getPath();

    @Override
    public String getUrl() {
        return BuildConfig.DOMAIN_HOST + getPath();
    }

    @Override
    public int getMethod() {
        return DEFAULT_METHOD;
    }

    @Override
    public Map<String, Object> getParams() {
        Map params = new HashMap();
        params.put(HttpConstants.REQUEST_PARAM_APP_ID, appId);
        params.put(HttpConstants.REQUEST_PARAM_PLATFORM_ID, platformId);
        return params;
    }
}
