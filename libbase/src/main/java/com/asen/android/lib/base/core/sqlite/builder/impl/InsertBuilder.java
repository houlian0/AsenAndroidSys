package com.asen.android.lib.base.core.sqlite.builder.impl;

import android.content.ContentValues;
import android.text.TextUtils;

import com.asen.android.lib.base.core.sqlite.builder.IInsertBuilder;
import com.asen.android.lib.base.core.sqlite.builder.type.BuilderType;
import com.asen.android.lib.base.core.sqlite.builder.type.SqliteType;
import com.asen.android.lib.base.core.sqlite.field.AField;
import com.asen.android.lib.base.core.sqlite.field.DataType;
import com.asen.android.lib.base.core.sqlite.field.DefaultType;
import com.asen.android.lib.base.core.sqlite.table.ATable;
import com.asen.android.lib.base.core.sqlite.table.TableConfig;
import com.asen.android.lib.base.core.sqlite.utils.DataSQLConstructor;
import com.asen.android.lib.base.core.sqlite.utils.SqlExceptionUtil;
import com.asen.android.lib.base.tool.util.DateUtil;
import com.asen.android.lib.base.tool.util.HexUtil;
import com.asen.android.lib.base.tool.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 插入的Builder
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/19 16:50
 */
class InsertBuilder extends BaseSqlBuilder implements IInsertBuilder {

    private String mTableName; // 表名

    private String mNullColumnHack; // nullColumnHack

    private ContentValues mValues; // 参数与值

//    private TableConfig mTableConfig; // 表信息

    /**
     * 构造函数
     *
     * @param table          表名
     * @param nullColumnHack 当values参数为空或者里面没有内容的时候，insert是会失败的(底层数据库不允许插入一个空行)，为了防止这种情况，
     *                       要在这里指定一个列名，到时候如果发现将要插入的行为空行时，就会将你指定的这个列名的值设为null，然后再向数据库中插入
     * @param values         要插入的参数
     */
    public InsertBuilder(String table, String nullColumnHack, ContentValues values) {
        setBuilderType(BuilderType.ANDROID_SQL);
        this.mTableName = table;
        this.mNullColumnHack = nullColumnHack;
        this.mValues = values;
    }

    /**
     * 构造函数
     *
     * @param sql    非查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     */
    public InsertBuilder(String sql, Object[] params) {
        super(sql, params);
    }

    /**
     * 构造函数
     *
     * @param senObj 包含有注解 {@link AField} 和 {@link ATable}的Java对象
     */
    public InsertBuilder(Object senObj) {
        super(senObj);
    }

//    /**
//     * 构造函数
//     *
//     * @param json JSON串形式
//     */
//    public InsertBuilder(String json) {
//        super(json);
//    }
//
//    @Override
//    void refreshSql(String sql, Object[] params) throws SQLException { // SQL语句转Android格式
//        if (sql == null) throw SqlExceptionUtil.create("SQL is null!", null);
//
//        char splitChart = ' '; // 以哪个字符拆分，中途会进行改变
//        int i = 0;
//        int length = sql.length();
//        int endIndex = 0;
//        int type = 0; // 类型为1时，获取表名；类型为2时，获取字段名；类型为3时，获取值
//
//        List<String> columns = new ArrayList<>(); // 字段名集合
//        List<String> values = new ArrayList<>(); // 字段值集合
//        while (i < length) {
//            int startIndex = i == 0 ? 0 : endIndex;
//
//            endIndex = sql.indexOf(splitChart, i);
//            if (endIndex < 0) endIndex = length;
//
//            String value = sql.substring(startIndex, endIndex).trim().toUpperCase();
//            if (value.contains("INFO") && type == 0) {
//                if (value.length() == 4) {
//                    splitChart = '(';
//                    type = 1;
//                } else {
//                    mTableName = value.substring(3, value.length());
//                    type = 2;
//                }
//            } else if (type == 1) {
//                mTableName = value;
//                splitChart = ')';
//                type = 2;
//            } else if (type == 2) {
//                String[] split = value.split(",");
//                for (String s : split) {
//                    columns.add(s.trim());
//                }
//
//                i = endIndex + 1;
//                endIndex = sql.indexOf('(', i);
//                type = 3;
//            } else if (type == 3) {
//                String[] split = value.split(",");
//                for (String s : split) {
//                    values.add(s.trim());
//                }
//            }
//
//            i = endIndex + 1;
//        }
//
//        int size = columns.size();
//        if (size != values.size()) {
//            throw SqlExceptionUtil.create("SQL Error: " + sql, null);
//        }
//
//        mValues = new ContentValues();
//        int number = 0;
//        for (i = 0; i < size; i++) {
//            String column = columns.get(i);
//            Object value = values.get(i);
//            if ("?".equals(value)) {
//                value = params[number++];
//            }
//            mValues.put(column, (String) value);
//        }
//    }

    @Override
    void refreshAndroidSql() throws SQLException {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        DataSQLConstructor.appendEntityName(sb, mTableName);

        StringBuilder params = null;
        StringBuilder values = null;
        List<Object> paramsList = getParamsList();
        Set<String> keySet = mValues.keySet();
        for (String key : keySet) {
            // 获取参数
            Object value = mValues.get(key);
            paramsList.add(value);
            // 拼接所有的列名
            if (params == null) {
                params = new StringBuilder();
            } else {
                params.append(", ");
            }
            DataSQLConstructor.appendEntityName(params, key);
            // 拼接所有的值对应的？
            if (values == null) {
                values = new StringBuilder("?");
            } else {
                values.append(", ?");
            }
        }

        sb.append("(");
        if (params == null) {
            if (mNullColumnHack != null) {
                DataSQLConstructor.appendEntityName(sb, mNullColumnHack);
            }
        } else {
            sb.append(params);
        }
        sb.append(") ");

        sb.append("VALUES (");
        if (values == null) {
            if (mNullColumnHack != null) {
                sb.append("null");
            }
        } else {
            sb.append(values);
        }
        sb.append(")");

        setSql(sb.toString());
    }

    @Override
    void refreshObject2Android(Object senObj) throws SQLException {
        Class<?> cls = senObj.getClass();

        ATable aTable = cls.getAnnotation(ATable.class);
        if (aTable != null) {
            String tableName = aTable.tableName();
            if (TextUtils.isEmpty(tableName)) {
                throw SqlExceptionUtil.create("TableName is empty! ", null);
            }
            mTableName = tableName;
        } else {
            throw SqlExceptionUtil.create("Has no ATable annotation! ", null);
        }
        mNullColumnHack = null;

        mValues = new ContentValues();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AField.class)) {
                if (!field.isAccessible())
                    field.setAccessible(true);

                AField annotation = field.getAnnotation(AField.class);
                String fieldName = annotation.fieldName();
                DefaultType defaultType = annotation.defaultType();
                String defaultValue = annotation.defaultValue();
//                String description = annotation.description();
//                boolean id = annotation.id();
//                boolean autoincrement = annotation.autoincrement();
                boolean canBeNull = annotation.canBeNull();
                long length = annotation.length();
                DataType dataType = annotation.dataType();
                String form = annotation.form();
                Class<?> type = field.getType();

                try {
                    Object valueObj = field.get(senObj);
                    // 设置默认值
                    if (valueObj == null) {
                        valueObj = getDefaultValue(defaultType, defaultValue, type);
                    }

                    if (!canBeNull && valueObj == null) {
                        throw SqlExceptionUtil.create(fieldName + " can not be null! ", null);
                    }

                    appendValue(mValues, fieldName, valueObj, dataType, form, length, type);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw SqlExceptionUtil.create("", e);
                }
            }
        }
    }

    @Override
    void refreshObject2Sql(Object senObj) throws SQLException {
        Class<?> cls = senObj.getClass();

        StringBuilder sb = new StringBuilder("INSERT INTO ");

        ATable aTable = cls.getAnnotation(ATable.class);
        if (aTable != null) {
            String tableName = aTable.tableName();
            if (TextUtils.isEmpty(tableName)) {
                throw SqlExceptionUtil.create("TableName is empty! ", null);
            }
            DataSQLConstructor.appendEntityName(sb, tableName);
        } else {
            throw SqlExceptionUtil.create("Has no ATable annotation! ", null);
        }

        StringBuilder keyBuilder = null, valueBuilder = null;

        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AField.class)) {
                if (!field.isAccessible())
                    field.setAccessible(true);

                AField annotation = field.getAnnotation(AField.class);
                String fieldName = annotation.fieldName();
                DefaultType defaultType = annotation.defaultType();
                String defaultValue = annotation.defaultValue();
//                String description = annotation.description();
//                boolean id = annotation.id();
//                boolean autoincrement = annotation.autoincrement();
                boolean canBeNull = annotation.canBeNull();
                long length = annotation.length();
                DataType dataType = annotation.dataType();
                String form = annotation.form();
                Class<?> type = field.getType();

                // 字段信息填充
                if (keyBuilder == null) {
                    keyBuilder = new StringBuilder();
                } else {
                    keyBuilder.append(",");
                }
                DataSQLConstructor.appendEntityName(sb, fieldName);

                // 值填充
                if (valueBuilder == null) {
                    valueBuilder = new StringBuilder();
                } else {
                    keyBuilder.append(",");
                }
                try {
                    Object valueObj = field.get(senObj);
                    // 设置默认值
                    if (valueObj == null) {
                        valueObj = getDefaultValue(defaultType, defaultValue, type);
                    }
                    // 判断是否不能为空
                    if (!canBeNull && valueObj == null) {
                        throw SqlExceptionUtil.create(fieldName + " can not be null! ", null);
                    }
                    appendValue(valueBuilder, valueObj, dataType, form, length, type);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw SqlExceptionUtil.create("", e);
                }
            }
        }

        if (keyBuilder == null) {
            throw SqlExceptionUtil.create("Have no field list!", null);
        }

        sb.append(" (").append(keyBuilder).append(") ");
        sb.append("VALUES (").append(valueBuilder).append(")");

        super.setSql(sb.toString());
        getParamsList().clear();
    }

    @Override
    public String toJson() throws JSONException, SQLException {
        JSONObject jsonObject = new JSONObject();
        // sql语句操作的类型
        jsonObject.put(SqliteType.KEY_NAME, SqliteType.INSERT.getName());
        // builder的类型，sql语句类型，还是其他
        jsonObject.put(BuilderType.KEY_NAME, builderType.getName());

        // 拼写具体内容：
        JSONObject contentObj = new JSONObject();
        if (builderType == BuilderType.SQL) {
            // 1、拼写sql语句
            contentObj.put(BaseBuilder.KEY_SQL_SQL, sql);
            if (paramsList != null) {
                contentObj.put(BaseBuilder.KEY_SQL_PARAMS, JsonUtil.listToJsonArray(paramsList));
            } else {
                contentObj.put(BaseBuilder.KEY_SQL_PARAMS, null);
            }
        } else if (builderType == BuilderType.ANDROID_SQL) {
            // 2、拼写Android原生SQL
            contentObj.put(BaseBuilder.KEY_ANDROID_TABLE, mTableName);
            Map<String, Object> valueMap = null;
            if (mValues != null) {
                valueMap = new HashMap<>();
                Set<String> keySet = mValues.keySet();
                for (String key : keySet) {
                    Object obj = mValues.get(key);
                    if (obj != null) {
                        if (obj.getClass() == byte[].class || obj.getClass() == Byte[].class) {
                            JSONObject object = new JSONObject();
                            object.put(BaseBuilder.KEY_ANDROID_VALUES_BYTES, HexUtil.encodeHexStr((byte[]) obj));
                            obj = object;
                        }
                    }
                    valueMap.put(key, obj);
                }
            }
            contentObj.put(BaseBuilder.KEY_ANDROID_NULL_COLUMN_HACK, mNullColumnHack);
            contentObj.put(BaseBuilder.KEY_ANDROID_VALUES, JsonUtil.mapToJsonObject(valueMap));
        } else if (builderType == BuilderType.OBJECT) {
            // 3、拼写Object对象
            Class<?> cls = getSenObj().getClass();

            ATable aTable = cls.getAnnotation(ATable.class);
            if (aTable != null) {
                String tableName = aTable.tableName();
                contentObj.put(BaseBuilder.KEY_OBJECT_TABLE, tableName);
            } else {
                throw SqlExceptionUtil.create("Has no ATable annotation! ", null);
            }

            contentObj.put(BaseBuilder.KEY_OBJECT_CLASS, cls.getName());

            JSONArray arrayFields = new JSONArray();
            Map<String, Object> mapValues = new HashMap<>();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(AField.class)) {
                    if (!field.isAccessible())
                        field.setAccessible(true);

                    JSONObject fieldObj = new JSONObject();
                    AField annotation = field.getAnnotation(AField.class);
                    String fieldName = annotation.fieldName();
                    DefaultType defaultType = annotation.defaultType();
                    String defaultValue = annotation.defaultValue();
                    boolean id = annotation.id();
                    boolean canBeNull = annotation.canBeNull();
                    long length = annotation.length();
                    DataType dataType = annotation.dataType();
                    String form = annotation.form();
                    Class<?> type = field.getType();


                    fieldObj.put(BaseBuilder.KEY_OBJECT_FIELDS_ID, id);
                    fieldObj.put(BaseBuilder.KEY_OBJECT_FIELDS_NAME, fieldName);
                    fieldObj.put(BaseBuilder.KEY_OBJECT_FIELDS_IS_DATE, type == Date.class);
                    arrayFields.put(fieldObj);

                    Object valueObj = null;
                    try {
                        valueObj = field.get(getSenObj());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    // 设置默认值
                    if (valueObj == null) {
                        valueObj = getDefaultValue(defaultType, defaultValue, type);
                    }
                    // 判断是否不能为空
                    if (!canBeNull && valueObj == null) {
                        throw SqlExceptionUtil.create(fieldName + " can not be null! ", null);
                    }

                    if (type == Date.class) { // 时间类型，只传固定格式的
                        dataType = DataType.DATE_STRING;
                        form = DateUtil.dateFormatYMDHMS;
                    }

                    Object value = getAppendValue(valueObj, dataType, form, length, type);
                    mapValues.put(fieldName, value);
                }
            }

            contentObj.put(BaseBuilder.KEY_OBJECT_FIELDS, arrayFields);
            contentObj.put(BaseBuilder.KEY_OBJECT_VALUES, JsonUtil.mapToJsonObject(mapValues));
        }
        jsonObject.put(BaseBuilder.KEY_CONTENT, contentObj);
        return jsonObject.toString();
    }

    /**
     * 将jsonObject转换成InsertBuilder对象
     *
     * @param jsonObject JSON对象
     * @return InsertBuilder对象
     */
    public static InsertBuilder fromJson(JSONObject jsonObject) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        BuilderType builderType = BuilderType.getBuilderTypeByName(jsonObject.optString(BuilderType.KEY_NAME));
        JSONObject contentObj = jsonObject.optJSONObject(BaseBuilder.KEY_CONTENT);
        if (builderType == BuilderType.SQL) {
            // 纯SQL语句
            String sql = contentObj.optString(BaseBuilder.KEY_SQL_SQL);
            JSONArray paramsArray = contentObj.optJSONArray(BaseBuilder.KEY_SQL_PARAMS);
            return new InsertBuilder(sql, JsonUtil.jsonArrayToList(paramsArray).toArray());
        } else if (builderType == BuilderType.ANDROID_SQL) {
            // Android的SQL执行
            String tableName = contentObj.optString(BaseBuilder.KEY_ANDROID_TABLE);
            String nullColumnHack = contentObj.optString(BaseBuilder.KEY_ANDROID_NULL_COLUMN_HACK, null);
            Map<String, Object> valuesMap = JsonUtil.jsonObjectToMap(contentObj.optJSONObject(BaseBuilder.KEY_ANDROID_VALUES));
            ContentValues contentValues = new ContentValues();
            Set<Map.Entry<String, Object>> entries = valuesMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                Object value = entry.getValue();
                if (value.getClass() == JSONObject.class) {
                    value = HexUtil.decodeHex(((JSONObject) value).optString(BaseBuilder.KEY_ANDROID_VALUES_BYTES));
                }
                appendValue(contentValues, entry.getKey(), value);
            }
            return new InsertBuilder(tableName, nullColumnHack, contentValues);
        } else if (builderType == BuilderType.OBJECT) {
            // 获取参数对象
            String className = contentObj.optString(BaseBuilder.KEY_OBJECT_CLASS);
//            String tableName = contentObj.optString(BaseBuilder.KEY_OBJECT_TABLE);
//            JSONArray fieldArray = contentObj.optJSONArray(BaseBuilder.KEY_OBJECT_FIELDS);
            JSONObject valuesObject = contentObj.optJSONObject(BaseBuilder.KEY_OBJECT_VALUES);
            // 反射机制创建实例对象
            Class<?> cls = Class.forName(className);
            Object newInstance = TableConfig.getNewInstance(cls);
            // 通过反射机制向实例对象填入内容信息
            Map<String, Object> valuesMap = JsonUtil.jsonObjectToMap(valuesObject);
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(AField.class)) {
                    if (!field.isAccessible())
                        field.setAccessible(true);

                    AField annotation = field.getAnnotation(AField.class);
                    String fieldName = annotation.fieldName();
                    Object value = valuesMap.get(fieldName);
                    if (value == null) continue;

                    appendField(field, newInstance, value);
                }
            }
            return new InsertBuilder(newInstance);
        } else {
            throw SqlExceptionUtil.create("Known builderType!", null);
        }
    }

    @Override
    public String getTableName() throws SQLException {
        refreshBuilder2Android();
        return mTableName;
    }

    @Override
    public String getNullColumnHack() throws SQLException {
        refreshBuilder2Android();
        return mNullColumnHack;
    }

    @Override
    public ContentValues getValues() throws SQLException {
        refreshBuilder2Android();
        return mValues;
    }

}
