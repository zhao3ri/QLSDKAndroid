package com.qinglan.sdk.model;

/**
 * Created by zhaoj on 2018/9/20
 * 游戏角色信息
 *
 * @author zhaoj
 */
public final class GameRoleInfo {
    private String zoneId;
    private String zoneName;
    private String roleId;
    private String roleLevel;
    private String serverId;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String id) {
        zoneId = id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String name) {
        zoneName = name;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String id) {
        roleId = id;
    }

    public String getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(String level) {
        roleLevel = level;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String id) {
        serverId = id;
    }
}
