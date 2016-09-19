package com.asen.android.lib.base.core.sqlite.builder.impl;

import android.content.ContentValues;
import android.text.TextUtils;

import com.asen.android.lib.base.core.sqlite.builder.IUpdateBuilder;
import com.asen.android.lib.base.core.sqlite.builder.type.BuilderType;
import com.asen.android.lib.base.core.sqlite.builder.type.SqliteType;
import com.asen.android.lib.base.core.sqlite.field.AField;
import com.asen.android.lib.base.core.sqlite.field.DataType;
import com.asen.android.lib.base.core.sqlite.field.DefaultType;
import com.asen.android.lib.base.core.sqlite.table.ATable;
import com.asen.android.lib.base.core.sqlite.table.TableConfig;
import com.asen.android.lib.base.core.sqlite.utils.DataSqlConstructor;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 更新的Builder
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/19 16:50
 */
class UpdateBuilder extends BaseSqlBuilder implements IUpdateBuilder {

    private String mTableName; // 表名

    private ContentValues mValues; // 参数与值

    private String mWhereClause; // where条件语句

    private String[] mWhereArgs; // where条件的参数语句

//    private TableConfig mTableConfig; // 表信息

    /**
     * 构造函数
     *
     * @param table       表名
     * @param values      要更新的参数
     * @param whereClause where条件语句
     * @param whereArgs   where条件中？的参数值
     */
    public UpdateBuilder(String table, ContentValues values, String whereClause, String[] whereArgs) {
        setBuilderType(BuilderType.ANDROID_SQL);
        this.mTableName = table;
        this.mValues = values;
        this.mWhereClause = whereClause;
        this.mWhereArgs = whereArgs;
    }

    /**
     * 构造函数
     *
     * @param sql    非查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     */
    public UpdateBuilder(String sql, Object[] params) {
        super(sql, params);
    }

    /**
     * 构造函数
     *
     * @param senObj 包含有注解 {@link AField} 和 {@link ATable}的Java对象
     */
    public UpdateBuilder(Object senObj) {
        super(senObj);
    }

//    /**
//     * 构造函数
//     *
//     * @param json JSON串形式
//     */
//    public UpdateBuilder(String json) {
//        super(json);
//    }

    @Override
    void refreshAndroidSql() throws SQLException {
        StringBuilder sb = new StringBuilder("UPDATE ");
        DataSqlConstructor.appendEntityName(sb, mTableName);

        sb.append("SET ");

        StringBuilder params = null;
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
            DataSqlConstructor.appendEntityName(params, key);
            params.append(" = ? ");
        }

        if (params == null) {
            throw SqlExceptionUtil.create("ContentValues is empty!", null);
        } else {
            sb.append(params);
        }

        sb.append("WHERE ").append(mWhereClause);
        Collections.addAll(paramsList, mWhereArgs);

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

        mValues = new ContentValues();

        StringBuilder whereSb = null;
        Field[] fields = cls.getDeclaredFields();
        List<String> whereArgsList = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AField.class)) {
                if (!field.isAccessible())
                    field.setAccessible(true);

                AField annotation = field.getAnnotation(AField.class);
                String fieldName = annotation.fieldName();
                DefaultType defaultType = annotation.defaultType();
                String defaultValue = annotation.defaultValue();
//                String description = annotation.description();
                boolean id = annotation.id(); // 主键
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

                    if (id) {
                        valueObj = getAppendValue(valueObj, dataType, form, length, type); // 获取处理后的数据
                        // 主键
                        if (whereSb == null) {
                            whereSb = new StringBuilder();
                            DataSqlConstructor.appendEntityName(whereSb, fieldName);
//                            whereSb.append(fieldName);
                        } else {
                            whereSb.append("and ");
                            DataSqlConstructor.appendEntityName(whereSb, fieldName);
//                            whereSb.append(fieldName);
                        }

                        if (valueObj == null) {
                            whereSb.append(" is ? ");
                        } else {
                            whereSb.append(" = ? ");
                        }
                        whereArgsList.add(valueObj == null ? null : valueObj.toString());
                    } else {
                        appendValue(mValues, fieldName, valueObj, dataType, form, length, type);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw SqlExceptionUtil.create("", e);
                }
            }
        }

        if (whereSb == null) {
            throw SqlExceptionUtil.create("Have no id field!", null);
        }

        mWhereClause = whereSb.toString();
        mWhereArgs = whereArgsList.toArray(new String[whereArgsList.size()]);
    }

    @Override
    void refreshObject2Sql(Object senObj) throws SQLException {
        Class<?> cls = senObj.getClass();

        StringBuilder sb = new StringBuilder("UPDATE ");

        ATable aTable = cls.getAnnotation(ATable.class);
        if (aTable != null) {
            String tableName = aTable.tableName();
            if (TextUtils.isEmpty(tableName)) {
                throw SqlExceptionUtil.create("TableName is empty! ", null);
            }
            DataSqlConstructor.appendEntityName(sb, tableName);
        } else {
            throw SqlExceptionUtil.create("Has no ATable annotation! ", null);
        }

        sb.append("SET ");

        StringBuilder setBuilder = null, whereBuilder = null;
        Field[] fields = cls.getDeclaredFields();
        List<Object> setParams = new ArrayList<>();
        List<Object> whereParams = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AField.class)) {
                if (!field.isAccessible())
                    field.setAccessible(true);

                AField annotation = field.getAnnotation(AField.class);
                String fieldName = annotation.fieldName();
                DefaultType defaultType = annotation.defaultType();
                String defaultValue = annotation.defaultValue();
//                String description = annotation.description();
                boolean id = annotation.id();
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

                    valueObj = getAppendValue(valueObj, dataType, form, length, type); // 获取处理后的数据

                    if (id) {
                        // 主键
                        if (whereBuilder == null) {
                            whereBuilder = new StringBuilder();
                            DataSqlConstructor.appendEntityName(whereBuilder, fieldName);
                        } else {
                            whereBuilder.append("and ");
                            DataSqlConstructor.appendEntityName(whereBuilder, fieldName);
                        }
                        whereBuilder.append(" = ? ");
                        whereParams.add(valueObj);
                    } else {
                        // 非主键
                        if (setBuilder == null) {
                            setBuilder = new StringBuilder();
                            DataSqlConstructor.appendEntityName(setBuilder, fieldName);
                        } else {
                            setBuilder.append(", ");
                            DataSqlConstructor.appendEntityName(setBuilder, fieldName);
                        }
                        setBuilder.append(" = ? ");
                        setParams.add(valueObj);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw SqlExceptionUtil.create("", e);
                }
            }
        }

        if (whereBuilder == null) {
            throw SqlExceptionUtil.create("Have no id field!", null);
        }

        // 设置SQL语句
        sb.append(setBuilder);
        sb.append("WHERE ");
        sb.append(whereBuilder);
        super.setSql(sb.toString());
        // 设置参数集合
        List<Object> paramsList = getParamsList();
        paramsList.clear();
        paramsList.addAll(setParams);
        paramsList.addAll(whereParams);
    }

    @Override
    public String toJson() throws JSONException, SQLException {
        JSONObject jsonObject = new JSONObject();
        // sql语句操作的类型
        jsonObject.put(SqliteType.KEY_NAME, SqliteType.UPDATE.getName());
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
            contentObj.put(BaseBuilder.KEY_ANDROID_WHERE_CLAUSE, mWhereClause);
            contentObj.put(BaseBuilder.KEY_ANDROID_WHERE_ARGS, JsonUtil.arrayToJsonArray(mWhereArgs));
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
     * 将jsonObject转换成 UpdateBuilder对象
     *
     * @param jsonObject JSON对象
     * @return UpdateBuilder对象
     */
    public static UpdateBuilder fromJson(JSONObject jsonObject) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        BuilderType builderType = BuilderType.getBuilderTypeByName(jsonObject.optString(BuilderType.KEY_NAME));
        JSONObject contentObj = jsonObject.optJSONObject(BaseBuilder.KEY_CONTENT);
        if (builderType == BuilderType.SQL) {
            // 纯SQL语句
            String sql = contentObj.optString(BaseBuilder.KEY_SQL_SQL);
            JSONArray paramsArray = contentObj.optJSONArray(BaseBuilder.KEY_SQL_PARAMS);
            return new UpdateBuilder(sql, JsonUtil.jsonArrayToList(paramsArray).toArray());

        } else if (builderType == BuilderType.ANDROID_SQL) {
            // Android的SQL执行
            String tableName = contentObj.optString(BaseBuilder.KEY_ANDROID_TABLE);
            String whereClause = contentObj.optString(BaseBuilder.KEY_ANDROID_WHERE_CLAUSE, null);

            List<Object> whereArgsList = JsonUtil.jsonArrayToList(contentObj.optJSONArray(BaseBuilder.KEY_ANDROID_WHERE_ARGS));
            String[] whereArgs = new String[whereArgsList.size()];
            for (int i = 0; i < whereArgsList.size(); i++) {
                Object obj = whereArgsList.get(i);
                whereArgs[i] = obj == null ? null : obj.toString();
            }

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
            return new UpdateBuilder(tableName, contentValues, whereClause, whereArgs);

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
            return new UpdateBuilder(newInstance);
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
    public ContentValues getValues() throws SQLException {
        refreshBuilder2Android();
        return mValues;
    }

    @Override
    public String getWhereClause() throws SQLException {
        return mWhereClause;
    }

    @Override
    public String[] getWhereArgs() throws SQLException {
        return mWhereArgs;
    }

}
