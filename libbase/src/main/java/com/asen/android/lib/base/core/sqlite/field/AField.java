package com.asen.android.lib.base.core.sqlite.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段注解
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 14:39
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AField {

    /**
     * 字段名
     */
    String fieldName();

    /**
     * 默认值的类型
     */
    DefaultType defaultType() default DefaultType.NULL;

    /**
     * 默认值：当默认值类型为STRING时，从此处获取；
     */
    String defaultValue() default "";

    /**
     * 描述
     */
    String description() default "";

    /**
     * 是否为主键
     */
    boolean id() default false;

    /**
     * 是否自增长
     */
    boolean autoincrement() default false;

    /**
     * 是否能够为空
     */
    boolean canBeNull() default true;

    /**
     * 字段长度（默认不限制长度）
     */
    long length() default -1;

    /**
     * 数据存储类型（时间和数字 以定义的存储格式输出）
     */
    DataType dataType() default DataType.DEFAULT;

    /**
     * 储存格式
     */
    String form() default "";


}
