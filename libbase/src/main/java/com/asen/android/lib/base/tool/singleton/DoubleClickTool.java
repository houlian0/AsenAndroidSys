package com.asen.android.lib.base.tool.singleton;

import android.content.Context;

import com.asen.android.lib.base.tool.util.ToastUtil;

import java.util.Date;

/**
 * Created by HL_SEN on 2015/9/22.
 * 两次返回键退出应用的判断类，调用isDoubleClick()方法即可
 *
 * @author ASEN
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
     * @param message 要展示的信息内容
     */
    public void showMessage(String message) {
        ToastUtil.showToast(_context, message);
    }

    /**
     * "再按一次返回键关闭程序"
     *
     * @return 是否已经是第二次点击
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
     * @param message 不是第二次点击的话，需要传入的提示信息
     * @return 是否已经是第二次点击
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
