package com.asen.android.lib.base.core.sqlite.table;

import com.asen.android.lib.base.core.sqlite.field.AField;
import com.asen.android.lib.base.core.sqlite.field.DataType;
import com.asen.android.lib.base.core.sqlite.field.DefaultType;
import com.asen.android.lib.base.core.sqlite.field.FieldInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过Class获取表信息
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/18 17:23
 */

public class TableConfig {

    private Class<?> dataClass;

    private String tableName;

    private TableInfo tableInfo;

    public TableConfig(Class<?> dataClass) {
        this.dataClass = dataClass;
        this.tableName = getTableName(dataClass);
        List<FieldInfo> fieldInfos = getFieldInfoList(dataClass);
        this.tableInfo = new TableInfo(tableName, fieldInfos);
    }

    /**
     * 获取对象类信息
     *
     * @return 对象类信息
     */
    public Class<?> getDataClass() {
        return dataClass;
    }

    /**
     * 获取表名
     *
     * @return 表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 获取表的所有信息
     *
     * @return 表的所有信息
     */
    public TableInfo getTableInfo() {
        return tableInfo;
    }

    /**
     * 获取所有的字段的List集合
     *
     * @return 所有的字段的List集合
     */
    public List<FieldInfo> getFieldList() {
        return tableInfo.getFieldList();
    }

    /**
     * 获取所有的ID字段信息集合
     *
     * @return 所有的ID字段信息集合
     */
    public List<FieldInfo> getIdFieldList() {
        return tableInfo.getIdFieldList();
    }

    /**
     * 根据字段名称获取字段信息
     *
     * @param fieldName 字段名称
     * @return 字段信息
     */
    public FieldInfo getFieldInfoByFieldName(String fieldName) {
        return tableInfo.getFieldInfoByFieldName(fieldName);
    }

    /**
     * 获取构建新的对象
     *
     * @return 新的对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public Object getNewInstance() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return getNewInstance(dataClass);
    }

    /**
     * 根据 Class<T> 获取字段信息
     *
     * @param clazz 对象的Class
     * @return 字段信息
     */
    public static List<FieldInfo> getFieldInfoList(Class<?> clazz) {
        List<FieldInfo> result = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AField.class)) {
                if (!field.isAccessible())
                    field.setAccessible(true);

                AField annotation = field.getAnnotation(AField.class);
                String fieldName = annotation.fieldName();
                DefaultType defaultType = annotation.defaultType();
                String defaultValue = annotation.defaultValue();
                String description = annotation.description();
                boolean id = annotation.id();
                boolean autoincrement = annotation.autoincrement();
                boolean canBeNull = annotation.canBeNull();
                long length = annotation.length();
                DataType dataType = annotation.dataType();
                String form = annotation.form();
                result.add(new FieldInfo(fieldName, defaultType, defaultValue, description, id, autoincrement, canBeNull, length, dataType, form, field.getType()));
            }
        }
        return result;
    }


    /**
     * 根据 Class<T> 获取表名
     *
     * @param clazz 对象的Class
     * @return 表名
     */
    public static String getTableName(Class<?> clazz) {
        ATable databaseTable = clazz.getAnnotation(ATable.class);
        String name = null;
        if (databaseTable != null && databaseTable.tableName() != null && databaseTable.tableName().length() > 0) {
            name = databaseTable.tableName();
        }
        return name;
    }


    /**
     * 获取构建新的对象
     *
     * @param cls 对象的Class
     * @param <T> 对象类型
     * @return 新的对象
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public static <T> T getNewInstance(Class<T> cls) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> constructor = cls.getDeclaredConstructors()[0];
        Type[] types = constructor.getGenericParameterTypes();
        Object[] object = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            if (types[i] == int.class || types[i] == byte.class || types[i] == long.class || types[i] == char.class || types[i] == float.class || types[i] == double.class || types[i] == short.class) {
                object[i] = 0;
            } else if (types[i] == boolean.class) {
                object[i] = false;
            }
        }
        return (T) constructor.newInstance(object);
    }

}
