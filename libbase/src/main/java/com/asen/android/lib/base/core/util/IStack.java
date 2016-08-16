package com.asen.android.lib.base.core.util;

/**
 * ջ ���� �ӿ�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 15:12
 */
public interface IStack<E> {

    /**
     * ���ջ����
     */
    void clear();

    /**
     * ջ����������
     *
     * @return size
     */
    int size();

    /**
     * �жϵ�ǰջ�Ƿ�Ϊ��
     *
     * @return trueΪ��
     */
    boolean isEmpty();

    /**
     * ����ջ���Ķ��󣬵��������Ƴ���ջ
     *
     * @return ջ���Ķ���
     */
    E peek();

    /**
     * ����ջ���Ķ��󣬲������Ƴ���ջ
     *
     * @return ջ���Ķ���
     */
    E pop();

    /**
     * ѹջ
     *
     * @param object ����
     */
    void push(E object);

    /**
     * ��ѯ������ջ�еĵڼ��㣬��ջ����
     *
     * @param o ��Ҫ��ѯ�Ķ���
     * @return ����
     */
    int search(Object o);

}
