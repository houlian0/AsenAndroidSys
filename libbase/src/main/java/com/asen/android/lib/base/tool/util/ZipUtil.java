package com.asen.android.lib.base.tool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Zip��ѹ��������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class ZipUtil {

    /**
     * ѹ��
     *
     * @param directory Ҫѹ�����ļ���File
     * @param zipFile   �����zip��File
     * @param password  ѹ��ʱ����
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     */
    public static void zip(File directory, File zipFile, String password) throws IOException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<>();
        queue.push(directory);
        ZipOutputStream zout = null;
        try {
            zout = new ZipOutputStream(new CipherOutputStream(new FileOutputStream(zipFile), createCipher(1, password)));
            while (!queue.isEmpty()) {
                directory = queue.pop();
                for (File kid : directory.listFiles()) {
                    String name = base.relativize(kid.toURI()).getPath();
                    if (kid.isDirectory()) {
                        queue.push(kid);
                        name = name + "/";
                        zout.putNextEntry(new ZipEntry(name));
                    } else {
                        zout.putNextEntry(new ZipEntry(name));
                        copy(kid, zout);
                        zout.closeEntry();
                    }
                }
            }
        } finally {
            if (null != zout)
                zout.close();
        }
    }

    /**
     * ��ѹ
     *
     * @param zipFile   Ҫ��ѹ��zip��File
     * @param directory ��ѹ�����ļ���File
     * @param password  ��ѹ������
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     */
    public static void unzip(File zipFile, File directory, String password) throws IOException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        if (!directory.exists()) {
            directory.mkdirs();
        }

        ZipInputStream input = null;
        try {
            input = new ZipInputStream(new CipherInputStream(new FileInputStream(zipFile), createCipher(2, password)));
            ZipEntry entry = null;
            while ((entry = input.getNextEntry()) != null) {
                File file = new File(directory, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    file.getParentFile().mkdirs();
                    copy(input, file);
                }
                input.closeEntry();
            }
        } finally {
            if (null != input)
                input.close();
        }
    }

    private static Cipher createCipher(int mode, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        String alg = "PBEWithMD5AndDES";
        SecureRandom sr = new SecureRandom();

        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(alg);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance(alg);
        cipher.init(mode, secretKey, new PBEParameterSpec("saltsalt".getBytes(), 2000));

        return cipher;
    }

    /**
     * ѹ��
     *
     * @param directory Ҫѹ�����ļ���File
     * @param zipFile   �����zip��File
     * @throws IOException
     */
    public static void zip(File directory, File zipFile) throws IOException {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<>();
        queue.push(directory);
        ZipOutputStream zout = null;
        try {
            zout = new ZipOutputStream(new FileOutputStream(zipFile));
            while (!queue.isEmpty()) {
                directory = queue.pop();
                for (File kid : directory.listFiles()) {
                    String name = base.relativize(kid.toURI()).getPath();
                    if (kid.isDirectory()) {
                        queue.push(kid);
                        name = name + "/";
                        zout.putNextEntry(new ZipEntry(name));
                    } else {
                        zout.putNextEntry(new ZipEntry(name));
                        copy(kid, zout);
                        zout.closeEntry();
                    }
                }
            }
        } finally {
            if (null != zout)
                zout.close();
        }
    }

    /**
     * ��ѹ
     *
     * @param zipFile   Ҫ��ѹ��zip��File
     * @param directory ��ѹ�����ļ���File
     * @throws IOException
     */
    public static void unzip(File zipFile, File directory) throws IOException {
        if (!directory.exists()) {
            directory.mkdirs();
        }

        ZipInputStream input = null;
        try {
            input = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry entry = null;
            while ((entry = input.getNextEntry()) != null) {
                File file = new File(directory, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    file.getParentFile().mkdirs();
                    copy(input, file);
                }

                input.closeEntry();
            }
        } finally {
            if (null != input)
                input.close();
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int readCount = in.read(buffer);
            if (readCount < 0) {
                break;
            }
            out.write(buffer, 0, readCount);
        }
    }

    private static void copy(File file, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            copy(in, out);
            in.close();
        } finally {
            in.close();
        }
    }

    private static void copy(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        try {
            copy(in, out);
            out.close();
        } finally {
            out.close();
        }
    }

}