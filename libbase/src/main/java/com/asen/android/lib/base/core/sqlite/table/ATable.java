package com.asen.android.lib.base.core.sqlite.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表名注解
 *
 * @author Asen
 * @version v1.0
 * @date 2016/4/1 9:40
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ATable {

    /**
     * 表名
     */
    String tableName();

    /**
     * 描述
     */
    String description() default "";

}
