package com.asen.android.lib.base.core.network.urlconn;

import java.io.DataOutputStream;

/**
 * ����Ϣ���ݵļ����ӿ�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/8/16 16:15
 */
public interface OnHttpOutputStreamListener {

    /**
     * �����ڵ�ǰ�����ﴫ��������Ϣ��������
     *
     * @param outputStream �����
     */
    public void outputData(DataOutputStream outputStream);

}
