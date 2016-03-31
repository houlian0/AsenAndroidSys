package com.asen.android.lib.base.tool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by HL_SEN on 2015/9/22.
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class ZipUtil {

    private static final int ZIPUTIL_CACHE = 512 * 1024;

    /**
     * 压缩单个文件
     *
     * @param filepath 文件位置
     * @param zippath  压缩后位置
     */
    public static boolean zipFile(String filepath, String zippath) {
        boolean result = false;

        InputStream input = null;
        ZipOutputStream zipOut = null;


        try {
            File file = new File(filepath);
            File zipFile = new File(zippath);
            input = new FileInputStream(file);
            zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            zipOut.putNextEntry(new ZipEntry(file.getName()));

            byte buffer[] = new byte[ZIPUTIL_CACHE];
            int temp = 0;
            while ((temp = input.read(buffer)) != -1) {
                zipOut.write(buffer, 0, temp);
            }
            System.out.println("zip " + filepath + " to " + zippath);

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                zipOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 一次性压缩多个文件，文件存放至一个文件夹中
     *
     * @param filepath 要被压缩的文件夹
     * @param zippath  压缩后位置
     */
    public static boolean zipMultiFile(String filepath, String zippath) {
        boolean result = false;

        try {
            File file = new File(filepath);// 要被压缩的文件夹
            File zipFile = new File(zippath);
            InputStream input = null;
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));

            byte buffer[] = new byte[ZIPUTIL_CACHE];
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; ++i) {
                    input = new FileInputStream(files[i]);
                    zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + files[i].getName()));

                    int temp = 0;
                    while ((temp = input.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, temp);
                    }
                    input.close();
                }
            } else {// 否则,则调用压缩单个文件的方法
                zipFile(filepath, zippath);
            }
            zipOut.close();
            System.out.println("zip directory is success");

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 解压缩（解压缩单个文件）
     *
     * @param zippath     压缩文件路径和文件名
     * @param outfilepath 解压后路径和文件名
     * @param filename    所解压的文件名
     */
    public static boolean zipContraFile(String zippath, String outfilepath, String filename) {
        boolean result = false;

        try {
            File file = new File(zippath);// 压缩文件路径和文件名
            File outFile = new File(outfilepath);// 解压后路径和文件名
            ZipFile zipFile = new ZipFile(file);
            ZipEntry entry = zipFile.getEntry(filename);// 所解压的文件名
            InputStream input = zipFile.getInputStream(entry);
            OutputStream output = new FileOutputStream(outFile);

            byte buffer[] = new byte[ZIPUTIL_CACHE];
            int temp = 0;
            while ((temp = input.read(buffer)) != -1) {
                output.write(buffer, 0, temp);
            }

            output.close();
            input.close();
            zipFile.close();

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 解压缩（压缩文件中包含多个文件）可代替上面的方法使用。 ZipInputStream类
     * 当我们需要解压缩多个文件的时候，ZipEntry就无法使用了，
     * 如果想操作更加复杂的压缩文件，我们就必须使用ZipInputStream类
     * 文件不存在则创建，文件存在则删除原有的再创建
     *
     * @param zippath    压缩文件路径和文件名
     * @param outzippath 解压后保存的文件路径
     */
    public static boolean ZipContraMultiFile(String zippath, String outzippath) {
        boolean result = false;

        ZipInputStream zipInput = null;
        ZipFile zipFile = null;
        InputStream input = null;
        OutputStream output = null;
        try {
            File file = new File(zippath);
            File outFile = null;
            zipFile = new ZipFile(file);
            zipInput = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = null;

            byte buffer[] = new byte[ZIPUTIL_CACHE];

            while ((entry = zipInput.getNextEntry()) != null) {
                System.out.println("解压缩" + entry.getName() + "文件");
                outFile = new File(outzippath + File.separator + entry.getName());

                if (entry.isDirectory()) {
                    if (!outFile.exists()) {
                        outFile.mkdirs();
                    }
                } else {
                    if (!outFile.getParentFile().exists()) {
                        outFile.getParentFile().mkdir();
                    }
                    if (!outFile.exists()) {
                        outFile.createNewFile();
                    } else {
                        outFile.delete();
                        outFile.createNewFile();
                    }
                    input = zipFile.getInputStream(entry);
                    output = new FileOutputStream(outFile);

                    int temp = 0;
                    while ((temp = input.read(buffer)) != -1) {
                        output.write(buffer, 0, temp);
                    }
                }
            }

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
                zipInput.close();
                zipFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
