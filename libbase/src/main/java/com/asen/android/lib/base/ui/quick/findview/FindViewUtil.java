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
 * 快速FindView的工具类
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
     * 获取快速FindView工具类的实例
     *
     * @param context Android的上下文
     * @return FindView工具类的实例
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
     * 从View中快速FindView
     *
     * @param view 外部布局
     * @param obj  需要find的实例
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
     * 从Activity中快速FindView
     *
     * @param activity 当前的Activity页面
     * @param obj      需要find的实例
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
     * 从HolderHelper{@link HolderHelper}中快速FindView
     *
     * @param helper Holder的帮助类
     * @param obj    需要find的实例
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
