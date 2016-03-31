package com.asen.android.lib.base;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * Simple to Introduction
 * 用ArrayList来定义栈
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 15:12
 */
public class SenListStack<E> implements IStack<E> {

    private List<E> mList;

    public SenListStack() {
        mList = new ArrayList<>();
    }

    @Override
    public int size() {
        return mList.size();
    }

    @Override
    public void clear() {
        mList.clear();
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @Override
    public E peek() {
        try {
            return mList.get(mList.size() - 1);
        } catch (Exception e) {
            throw new EmptyStackException();
        }
    }

    @Override
    public E pop() {
        try {
            E object = mList.get(mList.size() - 1);
            mList.remove(mList.size() - 1);
            return object;
        } catch (Exception e) {
            throw new EmptyStackException();
        }
    }

    @Override
    public void push(E object) {
        mList.add(object);
    }

    @Override
    public int search(Object o) {
        int size = mList.size();
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (mList.get(i) == null) {
                    return size - i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(mList.get(i))) {
                    return size - i;
                }
            }
        }
        return -1;
    }

}