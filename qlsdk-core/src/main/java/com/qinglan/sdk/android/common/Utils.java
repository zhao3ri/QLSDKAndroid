package com.qinglan.sdk.android.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;


import static android.Manifest.permission.READ_PHONE_STATE;
import static android.content.Context.TELEPHONY_SERVICE;
import static android.content.pm.PackageManager.FEATURE_TELEPHONY;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.provider.Settings.Secure.ANDROID_ID;
import static android.provider.Settings.Secure.getString;

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

    /**
     * 获取应用名称
     *
     * @return
     */
    public static String getAppName(Context context) {
        String appName = "";
        try {
            appName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.loadLabel(context.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    /**
     * 获取本地版本号
     *
     * @return
     */
    public static int getVersion(Context context) {
        int version = -1;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取本地版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /*
     * 获取程序图标
     */
    public static Drawable getAppIcon(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            String packName = context.getPackageName();
            ApplicationInfo info = pm.getApplicationInfo(packName, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得当前时区
     */
    public static String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getDisplayName(false, TimeZone.SHORT);
    }

    /**
     * 获得UTC时区
     */
    public static int getUTCTimeZone() {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int zone = zoneOffset / 60 / 60 / 1000;//时区，东时区数字为正，西时区为负
        return zone;
    }

    /**
     * 获得系统语言
     */
    public static String getLanguage() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "-" + locale.getCountry();
    }

    /**
     * 获取网络连接状态
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @SuppressLint("MissingPermission")
    public static String getIMSI(Context context) {
        if (hasPermission(context, READ_PHONE_STATE) && hasFeature(context, FEATURE_TELEPHONY)) {
            TelephonyManager telephonyManager = getSystemService(context, TELEPHONY_SERVICE);
            String imsi = telephonyManager.getSubscriberId();
            if (!TextUtils.isEmpty(imsi)) {
                return imsi;
            }
        }
        return "";
    }

    /**
     * 获取设备号
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        String androidId = getString(context.getContentResolver(), ANDROID_ID);
        if (!TextUtils.isEmpty(androidId) && !"9774d56d682e549c".equals(androidId) && !"unknown".equals(
                androidId) && !"000000000000000".equals(androidId)) {
            return androidId;
        }

        // Serial number, guaranteed to be on all non phones in 2.3+
        if (!TextUtils.isEmpty(Build.SERIAL)) {
            return Build.SERIAL;
        }

        // Telephony ID, guaranteed to be on all phones, requires READ_PHONE_STATE permission
        if (hasPermission(context, READ_PHONE_STATE) && hasFeature(context, FEATURE_TELEPHONY)) {
            TelephonyManager telephonyManager = getSystemService(context, TELEPHONY_SERVICE);
            String telephonyId = telephonyManager.getDeviceId();
            if (!TextUtils.isEmpty(telephonyId)) {
                return telephonyId;
            }
        }

        // If this still fails, generate random identifier that does not persist across installations
        return UUID.randomUUID().toString();
    }

    public static boolean hasPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == PERMISSION_GRANTED;
    }

    public static boolean hasFeature(Context context, String feature) {
        return context.getPackageManager().hasSystemFeature(feature);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSystemService(Context context, String serviceConstant) {
        return (T) context.getSystemService(serviceConstant);
    }


    private static final String NETWORK_CLASS_UNKNOWN = "UNKNOWN";
    private static final String NETWORK_CLASS_2G = "2G";
    private static final String NETWORK_CLASS_3G = "3G";
    private static final String NETWORK_CLASS_4G = "4G";
    private static final String NETWORK_CLASS_WIFI = "WIFI";

    /**
     * 判断网上 是 2G 3G WIFI
     *
     * @return
     */
    public static String getNetworkClass(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_CLASS_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return getNetworkType(context);
            }

        }
        return NETWORK_CLASS_UNKNOWN;
    }

    private static String getNetworkType(Context context) {
        int type = TelephonyManager.NETWORK_TYPE_UNKNOWN;
        if (hasPermission(context, READ_PHONE_STATE) && hasFeature(context, FEATURE_TELEPHONY)) {
            TelephonyManager telephonyManager = getSystemService(context, TELEPHONY_SERVICE);
            type = telephonyManager.getNetworkType();
        }
        String networkType = getNetworkTypeName(type);
        return networkType;
    }

    private static String getNetworkTypeName(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    /**
     * 查询手机的 MCC+MNC
     */
    public static String getSimCountryIso(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return tm.getSimCountryIso();
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获取sim运营商信息
     * 例如：46000中国移动 46001中国联通 46003中国电信
     */
    public static String getSimOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return tm.getSimOperatorName();
        } catch (Exception e) {

        }
        return null;
    }

    private static final String PHONE_TYPE_NONE = "NONE";
    private static final String PHONE_TYPE_GSM = "GSM";
    private static final String PHONE_TYPE_CDMA = "CDMA";
    private static final String PHONE_TYPE_SIP = "SIP";

    public static String getPhoneType(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int type = tm.getPhoneType();
        return getPhoneTypeName(type);
    }

    private static String getPhoneTypeName(int type) {
        switch (type) {
            case TelephonyManager.PHONE_TYPE_GSM:
                return PHONE_TYPE_GSM;
            case TelephonyManager.PHONE_TYPE_CDMA:
                return PHONE_TYPE_CDMA;
            case TelephonyManager.PHONE_TYPE_SIP:
                return PHONE_TYPE_SIP;
            default:
                return PHONE_TYPE_NONE;

        }
    }

    /**
     * 获取屏幕显示宽度.
     */
    public static int getDeviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕显示高度.
     */
    public static int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕高度.
     */
    public static int getScreenHeight(Context context) {
        int heightPixels;
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        heightPixels = metrics.heightPixels;
        // includes window decorations (statusbar bar/navigation bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                heightPixels = (Integer) Display.class
                        .getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
            // includes window decorations (statusbar bar/navigation bar)
        else if (Build.VERSION.SDK_INT >= 17)
            try {
                android.graphics.Point realSize = new android.graphics.Point();
                Display.class.getMethod("getRealSize",
                        android.graphics.Point.class).invoke(d, realSize);
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        return heightPixels;
    }

    /**
     * 获取屏幕宽度.
     */
    public static int getScreenWidth(Context context) {
        int widthPixels;
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        widthPixels = metrics.widthPixels;
        // includes window decorations (statusbar bar/navigation bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class
                        .getMethod("getRawWidth").invoke(d);
            } catch (Exception ignored) {
            }
            // includes window decorations (statusbar bar/navigation bar)
        else if (Build.VERSION.SDK_INT >= 17)
            try {
                android.graphics.Point realSize = new android.graphics.Point();
                Display.class.getMethod("getRealSize",
                        android.graphics.Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
            } catch (Exception ignored) {
            }
        return widthPixels;
    }

    /**
     * 获取屏幕像素密度.
     */
    public static float getDeviceDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dip2px(Context context, float dp) {
        final float scale = getDeviceDensity(context);
        return (int) (dp * scale + 0.5);
    }

    public static int px2dp(Context context, float px) {
        final float scale = getDeviceDensity(context);
        return (int) (px / scale + 0.5);
    }

    public static String getResolution(Context context) {
        return String.format("%sx%s", getDeviceHeight(context), getDeviceWidth(context));
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

    public static String object2Json(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    public static <T> T json2Object(String json, Class<T> cls) {
        Gson gson = new Gson();
        return gson.fromJson(json, cls);
    }

    public static <T> T json2Object(String json, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }


    public static boolean isEmptyStr(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static final String DEFAULT_FORMAT = "yyyyMMddHHmmss";

    public static Timestamp toTimestamp(String submitTime) {
        submitTime = new StringBuilder().append(
                submitTime.substring(0, 4) + "-" + submitTime.substring(4, 6) + "-" + submitTime.substring(6, 8)
                        + " " + submitTime.substring(8, 10) + ":" + submitTime.substring(10, 12) + ":"
                        + submitTime.substring(12, 14)).toString();
        return Timestamp.valueOf(submitTime);
    }

    public static String toTimeString(Date date) {
        return toTimeString(date, DEFAULT_FORMAT);
    }

    public static String toTimeString(long currentTimestamp) {
        return toTimeString(new Date(currentTimestamp), DEFAULT_FORMAT);
    }

    public static String toTimeString(Date date, String format) {
        if (date == null || format.isEmpty()) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
