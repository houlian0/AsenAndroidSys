package com.asen.android.lib.base.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * ��List��������󼯺ϣ����˼����У�ֻ���ָ�����޵����ݣ������ݳ���ָ������ʱ���Ƴ����������������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/7/12 15:12
 */
public class SenMaxListStack<T> implements IMaxStack<T> {

    private static final int MIN_MAX_SIZE = 1; // ���ٵ���󼯺���

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
        // �����ﵽ����ʱ���Ƴ����׸�����
        if (dataList.size() == maxSize) {
            dataList.remove(0);
        }
        // ����������
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
