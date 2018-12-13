package com.qinglan.sdk.android.demo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.qinglan.sdk.android.Callback;
import com.qinglan.sdk.android.PermissionActivity;
import com.qinglan.sdk.android.QLSDK;
import com.qinglan.sdk.android.common.Log;
import com.qinglan.sdk.android.model.GameRole;
import com.qinglan.sdk.android.model.UserInfo;
import com.qinglan.sdk.android.utils.PermissionUtils;
import com.qinglan.sdk.android.utils.ToastUtils;


public class WelComeActivity extends PermissionActivity implements OnClickListener {
    private static final String TAG = "WelComeActivity";
    private boolean boo = true; //处理注销后，返回登录界面，返回键去掉登录框，然后悬浮框会显示的瑕疵
    private LoadingDialog mDialog; //不是必须实现的效果

    private QLSDK qlSDK;

    private RelativeLayout welcomePage, mainPage, roleSet;
    private GameRole gameRole;
    private EditText mMonnyEdit, edRoleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        qlSDK = QLSDK.getInstance();
        qlSDK.onCreate(this);

        welcomePage = (RelativeLayout) findViewById(R.id.welcome_page);
        roleSet = (RelativeLayout) findViewById(R.id.role_set);
        mainPage = (RelativeLayout) findViewById(R.id.main_page);
        findViewById(R.id.doneCusompay).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
        findViewById(R.id.doneNotes).setOnClickListener(this);
        findViewById(R.id.item_begin).setOnClickListener(this);
        findViewById(R.id.item_create).setOnClickListener(this);
        findViewById(R.id.levelUpdate).setOnClickListener(this);
        edRoleName = (EditText) findViewById(R.id.role_name);
        mMonnyEdit = (EditText) findViewById(R.id.customPaytEdit);

        checkPermission();
    }

//    private void setupGamRole() {
//        gameRole = new GameRole();
//        gameRole.setRoleId("2333"); //必须传这个参数，没有传"0" (凡是涉及到这个对象gameInfo的所有api的实现，都要传RoleId这个参数)
//        gameRole.setRoleLevel("11");
//        gameRole.setZoneId("1234");
//        gameRole.setZoneName("我就是用来测试的啊");
//        gameRole.setServerId("1");
//    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        if (checkPermission(PermissionUtils.Permission.READ_PHONE_STATE)
                && checkPermission(PermissionUtils.Permission.WRITE_EXTERNAL_STORAGE)) {
            //若当前已获得权限，则做以下操作
            init();
        } else {
            //否则将请求获取权限
            requestPermission(PermissionUtils.Permission.READ_PHONE_STATE, PermissionUtils.Permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onPermissionGranted(PermissionUtils.Permission... p) {
        //当申请获得权限成功时的操作
        init();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.doneCusompay) {
            if (TextUtils.isEmpty(mMonnyEdit.getText())) {
                ToastUtils.showToast(this, "请输入金额");
                return;
            }
            pay();
        } else if (v.getId() == R.id.logout) {
            logout();
        } else if (v.getId() == R.id.exit) {
            exit();
        }
        if (v.getId() == R.id.item_begin) {
            // 提示，这个动作比较重要，这个涉及后面sdk的支付api是否能够正常使用
            // 开始游戏
            if (gameRole == null) {
                showDialog(false);
            } else {
                startGame();
            }
        } else if (v.getId() == R.id.item_create) {
            // 提示，这个动作比较重要，这个涉及后面sdk的支付api是否能够正常使用
            // 创建角色
            showDialog(true);
        } else if (v.getId() == R.id.levelUpdate) {
            //等级升级(如果不是角色升级的游戏，可以不实现这个方法)
            qlSDK.levelUpdate(this, gameRole);
        }

    }

    /**
     * 平台初始化
     */
    private void init() {
        qlSDK.initPlatform(this, new Callback.OnInitCompletedListener() {
            @Override
            public void onFinished(boolean success, String result) {
                //初始化完成后的操作
                if (success) {
                    login();
                } else {
                    ToastUtils.showToast(WelComeActivity.this, result);
                }
            }
        });
    }

    /**
     * 平台登录
     */
    private void login() {
        qlSDK.login(this, new Callback.OnLoginResponseListener() {
            @Override
            public void onSuccess(UserInfo user) {
                if (user != null) {
                    Log.d("id==" + user.getId() + ",name==" + user.getUserName() + ",session==" + user.getSessionId());
                }
                boo = true;

                roleSet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(String error) {
                ToastUtils.showToast(WelComeActivity.this, error);
            }
        });
    }

    /**
     * 开始游戏
     */
    private void startGame() {
        qlSDK.enterGame(this, true, gameRole, new Callback.OnGameStartedListener() {
            @Override
            public void onSuccess(long timestamp) {
                Log.d(TAG, "time==" + timestamp);
            }

            @Override
            public void onFailed(String result) {
                ToastUtils.showToast(WelComeActivity.this, result);
            }
        });
        welcomePage.setVisibility(View.GONE);
        roleSet.setVisibility(View.GONE);
        mainPage.setVisibility(View.VISIBLE);
    }

    /**
     * 创建角色
     */
    private void createRole() {
        mDialog = new LoadingDialog(this, "创建中……");
        mDialog.show();
        gameRole.setRoleName(edRoleName.getText().toString().trim());
        qlSDK.createGameRole(this, gameRole, new Callback.OnCreateRoleListener() {
            @Override
            public void onFinished(boolean success, String result) {
                if (mDialog.isShowing()) {
                    mDialog.cancel();
                }
                if (success) {
                    ToastUtils.showToast(WelComeActivity.this, "创建角色成功！");
                } else {
                    ToastUtils.showToast(WelComeActivity.this, result);
                }
            }
        });
    }

    private void showDialog(final boolean isCreate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.view_role_input, null);
        builder.setView(view);
        builder.setCancelable(true);
        final EditText etRoleId = (EditText) view.findViewById(R.id.et_role_id);
        final EditText etRoleName = (EditText) view.findViewById(R.id.et_role_name);
        final EditText etRoleLevel = (EditText) view.findViewById(R.id.et_role_level);
        final EditText etZoneId = (EditText) view.findViewById(R.id.et_zone_id);
        final EditText etZoneName = (EditText) view.findViewById(R.id.et_zone_name);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        //取消或确定按钮监听事件处理
        final AlertDialog dialog = builder.create();
        dialog.show();
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        if (gameRole != null) {
            etRoleId.setText(gameRole.getRoleId());
            etRoleName.setText(gameRole.getRoleName());
            etRoleLevel.setText(gameRole.getRoleLevel());
            etZoneId.setText(gameRole.getZoneId());
            etZoneName.setText(gameRole.getZoneName());
        }
        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etRoleId.getText().toString().trim())) {
                    ToastUtils.showToast(WelComeActivity.this, "角色id不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(etRoleName.getText().toString().trim())) {
                    ToastUtils.showToast(WelComeActivity.this, "角色名称不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(etZoneId.getText().toString().trim())) {
//                    ToastUtils.showToast(WelComeActivity.this, "分区id不能为空！");
                    etZoneId.setText("1234");
                }
                if (TextUtils.isEmpty(etZoneName.getText().toString().trim())) {
//                    ToastUtils.showToast(WelComeActivity.this, "分区名称不能为空！");
                    etZoneName.setText("Null");
                }
                gameRole = new GameRole();
                gameRole.setRoleId(etRoleId.getText().toString());
                gameRole.setRoleName(etRoleName.getText().toString());
                String level = TextUtils.isEmpty(etRoleLevel.getText().toString().trim()) ? "0" : etRoleLevel.getText().toString();
                gameRole.setRoleLevel(level);
                gameRole.setZoneId(etZoneId.getText().toString());
                gameRole.setZoneName(etZoneName.getText().toString());
                edRoleName.setText(etRoleName.getText().toString());
                if (isCreate) {
                    createRole();
                }
                dialog.cancel();
            }
        });
    }

    /**
     * 注销登录
     */
    private void logout() {
        qlSDK.logout(this, gameRole, new Callback.OnLogoutResponseListener() {
            @Override
            public void onSuccess() {
                boo = false;
                welcomePage.setVisibility(View.VISIBLE);
                mainPage.setVisibility(View.GONE);
                qlSDK.hideWinFloat(WelComeActivity.this);
                login();
                gameRole = null;
            }

            @Override
            public void onFailed(String error) {
                ToastUtils.showToast(WelComeActivity.this, error);
            }
        });
    }

    /**
     * 退出游戏
     */
    private void exit() {
        //yxf平台退出无回调
        qlSDK.exit(this, gameRole, new Callback.OnExitListener() {
            @Override
            public void onFinished(boolean success, String msg) {
                finish();
                System.exit(0);
            }
        });
    }

    /**
     * 支付
     */
    private void pay() {
        //提示，要按照正常游戏的步骤，创建角色，上传角色信息，开始游戏，进入到游戏主界面，再去调用支付api，不然会出问题
        //自定义充值(回调地址，你们自己设置，不需要我们后台去配置)
        PayRequest payInfo = new PayRequest();
        payInfo.setRoleId(gameRole.getRoleId());
        payInfo.setRoleName(gameRole.getRoleName());
        payInfo.setRoleLevel(gameRole.getRoleLevel());
        payInfo.setZoneId(gameRole.getZoneId());
        payInfo.setZoneName(gameRole.getZoneName());
        payInfo.setExtInfo("cusompay");
        payInfo.setAmount(Integer.parseInt(mMonnyEdit.getText().toString()));
//        payInfo.setNotifyUrl("http://esrsservice.yaoyuenet.com/platform/yuewan");//前端设置支付回调地址，设置你们自己的通知地址。
        payInfo.setGoodsCount(10);
        payInfo.setGoodsId("001");
        payInfo.setGoodsName("钻石");
        qlSDK.doPay(this, payInfo, new Callback.OnPayRequestListener() {
            @Override
            public void onSuccess(String orderId) {
                ToastUtils.showToast(WelComeActivity.this, "支付成功！" + orderId);
            }

            @Override
            public void onFailed(String result) {
                ToastUtils.showToast(WelComeActivity.this, result);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        qlSDK.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        qlSDK.onStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (boo) {
            qlSDK.onResume(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        qlSDK.onStart(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        qlSDK.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 提示，这个api很重要，要实现
        qlSDK.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        qlSDK.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 提示，这个api很重要，要实现
        qlSDK.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        // 提示，这个api很重要，要实现
        if (qlSDK != null) {
            qlSDK.attachBaseContext(newBase);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 提示，这个api很重要，要实现
        qlSDK.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            exit();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            qlSDK.showWinFloat(this);
        }
    }
}
