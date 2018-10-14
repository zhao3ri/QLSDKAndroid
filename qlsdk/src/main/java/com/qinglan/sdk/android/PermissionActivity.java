package com.qinglan.sdk.android;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.qinglan.sdk.android.utils.PermissionUtils;

/**
 * Created by zhaoj on 2018/10/14.
 */
public class PermissionActivity extends Activity {

    protected void checkPermission(PermissionUtils.Permission p) {
        if (PermissionUtils.checkSelfPermission(this, p.getId()) == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted(p);
        } else {
            requestPermission(p);
        }
    }

    protected void requestPermission(PermissionUtils.Permission p) {
        PermissionUtils.requestPermission(this, p.getId(), null);
    }

    protected void requestPermission(Activity activity, int... requestCode) {
//        return PermissionUtils.requestMultiPermissions(this,null,requestCode);
    }

    public void onPermissionGranted(PermissionUtils.Permission p) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, null);
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            onPermissionGranted(requestCode);
        }
    }
}
