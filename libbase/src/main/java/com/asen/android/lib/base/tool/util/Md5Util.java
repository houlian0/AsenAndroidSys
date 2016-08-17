package com.asen.android.lib.base.tool.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * MD5���ܹ�����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class Md5Util {

    /**
     * MD5���ܷ�ʽ1 - ת����Сд��ĸ
     *
     * @param str Ҫ���ܵ��ַ���
     * @return ���ܺ�Ľ��
     */
    public static String md5(String str) {
        char hexDigits[] = { // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte tmp[] = mdTemp.digest(); // MD5 �ļ�������һ�� 128 λ�ĳ�������
            // ���ֽڱ�ʾ���� 16 ���ֽ�
            char strs[] = new char[16 * 2]; // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ���
            // ���Ա�ʾ�� 16 ������Ҫ 32 ���ַ�
            int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
            for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
                // ת���� 16 �����ַ���ת��
                byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
                strs[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��,
                // >>> Ϊ�߼����ƣ�������λһ������
                strs[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
            }
            return new String(strs); // ����Ľ��ת��Ϊ�ַ���
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * MD5���ܷ�ʽ1 - ת���ɴ�д��ĸ
     *
     * @param str Ҫ���ܵ��ַ���
     * @return ���ܺ�Ľ��
     */
    public static String MD5(String str) {
        String result = md5(str);
        return result == null ? null : result.toUpperCase(Locale.getDefault());
    }

    /**
     * MD5���ܷ�ʽ2 - ת����Сд��ĸ
     *
     * @param str Ҫ���ܵ��ַ���
     * @return ���ܺ�Ľ��
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
     * MD5���ܷ�ʽ2 - ת���ɴ�д��ĸ
     *
     * @param str Ҫ���ܵ��ַ���
     * @return ���ܺ�Ľ��
     */
    public static String MD5Second(String str) {
        String result = md5Second(str);
        return result.toUpperCase(Locale.getDefault());
    }

}
