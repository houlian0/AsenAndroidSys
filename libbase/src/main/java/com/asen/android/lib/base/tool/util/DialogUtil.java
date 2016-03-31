package com.asen.android.lib.base.tool.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

/**
 * Simple to Introduction
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class DialogUtil {

    private static ProgressDialog progressDialog = null;

    private static ProgressDialog horizontalProgressDialog = null;

    private static Dialog dialog = null;

    /**
     * 展示 ProgressDialog
     *
     * @param context
     * @param msg
     */
    public static void showProgressDialog(Activity context, String msg) {
        cancelProgressDialog();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 取消展示 ProgressDialog
     */
    public static void cancelProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            progressDialog = null;
        }
    }

    /**
     * 展示 水平ProgressDialog
     *
     * @param context
     */
    public static void showHorizontalProgressDialog(Activity context, int progress) {
        if (horizontalProgressDialog == null || !horizontalProgressDialog.isShowing()) {
            horizontalProgressDialog = new ProgressDialog(context);
            horizontalProgressDialog.setCancelable(false);
            horizontalProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            horizontalProgressDialog.show();
        }
        horizontalProgressDialog.setProgress(progress);
    }

    /**
     * 取消展示 水平ProgressDialog
     */
    public static void cancelHorizontalProgressDialog() {
        if (horizontalProgressDialog != null && horizontalProgressDialog.isShowing())
            horizontalProgressDialog.cancel();
        horizontalProgressDialog = null;
    }

    private static void cancelDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dialog = null;
        }
    }

    /**
     * 简单的消息弹出框
     *
     * @param activity
     * @param title
     * @param message
     * @param btnTexts  最多三条有效值
     * @param listeners
     */
    public static void showTitleMessageDialog(Activity activity, String title, String message, String[] btnTexts, DialogInterface.OnClickListener[] listeners) {
        cancelDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        if (!TextUtils.isEmpty(message)) builder.setMessage(message);
        builder.setCancelable(false);

        if (btnTexts == null || btnTexts.length == 0) return;

        int length = listeners == null ? -1 : listeners.length;
        for (int i = 0; i < btnTexts.length; i++) {
            if (i == 0) {
                builder.setPositiveButton(btnTexts[i], length > i ? listeners[i] : null);
            } else if (i == 1) {
                builder.setNeutralButton(btnTexts[i], length > i ? listeners[i] : null);
            } else if (i == 2) {
                builder.setNegativeButton(btnTexts[i], length > i ? listeners[i] : null);
            }
        }
        dialog = builder.show();
    }

    /**
     * 简单的消息弹出框
     *
     * @param activity
     * @param title
     * @param message
     * @param listener
     */
    public static void showTitleMessageDialog(Activity activity, String title, String message, DialogInterface.OnClickListener listener) {
        showTitleMessageDialog(activity, title, message, new String[]{"确认", "取消"}, new DialogInterface.OnClickListener[]{listener});
    }

    /**
     * 简单的选择框
     *
     * @param itemsId
     * @param tvShow
     * @param listener
     */
    public static void showTextSelectDialog(Activity activity, int itemsId, TextView tvShow, DialogInterface.OnClickListener listener) {
        String[] strs = activity.getResources().getStringArray(itemsId);
        showTextSelectDialog(activity, strs, tvShow, listener);
    }

    /**
     * 简单的选择框
     *
     * @param strList
     * @param tvShow
     * @param listener
     */
    public static void showTextSelectDialog(Activity activity, List<String> strList, TextView tvShow, DialogInterface.OnClickListener listener) {
        String[] strs = strList.toArray(new String[]{});
        showTextSelectDialog(activity, strs, tvShow, listener);
    }

    /**
     * 简单的选择框
     *
     * @param strs
     * @param tvShow
     * @param listener
     */
    public static void showTextSelectDialog(Activity activity, final String[] strs, final TextView tvShow, final DialogInterface.OnClickListener listener) {
        cancelDialog();
        if (strs == null) return;

        int index = StringUtil.indexOfStrings(strs, tvShow.getText().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setSingleChoiceItems(strs, index == -1 ? 0 : index, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                tvShow.setText(strs[which]);
                if (listener != null) {
                    listener.onClick(dialog, which);
                }
                dialog.dismiss();
            }
        });
        dialog = builder.show();
    }

    /**
     * 年月日选择框
     *
     * @param activity
     * @param listener
     */
    public static void showDateSelectDialog(Activity activity, DatePickerDialog.OnDateSetListener listener) {
        cancelDialog();
        Calendar calendar = Calendar.getInstance();
        dialog = new DatePickerDialog(activity, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    /**
     * 快速的年月日选择框
     *
     * @param activity
     * @param tv
     */
    public static void showQuickDateSelectDialog(Activity activity, final TextView tv) {
        showQuickDateSelectDialog(activity, tv, true, true, true, null);
    }

    private static void showQuickDateSelectDialog(Activity activity, final TextView tv, final boolean selectYear, final boolean selectMonth, final boolean selectDay, final DatePickerDialog.OnDateSetListener listener) {
        cancelDialog();
        Calendar calendar = Calendar.getInstance();
        dialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                String monthOfYearStr = monthOfYear < 10 ? "0" + monthOfYear : "" + monthOfYear;
                String dayOfMonthStr = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
                tv.setText(year + (selectMonth ? "-" + monthOfYearStr : "") + (selectDay ? "-" + dayOfMonthStr : ""));
                if (listener != null) listener.onDateSet(view, year, monthOfYear, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

        DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
        if (dp != null) {
            if (!selectYear)
                ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
            if (!selectMonth)
                ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
            if (!selectDay)
                ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        }
    }

    private static DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

    /**
     * 快速的日期时间选择器
     *
     * @param activity
     * @param tv
     */
    public static void showQuickDateTimeSelectDialog(Activity activity, final TextView tv) {
        showQuickDateTimeSelectDialog(activity, tv, null, null);
    }

    /**
     * 快速的日期时间选择器
     *
     * @param activity
     * @param tv
     * @param listener1 DatePicker.OnDateChangedListener
     * @param listener2 TimePicker.OnTimeChangedListener
     */
    public static void showQuickDateTimeSelectDialog(Activity activity, final TextView tv, final DatePicker.OnDateChangedListener listener1, final TimePicker.OnTimeChangedListener listener2) {
        cancelDialog();
        final Calendar calendar = Calendar.getInstance();
        // 初始化布局
        Context context = activity.getApplicationContext();
        DisplayMetrics metrics = AppUtil.getDisplayMetrics(context);
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(metrics.heightPixels > metrics.widthPixels ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        final DatePicker datePicker = new DatePicker(context);
        datePicker.setCalendarViewShown(false);
        datePicker.setSpinnersShown(true);
        setDatePickerTextColor(datePicker);
        final TimePicker timePicker = new TimePicker(context);
        setTimePickerTextColor(timePicker);
        mainLayout.addView(datePicker);
        mainLayout.addView(timePicker);
        // 初始化监听
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                dialog.setTitle(DateUtil.getStringByFormat(calendar.getTime(), DateUtil.dateFormatYMDHMS));
            }
        });
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                dialog.setTitle(DateUtil.getStringByFormat(calendar.getTime(), DateUtil.dateFormatYMDHMS));
            }
        });

        dialog = new AlertDialog.Builder(activity)
                .setTitle(DateUtil.getStringByFormat(calendar.getTime(), DateUtil.dateFormatYMDHMS))
                .setView(mainLayout)
                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (tv != null)
                            tv.setText(DateUtil.getStringByFormat(calendar.getTime(), DateUtil.dateFormatYMDHMS));
                        if (listener1 != null)
                            listener1.onDateChanged(datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        if (listener2 != null)
                            listener2.onTimeChanged(timePicker, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    }
                }).show();
    }

    /**
     * 设置 DatePicker 颜色为黑色
     *
     * @param datePicker
     */
    private static void setDatePickerTextColor(DatePicker datePicker) {
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            View view = mSpinners.getChildAt(i);
            if (view instanceof NumberPicker) {
                NumberPicker picker = (NumberPicker) view;
                setNumberPickerTextColor(picker, Color.BLACK);
            }
        }
    }

    /**
     * 设置 DatePicker 颜色为黑色
     *
     * @param timePicker
     */
    private static void setTimePickerTextColor(TimePicker timePicker) {
        LinearLayout mSpinners = (LinearLayout) timePicker.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            View view = mSpinners.getChildAt(i);
            if (view instanceof NumberPicker) {
                NumberPicker picker = (NumberPicker) view;
                setNumberPickerTextColor(picker, Color.BLACK);
            }
        }
    }

    /**
     * 设置NumberPicker 控件的颜色
     *
     * @param numberPicker
     * @param color
     * @return
     */
    private static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        boolean result = false;
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    result = true;
                } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}