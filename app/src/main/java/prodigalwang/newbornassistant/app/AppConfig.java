package prodigalwang.newbornassistant.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

import prodigalwang.newbornassistant.utils.SDCardUtils;

/**
 * Created by ProdigalWang on 2016/12/7
 * <p>
 * app基础设置
 */

public class AppConfig {

    private AppConfig() {

    }

    private static AppConfig appConfig;

    public static AppConfig getInstance() {
        if (appConfig == null) {
            appConfig = new AppConfig();
            //appConfig.mContext = context;
        }
        return appConfig;
    }


    public final String APP_PATH = "NewbonAssitant";
    // 默认保存下载图片的路径
    public final String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + APP_PATH
            + File.separator + "images" + File.separator;

    // 默认保存大缓存的路径(PICASSO)
    public final String DEFAULT_CACHE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + APP_PATH
            + File.separator + "cache" + File.separator;

    public final String DEFAULT_NAV_FOLDER = Environment
            .getExternalStorageDirectory()
            + File.separator
            + APP_PATH
            + File.separator + "baiduNav";

    // 默认存放文件下载的路径
    public final String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + APP_PATH
            + File.separator + "download" + File.separator;

    //保存在内部cache文件夹
    public final String DEFAULT_USER_DATA = MyApplication.getContext()
            .getCacheDir() + File.separator + "user" + File.separator;

    public final String NOSD_CACHE_PATH = MyApplication.getContext()
            .getFilesDir() + File.separator;

    public final String DEFAULT_SAVE_CLASSTABLE = Environment
            .getExternalStorageDirectory()
            + File.separator
            + APP_PATH
            + File.separator + "classTable" + File.separator;
    public final String classTableName="ClassTable.txt";

    public final String userDataName="user.txt";

    public File creatCachepath() {
        File file = null;
        if (SDCardUtils.isSDCardEnable()) {
            file = new File(DEFAULT_CACHE_PATH);
        } else {
            file = new File(NOSD_CACHE_PATH);
        }
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    public File creatImagePath() {
        File file = null;
        if (SDCardUtils.isSDCardEnable()) {
            file = new File(DEFAULT_SAVE_IMAGE_PATH);
        } else {
            file = new File(NOSD_CACHE_PATH);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File creatUserDataPath() {
        File file = new File(DEFAULT_USER_DATA);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File creatClassTableFolder() {
        File file = null;
        if (SDCardUtils.isSDCardEnable()) {
            file = new File(DEFAULT_SAVE_CLASSTABLE);
        } else {
            file = new File(NOSD_CACHE_PATH);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
