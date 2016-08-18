package com.asen.android.lib.base.tool.util;

/**
 * ʮ�����ƹ�����
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class HexUtil {

    /**
     * ���ڽ���ʮ�������ַ��������Сд�ַ�����
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * ���ڽ���ʮ�������ַ�������Ĵ�д�ַ�����
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * ���ֽ�����ת��Ϊʮ������Сд�ַ�����
     *
     * @param data byte[]
     * @return ʮ������char[]
     */
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * ���ֽ�����ת��Ϊʮ�������ַ�����
     *
     * @param data        byte[]
     * @param toLowerCase true������Сд��ʽ ��false�����ɴ�д��ʽ
     * @return ʮ������char[]
     */
    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * ���ֽ�����ת��Ϊʮ�������ַ�����
     *
     * @param data     byte[]
     * @param toDigits ���ڿ��������char[]
     * @return ʮ������char[]
     */
    private static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * ���ֽ�����ת��Ϊʮ������Сд�ַ���
     *
     * @param data byte[]
     * @return ʮ������String
     */
    public static String encodeHexStr(byte[] data) {
        return encodeHexStr(data, true);
    }

    /**
     * ���ֽ�����ת��Ϊʮ�������ַ���
     *
     * @param data        byte[]
     * @param toLowerCase true ������Сд��ʽ �� false �����ɴ�д��ʽ
     * @return ʮ������String
     */
    public static String encodeHexStr(byte[] data, boolean toLowerCase) {
        return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * ���ֽ�����ת��Ϊʮ�������ַ���
     *
     * @param data     byte[]
     * @param toDigits ���ڿ��������char[]
     * @return ʮ������String
     */
    private static String encodeHexStr(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }

    /**
     * ��ʮ�������ַ���ת��Ϊ�ֽ�����
     *
     * @param str ʮ������char[]
     * @return byte[]
     * @throws RuntimeException ���Դʮ�������ַ�������һ����ֵĳ��ȣ����׳�����ʱ�쳣
     */
    public static byte[] decodeHex(String str) {
        return decodeHex(str.toCharArray());
    }

    /**
     * ��ʮ�������ַ�����ת��Ϊ�ֽ�����
     *
     * @param data ʮ������char[]
     * @return byte[]
     * @throws RuntimeException ���Դʮ�������ַ�������һ����ֵĳ��ȣ����׳�����ʱ�쳣
     */
    public static byte[] decodeHex(char[] data) {
        int len = data.length;
        if ((len & 0x01) != 0) {
            throw new RuntimeException("δ֪���ַ�");
        }
        byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    /**
     * ��ʮ�������ַ�ת����һ������
     *
     * @param ch    ʮ������char
     * @param index ʮ�������ַ����ַ������е�λ��
     * @return һ������
     * @throws RuntimeException ��ch����һ���Ϸ���ʮ�������ַ�ʱ���׳�����ʱ�쳣
     */
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("�Ƿ�16�����ַ� " + ch + " ������ " + index);
        }
        return digit;
    }

    /**
     * ��byte[]����ת��ΪString�ַ���
     *
     * @param data byte����
     * @return String ת������ַ���
     */
    public static String byteToArray(byte[] data) {
        String result = "";
        for (byte aData : data) {
            result += Integer.toHexString((aData & 0xFF) | 0x100)
                    .toUpperCase().substring(1, 3);
        }
        return result;
    }

}
