package com.qinglan.sdk.android.net;

public final class HttpConstants {
    public static final String REQUEST_PARAM_GAME_ID = "gameId";
    public static final String REQUEST_PARAM_CHANNEL_ID = "channelId";
    public static final String REQUEST_PARAM_TOKEN_EXTEND = "extend";

    public static final String REQUEST_PARAM_UID = "uid";
    public static final String REQUEST_PARAM_ZONE_ID = "zoneId";
    public static final String REQUEST_PARAM_ZONE_NAME = "zoneName";
    public static final String REQUEST_PARAM_ROLE_ID = "roleId";
    public static final String REQUEST_PARAM_ROLE_NAME = "roleName";
    public static final String REQUEST_PARAM_ROLE_LEV = "roleLevel";
    public static final String REQUEST_PARAM_DEVICE_ID = "deviceId";
    public static final String REQUEST_PARAM_CLIENT_TYPE = "clientType";
    public static final String REQUEST_PARAM_TIMESTAMP = "loginTime";

    public static final String REQUEST_PARAM_IMSI = "imsi";
    public static final String REQUEST_PARAM_LATITUDE = "latitude";
    public static final String REQUEST_PARAM_LONGITUDE = "longitude";
    public static final String REQUEST_PARAM_LOCATION = "location";
    public static final String REQUEST_PARAM_MANUFACTURER = "manufacturer";
    public static final String REQUEST_PARAM_MODEL = "model";
    public static final String REQUEST_PARAM_NETWORK_COUNTRY_ISO = "networkCountryIso";
    public static final String REQUEST_PARAM_NETWORK_TYPE = "networkType";
    public static final String REQUEST_PARAM_PHONE_TYPE = "phoneType";
    public static final String REQUEST_PARAM_OS_VERSION = "osVersion";
    public static final String REQUEST_PARAM_RESOLUTION = "resolution";
    public static final String REQUEST_PARAM_SIMOPERATOR_NAME = "simOperatorName";
    public static final String REQUEST_PARAM_API_VERSION = "apiVersion";

    public static final String REQUEST_PARAM_CP_ORDER_ID = "cpOrderId";
    public static final String REQUEST_PARAM_EXT_INFO = "extInfo";
    public static final String REQUEST_PARAM_AMOUNT = "amount";
    public static final String REQUEST_PARAM_NOTIFY_URL = "notifyUrl";
    public static final String REQUEST_PARAM_FIXED = "fixed";
    public static final String REQUEST_PARAM_GOLD = "gold";
    public static final String REQUEST_PARAM_ORDER_ID = "orderId";
    public static final String REQUEST_PARAM_GOODS_ID = "goodsId";
    public static final String REQUEST_PARAM_GOODS_NAME = "goodsName";
    public static final String REQUEST_PARAM_GOODS_COUNT = "goodsCount";

    public static final String RESPONSE_CODE = "code";
    public static final String RESPONSE_MESSAGE = "msg";
    public static final String RESPONSE_DATA = "data";

    public static final int RESPONSE_CODE_SUCCESS = 0;
    public static final int RESPONSE_CODE_READ_ERROR = -2;
    public static final int RESPONSE_CODE_UNKNOWN_ERROR = -3;

    public static final String RESPONSE_START_TIMESTAMP = "gameStartTime";
    public static final String RESPONSE_CREATE_TIMESTAMP = "roleCreateTime";
    public static final String RESPONSE_TOKEN = "token";
    public static final String RESPONSE_NOTIFY_URL = "notifyUrl";
    public static final String RESPONSE_ORDER_ID = "orderId";
    public static final String RESPONSE_ORDER_STATUS = "orderStatus";
    public static final String RESPONSE_ORDER_NOTIFY_STATUS = "orderNotifyStatus";

    //订单状态
    public final static int ORDER_STATUS_SUBMIT_SUCCESS = 0;        //提交成功
    public final static int ORDER_STATUS_SUBMIT_FAIL = 11;        //提交失败
    public final static int ORDER_STATUS_PAYMENT_SUCCESS = 12;        //支付成功
    public final static int ORDER_STATUS_PAYMENT_FAIL = 13;        //支付失败

    //订单通知状态
    public final static int ORDER_NOTIFY_STATUS_DEFAULT = 0;        //默认状态
    public final static int ORDER_NOTIFY_STATUS_WAITING = 21;        //等待通知
    public final static int ORDER_NOTIFY_STATUS_SUCCESS = 22;        //通知完成
    public final static int ORDER_NOTIFY_STATUS_FAIL = 23;        //通知失败
    public final static int ORDER_NOTIFY_STATUS_RESEND = 24;        //重发通知
}
