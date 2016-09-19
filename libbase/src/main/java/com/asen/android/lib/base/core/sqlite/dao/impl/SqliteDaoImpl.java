package com.asen.android.lib.base.core.sqlite.dao.impl;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.TextUtils;

import com.asen.android.lib.base.core.sqlite.builder.IBaseBuilder;
import com.asen.android.lib.base.core.sqlite.builder.ICreateOrUpdateBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IDeleteBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IInsertBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IQueryBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IReplaceBuilder;
import com.asen.android.lib.base.core.sqlite.builder.IUpdateBuilder;
import com.asen.android.lib.base.core.sqlite.builder.type.BuilderType;
import com.asen.android.lib.base.core.sqlite.dao.CallTransaction;
import com.asen.android.lib.base.core.sqlite.dao.ISqliteConn;
import com.asen.android.lib.base.core.sqlite.dao.ISqliteDao;
import com.asen.android.lib.base.core.sqlite.field.AField;
import com.asen.android.lib.base.core.sqlite.field.DataType;
import com.asen.android.lib.base.core.sqlite.table.TableConfig;
import com.asen.android.lib.base.core.sqlite.utils.DataSQLConstructor;
import com.asen.android.lib.base.core.sqlite.utils.SqlExceptionUtil;
import com.asen.android.lib.base.tool.util.ConvertUtil;
import com.asen.android.lib.base.tool.util.DateUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Sqlite数据库的Dao实现类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/18 9:33
 */
public class SqliteDaoImpl implements ISqliteDao {

    // Sqlite连接
    private ISqliteConn mSqliteConn;

    /**
     * 构造函数
     *
     * @param openHelper 确保一个程序只创建一个实例
     */
    public SqliteDaoImpl(SQLiteOpenHelper openHelper) {
        mSqliteConn = new SqliteConnImpl(openHelper);
    }

    @Override
    public SQLiteDatabase openWriteSQLiteDatabase() throws SQLException {
        return mSqliteConn.openWriteSQLiteDatabase();
    }

    @Override
    public void closeWriteSQLiteDatabase() throws SQLException {
        mSqliteConn.closeWriteSQLiteDatabase();
    }

    @Override
    public SQLiteDatabase openReadSQLiteDatabase() throws SQLException {
        return mSqliteConn.openReadSQLiteDatabase();
    }

    @Override
    public void closeReadSQLiteDatabase() throws SQLException {
        mSqliteConn.closeReadSQLiteDatabase();
    }

    @Override
    public void executeSql(String sql, Object... params) throws SQLException {
        try {
            SQLiteDatabase database = mSqliteConn.openWriteSQLiteDatabase();
            database.execSQL(sql, params);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeSql error!", e);
        } finally {
            mSqliteConn.closeWriteSQLiteDatabase();
        }
    }

    @Override
    public long executeInsert(String table, String nullColumnHack, ContentValues values) throws SQLException {
        try {
            SQLiteDatabase database = mSqliteConn.openWriteSQLiteDatabase();
            return database.insertOrThrow(table, nullColumnHack, values);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeInsert error!", e);
        } finally {
            mSqliteConn.closeWriteSQLiteDatabase();
        }
    }

    @Override
    public long executeReplace(String table, String nullColumnHack, ContentValues values) throws SQLException {
        try {
            SQLiteDatabase database = mSqliteConn.openWriteSQLiteDatabase();
            return database.replaceOrThrow(table, nullColumnHack, values);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeInsert error!", e);
        } finally {
            mSqliteConn.closeWriteSQLiteDatabase();
        }
    }

    @Override
    public int executeUpdate(String table, ContentValues values, String whereClause, String[] whereArgs) throws SQLException {
        try {
            SQLiteDatabase database = mSqliteConn.openWriteSQLiteDatabase();
            return database.update(table, values, whereClause, whereArgs);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeUpdate error!", e);
        } finally {
            mSqliteConn.closeWriteSQLiteDatabase();
        }
    }

    @Override
    public int executeDelete(String table, String whereClause, String[] whereArgs) throws SQLException {
        try {
            SQLiteDatabase database = mSqliteConn.openWriteSQLiteDatabase();
            return database.delete(table, whereClause, whereArgs);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeDelete error!", e);
        } finally {
            mSqliteConn.closeWriteSQLiteDatabase();
        }
    }

    @Override
    public <T> T executeTransaction(CallTransaction<T> call) throws SQLException {
        mSqliteConn.openWriteSQLiteDatabase();
        mSqliteConn.setTransaction(true); // 开始事务
        boolean isSuccess = false;
        try {
            T result = call.call();
            isSuccess = true; // 成功执行完
            return result;
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeTransaction error!", e);
        } finally {
            if (isSuccess) {
                mSqliteConn.setTransaction(false); // 关闭事务，并提交事务
            } else {
                mSqliteConn.endTransaction(); // 关闭事务，并回滚
            }
            mSqliteConn.closeWriteSQLiteDatabase();
        }
    }

    @Override
    public Cursor query(String sql, String... params) throws SQLException {
        try {
            SQLiteDatabase database = mSqliteConn.openReadSQLiteDatabase();
            return database.rawQuery(sql, params);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("query error!", e);
        }
    }

    @Override
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws SQLException {
        try {
            SQLiteDatabase database = mSqliteConn.openReadSQLiteDatabase();
            return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("query error!", e);
        }
    }

    @Override
    public <T> List<T> cursorToObject(Cursor cursor, Class<T> cls) throws SQLException, IllegalAccessException, InstantiationException, InvocationTargetException {
        List<T> result = new ArrayList<>();

        Field[] fields = cls.getDeclaredFields();
        while (cursor.moveToNext()) {
            T t = TableConfig.getNewInstance(cls); // 反射机制创建实例对象
            for (Field field : fields) {
                if (field.isAnnotationPresent(AField.class)) {
                    if (!field.isAccessible())
                        field.setAccessible(true);

                    AField annotation = field.getAnnotation(AField.class);
                    String fieldName = annotation.fieldName();// 字段名
                    int columnIndex = cursor.getColumnIndex(fieldName); // 字段名下标
                    int type = cursor.getType(columnIndex); // 字段名类型

                    if (Cursor.FIELD_TYPE_NULL == type) {
                        field.set(t, null);
                    } else if (field.getType() == Short.class || field.getType() == short.class) {
                        field.setShort(t, cursor.getShort(columnIndex));
                    } else if (field.getType() == Integer.class || field.getType() == int.class) {
                        field.setInt(t, cursor.getInt(columnIndex));
                    } else if (field.getType() == Long.class || field.getType() == long.class) {
                        field.setLong(t, cursor.getLong(columnIndex));
                    } else if (field.getType() == Float.class || field.getType() == float.class) {
                        field.setFloat(t, cursor.getFloat(columnIndex));
                    } else if (field.getType() == Double.class || field.getType() == double.class) {
                        field.setDouble(t, cursor.getDouble(columnIndex));
                    } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                        field.setBoolean(t, ConvertUtil.stringToBoolean(cursor.getString(columnIndex), false));
                    } else if (field.getType() == Date.class) {
                        String column = cursor.getString(columnIndex); // 获取字段的值
                        DataType dataType = annotation.dataType(); // 设置的时间类型
                        if (dataType == DataType.DATE_STRING) {
                            String form = annotation.form();
                            if (TextUtils.isEmpty(form)) {
                                throw SqlExceptionUtil.create("field datatype is data_string, but from is null!", null);
                            } else {
                                Date date = DateUtil.getDateByFormat(column, form);
                                field.set(t, date);
                            }
                        } else if (dataType == DataType.DATE_LONG) {
                            long timeLong = ConvertUtil.stringToLong(column, Long.MIN_VALUE);
                            field.set(t, timeLong == Long.MIN_VALUE ? null : new Date());
                        } else { // 默认时间类型采用 DATE_STRING
                            String form = annotation.form();
                            if (TextUtils.isEmpty(form)) {
                                throw SqlExceptionUtil.create("field datatype is data_string, but from is null!", null);
                            } else {
                                Date date = DateUtil.getDateByFormat(column, form);
                                field.set(t, date);
                            }
                        }
                    } else if (field.getType() == byte[].class || field.getType() == Byte[].class) {
                        field.set(t, cursor.getBlob(columnIndex));
                    } else {
                        field.set(t, cursor.getString(columnIndex));
                    }
                }
            }
            result.add(t);
        }

        return result;
    }

    // 将查询到的游标Cursor转成对象
    private List<Map<String, Object>> cursorToMap(Cursor cursor) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        if (cursor == null) return result;

        while (cursor.moveToNext()) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                int type = cursor.getType(i);
                if (type == Cursor.FIELD_TYPE_NULL) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                } else if (type == Cursor.FIELD_TYPE_BLOB) { // blob字段
                    map.put(cursor.getColumnName(i), cursor.getBlob(i));
                } else if (type == Cursor.FIELD_TYPE_INTEGER) { // blob字段
                    map.put(cursor.getColumnName(i), cursor.getLong(i));
                } else if (type == Cursor.FIELD_TYPE_FLOAT) { // blob字段
                    map.put(cursor.getColumnName(i), cursor.getDouble(i));
                } else {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
            }
            result.add(map);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> executeQuery(String sql, String... params) throws SQLException {
        Cursor cursor = null;
        try {
            cursor = query(sql, params);
            return cursorToMap(cursor);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeQuery error!", e);
        } finally {
            if (cursor != null)
                cursor.close();
            mSqliteConn.closeReadSQLiteDatabase();
        }
    }

    @Override
    public List<Map<String, Object>> executeQuery(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws SQLException {
        Cursor cursor = null;
        try {
            cursor = query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            return cursorToMap(cursor);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeQuery error!", e);
        } finally {
            if (cursor != null)
                cursor.close();
            mSqliteConn.closeReadSQLiteDatabase();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public <T> List<T> executeQuery(Class<T> cls, String sql, String... params) throws SQLException {
        Cursor cursor = null;
        try {
            cursor = query(sql, params);
            return cursorToObject(cursor, cls);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeQuery error!", e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw SqlExceptionUtil.create("newInstance object error!", e);
        } finally {
            if (cursor != null)
                cursor.close();
            mSqliteConn.closeReadSQLiteDatabase();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public <T> List<T> executeQuery(Class<T> cls, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws SQLException {
        Cursor cursor = null;
        try {
            cursor = query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            return cursorToObject(cursor, cls);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeQuery error!", e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw SqlExceptionUtil.create("newInstance object error!", e);
        } finally {
            if (cursor != null)
                cursor.close();
            mSqliteConn.closeReadSQLiteDatabase();
        }
    }

    @Override
    public long executeQueryCount(String sql, String... params) throws SQLException {
        Cursor cursor = null;
        try {
            cursor = query(sql, params);
            if (cursor.moveToNext() && cursor.getColumnCount() > 0) {
                return ConvertUtil.stringToLong(cursor.getString(0));
            }
            return -1;
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeQuery error!", e);
        } finally {
            if (cursor != null)
                cursor.close();
            mSqliteConn.closeReadSQLiteDatabase();
        }
    }

    @Override
    public long executeQueryCount(String table, String column, String selection, String[] selectionArgs) throws SQLException {
        Cursor cursor = null;
        try {
            cursor = query(table, new String[]{column}, selection, selectionArgs, null, null, null, null);
            if (cursor.moveToNext() && cursor.getColumnCount() > 0) {
                return ConvertUtil.stringToLong(cursor.getString(0));
            }
            return -1;
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeQuery error!", e);
        } finally {
            if (cursor != null)
                cursor.close();
            mSqliteConn.closeReadSQLiteDatabase();
        }
    }

    @Override
    public long executeDeleteBuilder(IDeleteBuilder deleteBuilder) throws SQLException {
        long result;
        BuilderType builderType = deleteBuilder.getBuilderType();
        if (builderType == BuilderType.SQL) {
            executeSql(deleteBuilder.getSql(), deleteBuilder.getParams());
            result = 1;
        } else {
            result = executeDelete(deleteBuilder.getTableName(), deleteBuilder.getWhereClause(), deleteBuilder.getWhereArgs());
        }
        return result;
    }

    @Override
    public long executeInsertBuilder(IInsertBuilder insertBuilder) throws SQLException {
        long result;
        BuilderType builderType = insertBuilder.getBuilderType();
        if (builderType == BuilderType.SQL) {
            executeSql(insertBuilder.getSql(), insertBuilder.getParams());
            result = 1;
        } else {
            result = executeInsert(insertBuilder.getTableName(), insertBuilder.getNullColumnHack(), insertBuilder.getValues());
        }
        return result;
    }

    @Override
    public long executeUpdateBuilder(IUpdateBuilder updateBuilder) throws SQLException {
        long result;
        BuilderType builderType = updateBuilder.getBuilderType();
        if (builderType == BuilderType.SQL) {
            executeSql(updateBuilder.getSql(), updateBuilder.getParams());
            result = 1;
        } else {
            result = executeUpdate(updateBuilder.getTableName(), updateBuilder.getValues(), updateBuilder.getWhereClause(), updateBuilder.getWhereArgs());
        }
        return result;
    }

    @Override
    public long executeReplaceBuilder(IReplaceBuilder replaceBuilder) throws SQLException {
        long result;
        BuilderType builderType = replaceBuilder.getBuilderType();
        if (builderType == BuilderType.SQL) {
            executeSql(replaceBuilder.getSql(), replaceBuilder.getParams());
            result = 1;
        } else {
            result = executeReplace(replaceBuilder.getTableName(), replaceBuilder.getNullColumnHack(), replaceBuilder.getValues());
        }
        return result;
    }

    // 私有的查询条数的方法
    private long privateQueryCount(String table, String column, String selection, String[] selectionArgs) throws SQLException {
        Cursor cursor = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT ");
            sb.append(column);
//            DataSQLConstructor.appendEntityName(sb, column);
            sb.append(" FROM ");
            DataSQLConstructor.appendEntityName(sb, table);
            sb.append("WHERE ");

            int indexOf = -1;
            int index = 0;
            while ((indexOf = selection.indexOf("?")) != -1) {
                String value = selectionArgs[index];

                StringBuilder tmp = new StringBuilder();
                tmp.append(selection.substring(0, indexOf));
                tmp.append(value == null ? null : "'" + value + "'");
                if (indexOf != selection.length() - 1) {
                    tmp.append(selection.substring(indexOf + 1, selection.length()));
                }
                selection = tmp.toString();

                index++;
            }
            sb.append(selection);

            cursor = query(sb.toString());
            if (cursor.moveToNext() && cursor.getColumnCount() > 0) {
                return ConvertUtil.stringToLong(cursor.getString(0));
            }
            return -1;
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("executeQuery error!", e);
        } finally {
            if (cursor != null)
                cursor.close();
            mSqliteConn.closeReadSQLiteDatabase();
        }
    }

    @Override
    public long executeCreateOrUpdateBuilder(ICreateOrUpdateBuilder createOrUpdateBuilder) throws SQLException {
        long result = 0;

        BuilderType builderType = createOrUpdateBuilder.getBuilderType();
        if (builderType == BuilderType.ANDROID_SQL || builderType == BuilderType.OBJECT) {
            long count = privateQueryCount(createOrUpdateBuilder.getTableName(), "COUNT(*)", createOrUpdateBuilder.getWhereClause(), createOrUpdateBuilder.getWhereArgs());
            if (count == 0) {
                // 未查询到结果，则新增
                result = executeInsert(createOrUpdateBuilder.getTableName(), createOrUpdateBuilder.getNullColumnHack(), createOrUpdateBuilder.getValues());
            } else {
                // 查询到结果时，则更新
                result = executeUpdate(createOrUpdateBuilder.getTableName(), createOrUpdateBuilder.getValues(), createOrUpdateBuilder.getWhereClause(), createOrUpdateBuilder.getWhereArgs());
            }
        }

        return result;
    }

    @Override
    public long executeSqliteBuilder(IBaseBuilder baseBuilder) throws SQLException {
        if (baseBuilder instanceof IInsertBuilder) {
            return executeInsertBuilder((IInsertBuilder) baseBuilder);
        } else if (baseBuilder instanceof IUpdateBuilder) {
            return executeUpdateBuilder((IUpdateBuilder) baseBuilder);
        } else if (baseBuilder instanceof IReplaceBuilder) {
            return executeReplaceBuilder((IReplaceBuilder) baseBuilder);
        } else if (baseBuilder instanceof IDeleteBuilder) {
            return executeDeleteBuilder((IDeleteBuilder) baseBuilder);
        } else if (baseBuilder instanceof ICreateOrUpdateBuilder) {
            return executeCreateOrUpdateBuilder((ICreateOrUpdateBuilder) baseBuilder);
        }
        return -1;
    }

    @Override
    public List<Map<String, Object>> executeQueryBuilder(IQueryBuilder queryBuilder) throws SQLException {
        BuilderType builderType = queryBuilder.getBuilderType();
        if (builderType == BuilderType.SQL) {
            Object[] params = queryBuilder.getParams();
            String[] paramsStrs = null;
            if (params != null) {
                paramsStrs = new String[params.length];
                for (int i = 0; i < params.length; i++) {
                    paramsStrs[i] = params[i] == null ? null : params[i].toString();
                }
            }
            return executeQuery(queryBuilder.getSql(), paramsStrs);
        } else {
            return executeQuery(queryBuilder.getTableName(), queryBuilder.getColumns(), queryBuilder.getSelection(), queryBuilder.getSelectionArgs(), queryBuilder.getGroupBy(), queryBuilder.getHaving(), queryBuilder.getOrderBy(), queryBuilder.getLimit());
        }
    }

    @Override
    public <T> List<T> executeQueryBuilder(Class<T> cls, IQueryBuilder queryBuilder) throws SQLException {
        BuilderType builderType = queryBuilder.getBuilderType();
        if (builderType == BuilderType.SQL) {
            Object[] params = queryBuilder.getParams();
            String[] paramsStrs = null;
            if (params != null) {
                paramsStrs = new String[params.length];
                for (int i = 0; i < params.length; i++) {
                    paramsStrs[i] = params[i] == null ? null : params[i].toString();
                }
            }
            return executeQuery(cls, queryBuilder.getSql(), paramsStrs);
        } else {
            return executeQuery(cls, queryBuilder.getTableName(), queryBuilder.getColumns(), queryBuilder.getSelection(), queryBuilder.getSelectionArgs(), queryBuilder.getGroupBy(), queryBuilder.getHaving(), queryBuilder.getOrderBy(), queryBuilder.getLimit());
        }
    }

}
