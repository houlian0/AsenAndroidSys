package com.asen.android.lib.base.global;

import android.content.Context;

import com.asen.android.lib.base.tool.util.SdCardUtil;

import java.io.File;

/**
 * �����ļ�·���ṹ��ȡ��
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class AppPath {

    /**
     * Ĭ��״̬�������ö����ã�û���ö�����
     */
    public static final byte TYPE_DEFAULT = 0x01;

    /**
     * ��ת״̬�������ÿ������ã������ÿ������ã������豸��ȡ���ÿ�ʱ�����෴��
     */
    public static final byte TYPE_REVERSAL = 0x02;

    /**
     * internal SD Card ��ȡ���ÿ�
     */
    public static final byte TYPE_INTERNAL_SD_CARD = 0x03;

    /**
     * External SD Card ��ȡ���ÿ�
     */
    public static final byte TYPE_EXTERNAL_SD_CARD = 0x04;

    private static String path = null;

    private static int mType = TYPE_DEFAULT;

    public static void setType(int type) {
        mType = type;
        path = null;
    }

    /**
     * ��ʼ��SD��·����ÿ��ִ�ж���ȡһ�Σ������������ÿ���û���������á�4.4�����ϵİ汾������SD��·��ΪAndroidָ��·��
     *
     * @param context Android������
     * @return
     */
    private static String initSdCard(Context context) {
        if (mType == TYPE_INTERNAL_SD_CARD) {
            // ���ÿ�
            path = SdCardUtil.getFirstExterPath();
        } else if (mType == TYPE_EXTERNAL_SD_CARD) {
            // ���ÿ�
            path = SdCardUtil.getSecondExterPath(context);
        } else if (mType == TYPE_REVERSAL) {
            // �����ÿ������ã������ÿ�������
            path = SdCardUtil.getFirstExterPath();
            if (path == null)
                path = SdCardUtil.getSecondExterPath(context);
        } else {
            // �����ö����ã�û���ö�����
            path = SdCardUtil.getSecondExterPath(context);
            if (path == null)
                path = SdCardUtil.getFirstExterPath();
        }
        return path;
    }

    /**
     * ���SD��·��
     *
     * @param context Android������
     * @return
     */
    private static String getSdCard(Context context) {
        if (path == null) {
            initSdCard(context);
        }
        return path;
    }

    /**
     * ��ȡ��Ŀ�ļ���·��
     *
     * @param context Android������
     * @return
     */
    public static File getAppProjectFile(Context context) {
        String path = getSdCard(context);
        return path == null ? null : new File(path, AppData.APP_PROJECT);
    }

    /**
     * ��ô�����Ϣ������ļ���·��(error)
     *
     * @param context Android������
     * @return
     */
    public static File getAppErrorFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_ERROR);
    }

    /**
     * ���Sqlite���ݿ��ļ���·��(db)
     *
     * @param context Android������
     * @return
     */
    public static File getAppSqliteFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_SQLITE);
    }

    /**
     * ���Perst���ݿ��ļ���·��(dbs)
     *
     * @param context Android������
     * @return
     */
    public static File getAppPerstFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_PERST);
    }

    /**
     * ��õ�ͼ�ļ���·��(map)
     *
     * @param context Android������
     * @return
     */
    public static File getAppMapFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_MAP);
    }

    /**
     * ��õ�ͼ�����ļ���·��(map)
     *
     * @param context Android������
     * @return
     */
    public static File getAppMapCacheFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_MAP_CACHE);
    }

    /**
     * ����ר���ļ���(download)
     *
     * @param context Android������
     * @return
     */
    public static File getAppDownlaodFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_DOWNLOAD);
    }

    /**
     * ���������Ƭ���ļ���·��
     *
     * @param context Android������
     * @return
     */
    public static File getAppPictureFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_PICTURE);
    }

    /**
     * ���������Ƶ���ļ���·��
     *
     * @param context Android������
     * @return
     */
    public static File getAppVideoFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_VIDEO);
    }

    /**
     * ������ֵ��ļ���·��
     *
     * @param context Android������
     * @return
     */
    public static File getAppMusicFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_MUSIC);
    }


}
