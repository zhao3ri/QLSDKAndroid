package com.qinglan.sdk.android.net;

/**
 * Created by zhaoj on 2018/9/20
 * 网络请求回调接口
 *
 * @author zhaoj
 */
public interface OnResponseListener {
    void onResponse(boolean success, String result);
}
