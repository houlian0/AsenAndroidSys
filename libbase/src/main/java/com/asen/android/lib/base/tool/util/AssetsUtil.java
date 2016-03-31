package com.asen.android.lib.base.tool.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by HL_SEN on 2015/9/22.
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class AssetsUtil {

    /**
     * @param context
     * @param assetPath
     * @param sdcardPath
     * @Title: copyFile
     * @Description: 复制单个文件
     */
    public static boolean copyFile(Context context, String assetPath, String sdcardPath) {
        InputStream is = null;
        FileOutputStream fs = null;

        boolean result = false;

        try {
            int byteread = 0;
            is = context.getAssets().open(assetPath); // 读入原文件
            fs = new FileOutputStream(sdcardPath);
            byte[] buffer = new byte[1024];
            while ((byteread = is.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
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
     * @param context
     * @param assetPath
     * @return
     * @Title: getListFile
     * @Description:
     */
    public static String[] getListFile(Context context, String assetPath) {
        try {
            String[] list = context.getAssets().list(assetPath);
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param context
     * @param assetPath
     * @param sdcardPath
     * @Title: copyFolder
     * @Description: 复制文件夹下的所有文件
     */
    public static void copyFolder(Context context, String assetPath, String sdcardPath) {
        try {
            String[] list = context.getAssets().list(assetPath);

            File f = new File(sdcardPath);
            if (!f.exists()) f.mkdirs();

            for (String s : list) {
                copyFile(context, assetPath + "/" + s, sdcardPath + "/" + s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
