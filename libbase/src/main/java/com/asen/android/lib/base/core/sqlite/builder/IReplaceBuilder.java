package com.asen.android.lib.base.core.sqlite.builder;

import android.content.ContentValues;

import com.asen.android.lib.base.core.sqlite.builder.type.BuilderType;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * 替换数据的builder
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/17 13:41
 */
public interface IReplaceBuilder {

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
