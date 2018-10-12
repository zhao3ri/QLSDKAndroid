package com.qinglan.sdk.http;

import com.qinglan.sdk.BuildConfig;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
public abstract class AbsRequest implements IRequest {
    private static final int DEFAULT_METHOD = HttpMethod.POST;

    public abstract String getPath();

    @Override
    public String getUrl() {
        return BuildConfig.DOMAIN_HOST + getPath();
    }

    @Override
    public int getMethod() {
        return DEFAULT_METHOD;
    }
}
