package com.asen.android.lib.base.tool.manage.fragment;

import android.os.Bundle;

/**
 * Fragment��ʾʱ�ļ����ӿ�
 *
 * @param <T>
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public interface OnFragmentRefreshListener<T> {

    /**
     * ��ʾFragmentʱ����
     *
     * @param parent ������Ϣ��Activity����Fragment��
     * @param data   ���ݵ�������Ϣ
     */
    public void onRefresh(T parent, Bundle data);

}
