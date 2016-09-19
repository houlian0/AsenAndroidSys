package com.asen.android.lib.base.core.sqlite.builder.impl;

import android.content.ContentValues;

import com.asen.android.lib.base.core.sqlite.builder.IBaseBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IBuilderMaker;
import com.asen.android.lib.base.core.sqlite.builder.ICreateOrUpdateBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IDeleteBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IInsertBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IQueryBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IReplaceBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IUpdateBuilder;

import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Builder制造类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/20 16:55
 */
public class BuilderMaker implements IBuilderMaker {

    private static volatile IBuilderMaker b = null;

    private BuilderMaker() {
    }

    public static IBuilderMaker getInstance() {
        if (null == b) {
            synchronized (BuilderMaker.class) {
                if (null == b) {
                    b = new BuilderMaker();
                }
            }
        }
        return b;
    }

    @Override
    public IBaseBuilder createSqliteBuilder(String json) throws JSONException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return BaseBuilder.fromJson(json);
    }

    @Override
    public IInsertBuilder createInsertBuilder(String sql, Object... params) {
        return new InsertBuilder(sql, params);
    }

    @Override
    public IInsertBuilder createInsertBuilder(String table, String nullColumnHack, ContentValues values) {
        return new InsertBuilder(table, nullColumnHack, values);
    }

    @Override
    public IInsertBuilder createInsertBuilder(Object senObj) {
        return new InsertBuilder(senObj);
    }

    @Override
    public IUpdateBuilder createUpdateBuilder(String sql, Object... params) {
        return new UpdateBuilder(sql, params);
    }

    @Override
    public IUpdateBuilder createUpdateBuilder(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return new UpdateBuilder(table, values, whereClause, whereArgs);
    }

    @Override
    public IUpdateBuilder createUpdateBuilder(Object senObj) {
        return new UpdateBuilder(senObj);
    }

    @Override
    public IReplaceBuilder createReplaceBuilder(String sql, Object... params) {
        return new ReplaceBuilder(sql, params);
    }

    @Override
    public IReplaceBuilder createReplaceBuilder(String table, String nullColumnHack, ContentValues values) {
        return new ReplaceBuilder(table, nullColumnHack, values);
    }

    @Override
    public IReplaceBuilder createReplaceBuilder(Object senObj) {
        return new ReplaceBuilder(senObj);
    }

    @Override
    public IDeleteBuilder createDeleteBuilder(String sql, Object... params) {
        return new DeleteBuilder(sql, params);
    }

    @Override
    public IDeleteBuilder createDeleteBuilder(String table, String whereClause, String[] whereArgs) {
        return new DeleteBuilder(table, whereClause, whereArgs);
    }

    @Override
    public IDeleteBuilder createDeleteBuilder(Object senObj) {
        return new DeleteBuilder(senObj);
    }

    @Override
    public ICreateOrUpdateBuilder createCreateOrUpdateBuilder(Object senObj) {
        return new CreateOrUpdateBuilder(senObj);
    }

    @Override
    public ICreateOrUpdateBuilder createCreateOrUpdateBuilder(String table, String nullColumnHack, ContentValues values, String whereClause, String[] whereArgs) {
        return new CreateOrUpdateBuilder(table, nullColumnHack, values, whereClause, whereArgs);
    }

    @Override
    public IQueryBuilder createQueryBuilder(String sql, String... params) {
        return new QueryBuilder(sql, params);
    }

    @Override
    public IQueryBuilder createQueryBuilder(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return new QueryBuilder(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

}
