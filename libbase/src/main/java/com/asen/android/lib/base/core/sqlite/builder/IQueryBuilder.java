package com.asen.android.lib.base.core.sqlite.builder;


import com.asen.android.lib.base.core.sqlite.builder.type.BuilderType;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * 查询数据的builder
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/17 13:35
 */
public interface IQueryBuilder {

    /**
     * 转成JSON串
     *
     * @return 失败返回null，否则返回对应的JSON串
     */
    public String toJson() throws JSONException, SQLException;

    /**
     * 获取表名
     *
     * @return 表名
     */
    public String getTableName() throws SQLException;

    /**
     * 获取要查询的字段信息集合
     *
     * @return 字段信息集合
     */
    public String[] getColumns();

    /**
     * 获取要查询的条件
     *
     * @return 要查询的条件
     */
    public String getSelection();

    /**
     * 获取where子句对应的条件值
     *
     * @return where子句对应的条件值
     */
    public String[] getSelectionArgs();

    /**
     * 获取分组方式
     *
     * @return 分组方式
     */
    public String getGroupBy();

    /**
     * 获取having条件
     *
     * @return having条件
     */
    public String getHaving();

    /**
     * 获取排序方式
     *
     * @return 排序方式
     */
    public String getOrderBy();

    /**
     * 获取限制返回数量的条数
     *
     * @return 限制返回记录的条数
     */
    public String getLimit();

    /**
     * 获取sql语句
     *
     * @return sql语句
     */
    public String getSql() throws SQLException;

    /**
     * 获取参数集合
     *
     * @return 参数集合
     */
    public Object[] getParams() throws SQLException;

    /**
     * 获取Builder的类型
     *
     * @return Builder的类型
     */
    public BuilderType getBuilderType();

}
