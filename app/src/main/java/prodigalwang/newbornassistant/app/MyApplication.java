package prodigalwang.newbornassistant.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.mapapi.SDKInitializer;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.weavey.loading.lib.LoadingLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.concurrent.TimeUnit;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import okhttp3.OkHttpClient;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.location.view.LocationService;
import prodigalwang.newbornassistant.utils.PicassoImageLoader;
import prodigalwang.newbornassistant.utils.SPHelper;
import prodigalwang.newbornassistant.utils.ThreadPoolUtil;

/**
 * Created by ProdigalWang on 2016/12/7
 */

public class MyApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    public LocationService locationService;

    public BMapManager mBMapManager = null;

    private final static long time = 6000;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        //配置Loading界面
        LoadingLayout.getConfig()
                .setErrorText(getString(R.string.error_text))
                .setEmptyText(getString(R.string.empty_text))
                .setNoNetworkText(getString(R.string.NoNetworkText))
                .setErrorImage(R.mipmap.define_error)
                .setEmptyImage(R.mipmap.define_empty)
                .setNoNetworkImage(R.mipmap.define_nonetwork)
                .setAllTipTextColor(R.color.gray)
                .setAllTipTextSize(14)
                .setReloadButtonText(getString(R.string.OnReload))
                .setReloadButtonTextSize(14)
                .setReloadButtonTextColor(R.color.gray)
                .setReloadButtonWidthAndHeight(150, 40)
                .setAllPageBackgroundColor(R.color.background)
                .setLoadingPageLayout(R.layout.loading_progressbar);

        //okhttp持久化cookies配置,保存到内存中，退出销毁--PersistentCookieStore保存到本地
        CookieJarImpl cookieJar = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .connectTimeout(time, TimeUnit.MILLISECONDS)
                .readTimeout(time, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);

        //修改Picasso使用自定义的线程池和缓存地址
        Picasso picasso = new Picasso
                .Builder(context)
                .executor(ThreadPoolUtil.getThreadpool())
                .downloader(new OkHttp3Downloader(AppConfig.getInstance().creatCachepath()))
                .build();
        Picasso.setSingletonInstance(picasso);

        //初始化SharedPreferences配置
        SPHelper.init(context);

        //初始化照片选择配置
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(0xF4, 0x43, 0x36))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(Color.rgb(0xF4, 0x43, 0x36))
                .setFabPressedColor(Color.rgb(0xD3, 0x2F, 0x2F))
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(Color.BLACK)
                .setCropControlColor(Color.rgb(0xF4, 0x43, 0x36))
                .build();

        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)//开启相机功能
                .setEnableEdit(true)//开启编辑功能
                .setEnableCrop(true)//开启裁剪功能
                .setEnableRotate(true)//开启选择功能
                .setCropSquare(true)//裁剪正方形
                .setEnablePreview(true)//是否开启预览功能
                .setMutiSelectMaxSize(3)//多选最大值
                .build();
        //配置imageloader
        PicassoImageLoader imageloader = new PicassoImageLoader();
        //设置核心配置信息
        CoreConfig coreConfig = new CoreConfig.Builder(context, imageloader, theme)
                .setEditPhotoCacheFolder(AppConfig.getInstance().creatImagePath())//裁剪后图片位置
                .setTakePhotoFolder(AppConfig.getInstance().creatImagePath())//配置拍照保存目录，不做配置的话默认是/sdcard/DCIM/GalleryFinal/
                .setFunctionConfig(functionConfig).build();

        GalleryFinal.init(coreConfig);

        // NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(context);


        //初始化百度地图SDK
        locationService = new LocationService(context);
        SDKInitializer.initialize(context);

        //初始化全景地图
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }
        mBMapManager.init(new MyGeneralListener());
    }

    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    private static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetPermissionState(int iError) {
            // 非零值表示key验证未通过
            if (iError != 0) {
//                // 授权Key错误：
//                Toast.makeText(PanoDemoApplication.getInstance().getApplicationContext(),
//                        "请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: " + iError, Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(PanoDemoApplication.getInstance().getApplicationContext(), "key认证成功", Toast.LENGTH_LONG)
//                        .show();
            }
        }
    }
//    setMutiSelect(boolean)//配置是否多选
//    setMutiSelectMaxSize(int maxSize)//配置多选数量
//    setCropWidth(int width)//裁剪宽度
//    setCropHeight(int height)//裁剪高度
//    setSelected(List)//添加已选列表,只是在列表中默认呗选中不会过滤图片
//    setFilter(List list)//添加图片过滤，也就是不在GalleryFinal中显示
//    setRotateReplaceSource(boolean)//配置选择图片时是否替换原始图片，默认不替换
//    setCropReplaceSource(boolean)//配置裁剪图片时是否替换原始图片，默认不替换
//    setForceCrop(boolean)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
//    setForceCropEdit(boolean)//在开启强制裁剪功能时是否可以对图片进行编辑（也就是是否显示旋转图标和拍照图标）
}
