package prodigalwang.newbornassistant.cache;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import prodigalwang.newbornassistant.app.AppConfig;
import prodigalwang.newbornassistant.app.MyApplication;
import prodigalwang.newbornassistant.bean.Entity;
import prodigalwang.newbornassistant.bean.User;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.NetUtil;
import prodigalwang.newbornassistant.utils.SDCardUtils;

import static android.R.attr.data;

/**
 * Created by ProdigalWang on 2016/12/7
 * 缓存管理
 */

public class CacheManager {

    // wifi缓存时间为5分钟
    private static long wifi_cache_time = 5 * 60 * 1000;
    // 其他网络环境为1小时
    private static long other_cache_time = 60 * 60 * 1000;

    /**
     * openFileOutput 保存在内部/data/data/com.xxx.xxx/files下的内容
     *
     * @param data
     * @param file
     */
    public static<T extends Entity> void saveInternalCache(List<T> data, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = MyApplication.getContext().openFileOutput(file, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.flush();
            //return true;
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Serializable readInternalCache(String file) {
        if (!isExistDataCache(file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = MyApplication.getContext().openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = MyApplication.getContext().getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 判断缓存是否存在
     *
     * @param cachefile
     * @return
     */
    public static boolean isExistDataCache(String cachefile) {
        boolean exist = false;
        File data = MyApplication.getContext().getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }


    /**
     * 保存数据到指定路径文件
     *
     * @param data     数据
     * @param folder 创建要保存的file文件夹
     * @param fileName 文件名
     */
    public static <T extends Entity> void saveData(T data, File folder, String fileName) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File f = new File(folder, fileName);
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 保存数据到指定路径文件
     *
     * @param data     数据
     * @param folder 创建要保存的file文件夹
     */
    public static <T extends Entity> void saveData(List<T> data, File folder, String fileName) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File f = new File(folder, fileName);
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 从文件读取序列化的对象流
     *
     * @param filePath 路径
     * @param fileName 文件名
     * @return 对象流
     */
    public static Serializable readData(String filePath, String fileName) {
        if (!isExistFile(filePath, fileName)) {
            return null;
        }

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File(filePath + fileName));
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void deleteFile(String filePath, String fileName) {

        File data = new File(filePath + fileName);
        if (data.exists()) {
            data.delete();
        }
    }

    public static boolean isExistFile(String filePath, String fileName) {
        boolean exist = false;
        File data = new File(filePath + fileName);
        if (data.exists()) {
            exist = true;
        }
        return exist;
    }

    /**
     * 判断缓存是否已经失效
     */
    public static boolean isCacheDataFailure(String cachefile) {
        File data = MyApplication.getContext().getFileStreamPath(cachefile);
        if (!data.exists()) {

            return true;
        }
        long existTime = System.currentTimeMillis() - data.lastModified();
        boolean failure = false;
        if (NetUtil.isWifi() == true) {
            failure = existTime > wifi_cache_time ? true : false;
        } else {
            failure = existTime > other_cache_time ? true : false;
        }
        return failure;
    }

}
