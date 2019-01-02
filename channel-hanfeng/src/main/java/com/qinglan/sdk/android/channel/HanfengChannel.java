package com.qinglan.sdk.android.channel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.channel.entity.HanfengVerifyRequest;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.BaseResult;
import com.qinglan.sdk.android.net.HttpConnectionTask;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.net.OnResponseListener;

import java.util.Map;

import hf.sdk.shell.callback.IHfSdkListener;
import hf.sdk.shell.proxy.HfCommonSdk;
import hf.sdk.shell.proxy.HfPayParams;
import hf.sdk.shell.proxy.HfUserExtraData;

import static com.qinglan.sdk.android.net.HttpConstants.RESPONSE_CODE_SUCCESS;

public class HanfengChannel extends BaseChannel implements IHfSdkListener {
    private static final String RESULT_USER_ID = "userId";

    private Callback.OnInitConnectedListener onInitConnectedListener;
    private Callback.OnLoginListener onLoginListener;
    private Callback.OnLogoutResponseListener onLogoutResponseListener;
    private Callback.OnExitListener onExitListener;
    private Callback.OnPayRequestListener onPayRequestListener;

    private String orderId;

    @Override
    public void load(ChannelParamsReader.ChannelParam p, Config config) {
        super.load(p, config);
        HfCommonSdk.getInstance().onApplicationCreate((Application) config.getContext().getApplicationContext());
    }

    @Override
    public void init(Activity activity, Callback.OnInitConnectedListener listener) {
        HfCommonSdk.getInstance().hfInit(activity, this);
    }

    @Override
    public void login(Activity activity, Callback.OnLoginListener listener) {
        onLoginListener = listener;
        HfCommonSdk.getInstance().hfLogin(activity);
    }

    @Override
    public void logout(Activity activity, GameRole role, Callback.OnLogoutResponseListener listener) {
        onLogoutResponseListener = listener;
        // 登出(注销) 传false. sdk会回调 IHfSdkListener 的onLogout方法,回调参数为false.等待收到回调后再退出游戏场景即可
        HfCommonSdk.getInstance().hfLogout(activity, false);
        HfCommonSdk.getInstance().hfSubmitData(activity, createRoleInfo(role, 0, HfUserExtraData.TYPE_EXIT_GAME));
    }

    @Override
    public void showWinFloat(Activity activity) {

    }

    @Override
    public void hideWinFloat(Activity activity) {

    }

    @Override
    public void exit(Activity activity, GameRole role, Callback.OnExitListener listener) {
        onExitListener = listener;
        HfCommonSdk.getInstance().hfExit(activity);
        HfCommonSdk.getInstance().hfSubmitData(activity, createRoleInfo(role, 0, HfUserExtraData.TYPE_EXIT_GAME));
    }

    @Override
    public void pay(Activity activity, GameRole role, GamePay pay, Map<String, Object> result, Callback.OnPayRequestListener listener) {
        onPayRequestListener = listener;
        orderId = result.get(HttpConstants.RESPONSE_ORDER_ID).toString();
        HfCommonSdk.getInstance().hfPay(activity, createPayParams(orderId, role, pay));
    }

    private HfPayParams createPayParams(String orderId, GameRole role, GamePay pay) {
        HfPayParams payParams = new HfPayParams();
        payParams.setHfUid(getUserId());// 这里填验证后返回的userId
        payParams.setBuyNum(pay.getGoodsCount());// 一般只买一份
        payParams.setPer_price((int) pay.getAmount());// 单位为分, 但只支持元为单位,所以传100的整数.
        payParams.setTotalPrice((int) (pay.getAmount() * pay.getGoodsCount())); /** 总价格 = 购买份数 * 单价 */
        payParams.setExtension(pay.getExtInfo());
        payParams.setOrderId(orderId);// cp的订单id
        payParams.setProductDesc(pay.getGoodsName());
        payParams.setProductId(pay.getGoodsId());
        payParams.setProductName(pay.getGoodsName());
        payParams.setRoleId(role.getRoleId());
        payParams.setRoleLevel(Integer.valueOf(role.getRoleLevel()));
        payParams.setRoleName(role.getRoleName());
        payParams.setServerId(Integer.valueOf(role.getZoneId()));
        payParams.setServerName(role.getZoneName());
//        payParams.setTime(System.currentTimeMillis());// 13位毫秒
        return payParams;
    }

    @Override
    public void createRole(Activity activity, GameRole role, long createTime, Callback.OnGameRoleRequestListener listener) {
        HfCommonSdk.getInstance().hfSubmitData(activity, createRoleInfo(role, createTime, HfUserExtraData.TYPE_CREATE_ROLE));
        if (listener != null)
            listener.onSuccess(role);
    }

    @Override
    public void selectRole(Activity activity, boolean showFloat, GameRole role, long createTime, Callback.OnGameRoleRequestListener listener) {
        HfCommonSdk.getInstance().hfSubmitData(activity, createRoleInfo(role, createTime, HfUserExtraData.TYPE_ENTER_GAME));
        if (listener != null)
            listener.onSuccess(role);
    }

    @Override
    public void levelUpdate(Activity activity, GameRole role, long createTime, Callback.OnLevelUpListener listener) {
        HfCommonSdk.getInstance().hfSubmitData(activity, createRoleInfo(role, createTime, HfUserExtraData.TYPE_LEVEL_UP));
        if (listener != null)
            listener.onFinished(true, "");
    }

    private HfUserExtraData createRoleInfo(GameRole role, long createTime, int dataType) {
        HfUserExtraData userExtraData = new HfUserExtraData();
        userExtraData.setDataType(dataType);
        userExtraData.setHfUid(getUserId());
        userExtraData.setServerId(Integer.valueOf(role.getZoneId()));
        userExtraData.setServerName(role.getZoneName());
        userExtraData.setRoleId(role.getRoleId());
        userExtraData.setRoleName(role.getRoleName());
        userExtraData.setRoleLevel(Integer.valueOf(role.getRoleLevel()));
        if (createTime != 0) {
            userExtraData.setRoleCreateTime((int) (createTime / 1000));//10位 秒
        }
        return userExtraData;
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
        HfCommonSdk.getInstance().onStart(activity);
    }

    @Override
    public void onRestart(Activity activity) {
        HfCommonSdk.getInstance().onRestart(activity);
    }

    @Override
    public void onResume(Activity activity) {
        HfCommonSdk.getInstance().onResume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        HfCommonSdk.getInstance().onPause(activity);
    }

    @Override
    public void onStop(Activity activity) {
        HfCommonSdk.getInstance().onStop(activity);
    }

    @Override
    public void onDestroy(Activity activity) {
        HfCommonSdk.getInstance().onDestroy(activity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        HfCommonSdk.getInstance().onActivityResult(activity, requestCode, resultCode, data);
    }

    @Override
    public void onNewIntent(Activity activity, Intent intent) {
        HfCommonSdk.getInstance().onNewIntent(activity, intent);
    }

    @Override
    public void onBackPressed(Activity activity) {

    }

    @Override
    public void attachBaseContext(Context newBase) {

    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        HfCommonSdk.getInstance().onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
    }

    @Override
    public void onWindowFocusChanged(Activity activity, boolean hasFocus) {
        HfCommonSdk.getInstance().onWindowFocusChanged(activity, hasFocus);
    }

    @Override
    public void onApplicationAttachBaseContext(Application app, Context base) {
        HfCommonSdk.getInstance().onApplicationAttachBaseContextInApplication(app, base);
    }

    @Override
    public void onApplicationConfiguration(Application app, Configuration newConfig) {
        HfCommonSdk.getInstance().onApplicationConfigurationChanged(app, newConfig);
    }

    @Override
    public void onApplicationTerminate(Application app) {
        HfCommonSdk.getInstance().onApplicationTerminate(app);
    }

    @Override
    public void onConfigurationChanged(Activity activity, Configuration newConfig) {
        HfCommonSdk.getInstance().onConfigurationChanged(activity, newConfig);
    }

    @Override
    public void initSuc(String s) {
        if (onInitConnectedListener != null)
            onInitConnectedListener.onSuccess(null);
    }

    @Override
    public void initFailed(String s) {
        if (onInitConnectedListener != null)
            onInitConnectedListener.onFailed(s);
    }

    @Override
    public void onLoginSuc(String data) {
        HanfengVerifyRequest request = Utils.jsonWithoutExpose2Object(data, HanfengVerifyRequest.class);
        verify(request);
    }

    private void verify(final HanfengVerifyRequest request) {
        if (request == null) {
            if (onLoginListener != null)
                onLoginListener.onFailed("");
            return;
        }
        request.channelId = getId();
        request.gameId = gameConfig.getGameId();
        new HttpConnectionTask().setResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(boolean success, String result) {
                if (success && !TextUtils.isEmpty(result) && getResult(result) != null) {
                    BaseResult res = getResult(result);
                    if (res.code == RESPONSE_CODE_SUCCESS) {
                        if (res.data != null && res.data.get(RESULT_USER_ID) != null) {
                            String userId = String.valueOf(res.data.get(RESULT_USER_ID));
                            if (onLoginListener != null) {
                                onLoginListener.onSuccess(getUser(userId, request));
                            }
                        }
                    } else {
                        if (onLoginListener != null)
                            onLoginListener.onFailed(getErrorMsg(res.code + "", res.msg));
                    }
                } else {
                    if (onLoginListener != null)
                        onLoginListener.onFailed(result);
                }
            }
        }).execute(request);
    }

    private UserInfo getUser(String userId, HanfengVerifyRequest request) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setUdToken(request.sid);
        return userInfo;
    }

    @Override
    public void onLoginFailed(String reason) {
        if (onLoginListener != null)
            onLoginListener.onFailed(reason);
    }

    @Override
    public void onLogout(boolean shouldRelogin) {
        if (shouldRelogin) {
            // 1.退出游戏场景,回到游戏主界面。
            // 2.重新调起sdk登录
        } else {
            // 1 退出游戏场景,回到游戏主界面。
            // 2 不需主动再调用sdk登录, 但需要有用户点击主界面能调起sdk登录的功能.
        }
        if (onLogoutResponseListener != null) {
            onLogoutResponseListener.onSuccess();
        }
    }

    @Override
    public void onPaySuc(String order) {
        Log.d("order==" + order);
        //支付回调成功查询订单状态
        queryOrderStatus(orderId, onPayRequestListener);
        orderId = null;
    }

    @Override
    public void onPayFail(String extension) {
        if (onPayRequestListener != null)
            onPayRequestListener.onFailed(extension);
    }

    @Override
    public void onExit() {
        if (onExitListener != null)
            onExitListener.onFinished(true, "");
    }

    @Override
    public void onResult(int i, String s) {

    }
}
