package com.qinglan.sdk.common;

/**
 * Created by zhaoj on 2018/9/19
 *
 * @author zhaoj
 */
public class Log {
    public static final int VERBOSE = 1;

    public static final int DEBUG = 2;

    public static final int INFO = 3;

    public static final int WARN = 4;

    public static final int ERROR = 5;

    private static int LEVEL = VERBOSE;

    private static final String TAG = "QLSDK";

    public static final void v(String msg) {
        if (LEVEL <= VERBOSE)
            android.util.Log.v(TAG, msg);
    }

    public static final void v(Object msg) {
        if (LEVEL <= VERBOSE)
            android.util.Log.v(TAG, "" + msg);
    }

    public static final void v(String tag, String msg) {
        if (LEVEL <= VERBOSE)
            android.util.Log.v(tag, msg);
    }

    public static final void v(String tag, String msg, Throwable tr) {
        if (LEVEL <= VERBOSE)
            android.util.Log.v(tag, msg, tr);
    }

    public static final void d(String msg) {
        if (LEVEL <= DEBUG)
            android.util.Log.d(TAG, msg);
    }

    public static final void d(Object msg) {
        if (LEVEL <= DEBUG)
            android.util.Log.d(TAG, msg + "");
    }

    public static final void d(String tag, String msg) {
        if (LEVEL <= DEBUG)
            android.util.Log.d(tag, msg);
    }

    public static final void d(String tag, String msg, Throwable tr) {
        if (LEVEL <= DEBUG)
            android.util.Log.d(tag, msg, tr);
    }

    public static final void i(String msg) {
        if (LEVEL <= INFO)
            android.util.Log.i(TAG, msg);
    }

    public static final void i(Object msg) {
        if (LEVEL <= INFO)
            android.util.Log.i(TAG, msg + "");
    }

    public static final void i(String tag, String msg) {
        if (LEVEL <= INFO)
            android.util.Log.i(tag, msg);
    }

    public static final void i(String tag, String msg, Throwable tr) {
        if (LEVEL <= INFO)
            android.util.Log.i(tag, msg, tr);
    }

    public static final void w(String msg) {
        if (LEVEL <= WARN)
            android.util.Log.w(TAG, msg);
    }

    public static final void w(Object msg) {
        if (LEVEL <= WARN)
            android.util.Log.w(TAG, msg + "");
    }

    public static final void w(Throwable msg) {
        if (LEVEL <= WARN)
            android.util.Log.w(TAG, "", msg);
    }

    public static final void w(String tag, String msg) {
        if (LEVEL <= WARN)
            android.util.Log.w(tag, msg);
    }

    public static final void w(String tag, String msg, Throwable tr) {
        if (LEVEL <= WARN)
            android.util.Log.w(tag, msg, tr);
    }

    public static final void e(String msg) {
        if (LEVEL <= ERROR)
            android.util.Log.e(TAG, msg);
    }

    public static final void e(Object msg) {
        if (LEVEL <= ERROR)
            android.util.Log.e(TAG, msg + "");
    }

    public static final void e(Throwable msg) {
        if (LEVEL <= ERROR)
            android.util.Log.e(TAG, "", msg);
    }

    public static final void e(String tag, String msg) {
        if (LEVEL <= ERROR)
            android.util.Log.e(tag, msg);
    }

    public static final void e(String tag, String msg, Throwable tr) {
        if (LEVEL <= ERROR)
            android.util.Log.e(tag, msg, tr);
    }

    public static final String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ":" + ste.getLineNumber();
    }

    public static void setLevel(int LEVEL) {
        Log.LEVEL = LEVEL;
    }
}
