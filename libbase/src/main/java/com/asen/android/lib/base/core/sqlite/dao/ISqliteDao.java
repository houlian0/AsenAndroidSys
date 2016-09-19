package com.asen.android.lib.base.core.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asen.android.lib.base.core.sqlite.builder.IBaseBuilder;
import com.asen.android.lib.base.core.sqlite.builder.ICreateOrUpdateBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IDeleteBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IInsertBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IQueryBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IReplaceBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IUpdateBuilder;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * SQL语句Dao的接口
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/17 11:57
 */
public interface ISqliteDao {

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
     * 执行非查询类的SQL语句
     *
     * @param sql    非查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     * @throws SQLException SQL语句异常
     */
    public void executeSql(String sql, Object... params) throws SQLException;

    /**
     * 执行插入语句
     *
     * @param table          表名
     * @param nullColumnHack 当values参数为空或者里面没有内容的时候，insert是会失败的(底层数据库不允许插入一个空行)，为了防止这种情况，
     *                       要在这里指定一个列名，到时候如果发现将要插入的行为空行时，就会将你指定的这个列名的值设为null，然后再向数据库中插入
     * @param values         要插入的参数
     * @return 返回插入记录的行号，如果为-1则插入失败
     * @throws SQLException SQL语句异常
     */
    public long executeInsert(String table, String nullColumnHack, ContentValues values) throws SQLException;

    /**
     * 执行替换语句
     *
     * @param table          表名
     * @param nullColumnHack 当values参数为空或者里面没有内容的时候，insert是会失败的(底层数据库不允许插入一个空行)，为了防止这种情况，
     *                       要在这里指定一个列名，到时候如果发现将要插入的行为空行时，就会将你指定的这个列名的值设为null，然后再向数据库中插入
     * @param values         要插入的参数
     * @return 返回插入记录的行号，如果为-1则插入失败
     * @throws SQLException SQL语句异常
     */
    public long executeReplace(String table, String nullColumnHack, ContentValues values) throws SQLException;

    /**
     * 执行更新语句
     *
     * @param table       表名
     * @param values      要更新的参数
     * @param whereClause 要更新的数据条件
     * @param whereArgs   条件中带'?'的参数，参数个数需与'?'个数一致
     * @return 返回更新记录的记录数量
     * @throws SQLException SQL语句异常
     */
    public int executeUpdate(String table, ContentValues values, String whereClause, String[] whereArgs) throws SQLException;

    /**
     * 执行删除语句
     *
     * @param table       表名
     * @param whereClause 要更新的数据条件
     * @param whereArgs   条件中带'?'的参数，参数个数需与'?'个数一致
     * @return 返回删除记录的记录数量
     * @throws SQLException
     */
    public int executeDelete(String table, String whereClause, String[] whereArgs) throws SQLException;

    /**
     * 执行SQL事务
     *
     * @param call call 事务接口
     * @param <T>  事务的返回值，可自定义
     * @return 事务的返回值
     * @throws SQLException SQL语句异常
     */
    public <T> T executeTransaction(CallTransaction<T> call) throws SQLException;

    /**
     * 查询结果将游标Cursor转成指定对象（此方法中不会关闭游标和数据库连接）
     *
     * @param cursor 游标
     * @param cls    对象的Class
     * @param <T>    对象类型
     * @return 返回查询到的对象集合
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public <T> List<T> cursorToObject(Cursor cursor, Class<T> cls) throws SQLException, IllegalAccessException, InstantiationException, InvocationTargetException;

    /**
     * 执行查询语句并返回游标（此方法中不会关闭游标和数据库连接）
     *
     * @param sql    查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     * @return 返回查询后所查询到的游标
     * @throws SQLException
     */
    public Cursor query(String sql, String... params) throws SQLException;

    /**
     * 执行查询语句并返回游标（此方法中不会关闭游标和数据库连接）
     *
     * @param table         要查询的标明
     * @param columns       想要显示的列，若为空则返回所有列
     * @param selection     where子句，声明要返回的行的要求，如果为空则返回表的所有行
     * @param selectionArgs where子句对应的条件值
     * @param groupBy       分组方式，若为空则不分组
     * @param having        having条件，若为空则返回全部
     * @param orderBy       排序方式，为空则为默认排序方式
     * @param limit         限制返回的记录的条数，为空则不限制
     * @return 返回查询后所查询到的游标
     * @throws SQLException SQL语句异常
     */
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws SQLException;

    /**
     * 执行查询类的SQL语句
     *
     * @param sql    查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     * @return 返回查询后所查询到的数据结果
     * @throws SQLException SQL语句异常
     */
    public List<Map<String, Object>> executeQuery(String sql, String... params) throws SQLException;


    /**
     * 执行查询语句
     *
     * @param table         要查询的标明
     * @param columns       想要显示的列，若为空则返回所有列
     * @param selection     where子句，声明要返回的行的要求，如果为空则返回表的所有行
     * @param selectionArgs where子句对应的条件值
     * @param groupBy       分组方式，若为空则不分组
     * @param having        having条件，若为空则返回全部
     * @param orderBy       排序方式，为空则为默认排序方式
     * @param limit         限制返回的记录的条数，为空则不限制
     * @return 返回查询后所查询到的数据结果
     * @throws SQLException SQL语句异常
     */
    public List<Map<String, Object>> executeQuery(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws SQLException;

    /**
     * 执行查询类的SQL语句
     *
     * @param cls    查询结果的对象
     * @param sql    查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     * @return 返回查询后所查询到的数据结果
     * @throws SQLException SQL语句异常
     */
    public <T> List<T> executeQuery(Class<T> cls, String sql, String... params) throws SQLException;

    /**
     * 执行查询语句
     *
     * @param cls           查询结果的对象
     * @param table         要查询的标明
     * @param columns       想要显示的列，若为空则返回所有列
     * @param selection     where子句，声明要返回的行的要求，如果为空则返回表的所有行
     * @param selectionArgs where子句对应的条件值
     * @param groupBy       分组方式，若为空则不分组
     * @param having        having条件，若为空则返回全部
     * @param orderBy       排序方式，为空则为默认排序方式
     * @param limit         限制返回的记录的条数，为空则不限制
     * @return 返回查询后所查询到的数据结果
     * @throws SQLException SQL语句异常
     */
    public <T> List<T> executeQuery(Class<T> cls, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws SQLException;

    /**
     * 执行查询数量类的SQL语句，其首个字段必须为数量值，如：select count(*) from ...
     *
     * @param sql    查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     * @return 返回查询后首个字段的数量值
     * @throws SQLException SQL语句异常
     */
    public long executeQueryCount(String sql, String... params) throws SQLException;

    /**
     * 执行查询数量类的SQL语句，其首个字段必须为数量值，如：select count(*) from ...
     *
     * @param table         表名
     * @param column        有且仅有一个为数量内容的字段名，如：count(*)
     * @param selection     查询条件
     * @param selectionArgs 查询条件中?的参数值
     * @return 返回查询后首个字段的数量值
     * @throws SQLException
     */
    public long executeQueryCount(String table, String column, String selection, String[] selectionArgs) throws SQLException;


    /**
     * 执行删除的builder
     *
     * @param deleteBuilder 删除的builder
     * @return 删除成功的记录数（sql语句的方式返回的记录数无意义）
     * @throws SQLException
     */
    public long executeDeleteBuilder(IDeleteBuilder deleteBuilder) throws SQLException;

    /**
     * 执行插入的builder
     *
     * @param insertBuilder 插入的builder
     * @return 插入成功的记录数（sql语句的方式返回的记录数无意义）
     * @throws SQLException
     */
    public long executeInsertBuilder(IInsertBuilder insertBuilder) throws SQLException;

    /**
     * 执行更新的builder
     *
     * @param updateBuilder 更新的builder
     * @return 更新成功的记录数（sql语句的方式返回的记录数无意义）
     * @throws SQLException
     */
    public long executeUpdateBuilder(IUpdateBuilder updateBuilder) throws SQLException;

    /**
     * 执行插入或替换的builder（该方式需要设置主键，且主键不为null，否则替换无效，每次都是插入操作）
     *
     * @param replaceBuilder 插入或替换的builder
     * @return 插入或替换成功的记录数（sql语句的方式返回的记录数无意义）
     * @throws SQLException
     */
    public long executeReplaceBuilder(IReplaceBuilder replaceBuilder) throws SQLException;

    /**
     * 执行插入或替换的builder
     *
     * @param createOrUpdateBuilder 插入或替换的builder
     * @return 插入或替换成功的记录数（sql语句的方式返回的记录数无意义）
     * @throws SQLException
     */
    public long executeCreateOrUpdateBuilder(ICreateOrUpdateBuilder createOrUpdateBuilder) throws SQLException;

    /**
     * 执行BaseBuilder操作
     *
     * @param baseBuilder BaseBuilder对象
     * @return 操作成功的记录数（sql语句的方式返回的记录数无意义）
     * @throws SQLException
     */
    public long executeSqliteBuilder(IBaseBuilder baseBuilder) throws SQLException;

    /**
     * 执行QueryBuilder操作
     *
     * @param queryBuilder 查询的Builder
     * @return 返回查询后所查询到的数据结果
     * @throws SQLException SQL语句异常
     */
    public List<Map<String, Object>> executeQueryBuilder(IQueryBuilder queryBuilder) throws SQLException;

    /**
     * 执行QueryBuilder操作
     *
     * @param cls          查询结果的对象
     * @param queryBuilder 查询的Builder
     * @return 返回查询后所查询到的数据结果
     * @throws SQLException SQL语句异常
     */
    public <T> List<T> executeQueryBuilder(Class<T> cls, IQueryBuilder queryBuilder) throws SQLException;

}
