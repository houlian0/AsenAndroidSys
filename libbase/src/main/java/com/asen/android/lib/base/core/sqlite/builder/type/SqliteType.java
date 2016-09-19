package com.asen.android.lib.base.core.sqlite.builder.type;

/**
 * SQL传递的类型，是insert还是update还是....
 *
 * @author Asen
 * @version v1.0
 * @date 2016/9/7 17:11
 */
public enum SqliteType {

    /**
     * 删除操作
     */
    DELETE("delete"),

    /**
     * 插入操作
     */
    INSERT("insert"),

    /**
     * 更新操作
     */
    UPDATE("update"),

    /**
     * 替换操作
     */
    REPLACE("replace"),

    /**
     * 插入或者替换操作，注意，必须指明主键
     */
    CREATE_OR_UPDATE("create_or_update"),

    /**
     * 查询操作
     */
    QUERY("query"),

    /**
     * 暂不支持的形式
     */
    UNKNOWN("unknown"),;

    public static final String KEY_NAME = "sqliteType"; // JSON串的键值

    private String name; // sqlite的类型

    /**
     * 构造函数
     *
     * @param name sqlite类型的名称
     */
    SqliteType(String name) {
        this.name = name;
    }

    /**
     * 获取sqlite类型的名称
     *
     * @return sqlite类型的名称
     */
    public String getName() {
        return name;
    }

    /**
     * 根据sqlite类型的名称获取对应的SqliteType
     *
     * @param name sqlite类型的名称
     * @return SqliteType对象
     */
    public static SqliteType getSqliteTypeByName(String name) {
        SqliteType[] types = SqliteType.values();
        for (SqliteType type : types) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return UNKNOWN;
    }

}
