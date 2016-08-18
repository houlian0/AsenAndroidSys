package com.asen.android.lib.base.core.network.urlconn;

import java.io.DataOutputStream;

/**
 * 流信息传递的监听接口
 *
 * @author Asen
 * @version v1.0
 * @date 2016/8/16 16:15
 */
public interface OnHttpOutputStreamListener {

    /**
     * 可以在当前方法里传递任意信息到服务器
     *
     * @param outputStream 输出流
     */
    public void outputData(DataOutputStream outputStream);

}
