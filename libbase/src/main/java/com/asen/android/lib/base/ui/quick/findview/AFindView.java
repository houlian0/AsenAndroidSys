package com.asen.android.lib.base.ui.quick.findview;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 快速FindView时需要的注解
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:08
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AFindView {

    /**
     * @return 资源ID
     */
    int id() default -1;

    /**
     * onClick(View v)
     *
     * @return 点击事件触发的方法名，方法带View参数
     */
    String onClick() default "";

}
