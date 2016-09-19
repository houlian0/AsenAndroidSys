package com.asen.android.lib.base.core.sqlite.dao;

import java.sql.SQLException;

/**
 * 调用事务的接口
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/18 14:36
 */
public interface CallTransaction<T> {

    /**
     * 事务操作的具体实现
     *
     * @return 事务执行完后，返回的结果
     * @throws SQLException
     */
    public T call() throws SQLException;
}
