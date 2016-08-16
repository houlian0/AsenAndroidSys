package com.asen.android.lib.base.global;

import android.content.Context;

import com.asen.android.lib.base.tool.util.SdCardUtil;

import java.io.File;

/**
 * 程序文件路径结构获取类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class AppPath {

    /**
     * 默认状态，有外置读外置，没外置读内置
     */
    public static final byte TYPE_DEFAULT = 0x01;

    /**
     * 反转状态，有内置卡读内置，无内置卡读外置（部分设备获取内置卡时，会相反）
     */
    public static final byte TYPE_REVERSAL = 0x02;

    /**
     * internal SD Card 仅取内置卡
     */
    public static final byte TYPE_INTERNAL_SD_CARD = 0x03;

    /**
     * External SD Card 仅取外置卡
     */
    public static final byte TYPE_EXTERNAL_SD_CARD = 0x04;

    private static String path = null;

    private static int mType = TYPE_DEFAULT;

    public static void setType(int type) {
        mType = type;
        path = null;
    }

    /**
     * 初始化SD卡路径，每次执行都获取一次，有外置拿外置卡、没外置拿内置。4.4及以上的版本，外置SD卡路径为Android指定路径
     *
     * @param context Android上下文
     * @return
     */
    private static String initSdCard(Context context) {
        if (mType == TYPE_INTERNAL_SD_CARD) {
            // 内置卡
            path = SdCardUtil.getFirstExterPath();
        } else if (mType == TYPE_EXTERNAL_SD_CARD) {
            // 外置卡
            path = SdCardUtil.getSecondExterPath(context);
        } else if (mType == TYPE_REVERSAL) {
            // 有内置卡读内置，无内置卡读外置
            path = SdCardUtil.getFirstExterPath();
            if (path == null)
                path = SdCardUtil.getSecondExterPath(context);
        } else {
            // 有外置读外置，没外置读内置
            path = SdCardUtil.getSecondExterPath(context);
            if (path == null)
                path = SdCardUtil.getFirstExterPath();
        }
        return path;
    }

    /**
     * 获得SD卡路径
     *
     * @param context Android上下文
     * @return
     */
    private static String getSdCard(Context context) {
        if (path == null) {
            initSdCard(context);
        }
        return path;
    }

    /**
     * 获取项目文件夹路径
     *
     * @param context Android上下文
     * @return
     */
    public static File getAppProjectFile(Context context) {
        String path = getSdCard(context);
        return path == null ? null : new File(path, AppData.APP_PROJECT);
    }

    /**
     * 获得错误信息保存的文件夹路径(error)
     *
     * @param context Android上下文
     * @return
     */
    public static File getAppErrorFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_ERROR);
    }

    /**
     * 获得Sqlite数据库文件夹路径(db)
     *
     * @param context Android上下文
     * @return
     */
    public static File getAppSqliteFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_SQLITE);
    }

    /**
     * 获得Perst数据库文件夹路径(dbs)
     *
     * @param context Android上下文
     * @return
     */
    public static File getAppPerstFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_PERST);
    }

    /**
     * 获得地图文件夹路径(map)
     *
     * @param context Android上下文
     * @return
     */
    public static File getAppMapFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_MAP);
    }

    /**
     * 获得地图缓存文件夹路径(map)
     *
     * @param context Android上下文
     * @return
     */
    public static File getAppMapCacheFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_MAP_CACHE);
    }

    /**
     * 下载专用文件夹(download)
     *
     * @param context Android上下文
     * @return
     */
    public static File getAppDownlaodFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_DOWNLOAD);
    }

    /**
     * 获得拍照照片的文件夹路径
     *
     * @param context Android上下文
     * @return
     */
    public static File getAppPictureFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_PICTURE);
    }

    /**
     * 获得拍摄视频的文件夹路径
     *
     * @param context Android上下文
     * @return
     */
    public static File getAppVideoFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_VIDEO);
    }

    /**
     * 获得音乐的文件夹路径
     *
     * @param context Android上下文
     * @return
     */
    public static File getAppMusicFile(Context context) {
        File parentFile = getAppProjectFile(context);
        return new File(parentFile, AppData.APP_MUSIC);
    }


}
