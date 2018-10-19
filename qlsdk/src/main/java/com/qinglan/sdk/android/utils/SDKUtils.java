package com.qinglan.sdk.android.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.qinglan.sdk.android.common.Utils;

import java.text.DecimalFormat;


public class SDKUtils {
    public static final String META_DATA_APP_ID = "QL_GAMEID";

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
        if (info == null) {
            return null;
        }
        Object value = info.metaData.get(META_DATA_APP_ID);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            DecimalFormat df = new DecimalFormat(	"000000000000");
            return df.format(value);
        }
        return value.toString();
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
