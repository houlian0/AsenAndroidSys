package com.asen.android.lib.base.core.util;

import java.util.List;

/**
 * �������ջ ���� �ӿ�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/7/12 16:57
 */
public interface IMaxStack<E> {

    /**
     * ���ջ����
     */
    public void clear();

    /**
     * ջ�е�ǰ���ݵ�����
     *
     * @return size
     */
    public int size();

    /**
     * �жϵ�ǰջ�Ƿ�Ϊ��
     *
     * @return trueΪ��
     */
    public boolean isEmpty();

    /**
     * ѹջ
     *
     * @param object ����
     */
    public void push(E object);

    /**
     * ��ȡ���һ����¼
     *
     * @return object ����
     */
    public E getLastData();

    /**
     * ��ȡ��������
     *
     * @param compareListener ���������жϽӿ�
     * @return ���Ŷ���
     */
    public E getGood(IGoodCompareListener<E> compareListener);

    /**
     * ���������жϽӿ�
     *
     * @param <E>
     */
    public interface IGoodCompareListener<E> {

        /**
         * �Ӽ�����ȡ����������
         *
         * @param dataList ���ݼ���
         * @return ��������
         */
        public E getGood(List<E> dataList);
    }

}
