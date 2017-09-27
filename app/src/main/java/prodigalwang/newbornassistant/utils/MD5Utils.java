package prodigalwang.newbornassistant.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ProdigalWang
 * @Time 2016年7月1日 上午11:59:59
 * 使用MD5加密
 */
public class MD5Utils {

    /**
     * md5加密字符串
     *
     * @param str 要加密的字符串
     * @return 加密完的字符串
     */
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(str.getBytes("UTF-8"));
        byte[] encryption = md5.digest();

        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < encryption.length; i++) {
            if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
            } else {
                strBuf.append(Integer.toHexString(0xff & encryption[i]));
            }
        }

        return strBuf.toString();

    }

    /**
     * 检查是否匹配MD5加密的数据
     *
     * @param newpasswd 要匹配的密码
     * @param oldpasswd 数据库中的密码
     * @return true or false
     */
    public static boolean checkpassword(String newpasswd, String oldpasswd) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (EncoderByMd5(newpasswd).equals(oldpasswd))
            return true;
        else
            return false;
    }


}
