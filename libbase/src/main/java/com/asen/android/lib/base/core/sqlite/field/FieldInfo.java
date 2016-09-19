package com.asen.android.lib.base.core.sqlite.field;

/**
 * Created by ASEN on 2016/4/5.
 *
 * @author Asen
 * @version v1.0
 * @date 2016/4/5 11:51
 */
public class FieldInfo {

    /**
     * 字段的对象类
     */
    private Class<?> fieldType;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 默认值类型
     */
    private DefaultType defaultType;

    /**
     * 默认值，如果默认32位位置值，则为：[sys_uuid]；如果默认是当前系统时间，则为：[sys_date]
     */
    private String defaultValue;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否为主键
     */
    private boolean id;

    /**
     * 是否自增长
     */
    private boolean autoincrement;

    /**
     * 是否能够为空
     */
    private boolean canBeNull;

    /**
     * 字段长度（默认不限制长度）
     */
    private long length;

    /**
     * 数据存储类型（时间和数字 以定义的存储格式输出）
     */
    private DataType dataType;

    /**
     * 储存格式
     */
    private String form;

    public FieldInfo(String fieldName, DefaultType defaultType, String defaultValue, String description, boolean id, boolean autoincrement, boolean canBeNull, long length, DataType dataType, String form, Class<?> fieldType) {
        this.fieldName = fieldName;
        this.defaultType = defaultType;
        this.defaultValue = defaultValue;
        this.description = description;
        this.id = id;
        this.autoincrement = autoincrement;
        this.canBeNull = canBeNull;
        this.length = length;
        this.dataType = dataType;
        this.form = form;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public DefaultType getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(DefaultType defaultType) {
        this.defaultType = defaultType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isAutoincrement() {
        return autoincrement;
    }

    public void setAutoincrement(boolean autoincrement) {
        this.autoincrement = autoincrement;
    }

    public boolean isCanBeNull() {
        return canBeNull;
    }

    public void setCanBeNull(boolean canBeNull) {
        this.canBeNull = canBeNull;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }
}
