package com.asen.android.lib.base.tool.singleton;

import android.content.Context;

import com.asen.android.lib.base.tool.util.ToastUtil;

import java.util.Date;

/**
 * 两次返回键退出应用的判断类，调用isDoubleClick()方法即可
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public class DoubleClickTool {

    private static volatile DoubleClickTool tt;

    private Context _context;

    private long preTime;

    private long totalTime = 2000;// 双点击，间隔两秒

    private DoubleClickTool(Context context) {
        _context = context;
        preTime = 0;
    }

    /**
     * 获取DoubleClickTool实例 ,单例模式
     */
    public static DoubleClickTool getInstance(Context context) {
        if (null == tt) {
            synchronized (DoubleClickTool.class) {
                if (null == tt) {
                    tt = new DoubleClickTool(context);
                }
            }
        }
        return tt;
    }

    /**
     * 弹出Toast显示信息
     *
     * @param message 要展示的信息内容
     */
    void showMessage(String message) {
        ToastUtil.showToast(_context, message);
    }

    /**
     * 判断是否是连续的第二次点击，如果不是的话，弹出"再按一次返回键关闭程序"的对话框
     *
     * @return 是否已经是第二次点击。是的话，返回true，否则返回false。
     */
    public boolean isDoubleClick() {
        long nowTime = new Date().getTime();
        if (nowTime < preTime + totalTime) {
            return true;
        } else {
            showMessage("再按一次返回键关闭程序");
            preTime = nowTime;
            return false;
        }
    }

    /**
     * 判断是否是连续的第二次点击，如果不是的话，弹出"再按一次返回键关闭程序"的对话框
     *
     * @param message 传入不是第二次点击时的提示信息
     * @return 是否已经是第二次点击。是的话，返回true，否则返回false。
     */
    public boolean isDoubleClick(String message) {
        long nowTime = new Date().getTime();
        if (nowTime < preTime + totalTime) {
            return true;
        } else {
            showMessage(message);
            preTime = nowTime;
            return false;
        }
    }
    
}
