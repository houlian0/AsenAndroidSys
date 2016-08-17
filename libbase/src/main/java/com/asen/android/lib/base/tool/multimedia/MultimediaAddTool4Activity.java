package com.asen.android.lib.base.tool.multimedia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.asen.android.lib.base.global.AppPath;
import com.asen.android.lib.base.tool.util.AppUtil;
import com.asen.android.lib.base.tool.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 增加多媒体的管理类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
class MultimediaAddTool4Activity extends MultimediaAddTool {

    private static final String TAG = MultimediaAddTool4Activity.class.getSimpleName();

    private static final String SUFFIX_PICTURE = "jpg";

    private static final String SUFFIX_VIDEO = "mp4";

    private static final String SUFFIX_MUSIC = "mp3";

    private final static int REQUEST_CODE_PICTURE = 0x1101;

    private final static int REQUEST_CODE_VIDEO = 0x1102;

    private final static int REQUEST_CODE_MUSIC = 0x1103;

    private Activity mActivity;

    private Context mContext;

    private List<File> mFileList;

    private File lastFile = null;

    private File pictureFile = null;

    private File videoFile = null;

    private File musicFile = null;

    private IMultimediaAddListener mAddListener;

    MultimediaAddTool4Activity(Activity activity, IMultimediaAddListener listener) {
        this(activity, listener, null);
    }

    MultimediaAddTool4Activity(Activity activity, IMultimediaAddListener listener, List<File> fileList) {
        mFileList = fileList == null ? new ArrayList<File>() : fileList;
        mActivity = activity;
        mContext = mActivity.getApplicationContext();
        mAddListener = listener;

        pictureFile = AppPath.getAppPictureFile(mContext);
        videoFile = AppPath.getAppVideoFile(mContext);
        musicFile = AppPath.getAppMusicFile(mContext);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (lastFile == null) return;

        if (requestCode == REQUEST_CODE_PICTURE && resultCode == Activity.RESULT_OK) {
            mFileList.add(lastFile);
            refreshMedia(pictureFile); // 将拍摄的照片文件刷新到系统文件数据库
            refreshListener();
        } else if (requestCode == REQUEST_CODE_VIDEO && resultCode == Activity.RESULT_OK) {
            mFileList.add(lastFile);
            refreshMedia(videoFile); // 将拍摄的视频文件刷新到系统文件数据库
            refreshListener();
        } else if (requestCode == REQUEST_CODE_MUSIC && resultCode == Activity.RESULT_OK) {
            mFileList.add(lastFile);
            refreshMedia(musicFile); // 将拍摄的音频文件刷新到系统文件数据库
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

    private void refreshMedia(File folder) {
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + folder.getPath())));
    }

    // 刷新多媒体增加接口
    private void refreshListener() {
        if (mAddListener != null)
            mAddListener.multimediaAdd(lastFile, mFileList);
    }

    @Override
    public File getLastFile() {
        return lastFile;
    }

    @Override
    public List<File> getFileList() {
        return mFileList;
    }

    @Override
    public boolean startPicture() {
        return startPicture(AppUtil.getUUid());
    }

    @Override
    public boolean startPicture(String name) {
        if (pictureFile.exists() || pictureFile.mkdirs()) {
            String fullName = name + "." + SUFFIX_PICTURE;
            lastFile = new File(pictureFile, fullName);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(lastFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            intent.putExtra(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA, true);
            mActivity.startActivityForResult(intent, REQUEST_CODE_PICTURE);
            return true;
        } else {
            Log.e(TAG, pictureFile.getPath() + " is not exists!!!");
            return false;
        }
    }

    @Override
    public boolean startVideo() {
        return startVideo(AppUtil.getUUid());
    }

    @Override
    public boolean startVideo(String name) {
        return startVideo(AppUtil.getUUid(), -1, -1);
    }

    @Override
    public boolean startVideo(int time, int fileSize) {
        return startVideo(AppUtil.getUUid(), time, fileSize);
    }

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
            mActivity.startActivityForResult(intent, REQUEST_CODE_VIDEO);
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
//        if (musicFile.exists() || musicFile.mkdirs()) {
//            String fullName = name + "." + SUFFIX_MUSIC;
//            lastFile = new File(musicFile, fullName);
//
//            Intent intent = new Intent(MultimediaAddTool.ACTION_AUDIO_RECORD);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(lastFile));
//            mActivity.startActivityForResult(intent, REQUEST_CODE_MUSIC);
//            return true;
//        } else {
//            Log.e(TAG, musicFile.getPath() + " is not exists!!!");
//            return false;
//        }
        ToastUtil.showToast(mContext, "预留功能");
        return false;
    }

}