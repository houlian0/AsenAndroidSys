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
 * Sd����ȡ�Ĺ�����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class SdCardUtil {

    /**
     * �������sd��
     * ע�������ֻ����磺С��1S��������û������sd���ģ��ⲿ����Ŀ���Ϊ����sd�������������sd����������sd��������
     *
     * @return ��������sd��·����û�еĻ�������null
     */
    public static String getFirstExternalPath() {
        return isFirstSdcardMounted() ? Environment.getExternalStorageDirectory().getPath() : null;
    }

    /**
     * ����sd��·��
     *
     * @return ����ֵ����File seperater "/", ���û�����õڶ���sd��,����null
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
     * �ж�����sd���Ƿ����
     *
     * @return ���÷���true�����򷵻�false
     */
    public static boolean isFirstSdcardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * �ж�����sd���Ƿ����
     *
     * @return ���÷���true�����򷵻�false
     */
    public static boolean isSecondSDcardMounted(Context context) {
        String sd2 = getSecondExternalPath(context);
        return sd2 != null && checkFsWritable(sd2 + File.separator);
    }

    /**
     * �ж��ļ����Ƿ��д
     *
     * @param dir �ļ�·��
     * @return ��д����true�����򷵻�false
     */
    public static boolean isWritable(String dir) {
        return checkFsWritable(dir);
    }

    /**
     * ��������sd���Ƿ�ж�أ�����ֱ���ж�����sd���Ƿ�Ϊnull����Ϊ������sd���γ�ʱ����Ȼ�ܵõ�����sd��·����
     * �����ַ����ǰ���android�ȸ����DICM�ķ����� ����һ���ļ���Ȼ������ɾ�������Ƿ�ж������sd��
     * ע��������һ��Сbug����ʹ����sd��û��ж�أ����Ǵ洢�ռ䲻���󣬻����ļ����������������ʱ��Ҳ���ܴ������ļ�����ʱ��ͳһ��ʾ�û�����sd����
     *
     * @param dir �ļ�·��
     * @return ��д����true�����򷵻�false
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
        // �õ�·��
        try {
            Process proc = runtime.exec("mount");// ���� "cat /proc/mounts"
            is = proc.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                // ��������linux�������˵�
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
