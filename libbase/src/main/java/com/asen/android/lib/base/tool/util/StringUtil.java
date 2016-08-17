package com.asen.android.lib.base.tool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �ַ���������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class StringUtil {

    /**
     * ���String��String[]�е��±�
     *
     * @param strs String����
     * @param str  Ҫ��ѯ��string
     * @return ��Ӧ���±꣬���û�鵽�Ļ�������-1
     */
    public static int indexOfStrings(CharSequence[] strs, CharSequence str) {
        int index = -1;
        if (strs != null && str != null) {
            int length = strs.length;
            for (int i = 0; i < length; i++) {
                if (str.equals(strs[i])) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    /**
     * ��������nullת��Ϊ����.
     *
     * @param str ָ�����ַ���
     * @return �ַ�����String����
     */
    public static String parseEmpty(String str) {
        if (str == null || "null".equals(str.trim())) {
            str = "";
        }
        return str.trim();
    }

    /**
     * ȥ���ַ���ĩβ�Ŀո�ȿշ���
     *
     * @param s ָ�����ַ���
     * @return �������ַ���
     */
    public String replaceBlank(String s) {
        if (s == null)
            return null;
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(s);
        return m.replaceAll("");
    }

    /**
     * �ж��ַ����Ƿ�Ϊ��
     *
     * @param s ��Ҫ�жϵ��ַ���
     * @return �����Ϊ�գ�����true�����򷵻�false
     */
    public static boolean isNotEmpty(String s) {
        return s != null && !"".equals(s.trim());
    }

    /**
     * �ж��ַ����Ƿ�Ϊ��
     *
     * @param s ��Ҫ�жϵ��ַ���
     * @return ���Ϊ�գ�����true�����򷵻�false
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    /**
     * ��ȡ�ַ��������ַ��ĳ��ȣ�ÿ��������2���ַ���.
     *
     * @param str ָ�����ַ���
     * @return �����ַ��ĳ���
     */
    public static int chineseLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* ��ȡ�ֶ�ֵ�ĳ��ȣ�����������ַ�����ÿ�������ַ�����Ϊ2������Ϊ1 */
        if (!isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                /* ��ȡһ���ַ� */
                String temp = str.substring(i, i + 1);
                /* �ж��Ƿ�Ϊ�����ַ� */
                if (temp.matches(chinese)) {
                    valueLength += 2;
                }
            }
        }
        return valueLength;
    }

    /**
     * ��������ȡ�ַ����ĳ���.
     *
     * @param str ָ�����ַ���
     * @return �ַ����ĳ��ȣ������ַ���2����
     */
    public static int strLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            // ��ȡ�ֶ�ֵ�ĳ��ȣ�����������ַ�����ÿ�������ַ�����Ϊ2������Ϊ1
            for (int i = 0; i < str.length(); i++) {
                // ��ȡһ���ַ�
                String temp = str.substring(i, i + 1);
                // �ж��Ƿ�Ϊ�����ַ�
                if (temp.matches(chinese)) {
                    // �����ַ�����Ϊ2
                    valueLength += 2;
                } else {
                    // �����ַ�����Ϊ1
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }

    /**
     * ��������ȡָ�����ȵ��ַ�����λ��.
     *
     * @param str  ָ�����ַ���
     * @param maxL Ҫȡ���ĳ��ȣ��ַ����ȣ������ַ���2����
     * @return �ַ�������λ��
     */
    public static int subStringLength(String str, int maxL) {
        int currentIndex = 0;
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        // ��ȡ�ֶ�ֵ�ĳ��ȣ�����������ַ�����ÿ�������ַ�����Ϊ2������Ϊ1
        for (int i = 0; i < str.length(); i++) {
            // ��ȡһ���ַ�
            String temp = str.substring(i, i + 1);
            // �ж��Ƿ�Ϊ�����ַ�
            if (temp.matches(chinese)) {
                // �����ַ�����Ϊ2
                valueLength += 2;
            } else {
                // �����ַ�����Ϊ1
                valueLength += 1;
            }
            if (valueLength >= maxL) {
                currentIndex = i;
                break;
            }
        }
        return currentIndex;
    }

    /**
     * �������ֻ��Ÿ�ʽ��֤.
     *
     * @param str ָ�����ֻ������ַ���
     * @return �Ƿ�Ϊ�ֻ������ʽ:��Ϊtrue������false
     */
    public static boolean isMobileNo(String str) {
        boolean isMobileNo = false;
        try {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(str);
            isMobileNo = m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isMobileNo;
    }

    /**
     * �������Ƿ�ֻ����ĸ������.
     *
     * @param str ָ�����ַ���
     * @return �Ƿ�ֻ����ĸ������:��Ϊtrue������false
     */
    public static Boolean isNumberLetter(String str) {
        Boolean isNoLetter = false;
        String expr = "^[A-Za-z0-9]+$";
        if (str.matches(expr)) {
            isNoLetter = true;
        }
        return isNoLetter;
    }

    /**
     * �������Ƿ�ֻ������.
     *
     * @param str ָ�����ַ���
     * @return �Ƿ�ֻ������:��Ϊtrue������false
     */
    public static Boolean isNumber(String str) {
        Boolean isNumber = false;
        String expr = "^[0-9]+$";
        if (str.matches(expr)) {
            isNumber = true;
        }
        return isNumber;
    }

    /**
     * �������Ƿ�������.
     *
     * @param str ָ�����ַ���
     * @return �Ƿ�������:��Ϊtrue������false
     */
    public static Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

    /**
     * �������Ƿ�������.
     *
     * @param str ָ�����ַ���
     * @return �Ƿ�������:��Ϊtrue������false
     */
    public static Boolean isChinese(String str) {
        Boolean isChinese = true;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            // ��ȡ�ֶ�ֵ�ĳ��ȣ�����������ַ�����ÿ�������ַ�����Ϊ2������Ϊ1
            for (int i = 0; i < str.length(); i++) {
                // ��ȡһ���ַ�
                String temp = str.substring(i, i + 1);
                // �ж��Ƿ�Ϊ�����ַ�
                if (!temp.matches(chinese)) {
                    isChinese = false;
                }
            }
        }
        return isChinese;
    }

    /**
     * �������Ƿ��������.
     *
     * @param str ָ�����ַ���
     * @return �Ƿ��������:��Ϊtrue������false
     */
    public static Boolean isContainChinese(String str) {
        Boolean isChinese = false;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            // ��ȡ�ֶ�ֵ�ĳ��ȣ�����������ַ�����ÿ�������ַ�����Ϊ2������Ϊ1
            for (int i = 0; i < str.length(); i++) {
                // ��ȡһ���ַ�
                String temp = str.substring(i, i + 1);
                // �ж��Ƿ�Ϊ�����ַ�
                if (temp.matches(chinese)) {
                    isChinese = true;
                }
            }
        }
        return isChinese;
    }

    /**
     * ���������������л��String.
     *
     * @param is ������
     * @return ��õ�String
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            // ���һ��\nɾ��
            if (sb.indexOf("\n") != -1 && sb.lastIndexOf("\n") == sb.length() - 1) {
                sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n") + 1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * ��������׼������ʱ�����͵����ݣ�������λ�Ĳ�0.
     *
     * @param dateTime Ԥ��ʽ��ʱ���ַ�������:2012-3-2 12:2:20
     * @return String ��ʽ���õ�ʱ���ַ�������:2012-03-20 12:02:20
     */
    public static String dateTimeFormat(String dateTime) {
        StringBuilder sb = new StringBuilder();
        try {
            if (isEmpty(dateTime)) {
                return null;
            }
            String[] dateAndTime = dateTime.split(" ");
            if (dateAndTime.length > 0) {
                for (String str : dateAndTime) {
                    if (str.contains("-")) {
                        String[] date = str.split("-");
                        for (int i = 0; i < date.length; i++) {
                            String str1 = date[i];
                            sb.append(strFormat2(str1));
                            if (i < date.length - 1) {
                                sb.append("-");
                            }
                        }
                    } else if (str.contains(":")) {
                        sb.append(" ");
                        String[] date = str.split(":");
                        for (int i = 0; i < date.length; i++) {
                            String str1 = date[i];
                            sb.append(strFormat2(str1));
                            if (i < date.length - 1) {
                                sb.append(":");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    /**
     * ����������2���ַ�����ǰ�油��0��.
     *
     * @param str ָ�����ַ���
     * @return ����2���ַ����ַ���
     */
    public static String strFormat2(String str) {
        try {
            if (str.length() <= 1) {
                str = "0" + str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * ��������ȡ�ַ�����ָ���ֽڳ���.
     *
     * @param str    the str
     * @param length ָ���ֽڳ���
     * @return ��ȡ����ַ���
     */
    public static String cutString(String str, int length) {
        return cutString(str, length, "");
    }

    /**
     * ��������ȡ�ַ�����ָ���ֽڳ���.
     *
     * @param str    �ı�
     * @param length �ֽڳ���
     * @param dot    ʡ�Է���
     * @return ��ȡ����ַ���
     */
    public static String cutString(String str, int length, String dot) {
        int strBLen = strlen(str, "GBK");
        if (strBLen <= length) {
            return str;
        }
        int temp = 0;
        StringBuffer sb = new StringBuffer(length);
        char[] ch = str.toCharArray();
        for (char c : ch) {
            sb.append(c);
            if (c > 256) {
                temp += 2;
            } else {
                temp += 1;
            }
            if (temp >= length) {
                if (dot != null) {
                    sb.append(dot);
                }
                break;
            }
        }
        return sb.toString();
    }

    /**
     * ��������ȡ�ַ����ӵ�һ��ָ���ַ�.
     *
     * @param str1   ԭ�ı�
     * @param str2   ָ���ַ�
     * @param offset ƫ�Ƶ�����
     * @return ��ȡ����ַ���
     */
    public static String cutStringFromChar(String str1, String str2, int offset) {
        if (isEmpty(str1)) {
            return "";
        }
        int start = str1.indexOf(str2);
        if (start != -1) {
            if (str1.length() > start + offset) {
                return str1.substring(start + offset);
            }
        }
        return "";
    }

    /**
     * ��������ȡ�ֽڳ���.
     *
     * @param str     �ı�
     * @param charset �ַ�����GBK��
     * @return the int
     */
    public static int strlen(String str, String charset) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int length = 0;
        try {
            length = str.getBytes(charset).length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * ��ȡ��С������.
     *
     * @param size �ֽڸ���
     * @return ��С������
     */
    public static String getSizeDesc(long size) {
        String suffix = "B";
        if (size >= 1024) {
            suffix = "K";
            size = size >> 10;
            if (size >= 1024) {
                suffix = "M";
                // size /= 1024;
                size = size >> 10;
                if (size >= 1024) {
                    suffix = "G";
                    size = size >> 10;
                    // size /= 1024;
                }
            }
        }
        return size + suffix;
    }

    /**
     * ������ip��ַת��Ϊ10������.
     *
     * @param ip the ip
     * @return the long
     */
    public static long ip2int(String ip) {
        ip = ip.replace(".", ",");
        String[] items = ip.split(",");
        return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16 | Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
    }

}
