package com.asen.android.lib.base.core.sqlite.utils;


import java.sql.SQLException;

/**
 * SQL语句异常管理
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/18 11:56
 */
public class SqlExceptionUtil {

    /**
     * 创建新的SQL语句异常
     *
     * @param message 消息
     * @param cause   错误
     * @return SQL语句异常
     */
    public static SQLException create(String message, Throwable cause) {
        SQLException sqlException = new SQLException(message);
        if (cause != null)
            sqlException.initCause(cause);
        return sqlException;
    }

}
