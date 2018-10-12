package com.qinglan.sdk.model;

/**
 * Created by zhaoj on 2018/9/20
 * 游戏支付信息
 *
 * @author zhaoj
 */
public final class GamePayInfo {
    private String money;
    private String cpOrderId;
    private String extInfo;
    private String notifyUrl;
    private String productName;
    private int productCount;
    private String productId;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCpOrderId() {
        return cpOrderId;
    }

    public void setCpOrderId(String id) {
        cpOrderId = id;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String info) {
        extInfo = info;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String url) {
        notifyUrl = url;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String name) {
        productName = name;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int count) {
        productCount = count;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String id) {
        productId = id;
    }
}
