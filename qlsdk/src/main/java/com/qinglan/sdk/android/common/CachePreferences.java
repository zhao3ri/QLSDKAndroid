package com.qinglan.sdk.android.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhaoj on 2018/10/13.
 */
public class CachePreferences {
    protected static SharedPreferences getPreferences(Context context, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    protected static SharedPreferences.Editor getEditor(Context context, String name) {
        SharedPreferences.Editor editor = getPreferences(context, name).edit();
        return editor;
    }

    public static <T extends Object> T get(Context context, String name, String key, Object defValue) {
        if (defValue instanceof Integer) {
            return (T) Integer.valueOf(getPreferences(context, name).getInt(key, (int) defValue));
        }
        if (defValue instanceof String) {
            return (T) getPreferences(context, name).getString(key, (String) defValue);
        }
        if (defValue instanceof Boolean) {
            return (T) Boolean.valueOf(getPreferences(context, name).getBoolean(key, (boolean) defValue));
        }
        if (defValue instanceof Long) {
            return (T) Long.valueOf(getPreferences(context, name).getLong(key, (long) defValue));
        }
        if (defValue instanceof Float) {
            return (T) Float.valueOf(getPreferences(context, name).getFloat(key, (float) defValue));
        }
        return (T) defValue;
    }

    public static void put(Context context, String name, String key, Object value) {
        SharedPreferences.Editor editor = getEditor(context, name);
        if (value instanceof Integer) {
            editor.putInt(key, (int) value);
        } else if (value instanceof String) {
            editor.putString(key, String.valueOf(value));
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (float) value);
        }
        editor.commit();
    }

    protected static void clear(Context context, String name) {
        SharedPreferences.Editor editor = getEditor(context, name);
        editor.clear();
        editor.commit();
    }

    protected static void delete(Context context, String name, String key) {
        SharedPreferences.Editor editor = getEditor(context, name);
        editor.remove(key);
        editor.commit();
    }
}
