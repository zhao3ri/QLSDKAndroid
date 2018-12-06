package com.qinglan.sdk.android;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.qinglan.sdk.android.common.CachePreferences;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.PayRequest;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.platform.IPlatform;
import com.qinglan.sdk.android.utils.SDKUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zhaoj on 2018/10/17.
 */
class SDKPresenter implements IPresenter {
    private Context mContext;
    private IConnector iConnector;
    private IPlatform iPlatform;
    private String gameId;
    private boolean isLogged = false;//是否登录
    private boolean isHeartBeating = false;//心跳是否启动
    private long loginTime = 0;//登录时间戳
    private long createTime = 0;//创角时间戳

    private static final long HEART_RATE = 1000 * 60 * 20;//20分钟更新一次心跳
    private static final String HEART_BEAT_ACTION = "com.qinglan.sdk.android.HEART_BEAT_ACTION";
    private AlarmManager heartBeatManager;

    private static final String KEY_GAME_ROLE = "IPresenter.game_role";
    private BroadcastReceiver heartBeatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(HEART_BEAT_ACTION + getGameId())) {
                if (!isLogged) {
                    return;
                }
//                GameRole role = (GameRole) intent.getSerializableExtra(KEY_GAME_ROLE);
                GameRole role = GameRole.readJson(intent.getStringExtra(KEY_GAME_ROLE));
                if (role != null) {
                    Log.d("heartBeat!");
                    Log.d("Intent Extra:" + role.toString());
                    iConnector.startHeartBeat(mContext, role, String.valueOf(loginTime), null);
                }
            }
        }
    };

    public SDKPresenter(@NonNull Context context, String id, @NonNull IPlatform platform) {
        mContext = context;
        heartBeatManager = (AlarmManager) mContext.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        gameId = id;
        iPlatform = platform;
        iConnector = new SDKConnector(this);
    }

    @Override
    public void init(final Activity activity, final Callback.OnInitCompletedListener listener) {
        iPlatform.init(activity, new Callback.OnInitConnectedListener() {
            @Override
            public void initSuccess(UserInfo user) {
                if (user != null) {
                    saveUserInfo(user);
                }
                iConnector.initSdk(activity, listener);
            }

            @Override
            public void initFailed(String msg) {
                if (listener != null)
                    listener.onCompleted(false, msg);
            }
        });
    }

    @Override
    public void login(final Activity activity, final Callback.OnLoginResponseListener listener) {
        iPlatform.login(activity, new Callback.OnLoginListener() {
            @Override
            public void loginSuccess(final UserInfo userInfo) {
                //当平台登录成功后，调用SDK获取session
                iConnector.getToken(userInfo.getId(), new Callback.GetTokenListener() {
                    @Override
                    public void onFinished(boolean success, String result) {
                        //获取session成功，保存用户信息，并回调成功方法，否则回调失败方法
                        if (success) {
                            isLogged = true;
                            setSession(result);
                            if (userInfo != null)
                                saveUserInfo(userInfo);
                            if (listener != null)
                                listener.onSuccess(getUserInfo());
                        } else {
                            if (listener != null)
                                listener.onFailed(result);
                        }
                    }//onSuccess
                });
            }

            @Override
            public void loginFailed(String msg) {
                if (listener != null)
                    listener.onFailed(msg);
            }
        });
    }

    @Override
    public void enterGame(final Activity activity, final boolean showFloat, final GameRole gameRole, final Callback.OnGameStartedListener listener) {
        iConnector.refreshSession(activity, gameRole, new Callback.OnRefreshSessionListener() {
            @Override
            public void onRefreshed(boolean success, final long loginTimestamp, long createTimestamp, String result) {
                if (success) {//刷新SDK数据成功
                    loginTime = loginTimestamp;
                    createTime = createTimestamp;
                    iPlatform.selectRole(activity, showFloat, gameRole, createTimestamp, new Callback.OnGameRoleRequestListener() {
                        @Override
                        public void onSuccess(final GameRole role) {
                            if (!isHeartBeating) {//若当前心跳未启动
                                //启动心跳
                                startHeartBeat(role);
                                isHeartBeating = true;
                            }
                            if (listener != null)
                                listener.onGameStarted(loginTimestamp);
                            if (showFloat) {
                                iPlatform.showWinFloat(activity);
                            }
                        }

                        @Override
                        public void onFailed(String msg) {
                            if (listener != null)
                                listener.onFailed(msg);
                        }
                    });
                } else {
                    if (listener != null)
                        listener.onFailed(result);
                }
            }//onRefreshed
        });
    }

    private void startHeartBeat(GameRole role) {
        PendingIntent pi = getPendingIntent(role);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            heartBeatManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), HEART_RATE, pi);
        } else {
            heartBeatManager.setWindow(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), HEART_RATE, pi);
        }
    }

    private void stopHeartBeat(GameRole role) {
        heartBeatManager.cancel(getPendingIntent(role));
    }

    private PendingIntent getPendingIntent(GameRole role) {
        Intent intent = new Intent(HEART_BEAT_ACTION + getGameId());
        if (role != null) {
            Log.d(role.toString());
            //6.0之后使用getSerializable取不到值，换成json的形式传递数据
//            intent.putExtra(KEY_GAME_ROLE, role);
            intent.putExtra(KEY_GAME_ROLE, GameRole.toJson(role));
        }
        PendingIntent pi = PendingIntent
                .getBroadcast(mContext.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }

    @Override
    public void createRole(final Activity activity, final GameRole role, final Callback.OnCreateRoleFinishedListener listener) {
        iConnector.createRole(activity, role, new Callback.OnCreateRoleFinishedListener() {
            @Override
            public void onFinished(boolean success, final String result) {
                if (success) {
                    long timestamp = 0;
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        timestamp = jsonObject.optLong(HttpConstants.RESPONSE_CREATE_TIMESTAMP);
                    } catch (JSONException e) {
                        Log.e("get create time error.");
                        e.getMessage();
                    }
                    createTime = timestamp;
                    iPlatform.createRole(activity, role, createTime, new Callback.OnGameRoleRequestListener() {
                        @Override
                        public void onSuccess(GameRole role) {
                            if (listener != null)
                                listener.onFinished(true, result);
                        }

                        @Override
                        public void onFailed(String msg) {
                            if (listener != null)
                                listener.onFinished(false, msg);
                        }
                    });
                } else {
                    if (listener != null)
                        listener.onFinished(false, result);
                }
            }
        });

    }

    @Override
    public void logout(final Activity activity, final GameRole role, final Callback.OnLogoutResponseListener listener) {
        //发起平台注销请求
        iPlatform.logout(activity, role, new Callback.OnLogoutResponseListener() {
            @Override
            public void onSuccess() {
                //平台注销返回成功，发起SDK清除用户数据请求
                iConnector.cleanSession(activity, role, new Callback.OnLogoutResponseListener() {
                    @Override
                    public void onSuccess() {
                        //若成功，则更改和删除相关数据，回调成功接口方法
                        clear();
                        if (listener != null)
                            listener.onSuccess();
                    }

                    @Override
                    public void onFailed(String error) {
                        if (listener != null)
                            listener.onFailed(error);
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                if (listener != null)
                    listener.onFailed(error);
            }
        });
    }

    @Override
    public void exitGame(@NonNull final Activity activity, @NonNull final GameRole role, final Callback.OnExitListener listener) {
        exitGameWithTips(activity, role, listener, "退出游戏", "不多玩一会吗？", "取消", "确定");
    }

    @Override
    public void exitGameWithTips(@NonNull final Activity activity, final GameRole role, final Callback.OnExitListener listener, String title, String msg, String negativeButtonText, String positiveButtonText) {
        if (iPlatform.isCustomLogoutUI()) {//若平台有自定义的退出UI，直接调用退出方法
            exit(activity, role, listener);
            return;
        }
        new AlertDialog.Builder(activity).setTitle(title)
                .setMessage(msg)
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                exit(activity, role, listener);
                dialog.dismiss();
            }
        }).setCancelable(false).show();
    }

    private void exit(final Activity activity, final GameRole role, final Callback.OnExitListener listener) {
        iPlatform.exit(activity, role, new Callback.OnExitListener() {
            @Override
            public void onCompleted(boolean success, String msg) {
                if (success) {
                    if (TextUtils.isEmpty(getUserInfo().getId())) {
                        listener.onCompleted(true, msg);
                        return;
                    }
                    //当平台退出成功时，调用SDK的退出
                    iConnector.exit(activity, role, new Callback.OnExitListener() {
                        @Override
                        public void onCompleted(boolean success, String msg) {
                            //若平台退出成功，不管SDK最后请求结果如何都需要清除数据
                            clear();
                            listener.onCompleted(true, msg);
                        }
                    });
                } else {
                    if (listener != null)
                        listener.onCompleted(false, msg);
                }
            }
        });
    }

    @Override
    public void doPay(@NonNull final Activity activity, final PayRequest request, final Callback.OnPayRequestListener listener) {
        int fixed = 0;
        if (request.getAmount() > 0) {
            fixed = 1;
        }
        final GameRole game = getGameRole(request);
        final GamePay pay = getGamePay(request);
        iConnector.generateOrder(activity, game, pay, fixed, String.valueOf(loginTime), new Callback.GenerateOrderListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                request.setNotifyUrl(result.get(HttpConstants.RESPONSE_NOTIFY_URL).toString());
                iPlatform.pay(activity, game, pay, result, new Callback.OnPayRequestListener() {
                    @Override
                    public void onSuccess(String orderId) {
                        if (listener != null)
                            listener.onSuccess(orderId);
                    }

                    @Override
                    public void onFailed(String msg) {
                        if (listener != null)
                            listener.onFailed(msg);
                    }
                });
            }

            @Override
            public void onFailed(String msg) {
                if (listener != null)
                    listener.onFailed(msg);
            }
        });
    }

    private GameRole getGameRole(PayRequest pay) {
        GameRole role = new GameRole();
        role.setRoleId(pay.getRoleId());
        role.setRoleName(pay.getRoleName());
        role.setRoleLevel(pay.getRoleLevel());
        if (TextUtils.isEmpty(pay.getServerId())) {
            role.setServerId(String.valueOf(1));
        } else {
            role.setServerId(pay.getServerId());
        }
        role.setZoneId(pay.getZoneId());
        role.setZoneName(pay.getZoneName());
        return role;
    }

    private GamePay getGamePay(PayRequest request) {
        GamePay pay = new GamePay();
        pay.setNotifyUrl(request.getNotifyUrl());
        pay.setAmount(request.getAmount());
        pay.setProductId(request.getProductId());
        pay.setProductCount(request.getProductCount());
        pay.setProductName(request.getProductName());
        return pay;
    }

    @Override
    public void levelUpdate(Activity activity, GameRole role) {
        iPlatform.levelUpdate(activity, role, createTime, new Callback.OnLevelUpListener() {
            @Override
            public void onCompleted(boolean success, String msg) {
                Log.d("level up->success: " + success + ",msg: " + msg);
            }
        });
    }

    @Override
    public String getGameId() {
        if (TextUtils.isEmpty(gameId)) {
            gameId = SDKUtils.getGameId(mContext.getApplicationContext());
        }
        return gameId;
    }

    @Override
    public int getPlatformId() {
        return iPlatform.getId();
    }

    @Override
    public String getPlatformName() {
        return iPlatform.getName();
    }

    @Override
    public void showFloat(Activity activity) {
        iPlatform.showWinFloat(activity);
    }

    @Override
    public void hideFloat(Activity activity) {
        iPlatform.hideWinFloat(activity);
    }

    @Override
    public void onCreate(Activity activity) {
        iPlatform.onCreate(activity);
    }

    @Override
    public void onStart(Activity activity) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HEART_BEAT_ACTION + getGameId());
        activity.registerReceiver(heartBeatReceiver, intentFilter);
        iPlatform.onStart(activity);
    }

    @Override
    public void onResume(Activity activity) {
        iPlatform.onResume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        iPlatform.onPause(activity);
    }

    @Override
    public void onStop(Activity activity) {
        iPlatform.onStop(activity);
    }

    @Override
    public void onDestroy(Activity activity) {
        activity.unregisterReceiver(heartBeatReceiver);
        iPlatform.onDestroy(activity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        iPlatform.onActivityResult(activity, requestCode, resultCode, data);
    }

    @Override
    public void onNewIntent(Intent intent) {
        iPlatform.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        iPlatform.onBackPressed();
    }

    @Override
    public void attachBaseContext(Context newBase) {
        iPlatform.attachBaseContext(newBase);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        iPlatform.onConfigurationChanged(newConfig);
    }

    @Override
    public void saveUserInfo(UserInfo user) {
        UserPreferences.saveUserInfo(mContext, user);
        iPlatform.setUser(user);
        Log.d(getUserInfo().toString());
    }

    @Override
    public UserInfo getUserInfo() {
        UserInfo user = UserPreferences.getUserInfo(mContext);
        return user;
    }

    @Override
    public String getUid() {
        return UserPreferences.get(mContext, UserPreferences.KEY_UID, "");
    }

    @Override
    public void setSession(String session) {
        UserPreferences.put(mContext, UserPreferences.KEY_SESSION_ID, session);
    }

    @Override
    public void clear() {
        isLogged = false;
        isHeartBeating = false;
        loginTime = 0;
        createTime = 0;
        UserPreferences.clear(mContext);
        stopHeartBeat(null);
        Log.d("User===" + getUserInfo().toString());
    }

    private static final String KEY_DEVICE_ID = "deviceId";
    private static final String KEY_CACHE_NAME = "cache_data";

    @Override
    public String getDeviceId() {
        String deviceId = CachePreferences.get(mContext, KEY_CACHE_NAME, KEY_DEVICE_ID, "");
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Utils.getDeviceId(mContext);
            //保存device id到data里，避免取不到手机的设备号时每次都返回不同的随机码
            CachePreferences.put(mContext, KEY_CACHE_NAME, KEY_DEVICE_ID, deviceId);
        }
        return deviceId;
    }

}
