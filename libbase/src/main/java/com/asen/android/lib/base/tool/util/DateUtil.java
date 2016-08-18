package com.asen.android.lib.base.tool.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * ʱ���ʽ������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class DateUtil {

    /**
     * The tag.
     */
    private static String TAG = DateUtil.class.getSimpleName();

    /**
     * ʱ�����ڸ�ʽ����������ʱ����.
     */
    public static final String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * ʱ�����ڸ�ʽ����������.
     */
    public static final String dateFormatYMD = "yyyy-MM-dd";

    /**
     * ʱ�����ڸ�ʽ��������.
     */
    public static final String dateFormatYM = "yyyy-MM";

    /**
     * ʱ�����ڸ�ʽ������.
     */
    public static final String dateFormatY = "yyyy";

    /**
     * ʱ�����ڸ�ʽ����������ʱ��.
     */
    public static final String dateFormatYMDHM = "yyyy-MM-dd HH:mm";

    /**
     * ʱ�����ڸ�ʽ��������.
     */
    public static final String dateFormatMD = "MM/dd";

    /**
     * ʱ����.
     */
    public static final String dateFormatHMS = "HH:mm:ss";

    /**
     * ʱ��.
     */
    public static final String dateFormatHM = "HH:mm";

    /**
     * ����.
     */
    public static final String AM = "AM";

    /**
     * ����.
     */
    public static final String PM = "PM";

    /**
     * ��õ���Ŀ�ʼʱ��
     *
     * @return ����Ŀ�ʼʱ�䣬00:00:00 000
     */
    public static Date getTodayStartDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * ��õ���Ľ���ʱ��
     *
     * @return ����Ľ���ʱ�䣬23:59:59 999
     */
    public static Date getTodayEndDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * ���ָ�����ڵĵĿ�ʼʱ��
     *
     * @return ָ�����ڵĵĿ�ʼʱ�䣬00:00:00 000
     */
    public static Date getStartDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * ���ָ�����ڵĽ���ʱ��
     *
     * @return ָ�����ڵĽ���ʱ�䣬23:59:59 999
     */
    public static Date getEndDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * ��õ�ǰ���� + ƫ�����ں�Ŀ�ʼʱ��
     *
     * @param offerSet ƫ�Ƶ�����
     * @return ��ʼʱ�䣬00:00:00 000
     */
    public static Date getStartDate(int offerSet) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, offerSet);
        return calendar.getTime();
    }

    /**
     * ��õ�ǰ���� + ƫ�����ں�Ľ���ʱ��
     *
     * @param offerSet ƫ�Ƶ�����
     * @return ����ʱ�䣬23:59:59 999
     */
    public static Date getEndDate(int offerSet) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.add(Calendar.DATE, offerSet);
        return calendar.getTime();
    }

    /**
     * ������String���͵�����ʱ��ת��ΪDate����.
     *
     * @param strDate String��ʽ������ʱ��
     * @param format  ��ʽ���ַ������磺"yyyy-MM-dd HH:mm:ss"
     * @return Date Date��������ʱ��
     */
    public static Date getDateByFormat(String strDate, String format) {
        if (strDate == null) return null;
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = mSimpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * ��������ȡƫ��֮���Date.
     *
     * @param date          ����ʱ��
     * @param calendarField Calendar���ԣ���Ӧoffset��ֵ��
     *                      ��(Calendar.DATE,��ʾ+offset��,Calendar.HOUR_OF_DAY,��ʾ��offsetСʱ)
     * @param offset        ƫ��(ֵ����0,��ʾ+,ֵС��0,��ʾ��)
     * @return Date ƫ��֮�������ʱ��
     */
    public static Date getDateByOffset(Date date, int calendarField, int offset) {
        Calendar c = new GregorianCalendar();
        try {
            c.setTime(date);
            c.add(calendarField, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * ��������ȡָ������ʱ����ַ���(��ƫ��).
     *
     * @param strDate       String��ʽ������ʱ��
     * @param format        ��ʽ���ַ������磺"yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar���ԣ���Ӧoffset��ֵ��
     *                      ��(Calendar.DATE,��ʾ+offset��,Calendar.HOUR_OF_DAY,��ʾ��offsetСʱ)
     * @param offset        ƫ��(ֵ����0,��ʾ+,ֵС��0,��ʾ��)
     * @return String String���͵�����ʱ��
     */
    public static String getStringByOffset(String strDate, String format,
                                           int calendarField, int offset) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(mSimpleDateFormat.parse(strDate));
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /**
     * ������Date����ת��ΪString����(��ƫ��).
     *
     * @param date          the date
     * @param format        the format
     * @param calendarField the calendar field
     * @param offset        the offset
     * @return String String��������ʱ��
     */
    public static String getStringByOffset(Date date, String format, int calendarField, int offset) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(date);
            c.add(calendarField, offset);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * ������Date����ת��ΪString����.
     *
     * @param date   the date
     * @param format the format
     * @return String String��������ʱ��
     */
    public static String getStringByFormat(Date date, String format) {
        if (date == null) return null;

        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * ��������ȡָ������ʱ����ַ���,���ڵ�����Ҫ�ĸ�ʽ.
     *
     * @param strDate String��ʽ������ʱ�䣬����Ϊyyyy-MM-dd HH:mm:ss��ʽ
     * @param format  �����ʽ���ַ������磺"yyyy-MM-dd HH:mm:ss"
     * @return String ת�����String���͵�����ʱ��
     */
    public static String getStringByFormat(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMDHMS);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /**
     * ��������ȡmilliseconds��ʾ������ʱ����ַ���.
     *
     * @param milliseconds the milliseconds
     * @param format       ��ʽ���ַ������磺"yyyy-MM-dd HH:mm:ss"
     * @return String ����ʱ���ַ���
     */
    public static String getStringByFormat(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    /**
     * ��������ȡ��ʾ��ǰ����ʱ����ַ���.
     *
     * @param format ��ʽ���ַ������磺"yyyy-MM-dd HH:mm:ss"
     * @return String String���͵ĵ�ǰ����ʱ��
     */
    public static String getCurrentDate(String format) {
        LogUtil.d(TAG, "getCurrentDate:" + format);
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;

    }

    /**
     * ��������ȡ��ʾ��ǰ����ʱ����ַ���(��ƫ��).
     *
     * @param format        ��ʽ���ַ������磺"yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar���ԣ���Ӧoffset��ֵ��
     *                      ��(Calendar.DATE,��ʾ+offset��,Calendar.HOUR_OF_DAY,��ʾ��offsetСʱ)
     * @param offset        ƫ��(ֵ����0,��ʾ+,ֵС��0,��ʾ��)
     * @return String String���͵�����ʱ��
     */
    public static String getCurrentDateByOffset(String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;

    }

    /**
     * ���������������������������.
     *
     * @param milliseconds1 ��һ��ʱ��ĺ����ʾ
     * @param milliseconds2 �ڶ���ʱ��ĺ����ʾ
     * @return int ���������
     */
    public static int getOffectDay(long milliseconds1, long milliseconds2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(milliseconds1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(milliseconds2);
        // ���ж��Ƿ�ͬ��
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = d1 - d2;
        }
        return day;
    }

    /**
     * �����������������������Сʱ��.
     *
     * @param date1 ��һ��ʱ��ĺ����ʾ
     * @param date2 �ڶ���ʱ��ĺ����ʾ
     * @return int �����Сʱ��
     */
    public static int getOffectHour(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int h = 0;
        int day = getOffectDay(date1, date2);
        h = h1 - h2 + day * 24;
        return h;
    }

    /**
     * ����������������������ķ�����.
     *
     * @param date1 ��һ��ʱ��ĺ����ʾ
     * @param date2 �ڶ���ʱ��ĺ����ʾ
     * @return int ����ķ�����
     */
    public static int getOffectMinutes(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int m1 = calendar1.get(Calendar.MINUTE);
        int m2 = calendar2.get(Calendar.MINUTE);
        int h = getOffectHour(date1, date2);
        int m = 0;
        m = m1 - m2 + h * 60;
        return m;
    }

    /**
     * ��������ȡ����һ.
     *
     * @param format the format
     * @return String String��������ʱ��
     */
    public static String getFirstDayOfWeek(String format) {
        return getDayOfWeek(format, Calendar.MONDAY);
    }

    /**
     * ��������ȡ������.
     *
     * @param format the format
     * @return String String��������ʱ��
     */
    public static String getLastDayOfWeek(String format) {
        return getDayOfWeek(format, Calendar.SUNDAY);
    }

    /**
     * ��������ȡ���ܵ�ĳһ��.
     *
     * @param format        the format
     * @param calendarField the calendar field
     * @return String String��������ʱ��
     */
    public static String getDayOfWeek(String format, int calendarField) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            int week = c.get(Calendar.DAY_OF_WEEK);
            if (week == calendarField) {
                strDate = mSimpleDateFormat.format(c.getTime());
            } else {
                int offectDay = calendarField - week;
                if (calendarField == Calendar.SUNDAY) {
                    offectDay = 7 - Math.abs(offectDay);
                }
                c.add(Calendar.DATE, offectDay);
                strDate = mSimpleDateFormat.format(c.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * ��������ȡ���µ�һ��.
     *
     * @param format the format
     * @return String String��������ʱ��
     */
    public static String getFirstDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            // ��ǰ�µĵ�һ��
            c.set(GregorianCalendar.DAY_OF_MONTH, 1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;

    }

    /**
     * ��������ȡ�������һ��.
     *
     * @param format the format
     * @return String String��������ʱ��
     */
    public static String getLastDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            // ��ǰ�µ����һ��
            c.set(Calendar.DATE, 1);
            c.roll(Calendar.DATE, -1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * ��������ȡ��ʾ��ǰ���ڵ�0��ʱ�������.
     *
     * @return the first time of day
     */
    public static long getFirstTimeOfDay() {
        Date date = null;
        try {
            String currentDate = getCurrentDate(dateFormatYMD);
            date = getDateByFormat(currentDate + " 00:00:00", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * ��������ȡ��ʾ��ǰ����24��ʱ�������.
     *
     * @return the last time of day
     */
    public static long getLastTimeOfDay() {
        Date date = null;
        try {
            String currentDate = getCurrentDate(dateFormatYMD);
            date = getDateByFormat(currentDate + " 24:00:00", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * �������ж��Ƿ�������()
     * (year�ܱ�4���� ���� ���ܱ�100����) ���� year�ܱ�400����,�����Ϊ����.
     *
     * @param year �������2012��
     * @return boolean �Ƿ�Ϊ����
     */
    public static boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ����������ʱ�䷵�ظ�ʽ�����ʱ�������. С��1Сʱ��ʾ���ٷ���ǰ ����1Сʱ��ʾ���죫ʵ�����ڣ����ڽ���ȫ����ʾʵ��ʱ��
     *
     * @param strDate   the str date
     * @param outFormat the out format
     * @return the string
     */
    public static String formatDateStr2Desc(String strDate, String outFormat) {

        DateFormat df = new SimpleDateFormat(dateFormatYMDHMS);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c2.setTime(df.parse(strDate));
            c1.setTime(new Date());
            int d = getOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
            if (d == 0) {
                int h = getOffectHour(c1.getTimeInMillis(),
                        c2.getTimeInMillis());
                if (h > 0) {
                    return "����" + getStringByFormat(strDate, dateFormatHM);
                    // return h + "Сʱǰ";
                } else if (h < 0) {
                    // return Math.abs(h) + "Сʱ��";
                } else if (h == 0) {
                    int m = getOffectMinutes(c1.getTimeInMillis(),
                            c2.getTimeInMillis());
                    if (m > 0) {
                        return m + "����ǰ";
                    } else if (m < 0) {
                        // return Math.abs(m) + "���Ӻ�";
                    } else {
                        return "�ո�";
                    }
                }

            } else if (d > 0) {
                if (d == 1) {
                    // return "����"+getStringByFormat(strDate,outFormat);
                } else if (d == 2) {
                    // return "ǰ��"+getStringByFormat(strDate,outFormat);
                }
            } else if (d < 0) {
                if (d == -1) {
                    // return "����"+getStringByFormat(strDate,outFormat);
                } else if (d == -2) {
                    // return "����"+getStringByFormat(strDate,outFormat);
                } else {
                    // return Math.abs(d) +
                    // "���"+getStringByFormat(strDate,outFormat);
                }
            }

            String out = getStringByFormat(strDate, outFormat);
            if (!TextUtils.isEmpty(out)) {
                return out;
            }
        } catch (Exception e) {
        }

        return strDate;
    }

    /**
     * ȡָ������Ϊ���ڼ�.
     *
     * @param strDate  ָ������
     * @param inFormat ָ�����ڸ�ʽ
     * @return String ���ڼ�
     */
    public static String getWeekNumber(String strDate, String inFormat) {
        String week = "������";
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat(inFormat);
        try {
            calendar.setTime(df.parse(strDate));
        } catch (Exception e) {
            return "����";
        }
        int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        switch (intTemp) {
            case 0:
                week = "������";
                break;
            case 1:
                week = "����һ";
                break;
            case 2:
                week = "���ڶ�";
                break;
            case 3:
                week = "������";
                break;
            case 4:
                week = "������";
                break;
            case 5:
                week = "������";
                break;
            case 6:
                week = "������";
                break;
        }
        return week;
    }

    /**
     * ������ڵ�Intֵ��0-6
     *
     * @return 0 ~ 6:���� ~ ����
     */
    public static int getCurrentWeekNum() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * ��һ���� 1����һ����2 ...
     *
     * @return 1 ~ 14
     */
    public static int getCurrentWeekMark() {
        int weekNum = DateUtil.getCurrentWeekNum();
        String quantum = DateUtil.getTimeQuantum();

        int result = weekNum << 1;
        if ("AM".equals(quantum)) {
            result += 1;
        } else {
            result += 2;
        }
        return result;
    }

    /**
     * ���ݸ����������ж��Ƿ�Ϊ������
     *
     * @param strDate ��ʽ��������
     * @param format  ���ڵĸ�ʽ
     * @return PM or AM
     */
    public static String getTimeQuantum(String strDate, String format) {
        Date mDate = getDateByFormat(strDate, format);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(mDate);
        // int hour = mDate.getHours();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 12)
            return "PM";
        else
            return "AM";
    }

    /**
     * �жϵ�ǰʱ���Ƿ�Ϊ������
     *
     * @return PM or AM
     */
    public static String getTimeQuantum() {
        Calendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 12)
            return "PM";
        else
            return "AM";
    }

    /**
     * ���ݸ����ĺ��������ʱ�������
     *
     * @param milliseconds ����ֵ
     * @return ��������
     */
    public static String getTimeDescription(long milliseconds) {
        if (milliseconds > 1000) {
            // ����һ��
            if (milliseconds / 1000 / 60 > 1) {
                long minute = milliseconds / 1000 / 60;
                long second = milliseconds / 1000 % 60;
                return minute + "��" + second + "��";
            } else {
                // ��ʾ��
                return milliseconds / 1000 + "��";
            }
        } else {
            return milliseconds + "����";
        }
    }
}
