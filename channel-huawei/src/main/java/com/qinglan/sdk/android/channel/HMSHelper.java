package com.qinglan.sdk.android.channel;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.huawei.android.hms.agent.pay.PaySignUtil;
import com.huawei.hms.support.api.entity.game.GamePlayerInfo;
import com.huawei.hms.support.api.entity.game.GameUserData;
import com.huawei.hms.support.api.entity.pay.PayReq;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.BaseResult;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.OnResponseListener;
import com.qinglan.sdk.android.channel.entity.HMSPaySignRequest;
import com.qinglan.sdk.android.channel.entity.HMSVerifyRequest;

import java.util.Map;

import static com.qinglan.sdk.android.net.HttpConstants.RESPONSE_CODE_SUCCESS;


public class HMSHelper {
    private static final String MERCHANT_NAME = "Qinglanbingshui";

    private static final int RETURN_CODE_ERROR = -1;

    private long gameId;
    private int platformId;
    private String pubKey;
    private String appID;
    private String cpId;

    private HMSHelper() {
    }

    public HMSHelper(long gameId, int platformId, String appID, String pubKey, String cpId) {
        this.appID = appID;
        this.pubKey = pubKey;
        this.gameId = gameId;
        this.platformId = platformId;
        this.cpId = cpId;
    }

    public UserInfo getUserInfo(GameUserData data) {
        UserInfo user = new UserInfo();
        user.setId(data.getPlayerId());
        user.setUserName(data.getDisplayName());
        return user;
    }

    public void verifyLogin(GameUserData data, final OnVerifyRequestListener listener) {
        HMSVerifyRequest request = new HMSVerifyRequest();
        request.gameId = String.valueOf(gameId);
        request.channelId = platformId;
        request.appID = appID;
        request.cpID = cpId;
        request.ts = data.getTs();
        request.playerId = data.getPlayerId();
        request.playerLevel = String.valueOf(data.getPlayerLevel());
        request.playerSSign = data.getGameAuthSign();
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {

            @Override
            public void onResponse(boolean success, String result) {
                Log.d("verify result===" + result);
                if (success && !TextUtils.isEmpty(result) && getResult(result) != null) {
                    BaseResult res = getResult(result);
                    if (res.code == RESPONSE_CODE_SUCCESS) {
                        if (listener != null)
                            listener.onRequest(true, res.code, res.msg, res.data);
                    } else {
                        if (listener != null)
                            listener.onRequest(false, res.code, res.msg, res.data);
                    }
                } else {
                    if (listener != null)
                        listener.onRequest(false, RETURN_CODE_ERROR, result, null);
                }

            }
        }).execute(request);
    }

    private BaseResult getResult(String content) {
        BaseResult result = Utils.json2Object(content, BaseResult.class);
        return result;
    }

    /**
     * 保存玩家信息
     */
    public GamePlayerInfo getPlayerInfo(GameRole role) {
        GamePlayerInfo playerInfo = new GamePlayerInfo();
        playerInfo.rank = role.getRoleLevel();
        playerInfo.role = role.getRoleName();
        playerInfo.sociaty = role.getZoneId();
        return playerInfo;
    }

    /**
     * 创建支付请求,并对参数进行签名
     */
    public void createPayRequest(GamePay pay, String orderId, /*String url,*/ OnSignRequestListener listener) {
        PayReq payReq = new PayReq();

        //生成总金额
        String amount = String.format("%.2f", pay.getAmount() / 100);

        //商品名称
        payReq.productName = pay.getGoodsName();
        //商品描述
        payReq.productDesc = pay.getGoodsName();
        // 商户ID，来源于开发者联盟的“支付ID”
        payReq.merchantId = cpId;
        // 应用ID，来源于开发者联盟
        payReq.applicationID = appID;
        // 支付金额
        payReq.amount = amount;
        // 商户订单号：开发者在支付前生成，用来唯一标识一次支付请求
        payReq.requestId = orderId;
        // 渠道号
        payReq.sdkChannel = 1;
//        payReq.url = url;

        // 商户名称，必填，不参与签名。开发者注册的公司名称
        payReq.merchantName = MERCHANT_NAME;
        //分类，必填，不参与签名。该字段会影响风控策略
        // X4：主题,X5：应用商店,  X6：游戏,X7：天际通,X8：云空间,X9：电子书,X10：华为学习,X11：音乐,X12 视频,
        // X31 话费充值,X32 机票/酒店,X33 电影票,X34 团购,X35 手机预购,X36 公共缴费,X39 流量充值
        payReq.serviceCatalog = "X6"; // 应用设置为"X5"，游戏设置为"X6"
        //商户保留信息，选填不参与签名，支付成功后会华为支付平台会原样 回调CP服务端
        payReq.extReserved = pay.getExtInfo();
        signPay(payReq, listener);
    }

    /**
     * 对支付请求参数进行签名
     */
    private void signPay(final PayReq payReq, final OnSignRequestListener listener) {
        HMSPaySignRequest request = new HMSPaySignRequest();
        request.gameId = String.valueOf(gameId);
        request.channelId = platformId;
        request.content = PaySignUtil.getStringForSign(payReq);
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {

            @Override
            public void onResponse(boolean success, String result) {
                Log.d("sign pay result===" + result);
                if (success && TextUtils.isEmpty(result)) {
                    payReq.sign = result;
                    listener.onRequest(true, payReq);
                } else {
                    listener.onRequest(false, null);
                }

            }
        }).execute(request);
    }

    interface OnSignRequestListener {
        void onRequest(boolean success, PayReq req);
    }

    interface OnVerifyRequestListener {
        void onRequest(boolean success, int code, String msg, Map<String, Object> data);
    }
}
