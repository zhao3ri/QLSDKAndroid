package com.qinglan.sdk.android.model;

/**
 * Created by zhaoj on 2018/9/20
 * 游戏支付信息
 *
 * @author zhaoj
 */
public final class GamePay {
    private long amount;
    private String cpOrderId;
    private String extInfo;
    private String notifyUrl;
    private String productName;
    private int productCount;
    private String productId;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
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
