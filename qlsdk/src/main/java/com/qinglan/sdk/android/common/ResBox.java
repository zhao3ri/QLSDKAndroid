package com.qinglan.sdk.android.common;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhaoj on 2018/10/13.
 */
public final class ResBox {
    private static ResBox R = null;
    private Map<String, Integer> map = new HashMap();
    private Context mContext = null;
    private Map<String, SocializeResource> mResources;
    private static final String DEFAULT_PACKAGE_NAME = "com.qinglan.sdk.android";

    private ResBox(Context context) {
        mContext = context.getApplicationContext();
    }

    public static synchronized ResBox get(Context context) {
        if (R == null) {
            R = new ResBox(context);
        }

        return R;
    }

    public int layout(String resName) {
        return getResId(mContext, "layout", resName);
    }

    public int id(String resName) {
        return getResId(mContext, "id", resName);
    }

    public int drawable(String resName) {
        return getResId(mContext, "drawable", resName);
    }

    public int style(String resName) {
        return getResId(mContext, "style", resName);
    }

    public int string(String resName) {
        return getResId(mContext, "string", resName);
    }

    public String getString(String resName) {
        return mContext.getString(string(resName));
    }

    public int color(String resName) {
        return getResId(mContext, "color", resName);
    }

    public int dimen(String resName) {
        return getResId(mContext, "dimen", resName);
    }

    public int raw(String resName) {
        return getResId(mContext, "raw", resName);
    }

    public int anim(String resName) {
        return getResId(mContext, "anim", resName);
    }

    public int styleable(String resName) {
        return getResId(mContext, "styleable", resName);
    }

    public int array(String resName) {
        return getResId(mContext, "array", resName);
    }

    public ResBox(Context resName, Map<String, SocializeResource> resourceMap) {
        mResources = resourceMap;
        mContext = resName;
    }

    public static int getResourceId(Context context, String resType, String resName) {
        Resources resources = context.getResources();

        int resId = resources.getIdentifier(resName, resType, context.getPackageName());
        if (resId <= 0) {
            throw new RuntimeException("获取资源ID失败:(packageName=" + context.getPackageName() + " type=" + resType + " name=" + resName);
        } else {
            return resId;
        }
    }

    private static int getResId(Context context, String type, String name) {
        return getResId(DEFAULT_PACKAGE_NAME, type, name);
    }

    public static int getResId(String packageName, String type, String name) {
        try {
            Class<?> cls = Class.forName(packageName + ".R");
            for (Class<?> childClass : cls.getClasses()) {
                String simple = childClass.getSimpleName();
                if (simple.equals(type)) {
                    for (Field field : childClass.getFields()) {
                        String fieldName = field.getName();
                        if (fieldName.equals(name)) {
                            return (Integer) field.get(null);
                        }
                    }
                }//if
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("获取资源ID失败:(packageName=" + packageName + " type=" + type + " name=" + name);
    }

    public static String getString(Context context, String resName) {
        int id = getResourceId(context, "string", resName);
        return context.getString(id);
    }

    public synchronized Map<String, SocializeResource> batch() {
        if (this.mResources == null) {
            return this.mResources;
        } else {
            Set resName = this.mResources.keySet();

            ResBox.SocializeResource socializeResource;
            for (Iterator iterator = resName.iterator(); iterator.hasNext(); socializeResource.mIsCompleted = true) {
                String name = (String) iterator.next();
                socializeResource = (ResBox.SocializeResource) this.mResources.get(name);
                socializeResource.mId = getResourceId(mContext, socializeResource.mType, socializeResource.mName);
            }

            return this.mResources;
        }
    }

    public static int[] getStyleableArrts(Context context, String resName) {
        return getResourceDeclareStyleableIntArray(context, resName);
    }

    public static int[] getStyleableArrts(String resName) {
        return getResourceDeclareStyleableIntArray(null, resName);
    }

    private static final int[] getResourceDeclareStyleableIntArray(Context context, String resName) {
        try {
            String packageName = context == null ? DEFAULT_PACKAGE_NAME : context.getPackageName();
            Field[] styleableFields = Class.forName(packageName + ".R$styleable").getFields();
            Field[] fields = styleableFields;
            int length = styleableFields.length;

            for (int i = 0; i < length; ++i) {
                Field field = fields[i];
                if (field.getName().equals(resName)) {
                    int[] styleableArray = (int[]) ((int[]) field.get((Object) null));
                    return styleableArray;
                }
            }
        } catch (Throwable var8) {
            var8.printStackTrace();
        }

        return null;
    }

    public static class SocializeResource {
        public String mType;
        public String mName;
        public boolean mIsCompleted = false;
        public int mId;

        public SocializeResource(String resType, String resName) {
            this.mType = resType;
            this.mName = resName;
        }
    }

}

