package com.asen.android.lib.base.core.sqlite.builder.type;


import com.asen.android.lib.base.core.sqlite.field.AField;
import com.asen.android.lib.base.core.sqlite.table.ATable;

/**
 * SQL执行的Builder类型
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/20 11:44
 */
public enum BuilderType {

    /**
     * 纯SQL语句加参数的形式，参数可以为null
     * 注：用此方式进行数据的增删改，是无法返回改变的记录数的
     */
    SQL("sql"),
    /**
     * Android建议的形式
     */
    ANDROID_SQL("android_sql"),
    /**
     * 通过传入包含有注解 {@link AField} 和 {@link ATable}的Java对象的方式
     */
    OBJECT("object"),
    /**
     * 暂不支持的形式
     */
    UNKNOWN("unknown"),;

    public static final String KEY_NAME = "builderType"; // JSON串的键值

    private String name; // builder的类型

    /**
     * 构造函数
     *
     * @param name builder类型的名称
     */
    BuilderType(String name) {
        this.name = name;
    }

    /**
     * 获取builder类型的名称
     *
     * @return builder类型的名称
     */
    public String getName() {
        return name;
    }

    /**
     * 根据builder类型的名称获取对应的BuilderType
     *
     * @param name builder类型的名称
     * @return BuilderType对象
     */
    public static BuilderType getBuilderTypeByName(String name) {
        BuilderType[] types = BuilderType.values();
        for (BuilderType type : types) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return BuilderType.UNKNOWN;
    }

}
