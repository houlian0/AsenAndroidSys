package com.asen.android.lib.base.tool.singleton;

import android.content.Context;

import com.asen.android.lib.base.tool.util.ToastUtil;

import java.util.Date;

/**
 * ���η��ؼ��˳�Ӧ�õ��ж��࣬����isDoubleClick()��������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public class DoubleClickTool {

    private static volatile DoubleClickTool tt;

    private Context _context;

    private long preTime;

    private long totalTime = 2000;// ˫������������

    private DoubleClickTool(Context context) {
        _context = context;
        preTime = 0;
    }

    /**
     * ��ȡDoubleClickToolʵ�� ,����ģʽ
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
     * ����Toast��ʾ��Ϣ
     *
     * @param message Ҫչʾ����Ϣ����
     */
    void showMessage(String message) {
        ToastUtil.showToast(_context, message);
    }

    /**
     * �ж��Ƿ��������ĵڶ��ε����������ǵĻ�������"�ٰ�һ�η��ؼ��رճ���"�ĶԻ���
     *
     * @return �Ƿ��Ѿ��ǵڶ��ε�����ǵĻ�������true�����򷵻�false��
     */
    public boolean isDoubleClick() {
        long nowTime = new Date().getTime();
        if (nowTime < preTime + totalTime) {
            return true;
        } else {
            showMessage("�ٰ�һ�η��ؼ��رճ���");
            preTime = nowTime;
            return false;
        }
    }

    /**
     * �ж��Ƿ��������ĵڶ��ε����������ǵĻ�������"�ٰ�һ�η��ؼ��رճ���"�ĶԻ���
     *
     * @param message ���벻�ǵڶ��ε��ʱ����ʾ��Ϣ
     * @return �Ƿ��Ѿ��ǵڶ��ε�����ǵĻ�������true�����򷵻�false��
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
