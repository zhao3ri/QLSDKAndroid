package com.qinglan.sdk.android.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.qinglan.sdk.android.common.Utils;


public class SDKUtils {
    public static final String META_DATA_APP_ID = "QLSDK_GAMEID";

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
        String gameId = "";
        if (info == null) {
            return null;
        }
        int id = info.metaData.getInt(META_DATA_APP_ID);
        if (id != 0) {
            gameId = context.getResources().getString(id);
            return gameId;
        }
        return gameId;
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
