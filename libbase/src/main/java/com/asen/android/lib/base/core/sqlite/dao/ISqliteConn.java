package com.asen.android.lib.base.core.sqlite.dao;

import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;


/**
 * 数据库连接接口
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/18 11:22
 */
public interface ISqliteConn {

    /**
     * 打开只读的数据库连接
     *
     * @return 只读的数据库连接
     * @throws SQLException
     */
    public SQLiteDatabase openReadSQLiteDatabase() throws SQLException;

    /**
     * 关闭只读的数据库连接
     *
     * @throws SQLException
     */
    public void closeReadSQLiteDatabase() throws SQLException;

    /**
     * 打开可读写的数据库连接
     *
     * @return 可读写的数据库连接
     * @throws SQLException
     */
    public SQLiteDatabase openWriteSQLiteDatabase() throws SQLException;

    /**
     * 关闭可读写的数据库连接
     *
     * @throws SQLException
     */
    public void closeWriteSQLiteDatabase() throws SQLException;

    /**
     * 判断可读写的数据库是否在事务中
     *
     * @return true，当前数据库处在事务中；false，当前数据库不处在事务中
     * @throws SQLException
     */
    public boolean isInTransaction() throws SQLException;

    /**
     * 设置事务的状态，正常开始或结束事务
     *
     * @param isTransaction 是否开始或取消事务
     * @throws SQLException
     */
    public void setTransaction(boolean isTransaction) throws SQLException;

    /**
     * 非正常的情况下结束事务
     *
     * @throws SQLException
     */
    public void endTransaction() throws SQLException;


}
