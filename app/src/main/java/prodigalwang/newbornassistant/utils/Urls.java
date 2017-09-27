package prodigalwang.newbornassistant.utils;

/**
 * Created by ProdigalWang on 2016/12/9
 */

public class Urls {

    public static final String IP_TECENT="http://139.199.37.81:8080/";
    public static final String IP_LOCAL="http://192.168.1.105:8080/";

    //主机服务基础地址
    public static  final String HOST_LOCAL=IP_TECENT+"newborn_service/";

    //校区网址和附件下载
    public static final String HOST_SCHOOL="http://lyc.sut.edu.cn";
    public static final String SHOOL_NEWS_DETAIL="http://lyc.sut.edu.cn/xydt_detail.asp?id=";
    public static final String SCHOOL_IMAGENEWS_DETAIL="http://lyc.sut.edu.cn/news_detail.asp?id=";

    //获取新闻和公告(type决定)
    public static final String HOME_NEWS=HOST_LOCAL+"getNews";
    //获取轮播新闻
    public static final String HOME_NEWS_IMAGE_HEADER=HOST_LOCAL+"getImageNews";
    //获取新闻详情html
    public static final String HOME_NES_DETAIL=HOST_LOCAL+"news_html/";
    public static final String HOME_NEWS_HEADER_DETAIL=HOST_LOCAL+"images_news_html/";
    //获取校园美景
    public static final String HOME_SCHOOL_IMAGE=HOST_LOCAL+"getSchoolImage";
    //获取公告详情html
    public static final String HOME_NOTICE_DETAIL=HOST_LOCAL+"notice_html/";
    //获取攻略
    public static final String HOME_TIPS_GET_POST=HOST_LOCAL+"getTipsPost";
    //攻略图片
    public static final String HOME_TIPS_POST_IMAGES=HOST_LOCAL+"tips_post_images/";

    //登录注册
    public static final String LOGIN=HOST_LOCAL+"Login";
    public static final String SIGNUP=HOST_LOCAL+"SignUp";
    //上传用户头像
    public static final String UPLOAD_HEDA_IMAGE=HOST_LOCAL+"UploadHeadImage";
    //获取用户头像
    public static final String GET_USER_HEAD_IMAGE=HOST_LOCAL+"user_head_image/";

    //百度地图查询接口
    public static final String SEARCHSITE_ONE="http://api.map.baidu.com/place/v2/search?ak=0s3MvbE3tR91YykU0Om3hUpo9AUpU6rN&output=json&query=";
    public static final String SEARCHSITE_TWO="&page_size=10&page_num=0&scope=1&region=全国&mcode=18:01:B8:AC:6E:1C:B1:27:20:1C:CA:E9:1B:3B:CE:BC:26:E1:BD:3B;prodigalwang.newbornassistant";

    //上传用户发布的帖子
    public static final String UPLOADTIPS=HOST_LOCAL+"uploadTips";
    //上传用户帖子中的图片
    public static final String UPLOADTIPS_IMAGES=HOST_LOCAL+"UploadTipsImage";

    //获取每日必应的json
    public static final String GET_BING_IMAGE="http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
    public static final String BING_URL="http://cn.bing.com";

    //教务图形验证码地址
    public static final String SCHOOL_GET_REGPIC="http://202.97.179.124:8000/ACTIONVALIDATERANDOMPICTURE.APPPROCESS";
    //教务登录请求地址
    public static final String SCHOOL_LOGIN_ET="http://202.97.179.124:8000/ACTIONLOGON.APPPROCESS?mode=4";
    //课表
    public  static final String SCHOOL_CLASS_TABLE = "http://202.97.179.124:8000/ACTIONQUERYELECTIVERESULTBYSTUDENT.APPPROCESS?mode=1";
    //成绩查询地址
    public  static final String SCHOOL_SORE = "http://202.97.179.124:8000/ACTIONQUERYSTUDENTSCHOOLREPORT.APPPROCESS?mode=1";

    //获取课表学期数
    public static final String SCHOOL_TERM="http://202.97.179.124:8000/ACTIONQUERYELECTIVERESULTBYSTUDENT.APPPROCESS";
}
