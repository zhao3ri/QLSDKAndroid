package com.qinglan.sdk.android.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.qinglan.sdk.android.R;
import com.qinglan.sdk.android.common.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tyland on 2018/4/29.
 */
public class PermissionUtils {
    public static final int CODE_RECORD_AUDIO = 0;
    public static final int CODE_GET_ACCOUNTS = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_CALL_PHONE = 3;
    public static final int CODE_CAMERA = 4;
    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    public static final int CODE_ACCESS_COARSE_LOCATION = 6;
    public static final int CODE_READ_EXTERNAL_STORAGE = 7;
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 8;
    public static final int CODE_MULTI_PERMISSION = 100;

    public enum Permission {
        RECORD_AUDIO(CODE_RECORD_AUDIO, Manifest.permission.RECORD_AUDIO),
        GET_ACCOUNTS(CODE_GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS),
        READ_PHONE_STATE(CODE_READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE),
        CALL_PHONE(CODE_CALL_PHONE, Manifest.permission.CALL_PHONE),
        CAMERA(CODE_CAMERA, Manifest.permission.CAMERA),
        ACCESS_FINE_LOCATION(CODE_ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
        ACCESS_COARSE_LOCATION(CODE_ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
        READ_EXTERNAL_STORAGE(CODE_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
        WRITE_EXTERNAL_STORAGE(CODE_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int permissionCode;
        String permissionName;

        Permission(int code, String name) {
            permissionCode = code;
            permissionName = name;
        }

        public int getCode() {
            return permissionCode;
        }

        public String getPermission() {
            return permissionName;
        }

        public static String[] names() {
            int length = values().length;
            String[] pms = new String[length];
            for (int i = 0; i < length; i++) {
                pms[i] = values()[i].permissionName;
            }
            return pms;
        }

        public static String getNameByCode(int id) {
            for (Permission p : values()) {
                if (p.permissionCode == id) {
                    return p.getPermission();
                }
            }
            return null;
        }

        public static Permission valuesOf(String name) {
            for (Permission p : values()) {
                if (p.permissionName.equals(name)) {
                    return p;
                }
            }
            return null;
        }

        public static Permission valuesOf(int id) {
            for (Permission p : values()) {
                if (p.permissionCode == id) {
                    return p;
                }
            }
            return null;
        }

        public static Permission[] subValues(@NonNull String[] names) {
            List<Permission> permissions = new ArrayList<>();
            for (String name : names) {
                for (Permission p : values()) {
                    if (p.permissionName.equals(name)) {
                        permissions.add(p);
                        continue;
                    }
                }
            }
            Permission[] permissionArray = new Permission[permissions.size()];
            return (Permission[]) permissions.toArray(permissionArray);
        }
    }

    public interface PermissionGrant {
        //        void onPermissionGranted(int requestCode);
        void onPermissionGranted(int requestCode, Permission... p);
    }

    public static int checkSelfPermission(Activity activity, int requestCode) {
//        final String requestPermission = requestPermissions[requestCode];
        final String requestPermission = Permission.getNameByCode(requestCode);
        int checkSelfPermission = PackageManager.PERMISSION_DENIED;

        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
        } catch (RuntimeException e) {
            Log.e("RuntimeException:" + e.getMessage());
        }
        return checkSelfPermission;
    }

    /**
     * Requests permission.
     *
     * @param activity
     * @param requestCode request code, e.g. if you need request CAMERA permission,parameters is PermissionUtils.CODE_CAMERA
     */
    public static int requestPermission(final Activity activity, final int requestCode, PermissionGrant permissionGrant) {
        return requestPermission(activity, Permission.valuesOf(requestCode), permissionGrant);
    }

    /**
     * Requests permission.
     */
    public static int requestPermission(final Activity activity, final Permission p, PermissionGrant permissionGrant) {
        if (activity == null) {
            return PackageManager.PERMISSION_DENIED;
        }

        Log.i("requestPermission requestCode:" + p.getCode());
        if (p.getCode() < 0 || p.getCode() >= Permission.values().length) {
            Log.w("requestPermission illegal requestCode:" + p.getCode());
            return PackageManager.PERMISSION_DENIED;
        }

        //如果是6.0以下的手机，ActivityCompat.checkSelfPermission()会始终等于PERMISSION_GRANTED，
        // 但是，如果用户关闭了你申请的权限，ActivityCompat.checkSelfPermission(),会导致程序崩溃(java.lang.RuntimeException: Unknown exception code: 1 msg null)，
        // 你可以使用try{}catch(){},处理异常，也可以在这个地方，低于23就什么都不做，
        // 个人建议try{}catch(){}单独处理，提示用户开启权限。
        if (Build.VERSION.SDK_INT < 23) {
            return PackageManager.PERMISSION_GRANTED;
        }

        final String requestPermission = p.getPermission();
        int checkSelfPermission = checkSelfPermission(activity, p.getCode());

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            Log.i("ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED");


            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                Log.i("requestPermission shouldShowRequestPermissionRationale");
                shouldShowRationale(activity, p, requestPermission);

            } else {
                Log.d("requestCameraPermission else");
                ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, p.getCode());
            }

        } else {
            Log.d("ActivityCompat.checkSelfPermission ==== PackageManager.PERMISSION_GRANTED");
            if (permissionGrant != null)
                permissionGrant.onPermissionGranted(p.getCode(), p);
        }
        return checkSelfPermission;
    }

    private static void requestMultiResult(Activity activity, String[] permissions, int[] grantResults, PermissionGrant permissionGrant) {

        if (activity == null) {
            return;
        }

        Log.d("onRequestPermissionsResult permissions length:" + permissions.length);
        Map<String, Integer> perms = new HashMap<>();

        ArrayList<String> notGranted = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            Log.d("permissions: [i]:" + i + ", permissions[i]" + permissions[i] + ",grantResults[i]:" + grantResults[i]);
            perms.put(permissions[i], grantResults[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permissions[i]);
            }
        }

        if (notGranted.size() == 0) {
            if (permissionGrant != null)
                permissionGrant.onPermissionGranted(CODE_MULTI_PERMISSION, Permission.subValues(permissions));
        } else {
            openSettingActivity(activity, "those permission need granted!");
        }

    }


    /**
     * 一次申请多个权限
     */
    public static void requestMultiPermissions(final Activity activity, PermissionGrant grant) {
        requestMultiPermissions(activity, grant, Permission.names());
    }

    public static void requestMultiPermissions(final Activity activity, PermissionGrant grant, String... permissions) {

        final List<String> permissionsList = getNoGrantedPermission(activity, permissions, false);
        final List<String> shouldRationalePermissionsList = getNoGrantedPermission(activity, permissions, true);

        if (permissionsList == null || shouldRationalePermissionsList == null) {
            return;
        }
        Log.d("requestMultiPermissions permissionsList:" + permissionsList.size() + ",shouldRationalePermissionsList:" + shouldRationalePermissionsList.size());

        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                    CODE_MULTI_PERMISSION);
            Log.d("showMessageOKCancel requestPermissions");

        } else if (shouldRationalePermissionsList.size() > 0) {
            showMessageOKCancel(activity, "should open those permission",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, shouldRationalePermissionsList.toArray(new String[shouldRationalePermissionsList.size()]),
                                    CODE_MULTI_PERMISSION);
                            Log.d("showMessageOKCancel requestPermissions");
                        }
                    });
        } else {
            grant.onPermissionGranted(CODE_MULTI_PERMISSION);
        }

    }

    private static final String PERMISSION_HINT = "没有此权限，无法开启这个功能，请开启权限：%s";

    private static void shouldShowRationale(final Activity activity, /*final int requestCode*/final Permission p, final String requestPermission) {
//        String[] permissionsHint = activity.getResources().getStringArray(ResContainer.get(activity).array("qlsdk_permissions"));
        showMessageOKCancel(activity, "Rationale: " + String.format(PERMISSION_HINT, p.name()), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{requestPermission},
                        p.getCode());
                Log.d("showMessageOKCancel requestPermissions:" + requestPermission);
            }
        });
    }

    private static void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    /**
     * @param activity
     * @param requestCode  Need consistent with requestPermission
     * @param permissions
     * @param grantResults
     */
    public static void requestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
                                                @NonNull int[] grantResults, PermissionGrant permissionGrant) {

        if (activity == null) {
            return;
        }
        Log.d("requestPermissionsResult requestCode:" + requestCode);

        if (requestCode == CODE_MULTI_PERMISSION) {
            requestMultiResult(activity, permissions, grantResults, permissionGrant);
            return;
        }

        if (requestCode < 0 || requestCode >= Permission.values().length) {
            Log.w("requestPermissionsResult illegal requestCode:" + requestCode);
            return;
        }

        Log.i("onRequestPermissionsResult requestCode:" + requestCode + ",permissions:" + permissions.toString()
                + ",grantResults:" + grantResults.toString() + ",length:" + grantResults.length);

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i("onRequestPermissionsResult PERMISSION_GRANTED");
            //success, do something, can use callback
            if (permissionGrant != null)
                permissionGrant.onPermissionGranted(requestCode);

        } else {
            Log.i("onRequestPermissionsResult PERMISSION NOT GRANTED");
            openSettingActivity(activity, "Result" + String.format(PERMISSION_HINT, Permission.valuesOf(requestCode).name()));
        }

    }

    private static void openSettingActivity(final Activity activity, String message) {

        showMessageOKCancel(activity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Log.d("getPackageName(): " + activity.getPackageName());
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });
    }


    /**
     * @param activity
     * @param isShouldRationale true: return no granted and shouldShowRequestPermissionRationale permissions, false:return no granted and !shouldShowRequestPermissionRationale
     * @return
     */
    public static ArrayList<String> getNoGrantedPermission(Activity activity, boolean isShouldRationale) {
        return getNoGrantedPermission(activity, Permission.names(), isShouldRationale);
    }

    /**
     * @param activity
     * @param request           request permissions array
     * @param isShouldRationale true: return no granted and shouldShowRequestPermissionRationale permissions, false:return no granted and !shouldShowRequestPermissionRationale
     * @return
     */
    public static ArrayList<String> getNoGrantedPermission(Activity activity, String[] request, boolean isShouldRationale) {
        ArrayList<String> permissions = new ArrayList<>();

        if (null == request || request.length == 0) {
            return permissions;
        }

        for (int i = 0; i < request.length; i++) {
            String requestPermission = request[i];

            int checkSelfPermission = -1;
            try {
                checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
            } catch (RuntimeException e) {

                Log.e("RuntimeException:" + e.getMessage());
                return null;
            }

            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                Log.i("getNoGrantedPermission ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED:" + requestPermission);

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                    Log.d("shouldShowRequestPermissionRationale if");
                    if (isShouldRationale) {
                        permissions.add(requestPermission);
                    }

                } else {

                    if (!isShouldRationale) {
                        permissions.add(requestPermission);
                    }
                    Log.d("shouldShowRequestPermissionRationale else");
                }

            }
        }

        return permissions;
    }

}
