package com.qinglan.sdk.android.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.qinglan.sdk.android.common.Utils;


public class SDKUtils {

    public static String getMeteData(Context context, String tag) {
        ApplicationInfo info = Utils.getApplicationInfo(context);
        if (info != null) {
            String rex = info.metaData.getString(tag);
            if (rex == null) {
                return null;
            }
            if (rex.startsWith(tag + ":")) {
                return rex.split(":")[1];
            } else {
                return null;
            }
        }
        return null;
    }

    public static String getAppId(Context context) {
        ApplicationInfo info = Utils.getApplicationInfo(context);
        if (info != null) {
            info = Utils.getApplicationInfo(context);
            String appId = info.metaData.getString("YG_APPID");
            if (appId == null) {
                return null;
            }
            if (appId.startsWith("YG_APPID:")) {
                return appId.split(":")[1];
            } else {
                return null;
            }
        }
        return null;
    }

    public static String getMeteDataNoTag(Context context, String tag) {
        ApplicationInfo info = Utils.getApplicationInfo(context);
        if (info != null) {
            String rex = info.metaData.getString(tag);
            if (rex == null) {
                return null;
            }
            return rex;
        }
        return null;
    }
}
