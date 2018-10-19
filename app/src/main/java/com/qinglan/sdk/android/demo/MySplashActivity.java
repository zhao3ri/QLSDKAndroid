package com.qinglan.sdk.android.demo;

import android.content.Intent;

import com.qinglan.sdk.android.BaseSplashActivity;


public class MySplashActivity extends BaseSplashActivity {
    @Override
    public void onSplashStop() {
        Intent intent = new Intent(this, WelComeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public int getSplashDrawableId() {
        return R.drawable.img_crash_screen;
    }

    @Override
    public long getSplashMills() {
        return 3*1000;
    }
}
