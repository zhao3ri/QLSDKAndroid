package com.qinglan.sdk.android.model;

import com.qinglan.sdk.android.common.Utils;

import java.io.Serializable;

/**
 * Created by zhaoj on 2018/9/20
 * 游戏角色信息
 *
 * @author zhaoj
 */
public final class GameRole implements Serializable {
    private static final long serialVersionUID = -1941612847047022843L;
    private String zoneId;
    private String zoneName;
    private String roleId;
    private String roleName;
    private String roleLevel;
    private String serverId = "1";

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public static String toJson(GameRole role) {
        return Utils.object2Json(role);
    }

    public static GameRole readJson(String json) {
        return Utils.json2Object(json, GameRole.class);
    }

    @Override
    public String toString() {
        return "GameRole:zoneId=" + zoneId + ",zoneName=" + zoneName
                + ",roleId=" + roleId + ",roleName=" + roleName + ",roleLevel=" + roleLevel;
    }
}
