package com.bai.javapoi.pojo.code;

/**响应码*/

public class ResultCode {


    /**空数据*/
    public static final int NO = 0;
    /**成功*/
    public static final int OK = 1;
    /**D90已拆除*/
    public static final int D90 = 2;
    /**其他错误*/
    public static final int OTHER_ERROR = -1;
    /**用户密码错误*/
    public static final int USER_PASSWORD_ERROR = -100;
    /**用户不存在*/
    public static final int USER_NONE = -101;
    /**请求过快*/
    public static final int REQUEST_TO0_FAST = -102;
    /**ip访问受限*/
    public static final int IP_VISIT_CANt = -103;
    /**请求类型错误*/
    public static final int REQUEST_TYPE_ERROR = -104;
    /**数据库执行错误*/
    private static final int SQL_ERROR = -200;
    /**权限校验失败*/
    private static final int ROLE_FALSE = -403;
    /**未知异常*/
    public static final int UNKNOWN_ABNORMAL = -9999;
    /**空指针异常*/
    private static final int NULL_POINT_ERROR = -2000;
    public static final int TOKEN_EXPIRED = -111;
}
