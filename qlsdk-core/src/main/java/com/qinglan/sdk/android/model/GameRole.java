package com.qinglan.sdk.android.model;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String serverId;

    private static final String ZONE_ID = "zoneId";
    private static final String ZONE_NAME = "zoneName";
    private static final String ROLE_ID = "roleId";
    private static final String ROLE_NAME = "roleName";
    private static final String ROLE_LEVEL = "roleLevel";
    private static final String SERVER_ID = "serverId";


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
        String json = "";
        JSONObject object = new JSONObject();
        try {
            object.put(ZONE_ID, role.zoneId);
            object.put(ZONE_NAME, role.zoneName);
            object.put(ROLE_ID, role.roleId);
            object.put(ROLE_NAME, role.roleName);
            object.put(ROLE_LEVEL, role.roleLevel);
            object.put(SERVER_ID, role.serverId);
            json = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static GameRole readJson(String json) {
        GameRole role = null;
        try {
            JSONObject object = new JSONObject(json);
            role = new GameRole();
            role.zoneId = object.getString(ZONE_ID);
            role.zoneName = object.getString(ZONE_NAME);
            role.roleId = object.getString(ROLE_ID);
            role.roleName = object.getString(ROLE_NAME);
            role.roleLevel = object.getString(ROLE_LEVEL);
            role.serverId = object.getString(SERVER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return role;
    }

    @Override
    public String toString() {
        return "GameRole:zoneId=" + zoneId + ",zoneName=" + zoneName
                + ",roleId=" + roleId + ",roleName=" + roleName + ",roleLevel=" + roleLevel;
    }
}
