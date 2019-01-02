package com.qinglan.sdk.android.channel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.bignox.sdk.INoxLogAgent;
import com.bignox.sdk.NoxSDKPlatform;
import com.bignox.sdk.NoxStatus;
import com.bignox.sdk.export.entity.KSAppEntity;
import com.bignox.sdk.export.entity.KSConsumeEntity;
import com.bignox.sdk.export.entity.KSUserEntity;
import com.bignox.sdk.export.entity.KSUserRoleEntity;
import com.bignox.sdk.export.listener.NoxEvent;
import com.bignox.sdk.export.listener.OnConsumeListener;
import com.bignox.sdk.export.listener.OnCreateRoleListener;
import com.bignox.sdk.export.listener.OnEntryListener;
import com.bignox.sdk.export.listener.OnExitListener;
import com.bignox.sdk.export.listener.OnInitListener;
import com.bignox.sdk.export.listener.OnLoginListener;
import com.bignox.sdk.export.listener.OnLogoutListener;
import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.channel.entity.YeshenVerifyRequest;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.BaseResult;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.OnResponseListener;

import java.util.Map;

import static com.qinglan.sdk.android.Constants.ACTION_LOGOUT;
import static com.qinglan.sdk.android.net.HttpConstants.RESPONSE_CODE_SUCCESS;

public class YeshenChannel extends BaseChannel implements OnLogoutListener {

    @Override
    public void init(Activity activity, final Callback.OnInitConnectedListener listener) {
        KSAppEntity appInfo = new KSAppEntity();
        appInfo.setAppId(gameConfig.getAppID());
        appInfo.setAppKey(gameConfig.getAppKey());
        NoxSDKPlatform.init(appInfo, activity, new OnInitListener() {
            @Override
            public void finish(NoxEvent<KSAppEntity> noxEvent) {
                switch (noxEvent.getStatus()) {
                    case NoxStatus.STATE_INIT_SUCCESS:
                        if (listener != null)
                            listener.onSuccess(null);
                        break;
                    case NoxStatus.STATE_NETWORK_ERROR:
                    case NoxStatus.STATE_REQUEST_TIMEOUT:
                    case NoxStatus.STATE_INIT_FAILED:
                    case NoxStatus.STATE_INIT_NO_INIT:
                    case NoxStatus.STATE_INIT_INITING:
                    default:
                        onFailed(listener, noxEvent);
                        break;
                }
            }
        });
        NoxSDKPlatform.getInstance().registerLogoutListener(this);
    }

    @Override
    public void login(Activity activity, final Callback.OnLoginListener listener) {
        NoxSDKPlatform.getInstance().noxLogin(new OnLoginListener() {
            @Override
            public void finish(NoxEvent<KSUserEntity> noxEvent) {
                switch (noxEvent.getStatus()) {
                    case NoxStatus.STATE_LOGIN_SUCCESS:
                        KSUserEntity userEntity = noxEvent.getObject();
                        UserInfo user = new UserInfo();
                        user.setUserName(userEntity.getUserName());
                        user.setId(userEntity.getUid());
                        user.setUdToken(userEntity.getAccessToken());
                        verifyAccount(user, listener);
                        break;
                    case NoxStatus.STATE_NETWORK_ERROR:
                    case NoxStatus.STATE_REQUEST_TIMEOUT:
                    case NoxStatus.STATE_INIT_NO_INIT:
                    case NoxStatus.STATE_LOGIN_CANCEL:
                    default:
                        onFailed(listener, noxEvent);
                        break;
                }
            }
        });
    }

    private void verifyAccount(final UserInfo user, final Callback.OnLoginListener listener) {
        YeshenVerifyRequest request = new YeshenVerifyRequest();
        request.gameId = gameConfig.getGameId();
        request.channelId = getId();
        request.accessToken = user.getUdToken();
        request.appID = gameConfig.getAppID();
        request.uid = user.getId();
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {

            @Override
            public void onResponse(boolean success, String result) {
                if (success && !TextUtils.isEmpty(result) && getResult(result) != null) {
                    BaseResult res = getResult(result);
                    if (res.code == RESPONSE_CODE_SUCCESS) {
                        if (listener != null)
                            listener.onSuccess(user);
                    } else {// 收到异常信息
                        onFailed(listener, getErrorMsg(res.code, res.msg));
                    }
                    return;
                }
                onFailed(listener, result);
            }
        }).execute(request);
    }

    @Override
    public void logout(Activity activity, GameRole role, final Callback.OnLogoutResponseListener listener) {
//        NoxSDKPlatform.getInstance().registerLogoutListener();
        NoxSDKPlatform.getInstance().noxLogout(new OnLogoutListener() {
            @Override
            public void finish(NoxEvent<KSUserEntity> event) {
                switch (event.getStatus()) {
                    case NoxStatus.STATE_LOGOUT_SUCCESS:
                        if (listener != null)
                            listener.onSuccess();
                        break;
                    case NoxStatus.STATE_NETWORK_ERROR:
                    case NoxStatus.STATE_REQUEST_TIMEOUT:
                    case NoxStatus.STATE_INIT_NO_INIT:
                    case NoxStatus.STATE_LOGOUT_ERROR:
                        onFailed(listener, event);
                        break;
                }
            }
        });
    }

    @Override
    public void showWinFloat(Activity activity) {

    }

    @Override
    public void hideWinFloat(Activity activity) {

    }

    @Override
    public void exit(Activity activity, GameRole role, final Callback.OnExitListener listener) {
        NoxSDKPlatform.getInstance().noxExit(new OnExitListener() {
            @Override
            public void finish(NoxEvent<KSAppEntity> event) {
                if (event.getStatus() == NoxStatus.STATE_EXIT_CANCEL) {// 用户取消退出
                    return;
                }
                switch (event.getStatus()) {
                    case NoxStatus.STATE_EXIT_SUCCESS:
                    case NoxStatus.STATE_EXIT_NOT_IMPLEMENT://渠道未实现
                        if (listener != null)
                            listener.onFinished(true, "");
                        break;
                    case NoxStatus.STATE_NETWORK_ERROR:
                    case NoxStatus.STATE_REQUEST_TIMEOUT:
                    case NoxStatus.STATE_INIT_NO_INIT:
                    case NoxStatus.STATE_EXIT_ERROR:
                        if (listener != null)
                            listener.onFinished(false, getErrorMsg(event));
                        break;
                }
            }
        });
    }

    @Override
    public void pay(Activity activity, GameRole role, GamePay pay, Map<String, Object> result, final Callback.OnPayRequestListener listener) {
        String orderId = result.get(HttpConstants.RESPONSE_ORDER_ID).toString();
        KSConsumeEntity ksConsumeEntity = new KSConsumeEntity();
        ksConsumeEntity.setGoodsTitle(pay.getGoodsName()); //必填项-商品名称
        ksConsumeEntity.setGoodsOrderId(orderId); //非必填项-商户订单id
        ksConsumeEntity.setGoodsDesc(pay.getGoodsName());//非必填项-商品描述
        ksConsumeEntity.setPrivateInfo(pay.getExtInfo()); //非必填项-商户私有信息。
        ksConsumeEntity.setOrderCoin(pay.getAmount()); //必填项-订单金额（大于0）单位为分
        ksConsumeEntity.setNotifyUrl(pay.getNotifyUrl()); //非必填项-设置游戏服务器的后端回调通知地址

        KSUserRoleEntity roleEntity = new KSUserRoleEntity();
        roleEntity.setRoleId(role.getRoleId());
        roleEntity.setRoleName(role.getRoleName());
        roleEntity.setZoneId(role.getZoneId());
        roleEntity.setZoneName(role.getZoneName());
        roleEntity.setServerId(role.getServerId());
        NoxSDKPlatform.getInstance().noxConsume(ksConsumeEntity, roleEntity, new OnConsumeListener() {
            @Override
            public void finish(NoxEvent<KSConsumeEntity> noxEvent) {
                switch (noxEvent.getStatus()) {
                    case NoxStatus.STATE_CONSUME_SUCCESS:
                        break;
                    case NoxStatus.STATE_NETWORK_ERROR:
                    case NoxStatus.STATE_REQUEST_TIMEOUT:
                    case NoxStatus.STATE_INIT_NO_INIT:
                    case NoxStatus.STATE_EXIT_ERROR:
                    case NoxStatus.STATE_CONSUME_FAILED://支付失败
                    case NoxStatus.STATE_CONSUME_CANCEL://支付取消
                    case NoxStatus.STATE_CONSUME_INVALIDMONEY://支付金额不合法
                        onFailed(listener, noxEvent);
                        break;
                }
            }
        });
    }

    @Override
    public void createRole(Activity activity, final GameRole role, long createTime,
                           final Callback.OnGameRoleRequestListener listener) {
        KSUserRoleEntity userRoleEntity = getUserRoleEntity(role, createTime);
        NoxSDKPlatform.getInstance().noxCreateRole(userRoleEntity, new OnCreateRoleListener() {
            @Override
            public void finish(NoxEvent<KSUserRoleEntity> noxEvent) {
                switch (noxEvent.getStatus()) {
                    case NoxStatus.STATE_CREATE_ROLE_SUCCESS:
                        if (listener != null)
                            listener.onSuccess(role);
                        break;
                    case NoxStatus.STATE_NETWORK_ERROR:
                    case NoxStatus.STATE_REQUEST_TIMEOUT:
                    case NoxStatus.STATE_INIT_NO_INIT:
                    case NoxStatus.STATE_CREATE_ROLE_ERROR:
                    default:
                        onFailed(listener, noxEvent);
                        break;
                }
            }
        });
    }

    private KSUserRoleEntity getUserRoleEntity(GameRole role, long createTime) {
        KSUserRoleEntity userRoleEntity = new KSUserRoleEntity();
        userRoleEntity.setServerId(role.getServerId());
        userRoleEntity.setZoneId(role.getZoneId());
        userRoleEntity.setZoneName(role.getZoneName());
        userRoleEntity.setRoleId(role.getRoleId());
        userRoleEntity.setRoleName(role.getRoleName());
        userRoleEntity.setRoleCreateTime(createTime);
        return userRoleEntity;
    }

    @Override
    public void selectRole(Activity activity, boolean showFloat, final GameRole role,
                           long createTime, final Callback.OnGameRoleRequestListener listener) {
        KSUserRoleEntity userRoleEntity = getUserRoleEntity(role, createTime);
        NoxSDKPlatform.getInstance().noxEntryGame(userRoleEntity, new OnEntryListener() {
            @Override
            public void finish(NoxEvent<KSUserRoleEntity> noxEvent) {
                switch (noxEvent.getStatus()) {
                    case NoxStatus.STATE_ENTRY_GAME_SUCCESS:
                        if (listener != null)
                            listener.onSuccess(role);
                        break;
                    case NoxStatus.STATE_NETWORK_ERROR:
                    case NoxStatus.STATE_REQUEST_TIMEOUT:
                    case NoxStatus.STATE_INIT_NO_INIT:
                    case NoxStatus.STATE_ENTRY_GAME_ERROR:
                    default:
                        onFailed(listener, noxEvent);
                        break;
                }
            }
        });
    }

    @Override
    public void levelUpdate(Activity activity, GameRole role, long createTime, Callback.
            OnLevelUpListener listener) {
        NoxSDKPlatform.getInstance().getLogAgent().levelUp(Integer.valueOf(role.getRoleLevel()));
        if (listener != null)
            listener.onFinished(true, "");
    }

    @Override
    public boolean isCustomLogoutUI() {
        return false;
    }

    @Override
    public void onCreate(Activity activity) {

    }

    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {
        NoxSDKPlatform.getInstance().noxResume();
    }

    @Override
    public void onPause(Activity activity) {
        NoxSDKPlatform.getInstance().noxPause();
    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {
        NoxSDKPlatform.getInstance().noxDestroy();
    }

    private void onFailed(Callback.DefaultResponseListener listener, NoxEvent event) {
        onFailed(listener, getErrorMsg(event));
    }

    private void onFailed(Callback.DefaultResponseListener listener, String msg) {
        if (listener != null)
            listener.onFailed(msg);
    }

    private String getErrorMsg(NoxEvent event) {
        return getErrorMsg(event.getStatus(), event.getMessage());
    }

    private String getErrorMsg(int code, String msg) {
        return getErrorMsg(String.valueOf(code), msg);
    }

    @Override
    public void finish(NoxEvent<KSUserEntity> noxEvent) {
        //当用户调用浮窗进行注销时，发送注销的广播
        Intent intent = new Intent(ACTION_LOGOUT + gameConfig.getGameId());
        gameConfig.getContext().sendBroadcast(intent);
    }
}
