package com.asen.android.lib.base.core.sqlite.utils;

import android.database.sqlite.SQLiteDatabase;

import com.asen.android.lib.base.core.sqlite.field.FieldInfo;
import com.asen.android.lib.base.core.sqlite.table.TableConfig;
import com.asen.android.lib.base.core.sqlite.table.TableInfo;
import com.asen.android.lib.base.tool.util.LogUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * 表新建删除等操作的工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/4/1 9:43
 */
public class TableUtils {

    public static final String TAG = TableUtils.class.getSimpleName();

    /**
     * 如果表不存在时，创建表
     *
     * @param db  sqlite数据库连接
     * @param cls 需要创建表的类
     * @throws SQLException
     */
    public static void createTableIfNotExists(SQLiteDatabase db, Class<?> cls) throws SQLException {
        TableConfig tableConfig = new TableConfig(cls);
        createTable(db, tableConfig.getTableInfo(), true);
    }

    /**
     * 如果表存在的话，删除表
     *
     * @param db  sqlite数据库连接
     * @param cls 需要删除表的类
     * @throws SQLException
     */
    public static void dropTable(SQLiteDatabase db, Class<?> cls) throws SQLException {
        dropTable(db, TableConfig.getTableName(cls));
    }

    /**
     * 清空表
     *
     * @param db       sqlite数据库连接
     * @param cls      需要清空表的类
     * @param isDelete 是否通过delete处理。如果true，通过 DELETE FROM 'TABLE' 处理；如果false，通过 TRUNCATE TABLE 'TABLE' 处理
     * @throws SQLException
     */
    public static void clearTable(SQLiteDatabase db, Class<?> cls, boolean isDelete) throws SQLException {
        clearTable(db, TableConfig.getTableName(cls), isDelete);
    }

    // 执行SQL语句
    private static void execSQL(SQLiteDatabase db, String sql) throws SQLException {
        try {
            db.execSQL(sql);
        } catch (android.database.SQLException e) {
            LogUtil.e(TAG, sql);
            throw SqlExceptionUtil.create("SQL ERROR: " + sql, e);
        }
    }

    /**
     * 清空表
     *
     * @param db        sqlite数据库连接
     * @param tableName 需要清空的表名
     * @param isDelete  是否通过delete处理。如果true，通过 DELETE FROM 'TABLE' 处理；如果false，通过 TRUNCATE TABLE 'TABLE' 处理
     * @throws SQLException
     */
    private static void clearTable(SQLiteDatabase db, String tableName, boolean isDelete) throws SQLException {
        StringBuilder sb = new StringBuilder("DELETE FROM "); // SQLITE 不支持 "TRUNCATE TABLE "
        DataSqlConstructor.appendEntityName(sb, tableName); // 拼接表名
        execSQL(db, sb.toString());

        if (!isDelete) {
            // 需要重置自增长
            sb = new StringBuilder("DELETE from SQLITE_SEQUENCE WHERE NAME = ");
            DataSqlConstructor.appendValueName(sb, tableName);
            execSQL(db, sb.toString());
        }
    }

    /**
     * 删除表
     *
     * @param db        sqlite数据库连接
     * @param tableName 需要删除的表名
     * @throws SQLException
     */
    public static void dropTable(SQLiteDatabase db, String tableName) throws SQLException {
        StringBuilder sb = new StringBuilder("DROP TABLE ");
        DataSqlConstructor.appendEntityName(sb, tableName); // 拼接表名
        execSQL(db, sb.toString());
    }

    /**
     * 创建表
     *
     * @param db            sqlite数据库连接
     * @param tableInfo     表信息
     * @param isIfNotExists 是否需要 IfNotExists 判断
     * @throws SQLException
     */
    private static void createTable(SQLiteDatabase db, TableInfo tableInfo, boolean isIfNotExists) throws SQLException {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        if (isIfNotExists) {
            sb.append("IF NOT EXISTS ");
        }
        DataSqlConstructor.appendEntityName(sb, tableInfo.getTableName()); // 拼接表名

        List<FieldInfo> idFieldList = tableInfo.getIdFieldList();
        int idSize = idFieldList.size();
        if (idSize == 0) throw SqlExceptionUtil.create("Need at least one id!", null);
        boolean isOnlyOnId = idSize == 1; // 是否只有一个唯一的ID

        sb.append("( ");

        List<FieldInfo> fieldList = tableInfo.getFieldList();
        for (int i = 0; i < fieldList.size(); i++) {
            FieldInfo info = fieldList.get(i);
            sb.append("\n\t");
            DataSqlConstructor.appendFieldInfo(sb, info, isOnlyOnId);
            if (i != fieldList.size() - 1 || !isOnlyOnId) {
                // 不是最后一条记录，或者不是只有一个唯一的id
                sb.append(", ");
            }
        }

        if (!isOnlyOnId) {
            for (int i = 0; i < idSize; i++) {
                FieldInfo info = idFieldList.get(i);
                if (i == 0) {
                    sb.append("\n\tCONSTRAINT ");
                    DataSqlConstructor.appendEntityName(sb, info.getFieldName()); // 拼接表名
                    DataSqlConstructor.appendPrimaryKey(sb);
                    sb.append("(");
                    DataSqlConstructor.appendEntityName(sb, info.getFieldName()); // 拼接表名
                } else {
                    sb.append(",");
                    DataSqlConstructor.appendEntityName(sb, info.getFieldName()); // 拼接表名
                }
            }
            sb.append(") ");
        }

        sb.append("\n) ");

        execSQL(db, sb.toString());
    }


}
