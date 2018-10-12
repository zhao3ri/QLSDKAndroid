package com.qinglan.sdk.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Utils {

    public static ApplicationInfo getApplicationInfo(Context context) {
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String mapToJsonArrayString(Map<String, Object> map) {
        if (map.isEmpty())
            return null;
        JSONArray jsonArray = new JSONArray();
        for (String key : map.keySet()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("key", key);
                jsonObject.put("value", map.get(key) + "");
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    public static String mapToJson(Map<String, String> map) {
        JSONObject jsonObject = new JSONObject();
        for (String key : map.keySet()) {
            try {
                jsonObject.put(key, map.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    public static boolean isEmptyStr(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }
}
