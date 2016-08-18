package com.asen.android.lib.base.tool.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Assets文件夹下的相关处理操作类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class AssetsUtil {

    /**
     * 拷贝assets中的单个文件到sd卡的指定路径下
     *
     * @param context    Android上下文
     * @param assetPath  assets中的文件路径
     * @param sdcardPath 要保存到的sd卡中的文件位置（包含文件名称）
     */
    public static boolean copyFile(Context context, String assetPath, String sdcardPath) {
        InputStream is = null;
        FileOutputStream fs = null;

        boolean result = false;

        try {
            int length = 0;
            is = context.getAssets().open(assetPath); // 读入原文件
            fs = new FileOutputStream(sdcardPath);
            byte[] buffer = new byte[1024];
            while ((length = is.read(buffer)) != -1) {
                fs.write(buffer, 0, length);
            }

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (fs != null) fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 获取所传入的assets路径下的子文件路径信息
     *
     * @param context   Android上下文
     * @param assetPath assets中的文件路径
     * @return 如果传入的assets是个文件夹，则返回其子文件的路径信息集；否则返回null
     */
    public static String[] getListFile(Context context, String assetPath) {
        try {
            return context.getAssets().list(assetPath);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 复制assets路径下的所有文件到sd卡的指定路径下
     *
     * @param context    Android上下文
     * @param assetPath  assets中的文件路径
     * @param sdcardPath 要保存到的sd卡中的文件夹位置（注：此处为文件夹）
     */
    public static void copyFolder(Context context, String assetPath, String sdcardPath) {
        FileUtil.createFolder(new File(sdcardPath));
        String[] listFile = getListFile(context, assetPath);
        if (listFile == null) {
            copyFile(context, assetPath, sdcardPath);
        } else {
            for (String s : listFile) {
                copyFolder(context, assetPath + "/" + s, sdcardPath + "/" + s);
            }
        }
    }
    
}
