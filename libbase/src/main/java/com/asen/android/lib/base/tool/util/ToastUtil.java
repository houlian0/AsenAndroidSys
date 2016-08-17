package com.asen.android.lib.base.tool.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Toast弹出信息的工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class ToastUtil {

    private static Toast toast = null;

    /**
     * Toast显示 Android默认的展示方式
     *
     * @param context Android上下文
     * @param msg     要显示的文字信息
     */
    public static void showSimpleToast(Context context, String msg) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Toast显示 下方显示
     *
     * @param context Android上下文
     * @param msg     要显示的文字信息
     */
    public static void showToast(Context context, String msg) {
        TextView textView = new TextView(context);
        textView.setText(msg);
        textView.setPadding(10, 5, 10, 5);
        textView.setBackgroundColor(Color.argb(0x85, 0xfc, 0xb1, 0x00));
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);

        if (toast != null)
            toast.cancel();
        toast = new Toast(context);
        toast.setView(textView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Toast显示 右边显示
     *
     * @param context Android上下文
     * @param msg     要显示的文字信息
     */
    public static void showToastRight(Context context, String msg) {
        TextView textView = new TextView(context);
        textView.setText(msg);
        textView.setPadding(10, 5, 10, 5);
        textView.setBackgroundColor(Color.argb(0x85, 0xfc, 0xb1, 0x00));
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);

        if (toast != null)
            toast.cancel();
        toast = new Toast(context);
        toast.setView(textView);
        toast.setGravity(Gravity.RIGHT, 60, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Toast显示 下方显示，必定在UI线程中执行
     *
     * @param context Android上下文
     * @param msg     要显示的文字信息
     */
    public static void showToast(final Activity activity, final String msg) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                TextView textView = new TextView(activity.getApplicationContext());
                textView.setText(msg);
                textView.setPadding(10, 5, 10, 5);
                textView.setBackgroundColor(Color.argb(0x85, 0xfc, 0xb1, 0x00));
                textView.setTextSize(16);
                textView.setTextColor(Color.BLACK);

                if (toast != null)
                    toast.cancel();
                toast = new Toast(activity);
                toast.setView(textView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    /**
     * Toast显示 右边显示
     *
     * @param context Android上下文
     * @param msg     要显示的文字信息
     */
    public static void showToastRight(final Activity activity, final String msg) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                TextView textView = new TextView(activity.getApplicationContext());
                textView.setText(msg);
                textView.setPadding(10, 5, 10, 5);
                textView.setBackgroundColor(Color.argb(0x85, 0xfc, 0xb1, 0x00));
                textView.setTextSize(16);
                textView.setTextColor(Color.BLACK);

                if (toast != null)
                    toast.cancel();
                toast = new Toast(activity);
                toast.setView(textView);
                toast.setGravity(Gravity.RIGHT, 60, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

}
