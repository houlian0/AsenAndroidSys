package com.asen.android.lib.base.core.util;

import java.util.List;

/**
 * 最大数量栈 定义 接口
 *
 * @author Asen
 * @version v1.0
 * @date 2016/7/12 16:57
 */
public interface IMaxStack<E> {

    /**
     * 清空栈数据
     */
    public void clear();

    /**
     * 栈中当前数据的数量
     *
     * @return size
     */
    public int size();

    /**
     * 判断当前栈是否为空
     *
     * @return true为空
     */
    public boolean isEmpty();

    /**
     * 压栈
     *
     * @param object 对象
     */
    public void push(E object);

    /**
     * 获取最后一条记录
     *
     * @return object 对象
     */
    public E getLastData();

    /**
     * 获取最优数据
     *
     * @param compareListener 最优数据判断接口
     * @return 最优对象
     */
    public E getGood(IGoodCompareListener<E> compareListener);

    /**
     * 最优数据判断接口
     *
     * @param <E>
     */
    public interface IGoodCompareListener<E> {

        /**
         * 从集合中取出最优数据
         *
         * @param dataList 数据集合
         * @return 最优数据
         */
        public E getGood(List<E> dataList);
    }

}
