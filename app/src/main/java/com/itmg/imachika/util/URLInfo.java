package com.itmg.imachika.util;

/**
 * Created by lenovo on 2017/11/13.
 *  网络接口
 */

public interface URLInfo {

//    public static final String BASE_URL = "https://app.imachika.com/api/";//公开
    String BASE_URL = "http://sigmanote.com/api/";//测试

    String logoUrl = "https://imachika.com/static/images/appimg/icon-";//首页图片的前缀
    String chat = BASE_URL + ":5152";//聊天的地址
//    String chat = "https://imachika.com/socket.io";//聊天的地址
//    String webSocket = "wss://imachika.com/socket.io/?transport=websocket";//聊天的地址
    String home =  BASE_URL + "cats/";
    String userInfo =  BASE_URL + "user/";
    String concern = BASE_URL + "follow/";//关注/取消关注
    String pullblack = BASE_URL + "pullblack/";//黑名单
    /**
     * 详细界面列表
     * 参数 catid ,location
     * */
    String infoList = BASE_URL + "nearbysearch";
    String infoDetail = BASE_URL + "placedetail";
    String contentUrl ="https://imachika.com/webview/item";
    String addReview = BASE_URL + "addreview";
    String addLike = BASE_URL + "addlike";
    String addReReview = BASE_URL + "addrereview";
    String addCollect = BASE_URL + "collection";

    String reportUrl = BASE_URL + "complain";

    String searchSortInfo = BASE_URL + "searchsortconfig";

    String shareListUrl ="https://imachika.com/nearbysearch";
    String shareDetailUrl ="https://imachika.com/item/";

    //Me页面
    String editInfoUrl = BASE_URL + "me/edit";//编辑个人资料
    String meCollection =  BASE_URL + "me/collection";
    /**
     * 参数：name/email/pwd/location/tel/gender/file
     * */
    String register = BASE_URL +"signup";
    /**
     * 参数：email/pwd/
     * */
    String login = BASE_URL +"login";

    String login_gg = BASE_URL + "savegg";
    String login_fb = BASE_URL + "savefb";
    String login_line = BASE_URL + "saveline";

    String web_url1 ="https://imachika.com/webview/terms_of_service/";
    String web_url2 ="https://imachika.com/webview/privacy/";

    String web_login = "https://imachika.com/login";
}
