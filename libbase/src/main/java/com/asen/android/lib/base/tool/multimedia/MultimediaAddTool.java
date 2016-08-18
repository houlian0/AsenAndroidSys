package com.asen.android.lib.base.tool.multimedia;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.io.File;
import java.util.List;

/**
 * ��ý����ӹ�����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public abstract class MultimediaAddTool {

    /**
     * MultimediaAddTool
     *
     * @param parent   ִ�еĸ��ࣨActivity or Fragment��
     * @param listener �������Ӽ���
     * @param fileList ��ʼ���ļ�����
     * @param <T>      Activity or Fragment
     * @return ����MultimediaAddTool��������Activity or Fragmentʱ������null
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
     * @param parent   ִ�еĸ��ࣨActivity or Fragment��
     * @param listener �������Ӽ���
     * @param <T>      Activity or Fragment
     * @return ����MultimediaAddTool��������Activity or Fragmentʱ������null
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
     * onActivityResult ʱ����
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * ������󱣴��File
     *
     * @param lastFile lastFile
     */
    public abstract void setLastFile(File lastFile);

    /**
     * ����File����
     *
     * @param fileList fileList
     */
    public abstract void setFileList(List<File> fileList);

    /**
     * ������һ�������ļ�
     *
     * @return ���һ�������ļ�
     */
    public abstract File getLastFile();


    /**
     * ������иù������е��ļ�
     *
     * @return ���иù������е��ļ�
     */
    public abstract List<File> getFileList();

    /**
     * ��ʼ����
     *
     * @return �ɹ����
     */
    public abstract boolean startPicture();

    /**
     * ��ʼ����
     *
     * @param name ��Ƭ���ƣ�������׺��
     * @return �ɹ����
     */
    public abstract boolean startPicture(String name);

    /**
     * ��ʼ¼��Ƶ
     *
     * @return �ɹ����
     */
    public abstract boolean startVideo();

    /**
     * ��ʼ¼��Ƶ
     *
     * @param name ��Ƶ���ƣ�������׺��
     * @return �ɹ����
     */
    public abstract boolean startVideo(String name);

    /**
     * ��ʼ¼��Ƶ
     *
     * @param time     ʱ����  С��0 ʱ��������
     * @param fileSize ��λ�ֽ� С��0 ʱ��������
     * @return �ɹ����
     */
    public abstract boolean startVideo(int time, int fileSize);

    /**
     * ��ʼ¼��Ƶ
     *
     * @param name     �ļ�����������׺��
     * @param time     ʱ���� С��0 ʱ��������
     * @param fileSize ��λ�ֽ� С��0 ʱ��������
     * @return �ɹ����
     */
    public abstract boolean startVideo(String name, int time, int fileSize);

    /**
     * ��ʼ¼��
     *
     * @return �ɹ����
     */
    public abstract boolean startAudio();

    /**
     * ��ʼ¼��
     *
     * @param name �������ƣ�������׺��
     * @return �ɹ����
     */
    public abstract boolean startAudio(String name);

}