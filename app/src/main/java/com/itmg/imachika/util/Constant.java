package com.itmg.imachika.util;

public class Constant {

    /*
        Intent
     */
    //拍照返回成功码
    public final static int SELECT_CAMERA_CODE = 1;
    public final static int SELECT_PHOTO_CODE = 2;
    //通报type  1 用户  2 店铺 3 评论  4 评论回复
    public final static String REPORT_USER = "1";
    public final static String REPORT_BUSS = "2";
    public final static String REPORT_REVIEW = "3";
    public final static String REPORT_REREVIEW = "4";

    //修改用户信息 type  1 名字  2 电话 3 地址  4 个性签名
    public final static int INFO_NAME = 1;
    public final static int INFO_SEX = 2;
    public final static int INFO_TEL = 3;
    public final static int INFO_ADDRESS= 4;
    public final static int INFO_SIGN = 5;

    /*存储使用的常量*/
    public static final String SP_IS_LOGIN ="isLogin";
    public static final String SP_USER_INFO ="userInfo";
    public static final String SP_PASSWORD="password";
    public static final String SP_MSG_COUNT = "messageCount";
    public static final String SP_USER_ID = "userId";
    public static final String SP_USER_NAME = "userName";


    public static final String DEFAULT_PAGE_SIZE = "5";

    public static final int LANGUAGE_TYPE_JP = 1;
    public static final int LANGUAGE_TYPE_CN = 2;
    public static final int LANGUAGE_TYPE_EN = 3;

}
