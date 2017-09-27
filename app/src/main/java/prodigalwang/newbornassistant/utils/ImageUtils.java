package prodigalwang.newbornassistant.utils;

import android.annotation.SuppressLint;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by ProdigalWang on 2017/1/14.
 */

public class ImageUtils {

    private ImageUtils() {

    }

    /**
     * 获取系统时间作为图片文件名
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getImageName() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String name = formatter.format(System.currentTimeMillis()) + ".png";
        return name;
    }


    /**
     * 得到文件所在路径（即全路径去掉完整文件名）
     *
     * @param filepath 文件全路径名称，like mnt/sda/XX.xx
     * @return 根路径，like mnt/sda
     */
    public static String getPathFromFilepath(final String filepath) {
        int pos = filepath.lastIndexOf('/');
        if (pos != -1) {
            return filepath.substring(0, pos);
        }
        return "";
    }

    /**
     * 重新整合路径，将路径一和路径二通过'/'连接起来得到新路径
     *
     * @param path1 路径一
     * @param path2 路径二
     * @return 新路径
     */
    public static String makePath(final String path1, final String path2) {
        if (path1.endsWith(File.separator)) {
            return path1 + path2;
        }
        return path1 + File.separator + path2;
    }

    /**
     * 重命名文件 ,先读取源文件,再创建新的文件名和路径
     * @param oldFilePath  原文件的绝对路径
     * @return
     */
    public static File reNameImage(String oldFilePath) {

        String name = makePath(getPathFromFilepath(oldFilePath), getImageName());
        File file = new File(name);
        return file;
    }

    /**
     * 截取文件绝对路径,提取出文件名。
     *
     * @param name
     * @return
     */
    public static String subImageName(String name) {

        int start = name.lastIndexOf("/");
        return name.substring(start + 1);
    }
}
