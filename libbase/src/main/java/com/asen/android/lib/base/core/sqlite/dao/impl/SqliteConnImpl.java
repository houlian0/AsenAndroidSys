package com.asen.android.lib.base.core.sqlite.dao.impl;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.asen.android.lib.base.core.sqlite.dao.ISqliteConn;
import com.asen.android.lib.base.core.sqlite.utils.SqlExceptionUtil;

import java.sql.SQLException;


/**
 * 数据库连接的实现
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/18 11:24
 */
class SqliteConnImpl implements ISqliteConn {

    private SQLiteOpenHelper mOpenHelper;

    public SqliteConnImpl(SQLiteOpenHelper openHelper) {
        this.mOpenHelper = openHelper;
    }

    // 只读的连接获取方式（支持多线程）
    private ThreadLocal<SQLiteDatabase> mReadSQLiteDatabase = new ThreadLocal<>();

    // 读写的连接获取方式（支持多线程）
    private ThreadLocal<SQLiteDatabase> mWriteSQLiteDatabase = new ThreadLocal<>();

    @Override
    public SQLiteDatabase openReadSQLiteDatabase() throws SQLException {
        SQLiteDatabase database = mWriteSQLiteDatabase.get();
        if (database == null) {
            database = mReadSQLiteDatabase.get();
            if (database == null) {
                database = mOpenHelper.getReadableDatabase();
                if (database == null) {
                    // 当数据库版本改变时，首次获取连接必须要在读写状态下
                    database = mOpenHelper.getWritableDatabase();
                    mWriteSQLiteDatabase.set(database);
                } else {
                    mReadSQLiteDatabase.set(database);
                }
            }
        }
        return database;
    }

    @Override
    public void closeReadSQLiteDatabase() throws SQLException {
        SQLiteDatabase database = mReadSQLiteDatabase.get();
        if (database != null) {
            database.close();
            mReadSQLiteDatabase.set(null);
        } else {
            closeWriteSQLiteDatabase();
        }
    }

    @Override
    public SQLiteDatabase openWriteSQLiteDatabase() throws SQLException {
        SQLiteDatabase database = mWriteSQLiteDatabase.get();
        if (database == null) {
            mWriteSQLiteDatabase.set(mOpenHelper.getWritableDatabase());
        }
        return mWriteSQLiteDatabase.get();
    }

    @Override
    public void closeWriteSQLiteDatabase() throws SQLException {
        SQLiteDatabase database = mWriteSQLiteDatabase.get();
        if (database != null && !isInTransaction()) {
            // 数据打开着，并且不在事务中，可以关闭数据库
            database.close();
            mWriteSQLiteDatabase.set(null);
        }
    }

    @Override
    public boolean isInTransaction() throws SQLException {
        try {
            SQLiteDatabase database = mWriteSQLiteDatabase.get();
            if (database == null) {
                throw SqlExceptionUtil.create("SQLiteDatabase is not open!", null);
            } else {
                return database.inTransaction();
            }
        } catch (Exception e) {
            throw SqlExceptionUtil.create("SQLiteDatabase is not open!", e);
        }
    }

    @Override
    public void setTransaction(boolean isTransaction) throws SQLException {
        SQLiteDatabase database = mWriteSQLiteDatabase.get();
        if (database == null) {
            throw SqlExceptionUtil.create("SQLiteDatabase is not open!", null);
        } else {
            if (isTransaction) { // 开始事务
                if (!database.inTransaction()) {
                    database.beginTransaction();
                }
            } else { // 结束事务
                if (database.inTransaction()) {
                    database.setTransactionSuccessful();
                    database.endTransaction();
                }
            }
        }
    }

    @Override
    public void endTransaction() throws SQLException {
        SQLiteDatabase database = mWriteSQLiteDatabase.get();
        if (database == null) {
            throw SqlExceptionUtil.create("SQLiteDatabase is not open!", null);
        } else {
            if (database.inTransaction()) {
                database.endTransaction();
            }
        }
    }

}
