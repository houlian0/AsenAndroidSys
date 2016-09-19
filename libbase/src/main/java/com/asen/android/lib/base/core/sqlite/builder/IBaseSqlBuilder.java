package com.asen.android.lib.base.core.sqlite.builder;

import java.sql.SQLException;

/**
 * 基本SQL语句的builder
 *
 * @author Asen
 * @version v1.0
 * @date 2016/9/18 16:26
 */
public interface IBaseSqlBuilder {

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

}
