package com.qinglan.sdk.android.net.impl;

import com.qinglan.sdk.android.BuildConfig;
import com.qinglan.sdk.android.net.AbsRequestInfo;
import com.qinglan.sdk.android.net.RequestParamKey;
import com.qinglan.sdk.android.net.Url;

import java.util.Map;

/**
 * Created by tyland on 2018/10/13.
 */
public class InitRequestInfo extends AbsRequestInfo {
    public String deviceId;
    public String manufacturer;
    public String model;
    public String systemVersion;
    public String os;
    public String latitude;
    public String longitude;
    public String imsi;
    public String location;
    public String networkCountryIso;
    public String networkType;
    public String phoneType;
    public String simOperatorName;
    public String resolution;

    @Override
    public String getPath() {
        return BuildConfig.DOMAIN_HOST + Url.INIT_URL;
    }

    @Override
    public Map<String, Object> getParams() {
        Map params = super.getParams();
        params.put(RequestParamKey.REQUEST_PARAM_CLIENT_TYPE, CLIENT_TYPE);
        params.put(RequestParamKey.REQUEST_PARAM_DEVICE_ID, deviceId);
        params.put(RequestParamKey.REQUEST_PARAM_IMSI, imsi);
        params.put(RequestParamKey.REQUEST_PARAM_LATITUDE, latitude);
        params.put(RequestParamKey.REQUEST_PARAM_LONGITUDE, longitude);
        params.put(RequestParamKey.REQUEST_PARAM_LOCATION, location);
        params.put(RequestParamKey.REQUEST_PARAM_MANUFACTURER, manufacturer);
        params.put(RequestParamKey.REQUEST_PARAM_MODEL, model);
        params.put(RequestParamKey.REQUEST_PARAM_NETWORK_COUNTRY_ISO, networkCountryIso);
        params.put(RequestParamKey.REQUEST_PARAM_NETWORK_TYPE, networkType);
        params.put(RequestParamKey.REQUEST_PARAM_PHONE_TYPE, phoneType);
        params.put(RequestParamKey.REQUEST_PARAM_PLATFORM, os);
        params.put(RequestParamKey.REQUEST_PARAM_RESOLUTION, resolution);
        params.put(RequestParamKey.REQUEST_PARAM_SIMOPERATOR_NAME, simOperatorName);
        params.put(RequestParamKey.REQUEST_PARAM_SYSTEM_VERSION, systemVersion);
        return super.getParams();
    }
}
