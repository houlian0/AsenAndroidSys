package com.asen.android.lib.base.tool.multimedia;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.io.File;
import java.util.List;

/**
 * 多媒体添加工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public abstract class MultimediaAddTool {

    /**
     * MultimediaAddTool
     *
     * @param parent   执行的父类（Activity or Fragment）
     * @param listener 附件增加监听
     * @param fileList 初始化文件集合
     * @param <T>      Activity or Fragment
     * @return 创建MultimediaAddTool，不属于Activity or Fragment时，返回null
     */
    public static <T> MultimediaAddTool createMultimediaAddTool(T parent, IMultimediaAddListener listener, List<File> fileList) {
        if (parent instanceof Activity) {
            return new MultimediaAddTool4Activity(((Activity) parent), listener, fileList);
        } else if (parent instanceof Fragment) {
            return new MultimediaAddTool4Fragment(((Fragment) parent), listener, fileList);
        } else {
            return null;
        }
    }

    /**
     * MultimediaAddTool
     *
     * @param parent   执行的父类（Activity or Fragment）
     * @param listener 附件增加监听
     * @param <T>      Activity or Fragment
     * @return 创建MultimediaAddTool，不属于Activity or Fragment时，返回null
     */
    public static <T> MultimediaAddTool createMultimediaAddTool(T parent, IMultimediaAddListener listener) {
        if (parent instanceof Activity) {
            return new MultimediaAddTool4Activity(((Activity) parent), listener);
        } else if (parent instanceof Fragment) {
            return new MultimediaAddTool4Fragment(((Fragment) parent), listener);
        } else {
            return null;
        }
    }

    /**
     * onActivityResult 时调用
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * 设置最后保存的File
     *
     * @param lastFile lastFile
     */
    public abstract void setLastFile(File lastFile);

    /**
     * 设置File集合
     *
     * @param fileList fileList
     */
    public abstract void setFileList(List<File> fileList);

    /**
     * 获得最后一个附件文件
     *
     * @return 最后一个附件文件
     */
    public abstract File getLastFile();


    /**
     * 获得所有该管理类中的文件
     *
     * @return 所有该管理类中的文件
     */
    public abstract List<File> getFileList();

    /**
     * 开始拍照
     *
     * @return 成功与否
     */
    public abstract boolean startPicture();

    /**
     * 开始拍照
     *
     * @param name 照片名称（不含后缀）
     * @return 成功与否
     */
    public abstract boolean startPicture(String name);

    /**
     * 开始录视频
     *
     * @return 成功与否
     */
    public abstract boolean startVideo();

    /**
     * 开始录视频
     *
     * @param name 视频名称（不含后缀）
     * @return 成功与否
     */
    public abstract boolean startVideo(String name);

    /**
     * 开始录视频
     *
     * @param time     时间秒  小于0 时不做限制
     * @param fileSize 单位字节 小于0 时不做限制
     * @return 成功与否
     */
    public abstract boolean startVideo(int time, int fileSize);

    /**
     * 开始录视频
     *
     * @param name     文件名（不含后缀）
     * @param time     时间秒 小于0 时不做限制
     * @param fileSize 单位字节 小于0 时不做限制
     * @return 成功与否
     */
    public abstract boolean startVideo(String name, int time, int fileSize);

    /**
     * 开始录音
     *
     * @return 成功与否
     */
    public abstract boolean startAudio();

    /**
     * 开始录音
     *
     * @param name 音乐名称（不含后缀）
     * @return 成功与否
     */
    public abstract boolean startAudio(String name);

}