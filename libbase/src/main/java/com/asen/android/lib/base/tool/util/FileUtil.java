package com.asen.android.lib.base.tool.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件操作工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class FileUtil {

    /**
     * 创建文件夹
     *
     * @param folderFile 文件File
     * @return 是否成功
     */
    public static boolean createFolder(File folderFile) {
        return folderFile.exists() || folderFile.mkdirs();
    }

    /**
     * 创建文件
     *
     * @param file 文件File
     * @return 是否成功
     */
    public static boolean createFile(File file) throws IOException {
        return createFolder(file.getParentFile()) && (file.exists() || file.createNewFile());
    }

    /**
     * 删除文件（不能删除文件夹）
     *
     * @param file 文件File
     * @return 是否成功
     */
    public static boolean deleteFile(File file) {
        return !file.exists() || file.delete();
    }

    /**
     * 删除文件夹
     *
     * @param folderFile 文件夹File
     * @return 是否成功
     */
    public static boolean deleteFolder(File folderFile) {
        if (folderFile.exists()) {
            if (folderFile.isFile()) {
                return folderFile.delete();
            } else {
                File[] files = folderFile.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (!deleteFolder(f)) {
                            return false;
                        }
                    }
                }
                return folderFile.delete();
            }
        } else {
            return true;
        }
    }

    /**
     * 删除文件 或者 文件夹
     *
     * @param file 文件File
     * @return 是否成功
     */
    public static boolean delete(File file) {
        return file.isFile() ? deleteFile(file) : deleteFolder(file);
    }


    /**
     * 复制单个文件
     *
     * @param oldFile 旧文件
     * @param newFile 新文件
     * @throws IOException
     */
    public static void copyFile(File oldFile, File newFile) throws IOException {
        if (oldFile.exists()) {
            InputStream is = null; // 读入原文件
            OutputStream fs = null;
            try {
                is = new FileInputStream(oldFile);
                fs = new FileOutputStream(newFile);
                int byteread = 0;
                byte[] buffer = new byte[1024];
                while ((byteread = is.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
            } catch (IOException e) {
                throw new IOException(e);
            } finally {
                if (is != null) is.close();
                if (fs != null) fs.close();
            }
        } else {
            throw new IOException("File [" + oldFile.getPath() + "] is not exists!!!");
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldFolder 旧文件夹
     * @param newFolder 新文件夹
     * @throws IOException
     */
    public static void copyFolder(File oldFolder, File newFolder) throws IOException {
        if (oldFolder.exists()) {
            boolean isNewFileCreated = createFolder(newFolder);
            if (isNewFileCreated) { // 新文件夹创建成功
                if (oldFolder.isFile()) {
                    throw new IOException("Old File [" + oldFolder.getPath() + "] is file!!!");
                } else {
                    File[] files = oldFolder.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.isFile()) {
                                copyFile(file, new File(newFolder, file.getName()));
                            } else {
                                copyFolder(file, new File(newFolder, file.getName()));
                            }
                        }
                    } else {
                        throw new IOException("Child File list [" + oldFolder.getPath() + "] get error!!!");
                    }
                }
            } else {
                throw new IOException("File [" + newFolder.getPath() + "] create error!!!");
            }
        } else {
            throw new IOException("File [" + oldFolder.getPath() + "] is not exists!!!");
        }
    }

    /**
     * 复制整个文件夹 或 单个文件
     *
     * @param oldFile 旧文件
     * @param newFile
     * @throws IOException
     */
    public static void copy(File oldFile, File newFile) throws IOException {
        if (oldFile.isFile()) {
            copyFile(oldFile, newFile);
        } else {
            copyFolder(oldFile, newFile);
        }
    }

    /**
     * 移动单个文件
     *
     * @param oldFile 旧文件
     * @param newFile 新文件
     * @throws IOException
     */
    public static void moveFile(File oldFile, File newFile) throws IOException {
        copyFile(oldFile, newFile);
        deleteFile(oldFile);
    }

    /**
     * 移动整个文件夹
     *
     * @param oldFolder
     * @param newFolder
     * @throws IOException
     */
    public static void moveFolder(File oldFolder, File newFolder) throws IOException {
        copyFolder(oldFolder, newFolder);
        deleteFolder(oldFolder);
    }

    /**
     * 移动单个文件 或者 整个文件夹
     *
     * @param oldFile 旧文件
     * @param newFile 新文件
     * @throws IOException
     */
    public static void move(File oldFile, File newFile) throws IOException {
        if (oldFile.isFile()) {
            moveFile(oldFile, newFile);
        } else {
            moveFolder(oldFile, newFile);
        }
    }

    /**
     * 重命名文件
     *
     * @param oldFile 旧文件
     * @param newFile 新文件
     * @return 是否成功
     */
    public static boolean rename(File oldFile, File newFile) {
        return oldFile.renameTo(newFile);
    }

    /**
     * 写入小型数据的TXT文件
     *
     * @param file     文件File
     * @param content  内容
     * @param isAppend 是否附加在后面继续写入
     * @throws IOException
     */
    public static void writeTxtFile(File file, String content, boolean isAppend) throws IOException {
        if (content == null) return;

        createFile(file);

        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend), "UTF-8"));
            writer.append(content);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (writer != null) writer.close();
        }
    }

    /**
     * 读取小型数据的TXT文件
     *
     * @param file 文件File
     * @throws IOException
     */
    public static String readTxtFile(File file) throws IOException {
        if (file.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String bufferStr;
                while ((bufferStr = reader.readLine()) != null) {
                    sb.append(bufferStr);
                }
                return sb.toString();
            } catch (IOException e) {
                throw new IOException(e);
            } finally {
                if (reader != null) reader.close();
            }
        } else {
            throw new IOException("File [" + file.getPath() + "] is not exists!!!");
        }
    }

    /**
     * 写入字节，适用于小型文件
     *
     * @param file     文件File
     * @param bytes    字节
     * @param isAppend 是否附加在后面继续写入
     * @throws IOException
     */
    public static void writeBytesFile(File file, byte[] bytes, boolean isAppend) throws IOException {
        FileOutputStream outputStream = null;

        createFile(file);

        try {
            outputStream = new FileOutputStream(file, isAppend);
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (outputStream != null) outputStream.close();
        }
    }

    /**
     * 读取小型数据的字节文件
     *
     * @param file 文件File
     * @throws IOException
     */
    public static byte[] readBytesFile(File file) throws IOException {
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            outputStream = new ByteArrayOutputStream(1024);
            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = inputStream.read(temp)) != -1) {
                outputStream.write(temp, 0, size);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
        }
    }

    /**
     * 计算文件的 MD5 值
     *
     * @param file 文件File
     * @return MD5值
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String getFileMD5(File file) throws IOException, NoSuchAlgorithmException {
        if (!file.isFile()) {
            throw new IOException("File [" + file.getPath() + "] is not a file!!!");
        }

        FileInputStream inputStream = null;
        try {
            int len;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            inputStream = new FileInputStream(file);
            byte buffer[] = new byte[2018];
            while ((len = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw e;
        } finally {
            if (inputStream != null) inputStream.close();
        }
    }

}
