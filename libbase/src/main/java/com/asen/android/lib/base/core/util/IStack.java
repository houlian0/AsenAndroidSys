package com.asen.android.lib.base.core.util;

/**
 * 栈 定义 接口
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 15:12
 */
public interface IStack<E> {

    /**
     * 清空栈数据
     */
    void clear();

    /**
     * 栈中数据数量
     *
     * @return size
     */
    int size();

    /**
     * 判断当前栈是否为空
     *
     * @return true为空
     */
    boolean isEmpty();

    /**
     * 返回栈顶的对象，但不将其移除出栈
     *
     * @return 栈顶的对象
     */
    E peek();

    /**
     * 返回栈顶的对象，并将其移除出栈
     *
     * @return 栈顶的对象
     */
    E pop();

    /**
     * 压栈
     *
     * @param object 对象
     */
    void push(E object);

    /**
     * 查询对象在栈中的第几层，从栈顶数
     *
     * @param o 需要查询的对象
     * @return 层数
     */
    int search(Object o);

}
