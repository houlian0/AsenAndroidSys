package com.asen.android.lib.base.tool.manage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

/**
 * ����Fragment�ĳ�����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public abstract class FragmentManager {

    /**
     * ����FragmentManager
     *
     * @param parent ִ�еĸ��ࣨFragmentActivity or Fragment��
     * @param <T>    FragmentActivity or Fragment
     * @return ����FragmentManager��������FragmentActivity or Fragmentʱ������null
     */
    public static <T> FragmentManager createFragmentManager(T parent) {
        if (parent instanceof FragmentActivity) {
            return new FragmentManager4Activity((FragmentActivity) parent);
        } else if (parent instanceof Fragment) {
            return new FragmentManager4Fragment((Fragment) parent);
        } else {
            return null;
        }
    }

    /**
     * �������Fragment�ļ���
     *
     * @return ����Fragment�ļ���
     */
    public abstract List<Fragment> getList();

    /**
     * ���Fragment
     *
     * @param fragment      Ҫ��ӵ�Fragment
     * @param frameLayoutId frameLayoutId
     */
    public abstract void add(Fragment fragment, int frameLayoutId);

    /**
     * ���Fragment
     *
     * @param fragment      Ҫ��ӵ�Fragment
     * @param frameLayoutId frameLayoutId
     * @param visible       �Ƿ�ɼ�
     */
    public abstract void add(Fragment fragment, int frameLayoutId, boolean visible);

    /**
     * ���Fragment
     *
     * @param fragment      Ҫ��ӵ�Fragment
     * @param frameLayoutId frameLayoutId
     * @param visible       �Ƿ�ɼ�
     * @param data          ˢ������
     */
    public abstract void add(Fragment fragment, int frameLayoutId, boolean visible, Bundle data);

    /**
     * �滻Fragment
     *
     * @param fragment      Ҫ�滻��Fragment
     * @param frameLayoutId frameLayoutId
     * @param data          ˢ������
     */
    public abstract void replace(Fragment fragment, int frameLayoutId, Bundle data);

    /**
     * ���Fragment����
     *
     * @param list          Ҫ��ӵ�Fragment���� ��ֻ����ʾ�����е�һ��Fragment��
     * @param frameLayoutId frameLayoutId
     */
    public abstract void addList(List<Fragment> list, int frameLayoutId);

    /**
     * ���Fragment����
     *
     * @param list          Ҫ��ӵ�Fragment���ϣ�ֻ����ʾ�����е�һ��Fragment��
     * @param frameLayoutId frameLayoutId
     * @param data          ˢ������
     */
    public abstract void addList(List<Fragment> list, int frameLayoutId, Bundle data);

    /**
     * ����Fragment
     *
     * @param fragment ���ص�Fragment
     */
    public abstract void hide(Fragment fragment);

    /**
     * ����Fragment
     *
     * @param cla ���ص�Fragment��
     */
    public abstract void hide(Class<?> cla);

    /**
     * ��ʾFragment
     *
     * @param fragment ��ʾ��Fragment
     */
    public abstract void show(Fragment fragment);

    /**
     * ��ʾFragment
     *
     * @param fragment ��ʾ��Fragment
     * @param data     ˢ������
     */
    public abstract void show(Fragment fragment, Bundle data);

    /**
     * �Ƴ�Fragment
     *
     * @param fragment �Ƴ���Fragment
     */
    public abstract void remove(Fragment fragment);

    /**
     * �Ƴ�����Fragment
     */
    public abstract void removeAll();

    /**
     * ��ʾFragment
     *
     * @param cla ��ʾ��Fragment��
     */
    public abstract void showFragment(Class<?> cla);

    /**
     * ��ʾFragment
     *
     * @param cla  ��ʾ��Fragment��
     * @param data ˢ������
     */
    public abstract void showFragment(Class<?> cla, Bundle data);

    /**
     * ����ʾFragment��������
     *
     * @param cla ��ʾ��Fragment��
     */
    public abstract void showFragmentOnly(Class<?> cla);

    /**
     * ����ʾFragment��������
     *
     * @param cla  ��ʾ��Fragment��
     * @param data ˢ������
     */
    public abstract void showFragmentOnly(Class<?> cla, Bundle data);

    /**
     * ���Fragment
     *
     * @param cla Ҫ��õ�Fragment�࣬�����ڷ���null
     */
    public abstract <T extends Fragment> T getFragment(Class<T> cla);

}
