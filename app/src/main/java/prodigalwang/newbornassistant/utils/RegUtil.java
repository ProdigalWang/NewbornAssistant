package prodigalwang.newbornassistant.utils;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by ProdigalWang on 2016/12/22.
 */

public class RegUtil {
    /**
     * 移动手机号码验证
     *
     * @param data 可能是手机号码字符串
     * @return 是手机号 ture
     */
    public static boolean isMobileNumber(String data) {
        /**
         * 手机号码:
         * 13[0-9], 14[5,7], 15[0, 1, 2, 3, 5, 6, 7, 8, 9], 17[6, 7, 8], 18[0-9], 170[0-9]
         * 移动号段: 134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705
         * 联通号段: 130,131,132,155,156,185,186,145,176,1709
         * 电信号段: 133,153,180,181,189,177,1700
         */
        String MOBILE = "^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$";
        /**
         * 中国移动：China Mobile
         * 134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705
         */
        String CM = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";
        /**
         * 中国联通：China Unicom
         * 130,131,132,155,156,185,186,145,176,1709
         */
        String CU = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";
        /**
         * 中国电信：China Telecom
         * 133,153,180,181,189,177,1700
         */
        String CT = "(^1((33|53|77|8[019])[0-9]|349)|(700\\d{7}$)";

//        if (data.matches(CM))
//            return true;
//        if (data.matches(CU))
//            return true;
//        if (data.matches(CT))
//            return true;
//        if (data.matches(MOBILE))
//            return true;
//        return false;

        return data.matches(MOBILE);
    }

    /**
     * 检测用户名是否为空或者超过长度
     *
     * @param userName
     * @return
     */
    public static boolean checkUsername(String userName) {
        if (TextUtils.isEmpty(userName) || userName.length() > 15) {
            return false;
        }
        return true;
    }

    /**
     * 检测两次输入的密码是否一致,有Toast通知
     *
     * @param context
     * @param Pwd1
     * @param Pwd2
     * @return true 表示一致
     */
    public static boolean checkPassword(Context context, String Pwd1, String Pwd2) {

        if (!Pwd1.equals(Pwd2)) {
            ToastUtil.showShort(context, "两次输入的密码不一致，请确认");
            return false;
        } else if (TextUtils.isEmpty(Pwd1)) {
            ToastUtil.showShort(context, "密码不能为空");
            return false;
        } else if (Pwd1.length() > 16 || Pwd1.length() < 6) {
            ToastUtil.showShort(context, "密码长度为6-16位！");
            return false;
        } else {
            return true;
        }
    }
}
