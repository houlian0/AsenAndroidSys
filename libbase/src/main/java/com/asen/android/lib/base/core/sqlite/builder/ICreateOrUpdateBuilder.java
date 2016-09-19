package com.asen.android.lib.base.core.sqlite.builder;

import android.content.ContentValues;

import com.asen.android.lib.base.core.sqlite.builder.type.BuilderType;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * 创建或更新的builder
 *
 * @author Asen
 * @version v1.0
 * @date 2016/9/18 16:17
 */
public interface ICreateOrUpdateBuilder {

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
     * 当values参数为空或者里面没有内容的时候，insert是会失败的(底层数据库不允许插入一个空行)，为了防止这种情况，
     * 要在这里指定一个列名，到时候如果发现将要插入的行为空行时，就会将你指定的这个列名的值设为null，然后再向数据库中插入
     *
     * @return values为null时，可以传入为空的字段信息
     */
    public String getNullColumnHack() throws SQLException;

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
     * 获取Builder的类型
     *
     * @return Builder的类型
     */
    public BuilderType getBuilderType();

}
