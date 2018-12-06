package com.qinglan.sdk.android.platform;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.game.handler.LoginHandler;
import com.huawei.android.hms.agent.game.handler.SaveInfoHandler;
import com.huawei.android.hms.agent.hwid.handler.SignOutHandler;
import com.huawei.android.hms.agent.pay.PaySignUtil;
import com.huawei.android.hms.agent.pay.handler.PayHandler;
import com.huawei.hms.support.api.entity.game.GamePlayerInfo;
import com.huawei.hms.support.api.entity.game.GameStatusCodes;
import com.huawei.hms.support.api.entity.game.GameUserData;
import com.huawei.hms.support.api.entity.pay.PayReq;
import com.huawei.hms.support.api.entity.pay.PayStatusCodes;
import com.huawei.hms.support.api.hwid.SignOutResult;
import com.huawei.hms.support.api.pay.PayResultInfo;
import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.Config;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.net.HttpConstants;

import java.util.Map;

import static com.qinglan.sdk.android.net.HttpConstants.RESPONSE_CODE_UNKNOWN_ERROR;

public class HMSPlatform extends BasePlatform {
    private HMSHelper helper;

    @Override
    public void load(PlatformParamsReader.PlatformParam p, Config config) {
        super.load(p, config);
        HMSAgent.init((Application) config.getContext().getApplicationContext());
        if (helper == null)
            helper = new HMSHelper(Long.valueOf(gameConfig.getGameId()), getId(), gameConfig.getAppID(), gameConfig.getPublicKey(), gameConfig.getCpID());
    }

    @Override
    public void init(Activity activity, final Callback.OnInitConnectedListener listener) {
        HMSAgent.connect(activity, new ConnectHandler() {
            @Override
            public void onConnect(int rst) {
                Log.d("HMS connect end:" + rst);
                if (listener != null)
                    listener.initSuccess(null);
            }
        });
    }

    /**
     * 当登录成功时，LoginHandler会回调2次onResult方法，
     * | This method recalls 2 times when the login is successful.
     * 第1次：只回调了playerid；特点：速度快；在要求快速登录，并且对安全要求不高时可以用此playerid登录
     * | 1th time: Only callback playerID; features: fast speed; You can log in with this playerID when you require fast logon and are not high on security requirements
     * 第2次：回调了所有信息，userData.getIsAuth()为1；此时需要对登录结果进行验签
     * | 2nd time: Callback All information, Userdata.getisauth () is 1;
     */
    @Override
    public void login(final Activity activity, final Callback.OnLoginListener listener) {
        HMSAgent.Game.login((new LoginHandler() {
            @Override
            public void onResult(int retCode, final GameUserData userData) {
                if (retCode == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS && userData != null) {
                    Log.d("game login: onResult: retCode=" + retCode
                            + "  user=" + userData.getDisplayName() + "|" + userData.getPlayerId() + "|" + userData.getIsAuth() + "|" + userData.getPlayerLevel());

                    if (userData.getIsAuth() == 1) {
                        helper.verifyLogin(userData, new HMSHelper.OnVerifyRequestListener() {
                            @Override
                            public void onRequest(boolean success, int code, Map<String, Object> resMap) {
                                if (success) {
                                    if (listener != null)
                                        listener.loginSuccess(helper.getUserInfo(userData));
                                } else {
                                    if (listener != null)
                                        listener.loginFailed(getErrorMsg("login", code));
                                }
                            }
                        });
                    }
                } else {
                    Log.d("game login: onResult: retCode=" + retCode);
                    if (listener != null)
                        listener.loginFailed(getErrorMsg("login", retCode));
                }
            }

            @Override
            public void onChange() {
                // 此处帐号登录发生变化，需要重新登录 | Account login changed here, login required
                Log.d("game login: login changed!");
                login(activity, listener);
            }
        }), 1);
    }

    @Override
    public void logout(Activity activity, GameRole role, final Callback.OnLogoutResponseListener listener) {
//        HMSAgent.Hwid.signOut(new SignOutHandler() {
//            @Override
//            public void onResult(int rtnCode, SignOutResult result) {
//                if (rtnCode == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS && result != null) {
//                    Log.d("退出登录成功");
//                    if (listener != null)
//                        listener.onSuccess();
//                } else {
//                    Log.d("退出登录失败:" + rtnCode);
//                    if (listener != null)
//                        listener.onFailed(result.getStatus().getStatusMessage());
//                }
//            }
//        });
    }

    @Override
    public void showWinFloat(Activity activity) {
        HMSAgent.Game.showFloatWindow(activity);
    }

    @Override
    public void hideWinFloat(Activity activity) {
        HMSAgent.Game.hideFloatWindow(activity);
    }

    @Override
    public void exit(Activity activity, GameRole role, Callback.OnExitListener listener) {
        if (listener != null) {
            listener.onCompleted(true, "");
        }
    }

    @Override
    public void pay(Activity activity, GameRole role, GamePay pay, Map<String, Object> result, final Callback.OnPayRequestListener listener) {
        final String orderId = result.get(HttpConstants.RESPONSE_ORDER_ID).toString();
//        String url = result.get(HttpConstants.RESPONSE_NOTIFY_URL).toString();
        helper.createPayRequest(pay, orderId, /*url,*/ new HMSHelper.OnSignRequestListener() {
            @Override
            public void onRequest(boolean success, PayReq req) {
                if (!success) {
                    if (listener != null)
                        listener.onFailed("sign fail");
                    return;
                }
                HMSAgent.Pay.pay(req, new PayHandler() {
                    @Override
                    public void onResult(int retCode, PayResultInfo payInfo) {
                        if (retCode == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS && payInfo != null) {
                            boolean checkRst = PaySignUtil.checkSign(payInfo, gameConfig.getPublicKey());
                            Log.d("game pay: onResult: pay success and checksign=" + checkRst);
                            if (checkRst) {
                                // 支付成功并且验签成功，发放商品
                                if (listener != null)
                                    listener.onSuccess(payInfo.getOrderID());
                            } else {
                                // 验证签名失败，需要查询订单状态：对于没有服务器的单机应用，调用查询订单接口查询；其他应用到开发者服务器查询订单状态。
                                Log.e("sign error, pay fail:" + payInfo.getOrderID());
                                queryOrderStatus(orderId, listener);
                            }
                        } else if (retCode == HMSAgent.AgentResultCode.ON_ACTIVITY_RESULT_ERROR
                                || retCode == PayStatusCodes.PAY_STATE_TIME_OUT
                                || retCode == PayStatusCodes.PAY_STATE_NET_ERROR) {
                            // 需要查询订单状态：对于没有服务器的单机应用，调用查询订单接口查询；其他应用到开发者服务器查询订单状态。
                            queryOrderStatus(orderId, listener);
                        } else {
                            Log.d("game pay: onResult: pay fail=" + retCode);
                            if (listener != null)
                                listener.onFailed(getErrorMsg("pay", retCode));
                        }
                    }//onResult
                });
            }
        });
    }

    @Override
    public void createRole(final Activity activity, final GameRole role, long createTime, final Callback.OnGameRoleRequestListener listener) {
        selectRole(activity, false, role, createTime, listener);
    }

    @Override
    public void selectRole(final Activity activity, final boolean showFloat, final GameRole role, long createTime, final Callback.OnGameRoleRequestListener listener) {
        GamePlayerInfo playerInfo = helper.getPlayerInfo(role);
        HMSAgent.Game.savePlayerInfo(playerInfo, new SaveInfoHandler() {
            @Override
            public void onResult(int rst) {
                if (rst == GameStatusCodes.GAME_STATE_SUCCESS) {
                    listener.onSuccess(role);
                } else {
                    listener.onFailed(getErrorMsg("", rst));
                }
            }
        });
    }

    @Override
    public void levelUpdate(Activity activity, GameRole role, long createTime, final Callback.OnLevelUpListener listener) {
        GamePlayerInfo playerInfo = helper.getPlayerInfo(role);
        HMSAgent.Game.savePlayerInfo(playerInfo, new SaveInfoHandler() {
            @Override
            public void onResult(int rst) {
                if (rst == GameStatusCodes.GAME_STATE_SUCCESS) {
                    listener.onCompleted(true, "");
                } else {
                    listener.onCompleted(false, getErrorMsg("", rst));
                }
            }
        });
    }

    private String getErrorMsg(String request, int code) {
        return String.format("%s error: %s", request, code);
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
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void attachBaseContext(Context newBase) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }
}
