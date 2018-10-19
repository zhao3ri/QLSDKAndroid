package com.qinglan.sdk.android;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.qinglan.sdk.android.utils.PermissionUtils;

/**
 * 权限请求Activity
 * android的权限系统自6.0之后就需要开发者去手动的申请，这些权限一般是涉及到用户的隐私，所以需要用户授权
 * Created by zhaoj on 2018/10/14.
 */
public class PermissionActivity extends Activity {

    private PermissionUtils.PermissionGrant grant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode, PermissionUtils.Permission... p) {
            PermissionActivity.this.onPermissionGranted(p);
        }
    };

    public final boolean checkPermission(@NonNull PermissionUtils.Permission p) {
        boolean hasPermission = false;
        if (PermissionUtils.checkSelfPermission(this, p.getCode()) == PackageManager.PERMISSION_GRANTED) {
            hasPermission = true;
        }
        return hasPermission;
    }

    public final void requestPermission(@NonNull PermissionUtils.Permission... p) {
        if (p.length == 0) {
            return;
        }
        if (p.length == 1) {
            PermissionUtils.requestPermission(this, p[0], grant);
        } else {
            PermissionUtils.requestMultiPermissions(this, grant, getPermissionIds(p));
        }
    }

    private String[] getPermissionIds(PermissionUtils.Permission[] permissions) {
        int length = permissions.length;
        String[] names = new String[length];
        for (int i = 0; i < length; i++) {
            names[i] = permissions[i].getPermission();
        }
        return names;
    }

    /**
     * 获取权限成功时调用此方法
     * 当只请求多个权限时，需所有权限都获取成功才会调用
     */
    public void onPermissionGranted(PermissionUtils.Permission... p) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, grant);
    }
}
