package com.asen.android.lib.base.tool.multimedia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.asen.android.lib.base.global.AppPath;
import com.asen.android.lib.base.tool.util.AppUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple to Introduction
 * 增加多媒体的管理类
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:19
 */
class MultimediaAddTool4Fragment extends MultimediaAddTool {

    private static final String TAG = MultimediaAddTool4Fragment.class.getSimpleName();

    private static final String SUFFIX_PICTURE = "jpg";

    private static final String SUFFIX_VIDEO = "mp4";

    private static final String SUFFIX_MUSIC = "mp3";

    private final static int REQUEST_CODE_PICTURE = 0x1001;

    private final static int REQUEST_CODE_VIDEO = 0x1002;

    private final static int REQUEST_CODE_MUSIC = 0x1003;

    private Fragment mFragment;

    private Context mContext;

    private List<File> mFileList;

    private File lastFile = null;

    private File pictureFile = null;

    private File videoFile = null;

    private File musicFile = null;

    private IMultimediaAddListener mAddListener;

    /**
     * 构造函数
     *
     * @param Fragment
     * @param listener
     */
    MultimediaAddTool4Fragment(Fragment Fragment, IMultimediaAddListener listener) {
        this(Fragment, listener, null);
    }

    /**
     * 构造函数
     *
     * @param Fragment
     * @param listener
     * @param fileList 初始化集合
     */
    MultimediaAddTool4Fragment(Fragment Fragment, IMultimediaAddListener listener, List<File> fileList) {
        mFileList = fileList == null ? new ArrayList<File>() : fileList;
        mFragment = Fragment;
        mContext = mFragment.getActivity().getApplicationContext();
        mAddListener = listener;

        pictureFile = AppPath.getAppPictureFile(mContext);
        videoFile = AppPath.getAppVideoFile(mContext);
        musicFile = AppPath.getAppMusicFile(mContext);
    }

    /**
     * onActivityResult 时调用
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICTURE && resultCode == Activity.RESULT_OK) {
            mFileList.add(lastFile);
            refreshMedia(pictureFile); // 将拍摄的多媒体文件刷新到系统文件数据库
            refreshListener();
        } else if (requestCode == REQUEST_CODE_VIDEO && resultCode == Activity.RESULT_OK) {
            mFileList.add(lastFile);
            refreshMedia(videoFile); // 将拍摄的多媒体文件刷新到系统文件数据库
            refreshListener();
        } else if (requestCode == REQUEST_CODE_MUSIC && resultCode == Activity.RESULT_OK) {
            mFileList.add(lastFile);
            refreshMedia(musicFile); // 将拍摄的多媒体文件刷新到系统文件数据库
            refreshListener();
        }
        lastFile = null;
    }

    @Override
    public void setLastFile(File lastFile) {
        this.lastFile = lastFile;
    }

    @Override
    public void setFileList(List<File> fileList) {
        this.mFileList = fileList;
    }

    /**
     * 将拍摄的多媒体文件刷新到系统文件数据库
     *
     * @param folder
     */
    private void refreshMedia(File folder) {
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + folder.getPath())));
    }

    // 刷新多媒体增加接口
    private void refreshListener() {
        if (mAddListener != null)
            mAddListener.multimediaAdd(lastFile, mFileList);
    }

    /**
     * 获得最后一个附件文件
     *
     * @return
     */
    @Override
    public File getLastFile() {
        return lastFile;
    }

    /**
     * 获得所有该管理类中的文件
     *
     * @return
     */
    @Override
    public List<File> getFileList() {
        return mFileList;
    }

    /**
     * 开始拍照
     *
     * @return
     */
    @Override
    public boolean startPicture() {
        return startPicture(AppUtil.getUUid());
    }

    /**
     * 开始拍照
     *
     * @param name 照片名称
     * @return
     */
    @Override
    public boolean startPicture(String name) {
        if (pictureFile.exists() || pictureFile.mkdirs()) {
            String fullName = name + "." + SUFFIX_PICTURE;
            lastFile = new File(pictureFile, fullName);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(lastFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            intent.putExtra(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA, true);
            mFragment.startActivityForResult(intent, REQUEST_CODE_PICTURE);
            return true;
        } else {
            Log.e(TAG, pictureFile.getPath() + " is not exists!!!");
            return false;
        }
    }

    /**
     * 开始录视频
     *
     * @return
     */
    @Override
    public boolean startVideo() {
        return startVideo(AppUtil.getUUid());
    }

    /**
     * 开始录视频
     *
     * @param name
     * @return
     */
    @Override
    public boolean startVideo(String name) {
        return startVideo(AppUtil.getUUid(), -1, -1);
    }

    /**
     * 开始录视频
     *
     * @param time     时间秒  <0 时不做限制
     * @param fileSize 单位字节 <0 时不做限制
     * @return
     */
    @Override
    public boolean startVideo(int time, int fileSize) {
        return startVideo(AppUtil.getUUid(), time, fileSize);
    }

    /**
     * 开始录视频
     *
     * @param name     文件名
     * @param time     时间秒 <0 时不做限制
     * @param fileSize 单位字节 <0 时不做限制
     * @return
     */
    @Override
    public boolean startVideo(String name, int time, int fileSize) {
        if (videoFile.exists() || videoFile.mkdirs()) {
            String fullName = name + "." + SUFFIX_VIDEO;
            lastFile = new File(videoFile, fullName);

            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(lastFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            if (time < 0)
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, time);
            if (fileSize < 0)
                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, fileSize);
            mFragment.startActivityForResult(intent, REQUEST_CODE_VIDEO);
            return true;
        } else {
            Log.e(TAG, videoFile.getPath() + " is not exists!!!");
            return false;
        }

    }

    @Override
    public boolean startAudio() {
        return startAudio(AppUtil.getUUid());
    }

    @Override
    public boolean startAudio(String name) {
        if (musicFile.exists() || musicFile.mkdirs()) {
            String fullName = name + "." + SUFFIX_MUSIC;
            lastFile = new File(musicFile, fullName);

            Intent intent = new Intent(MultimediaAddTool.ACTION_AUDIO_RECORD);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(lastFile));
            mFragment.startActivityForResult(intent, REQUEST_CODE_MUSIC);
            return true;
        } else {
            Log.e(TAG, musicFile.getPath() + " is not exists!!!");
            return false;
        }
    }
}