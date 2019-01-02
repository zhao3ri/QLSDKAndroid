package com.qinglan.sdk.android.channel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.ResContainer;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.BaseResult;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.OnResponseListener;
import com.qinglan.sdk.android.net.impl.QueryOrderRequestInfo;

import java.util.Map;

import static com.qinglan.sdk.android.net.HttpConstants.RESPONSE_CODE;
import static com.qinglan.sdk.android.net.HttpConstants.RESPONSE_CODE_UNKNOWN_ERROR;

public abstract class BaseChannel implements IChannel {
    private int channelId;
    private String channelName;
    protected Config gameConfig;
    private UserInfo user;
    protected static final String PARAM_PAY_SIGN = "sign";
    private static final String RES_NAME_APP_ID = "qlsdk_third_party_appid";
    private static final String RES_NAME_APP_KEY = "qlsdk_third_party_appkey";
    private static final String RES_NAME_PUBLIC_KEY = "qlsdk_third_party_pubkey";
    private static final String RES_NAME_SECRET_KEY = "qlsdk_third_party_seckey";
    private static final String RES_NAME_CP_ID = "qlsdk_third_party_cpid";
    private static final String RES_NAME_CP_KEY = "qlsdk_third_party_cpkey";

    @Override
    public void load(ChannelParamsReader.ChannelParam p, Config config) {
        channelId = p.id;
        channelName = p.name;
        gameConfig = config;
        setupConfig();
    }

    private void setupConfig() {
        if (TextUtils.isEmpty(gameConfig.getAppID())) {
            gameConfig.setAppID(getString(RES_NAME_APP_ID));
        }
        if (TextUtils.isEmpty(gameConfig.getAppKey())) {
            gameConfig.setAppKey(getString(RES_NAME_APP_KEY));
        }
        if (TextUtils.isEmpty(gameConfig.getPublicKey())) {
            gameConfig.setPublicKey(getString(RES_NAME_PUBLIC_KEY));
        }
        if (TextUtils.isEmpty(gameConfig.getSecretKey())) {
            gameConfig.setSecretKey(getString(RES_NAME_SECRET_KEY));
        }
        if (TextUtils.isEmpty(gameConfig.getCpID())) {
            gameConfig.setCpID(getString(RES_NAME_CP_ID));
        }
        if (TextUtils.isEmpty(gameConfig.getCpKey())) {
            gameConfig.setCpKey(getString(RES_NAME_CP_KEY));
        }
    }

    private String getString(String resName) {
        int resId = ResContainer.getResId(BaseChannel.class.getPackage().getName(), "string", resName);
        String res = null;
        try {
            res = gameConfig.getContext().getString(resId);
            Log.d("string==" + res);
        } catch (Resources.NotFoundException e) {
            Log.e(resName + " is not found!!");
        }
        return res;
    }

    @Override
    public int getId() {
        return channelId;
    }

    @Override
    public String getName() {
        return channelName;
    }

    @Override
    public final void setUser(UserInfo info) {
        user = info;
    }

    protected final String getUserId() {
        return user.getId();
    }

    protected final String getUserToken() {
        return user.getUdToken();
    }

    protected final String getUserName() {
        return user.getUserName();
    }

    protected void queryOrderStatus(final String orderId, final Callback.OnPayRequestListener listener) {
        final QueryOrderRequestInfo request = new QueryOrderRequestInfo();
        request.gameId = gameConfig.getGameId();
        request.channelId = getId();
        request.orderId = orderId;
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                Log.d("success: " + success + ", result: " + result);
                if (!success || TextUtils.isEmpty(result)) {
                    if (listener != null)
                        listener.onFailed(result);
                    return;
                }

                Map<String, Object> resMap = Utils.json2Object(result, new TypeToken<Map<String, Object>>() {
                }.getType());
                int code = Utils.getJsonValue(result, RESPONSE_CODE, RESPONSE_CODE_UNKNOWN_ERROR);
                if (success && null != resMap && !resMap.isEmpty()
                        && code == HttpConstants.RESPONSE_CODE_SUCCESS) {
                    int status = Utils.getJsonValue(result, HttpConstants.RESPONSE_ORDER_STATUS
                            , HttpConstants.ORDER_STATUS_SUBMIT_SUCCESS);
                    int notifyStatus = Utils.getJsonValue(result, HttpConstants.RESPONSE_ORDER_NOTIFY_STATUS
                            , HttpConstants.ORDER_NOTIFY_STATUS_DEFAULT);
                    if (status == HttpConstants.ORDER_STATUS_PAYMENT_SUCCESS && notifyStatus == HttpConstants.ORDER_NOTIFY_STATUS_SUCCESS) {
                        if (listener != null) {
                            listener.onSuccess(orderId);
                        }
                    } else {
                        if (listener != null)
                            listener.onFailed(result);
                    }
                } else {
                    if (listener != null)
                        listener.onFailed(result);
                }
            }
        }).execute(request);
    }

    protected String getErrorMsg(String code, String msg) {
        String error = String.format("%s:%s", code, msg);
        return error;
    }

    protected Map<String, Object> getParams(String result) {
        Map<String, Object> resMap = Utils.json2Object(result, new TypeToken<Map<String, Object>>() {
        }.getType());
        return resMap;
    }

    protected int getCode(Map<String, Object> res) {
        int code = RESPONSE_CODE_UNKNOWN_ERROR;
        if (null != res.get(RESPONSE_CODE)) {
            code = Integer.valueOf(res.get(RESPONSE_CODE).toString());
        }
        return code;
    }

    protected BaseResult getResult(String content) {
        BaseResult result = Utils.json2Object(content, BaseResult.class);
        return result;
    }

    @Override
    public void onCreate(Activity activity) {
    }

    @Override
    public void onStart(Activity activity) {
    }

    @Override
    public void onRestart(Activity activity) {
    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onPause(Activity activity) {

    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onNewIntent(Activity activity, Intent intent) {

    }

    @Override
    public void onBackPressed(Activity activity) {

    }

    @Override
    public void attachBaseContext(Context newBase) {

    }

    @Override
    public void onConfigurationChanged(Activity activity, Configuration newConfig) {

    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
    }

    @Override
    public void onWindowFocusChanged(Activity activity, boolean hasFocus) {
    }

    @Override
    public void onApplicationAttachBaseContext(Application app, Context base) {
    }

    @Override
    public void onApplicationConfiguration(Application app, Configuration newConfig) {
    }

    @Override
    public void onApplicationTerminate(Application app) {
    }
}
