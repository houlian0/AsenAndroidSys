package com.asen.android.lib.base.ui.quick.findview;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ����FindViewʱ��Ҫ��ע��
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:08
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AFindView {

    /**
     * �ؼ�����Դid
     */
    int id() default -1;

    /**
     * �ؼ�����¼������ķ�������������һ��View����
     */
    String onClick() default "";

}
