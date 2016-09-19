package com.asen.android.lib.base.core.sqlite.builder;

import android.content.ContentValues;

import com.asen.android.lib.base.core.sqlite.builder.type.BuilderType;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * 更新数据的builder
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/17 13:38
 */
public interface IUpdateBuilder {

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
     * 获取Android对象的Value
     *
     * @return ContentValues对象
     */
    public ContentValues getValues() throws SQLException;

    /**
     * 获取可选的WHERE子句，可以为null
     *
     * @return WHERE子句
     * @throws SQLException
     */
    public String getWhereClause() throws SQLException;

    /**
     * 获取指定参数个数的where语句中的参数，参数个数与where语句中？的个数一致
     *
     * @return 指定参数
     * @throws SQLException
     */
    public String[] getWhereArgs() throws SQLException;

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
