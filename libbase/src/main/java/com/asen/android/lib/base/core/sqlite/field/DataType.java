package com.asen.android.lib.base.core.sqlite.field;

/**
 * 数据格式转换类型
 *
 * @author Asen
 * @version v1.0
 * @date 2016/4/1 9:33
 */
public enum DataType {

    /**
     * 时间类型 用字符串形式输出，默认 yyyy-MM-dd HH:mm:ss 格式 或 采用指定格式输出
     */
    DATE_STRING("date_string"),
    /**
     * 时间类型 用长整形保存
     */
    DATE_LONG("date_long"),
    /**
     * 数字类型 采用指定格式输出
     */
    NUMBER_FORM("number_form"),
    /**
     * 默认数据类型
     */
    DEFAULT("default"),;

    private String name; // 数据格式转换类型名称

    /**
     * 构造函数
     *
     * @param name 数据格式转换类型名称
     */
    DataType(String name) {
        this.name = name;
    }

    /**
     * 获取数据格式的转换类型名称
     *
     * @return 数据格式转换类型名称
     */
    public String getName() {
        return name;
    }

    /**
     * 根据 数据格式转换类型名称 获取对应的 DataType对象
     *
     * @param name 数据格式转换类型名称
     * @return DataType对象
     */
    public static DataType getDateTypeByName(String name) {
        DataType[] types = values();
        for (DataType type : types) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return DataType.DEFAULT;
    }

}