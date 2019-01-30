package com.qinglan.sdk.android.net;

public interface Url {

    /**
     * 初始化URL
     */
    String INIT_URL = "account/initial";

    /**
     * 进入游戏URL
     */
    String LOGIN_URL = "account/login";

    /**
     * 用户心跳URL
     */
    String HEARTBEAT_URL = "account/heartbeat";

    /**
     * 帐号注销URL
     */
    String LOGOUT_URL = "account/logout";

    /**
     * 游戏退出URL
     */
    String QUIT_URL = "account/quit";

    /**
     * 创建角色URL
     */
    String ROLE_CREATE_URL = "account/role/create";

    /**
     * 生成订单URL
     */
    String ORDER_GENERATE_URL = "account/order/generate";

    /**
     * 查询订单URL
     */
    String ORDER_QUERY_URL = "account/order/query";

    /**
     * 获取用户信息URL
     */
    String GET_USER_INFO_URL = "";

    /**
     * 获取支付签名URL
     */
    String GET_PAYINFO_URL = "";

    /**
     * 获取token
     */
    String TOKEN_URL = "account/gettoken";
}
