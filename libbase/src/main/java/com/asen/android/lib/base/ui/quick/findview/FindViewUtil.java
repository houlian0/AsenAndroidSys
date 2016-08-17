package com.asen.android.lib.base.ui.quick.findview;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.asen.android.lib.base.tool.util.AppUtil;
import com.asen.android.lib.base.ui.quick.adapter.HolderHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ����FindView�Ĺ�����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:08
 */
public class FindViewUtil {

    private Context mContext;

    private static volatile FindViewUtil f;

    private FindViewUtil(Context context) {
        mContext = context;
    }

    /**
     * ��ȡ����FindView�������ʵ��
     *
     * @param context Android��������
     * @return FindView�������ʵ��
     */
    public static FindViewUtil getInstance(Context context) {
        if (null == f) {
            synchronized (FindViewUtil.class) {
                if (null == f) {
                    f = new FindViewUtil(context);
                }
            }
        }
        return f;
    }

    /**
     * ��View�п���FindView
     *
     * @param view �ⲿ����
     * @param obj  ��Ҫfind��ʵ��
     */
    public void findViews(View view, final Object obj) {
        Class<?> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(AFindView.class)) {
                    field.setAccessible(true);
                    AFindView fruitName = field.getAnnotation(AFindView.class);
                    int id = fruitName.id();
                    String onClick = fruitName.onClick();

                    String name = field.getName();

                    if (id == -1) {
                        id = AppUtil.getResourcesIdByName(mContext, name, "id");
                    }

                    View v = view.findViewById(id);
                    setClickListener(v, clazz, onClick, obj);

                    try {
                        field.set(obj, v);
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * ��Activity�п���FindView
     *
     * @param activity ��ǰ��Activityҳ��
     * @param obj      ��Ҫfind��ʵ��
     */
    public void findViews(Activity activity, Object obj) {
        Class<?> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(AFindView.class)) {
                    field.setAccessible(true);
                    AFindView fruitName = field.getAnnotation(AFindView.class);

                    int id = fruitName.id();
                    String name = field.getName();
                    if (id == -1) {
                        id = AppUtil.getResourcesIdByName(mContext, name, "id");
                    }

                    String onClick = fruitName.onClick();
                    View v = activity.findViewById(id);
                    setClickListener(v, clazz, onClick, obj);

                    try {
                        field.set(obj, v);
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * ��HolderHelper{@link HolderHelper}�п���FindView
     *
     * @param helper Holder�İ�����
     * @param obj    ��Ҫfind��ʵ��
     */
    public void findViews(HolderHelper helper, final Object obj) {
        Class<?> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(AFindView.class)) {
                    field.setAccessible(true);
                    AFindView fruitName = field.getAnnotation(AFindView.class);
                    int id = fruitName.id();
                    String onClick = fruitName.onClick();

                    String name = field.getName();

                    if (id == -1) {
                        id = AppUtil.getResourcesIdByName(mContext, name, "id");
                    }

                    View v = helper.findViewById(id);
                    setClickListener(v, clazz, onClick, obj);

                    try {
                        field.set(obj, v);
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private static void setClickListener(View v, final Class<?> clazz, final String onClick, final Object obj) {
        if (v != null && !TextUtils.isEmpty(onClick))
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Method m = clazz.getDeclaredMethod(onClick, View.class);
                        m.setAccessible(true);
                        m.invoke(obj, v);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
    }


}
