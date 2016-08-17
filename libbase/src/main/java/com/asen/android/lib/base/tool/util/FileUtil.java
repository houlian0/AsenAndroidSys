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
 * �ļ�����������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class FileUtil {

    /**
     * �����ļ���
     *
     * @param folderFile �ļ�File
     * @return �Ƿ�ɹ�
     */
    public static boolean createFolder(File folderFile) {
        return folderFile.exists() || folderFile.mkdirs();
    }

    /**
     * �����ļ�
     *
     * @param file �ļ�File
     * @return �Ƿ�ɹ�
     */
    public static boolean createFile(File file) throws IOException {
        return createFolder(file.getParentFile()) && (file.exists() || file.createNewFile());
    }

    /**
     * ɾ���ļ�������ɾ���ļ��У�
     *
     * @param file �ļ�File
     * @return �Ƿ�ɹ�
     */
    public static boolean deleteFile(File file) {
        return !file.exists() || file.delete();
    }

    /**
     * ɾ���ļ���
     *
     * @param folderFile �ļ���File
     * @return �Ƿ�ɹ�
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
     * ɾ���ļ� ���� �ļ���
     *
     * @param file �ļ�File
     * @return �Ƿ�ɹ�
     */
    public static boolean delete(File file) {
        return file.isFile() ? deleteFile(file) : deleteFolder(file);
    }


    /**
     * ���Ƶ����ļ�
     *
     * @param oldFile ���ļ�
     * @param newFile ���ļ�
     * @throws IOException
     */
    public static void copyFile(File oldFile, File newFile) throws IOException {
        if (oldFile.exists()) {
            InputStream is = null; // ����ԭ�ļ�
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
     * ���������ļ�������
     *
     * @param oldFolder ���ļ���
     * @param newFolder ���ļ���
     * @throws IOException
     */
    public static void copyFolder(File oldFolder, File newFolder) throws IOException {
        if (oldFolder.exists()) {
            boolean isNewFileCreated = createFolder(newFolder);
            if (isNewFileCreated) { // ���ļ��д����ɹ�
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
     * ���������ļ��� �� �����ļ�
     *
     * @param oldFile ���ļ�
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
     * �ƶ������ļ�
     *
     * @param oldFile ���ļ�
     * @param newFile ���ļ�
     * @throws IOException
     */
    public static void moveFile(File oldFile, File newFile) throws IOException {
        copyFile(oldFile, newFile);
        deleteFile(oldFile);
    }

    /**
     * �ƶ������ļ���
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
     * �ƶ������ļ� ���� �����ļ���
     *
     * @param oldFile ���ļ�
     * @param newFile ���ļ�
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
     * �������ļ�
     *
     * @param oldFile ���ļ�
     * @param newFile ���ļ�
     * @return �Ƿ�ɹ�
     */
    public static boolean rename(File oldFile, File newFile) {
        return oldFile.renameTo(newFile);
    }

    /**
     * д��С�����ݵ�TXT�ļ�
     *
     * @param file     �ļ�File
     * @param content  ����
     * @param isAppend �Ƿ񸽼��ں������д��
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
     * ��ȡС�����ݵ�TXT�ļ�
     *
     * @param file �ļ�File
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
     * д���ֽڣ�������С���ļ�
     *
     * @param file     �ļ�File
     * @param bytes    �ֽ�
     * @param isAppend �Ƿ񸽼��ں������д��
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
     * ��ȡС�����ݵ��ֽ��ļ�
     *
     * @param file �ļ�File
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
     * �����ļ��� MD5 ֵ
     *
     * @param file �ļ�File
     * @return MD5ֵ
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
