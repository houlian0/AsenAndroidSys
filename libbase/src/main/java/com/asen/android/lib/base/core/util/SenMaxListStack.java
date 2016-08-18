package com.asen.android.lib.base.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 用List来定义最大集合，即此集合中，只存放指定上限的数据，当数据超过指定数量时，移除出最早进来的数据
 *
 * @author Asen
 * @version v1.0
 * @date 2016/7/12 15:12
 */
public class SenMaxListStack<T> implements IMaxStack<T> {

    private static final int MIN_MAX_SIZE = 1; // 最少的最大集合数

    private List<T> dataList;

    private int maxSize = MIN_MAX_SIZE;

    public SenMaxListStack(int maxSize) {
        dataList = new ArrayList<>();
        this.maxSize = maxSize < MIN_MAX_SIZE ? MIN_MAX_SIZE : maxSize;
    }

    @Override
    public void clear() {
        dataList.clear();
    }

    @Override
    public int size() {
        return dataList.size();
    }

    @Override
    public boolean isEmpty() {
        return dataList.isEmpty();
    }

    @Override
    public void push(T object) {
        // 数量达到上限时，移除出首个数据
        if (dataList.size() == maxSize) {
            dataList.remove(0);
        }
        // 增加新数据
        dataList.add(object);
    }

    @Override
    public T getLastData() {
        return size() == 0 ? null : dataList.get(size() - 1);
    }

    @Override
    public T getGood(IGoodCompareListener<T> compareListener) {
        return compareListener != null ? compareListener.getGood(dataList) : null;
    }

}
