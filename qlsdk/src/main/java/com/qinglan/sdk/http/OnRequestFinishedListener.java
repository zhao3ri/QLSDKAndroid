package com.qinglan.sdk.http;

/**
 * Created by zhaoj on 2018/9/20
 * 网络请求回调接口
 *
 * @author zhaoj
 */
public interface OnRequestFinishedListener {
    void onFinished(boolean success, String result);
}
