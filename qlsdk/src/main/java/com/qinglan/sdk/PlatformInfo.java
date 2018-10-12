package com.qinglan.sdk;

/**
 * Created by zhaoj on 2018/9/20
 * 平台的枚举类
 *
 * @author zhaoj
 */
public enum PlatformInfo {
    DEFAULT(0, "default");

    private int platformId;
    private String platformName;

    PlatformInfo(int id, String name) {
        platformId = id;
        platformName = name;
    }

    public static PlatformInfo getPlatformById(int id) {
        for (PlatformInfo p : PlatformInfo.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public int getId() {
        return platformId;
    }

    public String getName() {
        return platformName;
    }
}
