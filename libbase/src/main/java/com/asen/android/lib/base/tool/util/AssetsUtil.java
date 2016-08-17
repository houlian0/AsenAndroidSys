package com.asen.android.lib.base.tool.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Assets�ļ����µ���ش��������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class AssetsUtil {

    /**
     * ����assets�еĵ����ļ���sd����ָ��·����
     *
     * @param context    Android������
     * @param assetPath  assets�е��ļ�·��
     * @param sdcardPath Ҫ���浽��sd���е��ļ�λ�ã������ļ����ƣ�
     */
    public static boolean copyFile(Context context, String assetPath, String sdcardPath) {
        InputStream is = null;
        FileOutputStream fs = null;

        boolean result = false;

        try {
            int length = 0;
            is = context.getAssets().open(assetPath); // ����ԭ�ļ�
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
     * ��ȡ�������assets·���µ����ļ�·����Ϣ
     *
     * @param context   Android������
     * @param assetPath assets�е��ļ�·��
     * @return ��������assets�Ǹ��ļ��У��򷵻������ļ���·����Ϣ�������򷵻�null
     */
    public static String[] getListFile(Context context, String assetPath) {
        try {
            return context.getAssets().list(assetPath);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * ����assets·���µ������ļ���sd����ָ��·����
     *
     * @param context    Android������
     * @param assetPath  assets�е��ļ�·��
     * @param sdcardPath Ҫ���浽��sd���е��ļ���λ�ã�ע���˴�Ϊ�ļ��У�
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
