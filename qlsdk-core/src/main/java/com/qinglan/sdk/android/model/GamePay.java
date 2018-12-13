package com.qinglan.sdk.android.model;

/**
 * Created by zhaoj on 2018/9/20
 * 游戏支付信息
 *
 * @author zhaoj
 */
public final class GamePay {
    private long amount;
    //    private String cpOrderId;
    private String extInfo;
    private String notifyUrl;
    private String goodsName;
    private int goodsCount = 1;
    private String goodsId;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

//    public String getCpOrderId() {
//        return cpOrderId;
//    }
//
//    public void setCpOrderId(String id) {
//        cpOrderId = id;
//    }

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String name) {
        goodsName = name;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int count) {
        goodsCount = count;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String id) {
        goodsId = id;
    }
}
