package com.asen.android.lib.base.tool.manage.action;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * ������Action
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public abstract class BaseAction<T> {

    /**
     * ����Action��ִ���ࣨActivity��Fragment��
     */
    protected T mParent; // ����Action��ִ���ࣨActivity��Fragment��

    /**
     * Android ������
     */
    protected Context mContext; // Android ������

    /**
     * ���� ����Action��ִ���ࣨActivity��Fragment��
     *
     * @param parent ����Action��ִ���ࣨActivity��Fragment��
     */
    void setParent(T parent) {
        mParent = parent;
        if (mParent instanceof Activity) {
            mContext = ((Activity) mParent).getApplicationContext();
        } else if (mParent instanceof Fragment) {
            mContext = ((Fragment) mParent).getActivity().getApplicationContext();
        }
    }

    /**
     * �����������ڴ˷����г�ʼ����Ϣ��ִֻ��һ�Σ�
     */
    public abstract void onCreate();

    /**
     * ִ�У�ÿ��ִ��Action������ã����ظ�ִ�У�
     *
     * @param isResume �Ƿ��ظ�ִ��
     * @param object   object����
     * @param bundle   bundle����
     */
    public abstract void execute(boolean isResume, Object object, Bundle bundle);

    /**
     * ���٣������ڴ˷�����������Ϣ��ִֻ��һ�Σ�
     */
    public abstract void onDestroy();

}