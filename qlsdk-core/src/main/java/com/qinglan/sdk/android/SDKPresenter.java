package com.qinglan.sdk.android;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
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

import com.qinglan.sdk.android.base.IConnector;
import com.qinglan.sdk.android.base.IPresenter;
import com.qinglan.sdk.android.common.CachePreferences;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.common.Utils;
import com.qinglan.sdk.android.model.GamePay;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.net.HttpConstants;
import com.qinglan.sdk.android.channel.IChannel;
import com.qinglan.sdk.android.utils.SDKUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.qinglan.sdk.android.Constants.ACTION_EXIT;
import static com.qinglan.sdk.android.Constants.ACTION_HEART_BEAT;
import static com.qinglan.sdk.android.Constants.ACTION_LOGOUT;
import static com.qinglan.sdk.android.Constants.KEY_GAME_ROLE;

/**
 * Created by zhaoj on 2018/10/17.
 */
class SDKPresenter implements IPresenter {
    private Context mContext;
    private IConnector iConnector;
    private IChannel iChannel;
    private String mGameId;
    private boolean isLogged = false;//是否登录
    private boolean isHeartBeating = false;//心跳是否启动
    private GameRole mRole;
    private long mGameStartTime = 0;//游戏开始时间戳
    private long mRoleCreateTime = 0;//角色创建时间戳

    private AlarmManager heartBeatManager;
    private static final long HEART_RATE = 1000 * 60 * 20;//20分钟更新一次心跳

    private BroadcastReceiver SDKReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_HEART_BEAT + getGameId())) {
                if (!isLogged) {
                    return;
                }
//                GameRole role = (GameRole) intent.getSerializableExtra(KEY_GAME_ROLE);
                GameRole role = mRole;
                if (role == null)
                    role = GameRole.readJson(intent.getStringExtra(KEY_GAME_ROLE));
                if (role != null) {
                    Log.d("heartBeat!");
                    Log.d("Intent Extra:" + role.toString());
                    iConnector.startHeartBeat(mContext, role, String.valueOf(mGameStartTime), null);
                }
            } else if (intent.getAction().equals(ACTION_LOGOUT + getGameId())) {
                iConnector.cleanSession(mContext, mRole, null);
            } else if (intent.getAction().equals(ACTION_EXIT + getGameId())) {
                iConnector.exit(mContext, mRole, null);
            }
        }
    };

    public SDKPresenter(@NonNull Context context, String id, @NonNull IChannel platform) {
        mContext = context;
        heartBeatManager = (AlarmManager) mContext.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mGameId = id;
        iChannel = platform;
        iConnector = new SDKConnector(this);
    }

    @Override
    public void init(final Activity activity, final Callback.OnInitCompletedListener listener) {
        iChannel.init(activity, new Callback.OnInitConnectedListener() {
            @Override
            public void onSuccess(UserInfo user) {
                if (user != null) {
                    saveUserInfo(user);
                }
                iConnector.initSdk(activity, listener);
            }

            @Override
            public void onFailed(String msg) {
                if (listener != null)
                    listener.onFinished(false, msg);
            }
        });
    }

    @Override
    public void login(final Activity activity, final Callback.OnLoginResponseListener listener) {
        iChannel.login(activity, new Callback.OnLoginListener() {
            @Override
            public void onSuccess(final UserInfo userInfo) {
                //当渠道登录成功后，调用SDK获取session
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
                            onFailedResponse(listener, result);
                        }
                    }//onSuccess
                });
            }

            @Override
            public void onFailed(String msg) {
                onFailedResponse(listener, msg);
            }
        });
    }

    @Override
    public void enterGame(final Activity activity, final boolean showFloat, final GameRole gameRole, final Callback.OnGameStartedListener listener) {
        iConnector.gameStart(activity, gameRole, new Callback.OnGameStartResponseListener() {
            @Override
            public void onResult(boolean success, final long startTimestamp, long createTimestamp, String result) {
                if (success) {//刷新SDK数据成功
                    mGameStartTime = startTimestamp;
                    mRoleCreateTime = createTimestamp;
                    iChannel.selectRole(activity, gameRole, createTimestamp, new Callback.OnGameRoleRequestListener() {
                        @Override
                        public void onSuccess(final GameRole role) {
                            updateRole(role);
                            if (!isHeartBeating) {//若当前心跳未启动
                                //启动心跳
                                startHeartBeat(role);
                                isHeartBeating = true;
                            }
                            if (listener != null)
                                listener.onSuccess(startTimestamp);
                            if (showFloat) {
                                iChannel.showWinFloat(activity);
                            }
                        }

                        @Override
                        public void onFailed(String msg) {
                            onFailedResponse(listener, msg);
                        }
                    });
                } else {
                    onFailedResponse(listener, result);
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
        Intent intent = new Intent(ACTION_HEART_BEAT + getGameId());
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
    public void createRole(final Activity activity, final GameRole role, final Callback.OnCreateRoleListener listener) {
        iConnector.createRole(activity, role, new Callback.OnCreateRoleListener() {
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
                    mRoleCreateTime = timestamp;
                    iChannel.createRole(activity, role, mRoleCreateTime, new Callback.OnGameRoleRequestListener() {
                        @Override
                        public void onSuccess(GameRole role) {
                            updateRole(role);
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
    public void logout(final Activity activity, final Callback.OnLogoutResponseListener listener) {
        //发起平台注销请求
        iChannel.logout(activity, mRole, new Callback.OnLogoutResponseListener() {
            @Override
            public void onSuccess() {
                //平台注销返回成功，发起SDK清除用户数据请求
                iConnector.cleanSession(activity, mRole, new Callback.OnLogoutResponseListener() {
                    @Override
                    public void onSuccess() {
                        //若成功，则更改和删除相关数据，回调成功接口方法
                        clear();
                        if (listener != null)
                            listener.onSuccess();
                    }

                    @Override
                    public void onFailed(String error) {
                        onFailedResponse(listener, error);
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                onFailedResponse(listener, error);
            }
        });
    }

    @Override
    public void exitGame(@NonNull final Activity activity, final Callback.OnExitListener listener) {
        exitGameWithTips(activity, listener, "退出游戏", "不多玩一会吗？", "取消", "确定");
    }

    @Override
    public void exitGameWithTips(@NonNull final Activity activity, final Callback.OnExitListener listener, String title, String msg, String negativeButtonText, String positiveButtonText) {
        if (iChannel.isCustomLogoutUI()) {//若平台有自定义的退出UI，直接调用退出方法
            exit(activity, listener);
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
                exit(activity, listener);
                dialog.dismiss();
            }
        }).setCancelable(false).show();
    }

    private void exit(final Activity activity, final Callback.OnExitListener listener) {
        iChannel.exit(activity, mRole, new Callback.OnExitListener() {
            @Override
            public void onFinished(boolean success, String msg) {
                if (success) {
                    if (TextUtils.isEmpty(getUserInfo().getId())) {
                        if (listener != null)
                            listener.onFinished(true, msg);
                        return;
                    }
                    //当平台退出成功时，调用SDK的退出
                    iConnector.exit(activity, mRole, new Callback.OnExitListener() {
                        @Override
                        public void onFinished(boolean success, String msg) {
                            //若平台退出成功，不管SDK最后请求结果如何都需要清除数据
                            clear();
                            if (listener != null)
                                listener.onFinished(true, msg);
                        }
                    });
                } else {
                    if (listener != null)
                        listener.onFinished(false, msg);
                }
            }
        });
    }

    @Override
    public void doPay(@NonNull final Activity activity, final GamePay pay, final Callback.OnPayRequestListener listener) {
        int fixed = 0;
        if (pay.getAmount() > 0) {
            fixed = 1;
        }
        iConnector.generateOrder(activity, mRole, pay, fixed, String.valueOf(mGameStartTime), new Callback.GenerateOrderListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                pay.setNotifyUrl(result.get(HttpConstants.RESPONSE_NOTIFY_URL).toString());
                iChannel.pay(activity, mRole, pay, result, new Callback.OnPayRequestListener() {
                    @Override
                    public void onSuccess(String orderId) {
                        if (listener != null)
                            listener.onSuccess(orderId);
                    }

                    @Override
                    public void onFailed(String msg) {
                        onFailedResponse(listener, msg);
                    }
                });
            }

            @Override
            public void onFailed(String msg) {
                onFailedResponse(listener, msg);
            }
        });
    }

    @Override
    public void levelUpdate(Activity activity, final GameRole role) {
        iChannel.levelUpdate(activity, role, mRoleCreateTime, new Callback.OnLevelUpListener() {
            @Override
            public void onFinished(boolean success, String msg) {
                updateRole(role);
                Log.d("level up->success: " + success + ",msg: " + msg);
            }
        });
    }

    @Override
    public String getGameId() {
        if (TextUtils.isEmpty(mGameId)) {
            mGameId = SDKUtils.getGameId(mContext.getApplicationContext());
        }
        return mGameId;
    }

    private void updateRole(GameRole gameRole) {
        if (gameRole == null) {
            gameRole = new GameRole();
        }
        mRole = gameRole;
        if (TextUtils.isEmpty(mRole.getServerId())) {
            mRole.setServerId(String.valueOf(1));
        }
    }

    @Override
    public int getChannelId() {
        return iChannel.getId();
    }

    @Override
    public String getChannelName() {
        return iChannel.getName();
    }

    @Override
    public void showFloat(Activity activity) {
        iChannel.showWinFloat(activity);
    }

    @Override
    public void hideFloat(Activity activity) {
        iChannel.hideWinFloat(activity);
    }

    @Override
    public void onCreate(Activity activity) {
        iChannel.onCreate(activity);
    }

    @Override
    public void onStart(Activity activity) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_HEART_BEAT + getGameId());
        intentFilter.addAction(ACTION_LOGOUT + getGameId());
        intentFilter.addAction(ACTION_EXIT + getGameId());
        activity.registerReceiver(SDKReceiver, intentFilter);
        iChannel.onStart(activity);
    }

    @Override
    public void onRestart(Activity activity) {
        iChannel.onRestart(activity);
    }

    @Override
    public void onResume(Activity activity) {
        iChannel.onResume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        iChannel.onPause(activity);
    }

    @Override
    public void onStop(Activity activity) {
        iChannel.onStop(activity);
    }

    @Override
    public void onDestroy(Activity activity) {
        activity.unregisterReceiver(SDKReceiver);
        iChannel.onDestroy(activity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        iChannel.onActivityResult(activity, requestCode, resultCode, data);
    }

    @Override
    public void onNewIntent(Activity activity, Intent intent) {
        iChannel.onNewIntent(activity, intent);
    }

    @Override
    public void onBackPressed(Activity activity) {
        iChannel.onBackPressed(activity);
    }

    @Override
    public void attachBaseContext(Context newBase) {
        iChannel.attachBaseContext(newBase);
    }

    @Override
    public void onConfigurationChanged(Activity activity, Configuration newConfig) {
        iChannel.onConfigurationChanged(activity, newConfig);
    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        iChannel.onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
    }

    @Override
    public void onWindowFocusChanged(Activity activity, boolean hasFocus) {
        iChannel.onWindowFocusChanged(activity, hasFocus);
    }

    @Override
    public void onApplicationAttachBaseContext(Application app, Context base) {
        iChannel.onApplicationAttachBaseContext(app, base);
    }

    @Override
    public void onApplicationConfiguration(Application app, Configuration newConfig) {
        iChannel.onApplicationConfiguration(app, newConfig);
    }

    @Override
    public void onApplicationTerminate(Application app) {
        iChannel.onApplicationTerminate(app);
    }

    @Override
    public void saveUserInfo(UserInfo user) {
        UserPreferences.saveUserInfo(mContext, user);
        iChannel.setUser(user);
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
        mGameStartTime = 0;
        mRoleCreateTime = 0;
        mRole = null;
        UserPreferences.clear(mContext);
        stopHeartBeat(null);
        Log.d("User===" + getUserInfo().toString());
    }

    private void onFailedResponse(Callback.DefaultResponseListener listener, String msg) {
        if (listener != null)
            listener.onFailed(msg);
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
