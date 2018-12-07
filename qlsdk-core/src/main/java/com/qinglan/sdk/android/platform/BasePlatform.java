package com.qinglan.sdk.android.platform;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.ResContainer;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.OnResponseListener;
import com.qinglan.sdk.android.net.impl.QueryOrderRequestInfo;

import java.util.Map;

public abstract class BasePlatform implements IPlatform {
    private int platformId;
    private String platformName;
    protected Config gameConfig;
    private UserInfo user;
    protected static final String PARAM_PAY_SIGN = "sign";
    private static final String RES_NAME_APP_ID = "qlsdk_third_party_appid";
    private static final String RES_NAME_APP_KEY = "qlsdk_third_party_appkey";
    private static final String RES_NAME_PUBLIC_KEY = "qlsdk_third_party_pubkey";
    private static final String RES_NAME_CP_ID = "qlsdk_third_party_cpid";


    @Override
    public void load(PlatformParamsReader.PlatformParam p, Config config) {
        platformId = p.id;
        platformName = p.name;
        gameConfig = config;
        if (TextUtils.isEmpty(gameConfig.getAppID())) {
            gameConfig.setAppID(ResContainer.getString(config.getContext(), RES_NAME_APP_ID));
        }
        if (TextUtils.isEmpty(gameConfig.getAppKey())) {
            gameConfig.setAppKey(ResContainer.getString(config.getContext(), RES_NAME_APP_KEY));
        }
        if (TextUtils.isEmpty(gameConfig.getPublicKey())) {
            gameConfig.setPublicKey(ResContainer.getString(config.getContext(), RES_NAME_PUBLIC_KEY));
        }
        if (TextUtils.isEmpty(gameConfig.getCpID())) {
            gameConfig.setCpID(ResContainer.getString(config.getContext(), RES_NAME_CP_ID));
        }
    }

    @Override
    public int getId() {
        return platformId;
    }

    @Override
    public String getName() {
        return platformName;
    }

    @Override
    public final void setUser(UserInfo info) {
        user = info;
    }

    protected final String getUserId() {
        return user.getId();
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
                int code = Utils.getJsonValue(result, HttpConstants.RESPONSE_CODE, HttpConstants.RESPONSE_CODE_UNKNOWN_ERROR);
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

}
