package com.asen.android.lib.base.global;

/**
 * 程序全局变量存放处
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class AppData {

    /**
     * APP应用是否处于DEBUG状态，DEBUG状态下，程序打印的LOG更全面
     */
    public static boolean DEBUG = false;

    /**
     * 项目数据文件夹名称
     */
    public static String APP_PROJECT = "asen";

    /**
     * 项目数据文件夹名称--错误信息
     */
    static String APP_ERROR = "error";

    /**
     * 项目数据文件夹名称--SQLITE数据文件夹
     */
    static String APP_SQLITE = "db";

    /**
     * 项目数据文件夹名称--PERST数据文件夹
     */
    static String APP_PERST = "dbs";

    /**
     * 项目数据文件夹名称--地图文件夹
     */
    static String APP_MAP = "map";

    /**
     * 项目数据文件夹名称--地图缓存文件夹
     */
    static String APP_MAP_CACHE = "map/cache";

    /**
     * 项目数据文件夹名称--下载专用文件夹
     */
    static String APP_DOWNLOAD = "download";

    /**
     * 项目数据文件夹名称--照片存放文件夹
     */
    static String APP_PICTURE = "multi/picture";

    /**
     * 项目数据文件夹名称--视频存放文件夹
     */
    static String APP_VIDEO = "multi/video";

    /**
     * 项目数据文件夹名称--音乐存放文件夹
     */
    static String APP_MUSIC = "multi/music";

}
