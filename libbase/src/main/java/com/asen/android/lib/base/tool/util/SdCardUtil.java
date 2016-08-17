package com.asen.android.lib.base.tool.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Sd卡获取的工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class SdCardUtil {

    /**
     * 获得内置sd卡
     * 注：部分手机，如：小米1S，本身是没有内置sd卡的，外部插入的卡即为内置sd卡，如果不插入sd卡，则属于sd卡不可用
     *
     * @return 返回内置sd卡路径，没有的话，返回null
     */
    public static String getFirstExternalPath() {
        return isFirstSdcardMounted() ? Environment.getExternalStorageDirectory().getPath() : null;
    }

    /**
     * 返回sd卡路径
     *
     * @return 返回值不带File seperater "/", 如果没有外置第二个sd卡,返回null
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getSecondExternalPath(Context context) {
        if (Version.hasKitKat()) {  // 4.4
            File[] filesDirs = context.getExternalFilesDirs(null);
            if (filesDirs == null || filesDirs.length == 0) return null;

            File file = filesDirs[filesDirs.length - 1];
            if (file != null) {
                String path = file.getAbsolutePath();
                return path.substring(0, path.length() - 1);
            }
            return null;
        } else {
            List<String> paths = getAllExternalSdcardPath();
            if (paths.size() == 2) {
                for (String path : paths) {
                    if (path != null && !path.equals(getFirstExternalPath())) {
                        return path;
                    }
                }
                return null;
            } else {
                return null;
            }
        }
    }

    /**
     * 判断内置sd卡是否可用
     *
     * @return 可用返回true，否则返回false
     */
    public static boolean isFirstSdcardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断外置sd卡是否可用
     *
     * @return 可用返回true，否则返回false
     */
    public static boolean isSecondSDcardMounted(Context context) {
        String sd2 = getSecondExternalPath(context);
        return sd2 != null && checkFsWritable(sd2 + File.separator);
    }

    /**
     * 判断文件夹是否可写
     *
     * @param dir 文件路径
     * @return 可写返回true，否则返回false
     */
    public static boolean isWritable(String dir) {
        return checkFsWritable(dir);
    }

    /**
     * 测试外置sd卡是否卸载，不能直接判断外置sd卡是否为null，因为当外置sd卡拔出时，仍然能得到外置sd卡路径。
     * 我这种方法是按照android谷歌测试DICM的方法， 创建一个文件，然后立即删除，看是否卸载外置sd卡
     * 注意这里有一个小bug，即使外置sd卡没有卸载，但是存储空间不够大，或者文件数已至最大数，此时，也不能创建新文件。此时，统一提示用户清理sd卡吧
     *
     * @param dir 文件路径
     * @return 可写返回true，否则返回false
     */
    private static boolean checkFsWritable(String dir) {
        if (dir == null)
            return false;

        File directory = new File(dir);

        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }

        File f = new File(directory, ".keysharetestgzc");
        try {
            if (f.exists()) {
                f.delete();
            }
            if (!f.createNewFile()) {
                return false;
            }
            f.delete();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static List<String> getAllExternalSdcardPath() {
        List<String> sdList = new ArrayList<>();

        String firstPath = getFirstExternalPath();

        Runtime runtime = Runtime.getRuntime();
        InputStream is = null;
        BufferedReader br = null;
        // 得到路径
        try {
            Process proc = runtime.exec("mount");// 或者 "cat /proc/mounts"
            is = proc.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                // 将常见的linux分区过滤掉
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("media"))
                    continue;
                if (line.contains("system") || line.contains("cache") || line.contains("sys") || line.contains("data")
                        || line.contains("tmpfs") || line.contains("shell") || line.contains("root") || line.contains("acct")
                        || line.contains("proc") || line.contains("misc") || line.contains("obb")) {
                    continue;
                }

                if (line.contains("fat") || line.contains("fuse") || (line.contains("ntfs"))) {
                    String columns[] = line.split(" ");
                    if (columns.length > 1) {
                        String path = columns[1];
                        if (path != null && !sdList.contains(path) && path.toLowerCase().contains("sd"))
                            sdList.add(columns[1]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!sdList.contains(firstPath) && firstPath != null) {
            sdList.add(firstPath);
        }

        return sdList;
    }
    
}
