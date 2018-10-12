package com.qinglan.sdk.model;

import android.text.TextUtils;

import com.qinglan.sdk.common.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class TokenInfo {
    private String accessToken;// Access Token值
    private long expiresIn;// Access Token的有效期（秒）
    private String account;// 登录账户名

    private static final String KEY_DATA = "data";
    private static final String KEY_EXPIRES = "expires_in";
    private static final String KEY_TOKEN = "access_token";
    private static final String KEY_ERROR = "error_code";

    /**
     * {
     * "data": {
     * "expires_in": "36000",
     * "access_token": "xxxxxxxxxxxxxxxx"
     * },
     * "error_code": 0
     * }
     */
    public static TokenInfo parseJson(String jsonStr) {
        TokenInfo tokenInfo = null;
        if (!TextUtils.isEmpty(jsonStr)) {
            Log.d(jsonStr);
            try {
                JSONObject dataJsonObj = new JSONObject(jsonStr);

                String accessToken = dataJsonObj.optString(KEY_DATA);
                Long expiresIn = dataJsonObj.optLong(KEY_EXPIRES);

                tokenInfo = new TokenInfo();
                tokenInfo.setAccessToken(accessToken);
                tokenInfo.setExpiresIn(expiresIn);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tokenInfo;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        accessToken = token;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String toJsonString() {

        JSONObject obj = new JSONObject();
        try {
            obj.put(KEY_ERROR, 0);

            JSONObject dataObj = new JSONObject();
            dataObj.put(KEY_EXPIRES, expiresIn);
            dataObj.put(KEY_TOKEN, accessToken);

            obj.put(KEY_DATA, dataObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj.toString();
    }

}
