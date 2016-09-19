package com.asen.android.lib.base.core.sqlite.builder.impl;

import android.content.ContentValues;
import android.text.TextUtils;

import com.asen.android.lib.base.core.sqlite.builder.IBaseBuilder;
import com.asen.android.lib.base.core.sqlite.builder.type.BuilderType;
import com.asen.android.lib.base.core.sqlite.builder.type.SqliteType;
import com.asen.android.lib.base.core.sqlite.field.AField;
import com.asen.android.lib.base.core.sqlite.field.DataType;
import com.asen.android.lib.base.core.sqlite.field.DefaultType;
import com.asen.android.lib.base.core.sqlite.table.ATable;
import com.asen.android.lib.base.core.sqlite.utils.DataSQLConstructor;
import com.asen.android.lib.base.core.sqlite.utils.SqlExceptionUtil;
import com.asen.android.lib.base.tool.util.AppUtil;
import com.asen.android.lib.base.tool.util.ConvertUtil;
import com.asen.android.lib.base.tool.util.DateUtil;
import com.asen.android.lib.base.tool.util.HexUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Builder的基类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/20 13:34
 */
abstract class BaseBuilder implements IBaseBuilder {

    public static final String KEY_CONTENT = "content";

    public static final String KEY_SQL_SQL = "sql";

    public static final String KEY_SQL_PARAMS = "params";

    public static final String KEY_ANDROID_TABLE = "table_name";

    public static final String KEY_ANDROID_NULL_COLUMN_HACK = "null_column_hack";

    public static final String KEY_ANDROID_WHERE_CLAUSE = "where_clause";

    public static final String KEY_ANDROID_WHERE_ARGS = "where_args";

    public static final String KEY_ANDROID_VALUES = "values";

    public static final String KEY_ANDROID_VALUES_BYTES = "values_bytes";

    public static final String KEY_ANDROID_COLUMNS = "columns";

    public static final String KEY_ANDROID_SELECTION = "selection";

    public static final String KEY_ANDROID_SELECTION_ARGS = "selection_args";

    public static final String KEY_ANDROID_GROUP_BY = "group_by";

    public static final String KEY_ANDROID_HAVING = "having";

    public static final String KEY_ANDROID_ORDER_BY = "order_by";

    public static final String KEY_ANDROID_LIMIT = "limit";

    public static final String KEY_OBJECT_TABLE = "table_name";

    public static final String KEY_OBJECT_CLASS = "class_name";

    public static final String KEY_OBJECT_FIELDS = "fields";

    public static final String KEY_OBJECT_FIELDS_ID = "field_id";

    public static final String KEY_OBJECT_FIELDS_NAME = "field_name";

    public static final String KEY_OBJECT_FIELDS_IS_DATE = "field_is_date";

    public static final String KEY_OBJECT_VALUES = "values";

    BuilderType builderType = BuilderType.UNKNOWN;

    Object mSenObj;

    /**
     * 构造函数
     */
    BaseBuilder() {
    }

    /**
     * 构造函数
     *
     * @param senObj 包含有注解 {@link AField} 和 {@link ATable}的Java对象
     */
    BaseBuilder(Object senObj) {
        builderType = BuilderType.OBJECT;
        this.mSenObj = senObj;
    }

    public BuilderType getBuilderType() {
        return builderType;
    }

    void setBuilderType(BuilderType builderType) {
        this.builderType = builderType;
    }

    Object getSenObj() {
        return mSenObj;
    }

    /**
     * 将SQL语句转换成 Android的格式
     *
     * @param sql    SQL语句
     * @param params 参数集
     * @throws SQLException
     */
    void refreshSql(String sql, Object[] params) throws SQLException {
        throw new SQLException("This is a sql type! [sql]:" + sql + ", [params]:" + Arrays.toString(params));
    }

    // 获取默认值
    Object getDefaultValue(DefaultType defaultType, String defaultValue, Class<?> fieldType) throws SQLException {
        Object valueObj = null;
        // 默认类型为number时，填写设置的默认值
        if (defaultType == DefaultType.NUMBER && !TextUtils.isEmpty(defaultValue)) {
            if (fieldType == Integer.class || fieldType == int.class) {
                valueObj = ConvertUtil.stringToInt(defaultValue);
            } else if (fieldType == Double.class || fieldType == double.class) {
                valueObj = ConvertUtil.stringToDouble(defaultValue);
            } else if (fieldType == Long.class || fieldType == long.class) {
                valueObj = ConvertUtil.stringToLong(defaultValue);
            } else if (fieldType == Float.class || fieldType == float.class) {
                valueObj = ConvertUtil.stringToFloat(defaultValue);
            } else {
                throw SqlExceptionUtil.create("Default type is number, but the field type is [" + fieldType + "]!", null);
            }
        }
        // 默认类型为字符串时
        else if (defaultType == DefaultType.STRING && !TextUtils.isEmpty(defaultValue)) {
            if (fieldType == String.class) {
                valueObj = defaultValue;
            } else {
                throw SqlExceptionUtil.create("Default type is string, but the field type is [" + fieldType + "]!", null);
            }
        }
        // 默认类型为系统当前时间
        else if (defaultType == DefaultType.SYS_DATE) {
            if (fieldType == Date.class) {
                valueObj = new Date();
            } else {
                throw SqlExceptionUtil.create("Default type is sys_date, but the field type is [" + fieldType + "]!", null);
            }
        }
        // 默认类型为32位唯一码
        else if (defaultType == DefaultType.SYS_UUID) {
            if (fieldType == String.class) {
                valueObj = AppUtil.getUUid();
            } else {
                throw SqlExceptionUtil.create("Default type is sys_uuid, but the field type is [" + fieldType + "]!", null);
            }
        }
        return valueObj;
    }

    // 向StringBuilder添加值信息
    Object getAppendValue(Object valueObj, DataType dataType, String form, long length, Class<?> type) throws SQLException {
        if (type == Integer.class || type == int.class) {
            return valueObj == null ? null : dataType == DataType.NUMBER_FORM ? ConvertUtil.stringToInt(new DecimalFormat(form).format(valueObj)) : (Integer) valueObj;
        } else if (type == Double.class || type == double.class) {
            return valueObj == null ? null : dataType == DataType.NUMBER_FORM ? ConvertUtil.stringToDouble(new DecimalFormat(form).format(valueObj)) : (Double) valueObj;
        } else if (type == Long.class || type == long.class) {
            return valueObj == null ? null : dataType == DataType.NUMBER_FORM ? ConvertUtil.stringToLong(new DecimalFormat(form).format(valueObj)) : (Long) valueObj;
        } else if (type == Float.class || type == float.class) {
            return valueObj == null ? null : dataType == DataType.NUMBER_FORM ? ConvertUtil.stringToFloat(new DecimalFormat(form).format(valueObj)) : (Float) valueObj;
        } else if (type == Date.class) {
            if (dataType == DataType.DATE_STRING) {
                return DateUtil.getStringByFormat((Date) valueObj, form);
            } else if (dataType == DataType.DATE_LONG) {
                return valueObj == null ? null : ((Date) valueObj).getTime();
            } else { // 默认采用年月日时分秒的形式
                return DateUtil.getStringByFormat((Date) valueObj, DateUtil.dateFormatYMDHMS);
            }
        } else if (type == String.class) {
            if (length != -1 && valueObj != null && valueObj.toString().length() > length) {
                valueObj = valueObj.toString().substring(0, (int) length); // 默认截取最大长度的字符串信息
            }
            return valueObj;
        } else if (type == byte[].class || type == Byte[].class) {
            if (valueObj == null) return null;
            return valueObj instanceof String ? valueObj : HexUtil.encodeHexStr((byte[]) valueObj);
        } else {
            return valueObj;
//            throw SqlExceptionUtil.create("unknown field type: " + type.toString(), null);
        }
    }

    // 向StringBuilder添加值信息
    void appendValue(StringBuilder sb, Object valueObj, DataType dataType, String form, long length, Class<?> type) throws SQLException {
        if (type == Integer.class || type == int.class) {
            if (valueObj == null) {
                DataSQLConstructor.appendValueName(sb, (Integer) null);
            } else {
                DataSQLConstructor.appendValueName(sb, dataType == DataType.NUMBER_FORM ? ConvertUtil.stringToInt(new DecimalFormat(form).format(valueObj)) : (Integer) valueObj);
            }
        } else if (type == Double.class || type == double.class) {
            if (valueObj == null) {
                DataSQLConstructor.appendValueName(sb, (Double) null);
            } else {
                DataSQLConstructor.appendValueName(sb, dataType == DataType.NUMBER_FORM ? ConvertUtil.stringToDouble(new DecimalFormat(form).format(valueObj)) : (Double) valueObj);
            }
        } else if (type == Long.class || type == long.class) {
            if (valueObj == null) {
                DataSQLConstructor.appendValueName(sb, (Long) null);
            } else {
                DataSQLConstructor.appendValueName(sb, dataType == DataType.NUMBER_FORM ? ConvertUtil.stringToLong(new DecimalFormat(form).format(valueObj)) : (Long) valueObj);
            }
        } else if (type == Float.class || type == float.class) {
            if (valueObj == null) {
                DataSQLConstructor.appendValueName(sb, (Float) null);
            } else {
                DataSQLConstructor.appendValueName(sb, dataType == DataType.NUMBER_FORM ? ConvertUtil.stringToFloat(new DecimalFormat(form).format(valueObj)) : (Float) valueObj);
            }
        } else if (type == Date.class) {
            if (dataType == DataType.DATE_STRING) {
                DataSQLConstructor.appendValueName(sb, DateUtil.getStringByFormat((Date) valueObj, form));
            } else if (dataType == DataType.DATE_LONG) {
                DataSQLConstructor.appendValueName(sb, valueObj == null ? null : ((Date) valueObj).getTime());
            } else { // 默认采用年月日时分秒的形式
                DataSQLConstructor.appendValueName(sb, DateUtil.getStringByFormat((Date) valueObj, DateUtil.dateFormatYMDHMS));
            }
        } else if (type == String.class) {
            if (length != -1 && valueObj != null && valueObj.toString().length() > length) {
                valueObj = valueObj.toString().substring(0, (int) length); // 默认截取最大长度的字符串信息
            }
            DataSQLConstructor.appendValueName(sb, (String) valueObj);
        } else if (type == byte[].class || type == Byte[].class) {
            throw SqlExceptionUtil.create("byte[] field type can't to sql!", null);
        } else {
            throw SqlExceptionUtil.create("unknown field type: " + type.toString(), null);
        }
    }

    // 向StringBuilder添加值信息
    void appendValue(ContentValues values, String fieldName, Object valueObj, DataType dataType, String form, long length, Class<?> type) throws SQLException {
        if (type == Integer.class || type == int.class) {
            if (valueObj == null) {
                values.put(fieldName, (Integer) null);
            } else {
                values.put(fieldName, dataType == DataType.NUMBER_FORM ? ConvertUtil.stringToInt(new DecimalFormat(form).format(valueObj)) : (Integer) valueObj);
            }
        } else if (type == Double.class || type == double.class) {
            if (valueObj == null) {
                values.put(fieldName, (Double) null);
            } else {
                values.put(fieldName, dataType == DataType.NUMBER_FORM ? ConvertUtil.stringToDouble(new DecimalFormat(form).format(valueObj)) : (Double) valueObj);
            }
        } else if (type == Long.class || type == long.class) {
            if (valueObj == null) {
                values.put(fieldName, (Long) null);
            } else {
                values.put(fieldName, dataType == DataType.NUMBER_FORM ? ConvertUtil.stringToLong(new DecimalFormat(form).format(valueObj)) : (Long) valueObj);
            }
        } else if (type == Float.class || type == float.class) {
            if (valueObj == null) {
                values.put(fieldName, (Float) null);
            } else {
                values.put(fieldName, dataType == DataType.NUMBER_FORM ? ConvertUtil.stringToFloat(new DecimalFormat(form).format(valueObj)) : (Float) valueObj);
            }
        } else if (type == Date.class) {
            if (dataType == DataType.DATE_STRING) {
                values.put(fieldName, DateUtil.getStringByFormat((Date) valueObj, form));
            } else if (dataType == DataType.DATE_LONG) {
                values.put(fieldName, valueObj == null ? null : ((Date) valueObj).getTime());
            } else { // 默认采用年月日时分秒的形式
                values.put(fieldName, DateUtil.getStringByFormat((Date) valueObj, DateUtil.dateFormatYMDHMS));
            }
        } else if (type == String.class) {
            if (length != -1 && valueObj != null && valueObj.toString().length() > length) {
                valueObj = valueObj.toString().substring(0, (int) length); // 默认截取最大长度的字符串信息
            }
            values.put(fieldName, (String) valueObj);
        } else if (type == byte[].class || type == Byte[].class) {
            values.put(fieldName, (byte[]) valueObj);
        } else {
            throw SqlExceptionUtil.create("unknown field type: " + type.toString(), null);
        }
    }

    // 向ContentValues增加元素
    static void appendValue(ContentValues values, String key, Object obj) throws SQLException {
        Class<?> type = obj.getClass();
        if (type == Integer.class || type == int.class) {
            values.put(key, (int) obj);
        } else if (type == Double.class || type == double.class) {
            values.put(key, (double) obj);
        } else if (type == Long.class || type == long.class) {
            values.put(key, (long) obj);
        } else if (type == Float.class || type == float.class) {
            values.put(key, (float) obj);
        } else if (type == String.class) {
            values.put(key, (String) obj);
        } else if (type == byte[].class || type == Byte[].class) {
            values.put(key, (byte[]) obj);
        } else {
            throw SqlExceptionUtil.create("unknown field type: " + type.toString(), null);
        }
    }

    // 通过反射向单个field填充数据
    static void appendField(Field field, Object senObj, Object value) throws IllegalAccessException, SQLException {
        Class<?> type = field.getType();
        if (type == Short.class || type == short.class) {
            field.setShort(senObj, (short) value);
        } else if (type == Integer.class || type == int.class) {
            field.setInt(senObj, (int) value);
        } else if (type == Long.class || type == long.class) {
            field.setLong(senObj, (long) value);
        } else if (type == Float.class || type == float.class) {
            field.setFloat(senObj, (float) value);
        } else if (type == Double.class || type == double.class) {
            field.setDouble(senObj, (double) value);
        } else if (type == Boolean.class || type == boolean.class) {
            field.setBoolean(senObj, value instanceof String ? ConvertUtil.stringToBoolean((String) value) : value instanceof Boolean ? (Boolean) value : false);
        } else if (type == Date.class) {
            field.set(senObj, DateUtil.getDateByFormat((String) value, DateUtil.dateFormatYMDHMS));
        } else if (type == String.class) {
            field.set(senObj, value);
        } else if (type == byte[].class || type == Byte[].class) {
            String valueS = value instanceof String ? (String) value : null;
            if (valueS != null)
                field.set(senObj, HexUtil.decodeHex(valueS));
        } else {
            throw SqlExceptionUtil.create("unknown field type: " + type.toString(), null);
        }
    }

    /**
     * 将json串转换成数据库builder对象
     *
     * @param json json串
     * @return 数据库的builder对象
     */
    public static BaseBuilder fromJson(String json) throws JSONException, SQLException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        JSONObject jsonObject = new JSONObject(json);
        SqliteType sqliteType = SqliteType.getSqliteTypeByName(jsonObject.optString(SqliteType.KEY_NAME));
        if (sqliteType == SqliteType.DELETE) {
            return DeleteBuilder.fromJson(jsonObject);
        } else if (sqliteType == SqliteType.INSERT) {
            return InsertBuilder.fromJson(jsonObject);
        } else if (sqliteType == SqliteType.UPDATE) {
            return UpdateBuilder.fromJson(jsonObject);
        } else if (sqliteType == SqliteType.REPLACE) {
            return ReplaceBuilder.fromJson(jsonObject);
        } else if (sqliteType == SqliteType.CREATE_OR_UPDATE) {
            return CreateOrUpdateBuilder.fromJson(jsonObject);
        } else if (sqliteType == SqliteType.QUERY) {
            return QueryBuilder.fromJson(jsonObject);
        } else {
            throw SqlExceptionUtil.create("UnKnown sqlite type!", null);
        }
    }

}
