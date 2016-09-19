package com.asen.android.lib.base.core.sqlite.utils;

import android.database.Cursor;

import com.asen.android.lib.base.core.sqlite.field.FieldInfo;

/**
 * 数据防止注入的SQL语句拼接器，拼接字段名、表名和值信息
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/19 10:34
 */
public class DataSQLConstructor {

    /**
     * 拼接字段值
     *
     * @param sb        待拼接的StringBuilder
     * @param valueName 需要防止注入的值
     */
    public static void appendValueName(StringBuilder sb, Float valueName) {
        if (valueName == null) {
            sb.append("null");
        } else {
            sb.append("'").append(valueName).append("'");
        }
    }

    /**
     * 拼接字段值
     *
     * @param sb        待拼接的StringBuilder
     * @param valueName 需要防止注入的值
     */
    public static void appendValueName(StringBuilder sb, Long valueName) {
        if (valueName == null) {
            sb.append("null");
        } else {
            sb.append("'").append(valueName).append("'");
        }
    }

    /**
     * 拼接字段值
     *
     * @param sb        待拼接的StringBuilder
     * @param valueName 需要防止注入的值
     */
    public static void appendValueName(StringBuilder sb, Double valueName) {
        if (valueName == null) {
            sb.append("null");
        } else {
            sb.append("'").append(valueName).append("'");
        }
    }

    /**
     * 拼接字段值
     *
     * @param sb        待拼接的StringBuilder
     * @param valueName 需要防止注入的值
     */
    public static void appendValueName(StringBuilder sb, Integer valueName) {
        if (valueName == null) {
            sb.append("null");
        } else {
            sb.append("'").append(valueName).append("'");
        }
    }

    /**
     * 拼接字段值
     *
     * @param sb        待拼接的StringBuilder
     * @param valueName 需要防止注入的值
     */
    public static void appendValueName(StringBuilder sb, String valueName) {
        if (valueName == null) {
            sb.append("null");
        } else {
            sb.append("'").append(valueName.replace("'", "''")).append("'");
        }
    }

    /**
     * 拼接表名或字段名
     *
     * @param sb         待拼接的StringBuilder
     * @param entityName 需要防止注入的名称
     */
    public static void appendEntityName(StringBuilder sb, String entityName) {
        sb.append("[").append(entityName).append("] ");
    }

    /**
     * 拼接建表时的字段信息
     *
     * @param sb          待拼接的StringBuilder
     * @param fieldInfo   字段信息
     * @param isOnlyOneId 是否只有一个主键
     */
    public static void appendFieldInfo(StringBuilder sb, FieldInfo fieldInfo, boolean isOnlyOneId) {
        appendEntityName(sb, fieldInfo.getFieldName());

        int sqliteType = fieldTypeToSqliteType(fieldInfo.getFieldType());
        appendSqliteType(sb, sqliteType);

        if (isOnlyOneId && fieldInfo.isId()) {
            appendPrimaryKey(sb);
        }

        if (Cursor.FIELD_TYPE_INTEGER == sqliteType && fieldInfo.isAutoincrement()) {
            // 自增长只能给INTEGER类型使用
            appendAutoincrement(sb);
        }

        if (!fieldInfo.isCanBeNull()) {
            // 不能为null时执行
            appendNotNull(sb);
        }
    }

    /**
     * 拼接建表时的主键信息
     *
     * @param sb 待拼接的StringBuilder
     */
    public static void appendPrimaryKey(StringBuilder sb) {
        sb.append("PRIMARY KEY ");
    }

    /**
     * 拼接建表时的Not null 信息
     *
     * @param sb 待拼接的StringBuilder
     */
    public static void appendNotNull(StringBuilder sb) {
        sb.append("NOT NULL ");
    }

    /**
     * 拼接建表时的自增长信息
     *
     * @param sb 待拼接的StringBuilder
     */
    public static void appendAutoincrement(StringBuilder sb) {
        sb.append("AUTOINCREMENT ");
    }

    /**
     * 拼接建表时的数据字段类型
     *
     * @param sb         待拼接的StringBuilder
     * @param sqliteType 数据库字段类型
     */
    public static void appendSqliteType(StringBuilder sb, int sqliteType) {
        if (Cursor.FIELD_TYPE_INTEGER == sqliteType) {
            sb.append("INTEGER ");
        } else if (Cursor.FIELD_TYPE_FLOAT == sqliteType) {
            sb.append("REAL ");
        } else if (Cursor.FIELD_TYPE_BLOB == sqliteType) {
            sb.append("BLOB ");
        } else if (Cursor.FIELD_TYPE_STRING == sqliteType) {
            sb.append("TEXT ");
        } else {
            sb.append("NULL ");
        }
    }

    // 字段类型转成sqlite数据库类型
    private static int fieldTypeToSqliteType(Class<?> fieldType) {
        if (fieldType == Integer.class || fieldType == int.class || fieldType == Long.class || fieldType == long.class
                || fieldType == Byte.class || fieldType == byte.class || fieldType == Short.class || fieldType == short.class) {
            return Cursor.FIELD_TYPE_INTEGER;
        } else if (fieldType == Double.class || fieldType == double.class || fieldType == Float.class || fieldType == float.class) {
            return Cursor.FIELD_TYPE_FLOAT;
        } else if (fieldType == byte[].class || fieldType == Byte[].class) {
            return Cursor.FIELD_TYPE_BLOB;
        } else {
            return Cursor.FIELD_TYPE_STRING;
        }
    }

}
