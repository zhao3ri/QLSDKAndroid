package com.qinglan.sdk.android.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.ResContainer;
import com.qinglan.sdk.android.common.Utils;


public class SDKUtils {
    public static final String META_DATA_GAME_ID = "QLSDK_GAMEID";
    public static final String RES_NAME_GAME_ID = "qlsdk_gameid";

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

    public static String getGameId(Context context) {
        ApplicationInfo info = Utils.getApplicationInfo(context);
        String gameId = "";
        if (info == null) {
            return null;
        }
        int id = info.metaData.getInt(META_DATA_GAME_ID);
        Log.d("read:string id===" + id);
        if (id != 0) {
            gameId = context.getResources().getString(id);
            Log.d("read:gameId===" + gameId);
        } else {
            //当获取不到meta-data中的resId时，直接通过资源名称获取gameId
            gameId = ResContainer.getString(context, RES_NAME_GAME_ID);
            Log.d("read:gameId===" + gameId);
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
