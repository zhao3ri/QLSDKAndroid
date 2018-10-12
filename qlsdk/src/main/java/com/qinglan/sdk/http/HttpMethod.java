package com.qinglan.sdk.http;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
public interface HttpMethod {
    int GET = 0x00;
    int POST = 0x01;
    int PUT = 0x02;
    int DELETE = 0x03;
    int HEAD = 0x04;
    int APP = 0x05;
    int IMAGE = 0x06;
}
