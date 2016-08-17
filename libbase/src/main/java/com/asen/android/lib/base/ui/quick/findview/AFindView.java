package com.asen.android.lib.base.ui.quick.findview;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 快速FindView时需要的注解
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:08
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AFindView {

    /**
     * 控件的资源id
     */
    int id() default -1;

    /**
     * 控件点击事件触发的方法名，方法带一个View参数
     */
    String onClick() default "";

}
