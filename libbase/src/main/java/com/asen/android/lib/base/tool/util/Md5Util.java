package com.asen.android.lib.base.tool.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * MD5加密工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class Md5Util {

    /**
     * MD5加密方式1 - 转换成小写字母
     *
     * @param str 要加密的字符串
     * @return 加密后的结果
     */
    public static String md5(String str) {
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte tmp[] = mdTemp.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char strs[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                strs[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                strs[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            return new String(strs); // 换后的结果转换为字符串
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * MD5加密方式1 - 转换成大写字母
     *
     * @param str 要加密的字符串
     * @return 加密后的结果
     */
    public static String MD5(String str) {
        String result = md5(str);
        return result == null ? null : result.toUpperCase(Locale.getDefault());
    }

    /**
     * MD5加密方式2 - 转换成小写字母
     *
     * @param str 要加密的字符串
     * @return 加密后的结果
     */
    public static String md5Second(String str) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * MD5加密方式2 - 转换成大写字母
     *
     * @param str 要加密的字符串
     * @return 加密后的结果
     */
    public static String MD5Second(String str) {
        String result = md5Second(str);
        return result.toUpperCase(Locale.getDefault());
    }

}
