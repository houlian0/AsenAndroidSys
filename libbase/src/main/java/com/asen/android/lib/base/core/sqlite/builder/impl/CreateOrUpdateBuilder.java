package com.asen.android.lib.base.core.sqlite.builder.impl;

import android.content.ContentValues;
import android.text.TextUtils;

import com.asen.android.lib.base.core.sqlite.builder.ICreateOrUpdateBuilder;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 创建或更新的Builder
 *
 * @author Asen
 * @version v1.0
 * @date 2016/9/18 16:17
 */
class CreateOrUpdateBuilder extends BaseBuilder implements ICreateOrUpdateBuilder {

    private String mTableName; // 表名

    private String mNullColumnHack; // nullColumnHack

    private ContentValues mValues; // 参数与值

    private String mWhereClause; // where条件语句

    private String[] mWhereArgs; // where条件的参数语句

    private boolean isNeadRefresh2Android = true; // 是否更新成android的语句

    /**
     * 构造函数
     *
     * @param senObj 包含有注解 {@link AField} 和 {@link ATable}的Java对象
     */
    public CreateOrUpdateBuilder(Object senObj) {
        super(senObj);
    }

    /**
     * 构造函数
     *
     * @param table          表名
     * @param nullColumnHack 当values参数为空或者里面没有内容的时候，insert是会失败的(底层数据库不允许插入一个空行)，为了防止这种情况，
     *                       要在这里指定一个列名，到时候如果发现将要插入的行为空行时，就会将你指定的这个列名的值设为null，然后再向数据库中插入
     * @param values         要新增或修改的参数
     * @param whereClause    where条件语句
     * @param whereArgs      where条件中？的参数值
     */
    public CreateOrUpdateBuilder(String table, String nullColumnHack, ContentValues values, String whereClause, String[] whereArgs) {
        setBuilderType(BuilderType.ANDROID_SQL);
        this.mTableName = table;
        this.mNullColumnHack = nullColumnHack;
        this.mValues = values;
        this.mWhereClause = whereClause;
        this.mWhereArgs = whereArgs;
    }

    @Override
    public String toJson() throws JSONException, SQLException {
        JSONObject jsonObject = new JSONObject();
        // sql语句操作的类型
        jsonObject.put(SqliteType.KEY_NAME, SqliteType.CREATE_OR_UPDATE.getName());
        // builder的类型，sql语句类型，还是其他
        jsonObject.put(BuilderType.KEY_NAME, builderType.getName());

        // 拼写具体内容：
        JSONObject contentObj = new JSONObject();

        if (builderType == BuilderType.OBJECT) {
            // 1、拼写Object对象
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
            contentObj.put(BaseBuilder.KEY_ANDROID_WHERE_CLAUSE, mWhereClause);
            contentObj.put(BaseBuilder.KEY_ANDROID_WHERE_ARGS, JsonUtil.arrayToJsonArray(mWhereArgs));
        }

        jsonObject.put(BaseBuilder.KEY_CONTENT, contentObj);

        return jsonObject.toString();
    }

    /**
     * 将jsonObject转换成 CreateOrUpdateBuilder对象
     *
     * @param jsonObject JSON对象
     * @return CreateOrUpdateBuilder对象
     */
    public static CreateOrUpdateBuilder fromJson(JSONObject jsonObject) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        BuilderType builderType = BuilderType.getBuilderTypeByName(jsonObject.optString(BuilderType.KEY_NAME));
        JSONObject contentObj = jsonObject.optJSONObject(BaseBuilder.KEY_CONTENT);

        if (builderType == BuilderType.OBJECT) {
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
            return new CreateOrUpdateBuilder(newInstance);
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

            String whereClause = contentObj.optString(BaseBuilder.KEY_ANDROID_WHERE_CLAUSE, null);

            List<Object> whereArgsList = JsonUtil.jsonArrayToList(contentObj.optJSONArray(BaseBuilder.KEY_ANDROID_WHERE_ARGS));
            String[] whereArgs = new String[whereArgsList.size()];
            for (int i = 0; i < whereArgsList.size(); i++) {
                Object obj = whereArgsList.get(i);
                whereArgs[i] = obj == null ? null : obj.toString();
            }

            return new CreateOrUpdateBuilder(tableName, nullColumnHack, contentValues, whereClause, whereArgs);
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

    @Override
    public String getWhereClause() throws SQLException {
        refreshBuilder2Android();
        return mWhereClause;
    }

    @Override
    public String[] getWhereArgs() throws SQLException {
        refreshBuilder2Android();
        return mWhereArgs;
    }

    // 刷新到Android格式
    void refreshBuilder2Android() throws SQLException {
        if (isNeadRefresh2Android) {
            if (BuilderType.ANDROID_SQL == getBuilderType()) {
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

    /**
     * 将对象格式 转成 Android格式
     *
     * @param senObj 指定对象
     */
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

                    appendValue(mValues, fieldName, valueObj, dataType, form, length, type);

                    if (id) {
                        valueObj = getAppendValue(valueObj, dataType, form, length, type); // 获取处理后的数据
                        // 主键
                        if (whereSb == null) {
                            whereSb = new StringBuilder();
                            DataSqlConstructor.appendEntityName(whereSb, fieldName);
                        } else {
                            whereSb.append("and ");
                            DataSqlConstructor.appendEntityName(whereSb, fieldName);
                        }

                        if (valueObj == null) {
                            whereSb.append(" is ? ");
                        } else {
                            whereSb.append(" = ? ");
                        }
                        whereArgsList.add(valueObj == null ? null : valueObj.toString());
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

}
