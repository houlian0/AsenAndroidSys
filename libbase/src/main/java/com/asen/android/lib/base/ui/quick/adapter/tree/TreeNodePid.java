package com.asen.android.lib.base.ui.quick.adapter.tree;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Simple to Introduction
 * 树节点父编号注解
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:04
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeNodePid {

}
