package com.asen.android.lib.base.core.sqlite.builder.impl;


import com.asen.android.lib.base.core.sqlite.builder.IBaseSqlBuilder;
import com.asen.android.lib.base.core.sqlite.builder.type.BuilderType;
import com.asen.android.lib.base.core.sqlite.field.AField;
import com.asen.android.lib.base.core.sqlite.table.ATable;
import com.asen.android.lib.base.core.sqlite.utils.SqlExceptionUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 基本的含Sql语句的builder
 *
 * @author Asen
 * @version v1.0
 * @date 2016/9/18 16:28
 */
abstract class BaseSqlBuilder extends BaseBuilder implements IBaseSqlBuilder {

    String sql;

    List<Object> paramsList;

    private boolean isNeadRefresh2Sql = true;

    private boolean isNeadRefresh2Android = true;

    /**
     * 构造函数
     */
    BaseSqlBuilder() {
        super();
        paramsList = new ArrayList<>();
    }

    /**
     * 构造函数
     *
     * @param senObj 包含有注解 {@link AField} 和 {@link ATable}的Java对象
     */
    BaseSqlBuilder(Object senObj) {
        super(senObj);
        paramsList = new ArrayList<>();
    }

    /**
     * 构造函数
     *
     * @param sql    SQL语句
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     */
    BaseSqlBuilder(String sql, Object[] params) {
        this();
        builderType = BuilderType.SQL;
        this.sql = sql;
        Collections.addAll(this.paramsList, params);
    }

    void setSql(String sql) {
        this.sql = sql;
    }

    void setParamsList(List<Object> paramsList) {
        this.paramsList = paramsList;
    }

    public List<Object> getParamsList() {
        return paramsList;
    }

    @Override
    public String getSql() throws SQLException {
        refreshBuilder2Sql();
        return sql;
    }

    @Override
    public Object[] getParams() throws SQLException {
        refreshBuilder2Sql();
        return paramsList == null || paramsList.size() == 0 ? null : paramsList.toArray();
    }

    // 刷新到Android格式
    void refreshBuilder2Android() throws SQLException {
        if (isNeadRefresh2Android) {
            if (BuilderType.SQL == getBuilderType()) {
                // 将SQL语句 转成 Android格式
                refreshSql(getSql(), getParams());
            } else if (BuilderType.ANDROID_SQL == getBuilderType()) {
                // 本身就是Android格式了
            } else if (BuilderType.OBJECT == getBuilderType()) {
                // 将包含有注解 {@link AField} 和 {@link ATable}的Java对象 转成 Android格式
                Object senObj = getSenObj();
                refreshObject2Android(senObj);
            } else {
                throw SqlExceptionUtil.create("Unknow builderType!", null);
            }
            isNeadRefresh2Android = false;
        }
    }

    // 刷新到Sql语句格式
    void refreshBuilder2Sql() throws SQLException {
        if (isNeadRefresh2Sql) {
            if (BuilderType.SQL == getBuilderType()) {
                // 本身就是SQL语句格式了
            } else if (BuilderType.ANDROID_SQL == getBuilderType()) {
                // 将Android格式 转成 SQL语句
                refreshAndroidSql();
            } else if (BuilderType.OBJECT == getBuilderType()) {
                // 将包含有注解 {@link AField} 和 {@link ATable}的Java对象 转成 SQL语句
                Object senObj = getSenObj();
                refreshObject2Sql(senObj);
            } else {
                throw SqlExceptionUtil.create("Unknown builderType!", null);
            }
            isNeadRefresh2Sql = false;
        }
    }

    /**
     * 将Android格式 转成 SQL语句
     *
     * @throws SQLException
     */
    abstract void refreshAndroidSql() throws SQLException;

    /**
     * 将对象格式 转成 Android格式
     *
     * @param senObj 指定对象
     */
    abstract void refreshObject2Android(Object senObj) throws SQLException;

    /**
     * 将对象格式 转成 SQL格式
     *
     * @param senObj 指定对象
     */
    abstract void refreshObject2Sql(Object senObj) throws SQLException;

}
