package com.asen.android.lib.base.core.sqlite.builder;

import android.content.ContentValues;

import com.asen.android.lib.base.core.sqlite.field.AField;
import com.asen.android.lib.base.core.sqlite.table.ATable;

import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Builder制造者接口
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/19 16:50
 */
public interface IBuilderMaker {

    /**
     * 根据JSON串生成 对应的Builder
     *
     * @param json JSON串
     * @return 对应的Builder对象
     */
    public IBaseBuilder createSqliteBuilder(String json) throws JSONException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException;

    /**
     * 创建插入的Builder
     *
     * @param sql    非查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     * @return 插入的Builder
     */
    public IInsertBuilder createInsertBuilder(String sql, Object... params);

    /**
     * 创建插入的Builder
     *
     * @param table          表名
     * @param nullColumnHack 当values参数为空或者里面没有内容的时候，insert是会失败的(底层数据库不允许插入一个空行)，为了防止这种情况，
     *                       要在这里指定一个列名，到时候如果发现将要插入的行为空行时，就会将你指定的这个列名的值设为null，然后再向数据库中插入
     * @param values         要插入的参数
     * @return 插入的Builder
     */
    public IInsertBuilder createInsertBuilder(String table, String nullColumnHack, ContentValues values);

    /**
     * 创建插入的Builder
     *
     * @param senObj 包含有注解 {@link AField} 和 {@link ATable}的Java对象
     * @return 插入的Builder
     */
    public IInsertBuilder createInsertBuilder(Object senObj);

    /**
     * 创建更新的Builder
     *
     * @param sql    非查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     * @return 更新的Builder
     */
    public IUpdateBuilder createUpdateBuilder(String sql, Object... params);

    /**
     * 创建更新的Builder
     *
     * @param table       表名
     * @param values      要更新的参数
     * @param whereClause 要更新的数据条件
     * @param whereArgs   条件中带'?'的参数，参数个数需与'?'个数一致
     * @return 更新的Builder
     */
    public IUpdateBuilder createUpdateBuilder(String table, ContentValues values, String whereClause, String[] whereArgs);

    /**
     * 创建更新的Builder
     *
     * @param senObj 包含有注解 {@link AField} 和 {@link ATable}的Java对象
     * @return 更新的Builder
     */
    public IUpdateBuilder createUpdateBuilder(Object senObj);

    /**
     * 创建插入或更新的Builder（只能作用与含主键的数据表）
     *
     * @param sql    非查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     * @return 插入或更新的Builder
     */
    public IReplaceBuilder createReplaceBuilder(String sql, Object... params);

    /**
     * 创建插入或更新的Builder（只能作用与含主键的数据表）
     *
     * @param table          表名
     * @param nullColumnHack 当values参数为空或者里面没有内容的时候，insert是会失败的(底层数据库不允许插入一个空行)，为了防止这种情况，
     *                       要在这里指定一个列名，到时候如果发现将要插入的行为空行时，就会将你指定的这个列名的值设为null，然后再向数据库中插入
     * @param values         要插入的参数
     * @return 插入或更新的Builder
     */
    public IReplaceBuilder createReplaceBuilder(String table, String nullColumnHack, ContentValues values);

    /**
     * 创建插入或更新的Builder（只能作用与含主键的数据表）
     *
     * @param senObj 包含有注解 {@link AField} 和 {@link ATable}的Java对象
     * @return 插入或更新的Builder
     */
    public IReplaceBuilder createReplaceBuilder(Object senObj);

    /**
     * 创建删除的Builder
     *
     * @param sql    非查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     * @return 删除的Builder
     */
    public IDeleteBuilder createDeleteBuilder(String sql, Object... params);

    /**
     * 创建删除的Builder
     *
     * @param table       表名
     * @param whereClause 要更新的数据条件
     * @param whereArgs   条件中带'?'的参数，参数个数需与'?'个数一致
     * @return 删除的Builder
     */
    public IDeleteBuilder createDeleteBuilder(String table, String whereClause, String[] whereArgs);

    /**
     * 创建删除的Builder
     *
     * @param senObj 包含有注解 {@link AField} 和 {@link ATable}的Java对象
     * @return 删除的Builder
     */
    public IDeleteBuilder createDeleteBuilder(Object senObj);

    /**
     * 创建更新或新增的Builder，根据注解中定义的主键先查询，后根据查询结果判断是更新还是新增（支持主键内容为null，支持多主键）
     *
     * @param senObj 包含有注解 {@link AField} 和 {@link ATable}的Java对象
     * @return 更新或新增的Builder
     */
    public ICreateOrUpdateBuilder createCreateOrUpdateBuilder(Object senObj);

    /**
     * 创建更新或新增的Builder，根据条件参数先查询，后根据查询的结果判断是更新还是新增，更新时根据给定的条件进行更新
     *
     * @param table          表名
     * @param nullColumnHack 当values参数为空或者里面没有内容的时候，insert是会失败的(底层数据库不允许插入一个空行)，为了防止这种情况，
     *                       要在这里指定一个列名，到时候如果发现将要插入的行为空行时，就会将你指定的这个列名的值设为null，然后再向数据库中插入
     * @param values         要新增或修改的参数
     * @param whereClause    where条件语句
     * @param whereArgs      where条件中？的参数值
     */
    public ICreateOrUpdateBuilder createCreateOrUpdateBuilder(String table, String nullColumnHack, ContentValues values, String whereClause, String[] whereArgs);

    /**
     * 创建查询的Builder
     *
     * @param sql    查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     * @return 查询的Builder
     */
    public IQueryBuilder createQueryBuilder(String sql, String... params);

    /**
     * 创建查询的Builder
     *
     * @param table         要查询的标明
     * @param columns       想要显示的列，若为空则返回所有列
     * @param selection     where子句，声明要返回的行的要求，如果为空则返回表的所有行
     * @param selectionArgs where子句对应的条件值
     * @param groupBy       分组方式，若为空则不分组
     * @param having        having条件，若为空则返回全部
     * @param orderBy       排序方式，为空则为默认排序方式
     * @param limit         限制返回的记录的条数，为空则不限制
     * @return 查询的Builder
     */
    public IQueryBuilder createQueryBuilder(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);

}
