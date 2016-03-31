package com.asen.android.lib.base.tool.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by HL_SEN on 2015/9/21.
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class Base64Util {

    private static final char[] base64EncodeChars = new char[]{'A', 'B', 'C',
            'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
            'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final byte[] base64DecodeChars = new byte[]{-1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57,
            58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7,
            8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
            25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1,
            -1, -1, -1, -1};

    /**
     * Base64 编码
     *
     * @param content     文本内容
     * @param charsetName 编码方式 如：UTF-8
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] encode(String content, String charsetName) throws UnsupportedEncodingException {
        return encode(content.getBytes(charsetName), charsetName);
    }

    /**
     * Base64 编码
     *
     * @param bytes       待编码的字节数组
     * @param charsetName 编码方式 如：UTF-8
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] encode(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
        return encodeStr(bytes).getBytes(charsetName);
    }

    /**
     * Base64 编码
     *
     * @param content     文本内容
     * @param charsetName 编码方式 如：UTF-8
     * @return 编码后的字符串
     * @throws UnsupportedEncodingException
     */
    public static String encodeStr(String content, String charsetName) throws UnsupportedEncodingException {
        return encodeStr(content.getBytes(charsetName));
    }

    /**
     * Base64 编码
     *
     * @param bytes 待编码的字节数组
     * @return 编码后的字符串
     */
    public static String encodeStr(byte[] bytes) {
        int length = bytes.length;
        int size = (int) Math.ceil(length * 1.36);
        StringBuilder sb = new StringBuilder(size);
        int r = length % 3;
        int len = length - r;
        int i = 0;
        int c;
        while (i < len) {
            c = (0x000000ff & bytes[i++]) << 16 | (0x000000ff & bytes[i++]) << 8
                    | (0x000000ff & bytes[i++]);
            sb.append(base64EncodeChars[c >> 18]);
            sb.append(base64EncodeChars[c >> 12 & 0x3f]);
            sb.append(base64EncodeChars[c >> 6 & 0x3f]);
            sb.append(base64EncodeChars[c & 0x3f]);
        }
        if (r == 1) {
            c = 0x000000ff & bytes[i++];
            sb.append(base64EncodeChars[c >> 2]);
            sb.append(base64EncodeChars[(c & 0x03) << 4]);
            sb.append("==");
        } else if (r == 2) {
            c = (0x000000ff & bytes[i++]) << 8 | (0x000000ff & bytes[i++]);
            sb.append(base64EncodeChars[c >> 10]);
            sb.append(base64EncodeChars[c >> 4 & 0x3f]);
            sb.append(base64EncodeChars[(c & 0x0f) << 2]);
            sb.append("=");
        }
        return sb.toString();
    }

    /**
     * Base64 解码
     *
     * @param content     文本内容
     * @param charsetName 编码方式 如：UTF-8
     * @return 解码后的字节组成的字符串
     * @throws UnsupportedEncodingException
     */
    public static String decodeStr(String content, String charsetName) throws UnsupportedEncodingException {
        return new String(decode(content, charsetName), charsetName);
    }

    /**
     * Base64 解码
     *
     * @param bytes       待解码的字节数组
     * @param charsetName 编码方式 如：UTF-8
     * @return 解码后的字节组成的字符串
     * @throws UnsupportedEncodingException
     */
    public static String decodeStr(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
        return new String(decode(bytes), charsetName);
    }

    /**
     * Base64 解码
     *
     * @param content     文本内容
     * @param charsetName 编码方式 如：UTF-8
     * @return 解码后的字节数组
     * @throws UnsupportedEncodingException
     */
    public static byte[] decode(String content, String charsetName) throws UnsupportedEncodingException {
        byte[] data = content.getBytes(charsetName);
        return decode(data);
    }

    /**
     * Base64 解码
     *
     * @param bytes 待解码的字节数组
     * @return 解码后的字节数组
     */
    public static byte[] decode(byte[] bytes) {
        int len = bytes.length;
        ByteArrayOutputStream buf = new ByteArrayOutputStream((int) (len * 0.67));
        int i = 0;
        int b1, b2, b3, b4;
        while (i < len) {
            do {
                if (i >= len) {
                    b1 = -1;
                    break;
                }
                b1 = base64DecodeChars[bytes[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1) {
                break;
            }
            do {
                if (i >= len) {
                    b2 = -1;
                    break;
                }
                b2 = base64DecodeChars[bytes[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1) {
                break;
            }
            buf.write(((b1 << 2) | ((b2 & 0x30) >>> 4)));
            do {
                if (i >= len) {
                    b3 = -1;
                    break;
                }
                b3 = bytes[i++];
                if (b3 == 61) {
                    b3 = -1;
                    break;
                }
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1) {
                break;
            }
            buf.write((((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
            do {
                if (i >= len) {
                    b4 = -1;
                    break;
                }
                b4 = bytes[i++];
                if (b4 == 61) {
                    b4 = -1;
                    break;
                }
                b4 = base64DecodeChars[b4];
            } while (b4 == -1);
            if (b4 == -1) {
                break;
            }
            buf.write((((b3 & 0x03) << 6) | b4));
        }
        return buf.toByteArray();
    }

}
