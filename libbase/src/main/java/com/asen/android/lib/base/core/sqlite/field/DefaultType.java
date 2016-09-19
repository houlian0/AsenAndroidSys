package com.asen.android.lib.base.core.sqlite.field;

/**
 * 数据默认值类型
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/19 9:54
 */
public enum DefaultType {
    /**
     * 默认为null
     */
    NULL,
    /**
     * 采用string类型的默认值，从defaultValue上获取值
     */
    STRING,
    /**
     * 采用number类型的默认值，从defaultValue上获取值，并将其智能转换后保存
     */
    NUMBER,
    /**
     * 默认采用32位唯一值
     */
    SYS_UUID,
    /**
     * 默认采用当前的系统时间
     */
    SYS_DATE
}
