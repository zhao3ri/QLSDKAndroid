package com.qinglan.sdk.http;

public interface Url {

    /**
     * 初始化URL
     */
    String INIT_URL = "account/initial";

    /**
     * 登录URL
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
    String ROLE_ESTABLISH_URL = "account/role/establish";

    /**
     * 生成订单URL
     */
    String ORDER_GENERATE_URL = "account/order/generate";

    /**
     * 获取用户信息URL
     */
    String GET_USERINFO_URL = "";

    /**
     * 获取支付签名URL
     */
    String GET_PAYINFO_URL = "";

    /**
     * 获取token
     */
    String TOKEN_URL = "account/gettoken";
}
