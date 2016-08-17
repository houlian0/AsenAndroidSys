package com.asen.android.lib.base.tool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * ���ܽ��� ������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class EncryptUtil {

    private static int SIZE = 512 * 1024; // ���ܻ�������С

    private static Key getKey(byte[] arrBTmp, String alg) {
        if (!(alg.equals("DES") || alg.equals("DESede") || alg.equals("AES"))) {
            System.out.println("alg type not find: " + alg);
            return null;
        }
        byte[] arrB;
        if (alg.equals("DES")) {
            arrB = new byte[8];
        } else if (alg.equals("DESede")) {
            arrB = new byte[24];
        } else {
            arrB = new byte[16];
        }
        int i = 0;
        int j = 0;
        while (i < arrB.length) {
            if (j > arrBTmp.length - 1) {
                j = 0;
            }
            arrB[i] = arrBTmp[j];
            i++;
            j++;
        }
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, alg);
        return key;
    }

    /**
     * �����ļ�����-���ļ���ʱ�ϳ�
     *
     * @param readPath  ��ȡ���ļ�λ�ã����ļ���
     * @param writePath ������ļ�λ�ã����ļ���
     * @param strKey    KEYֵ
     * @return �ɹ���񣬳ɹ�����true�����򷵻�false
     */
    public static boolean encryptFile(String readPath, String writePath, String strKey) {
        int result = encryptFile(readPath, writePath, strKey, "DES");
        return result == 0;
    }

    /**
     * �����ļ�����-���ļ���ʱ�ϳ�
     *
     * @param readPath  ��ȡ���ļ�λ�ã����ļ���
     * @param writePath ������ļ�λ�ã����ļ���
     * @param strKey    KEYֵ
     * @param alg       DES|DESede|AES
     * @return 0����ɹ�������ʧ��
     */
    public static int encryptFile(String readPath, String writePath, String strKey, String alg) {
        if (!(alg.equals("DES") || alg.equals("DESede") || alg.equals("AES"))) {
            System.out.println("alg type not find: " + alg);
            return -1;
        }

        if (readPath == null || writePath == null) {
            System.out.println("file path is null");
            return -2;
        }

        File readFile = new File(readPath);
        File writeFile = new File(writePath);
        if (!readFile.exists()) {
            System.out.println("readPath (" + readPath + ") is error");
            return -3;
        }

        if (writeFile.exists()) {
            writeFile.delete();
            System.out.println("writePath (" + writePath
                    + ") is exists, delete it");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        int ret = 0;

        try {
            fis = new FileInputStream(readFile);
            fos = new FileOutputStream(writeFile);
            Key key = getKey(strKey.getBytes(), alg);
            Cipher c;
            c = Cipher.getInstance(alg);
            c.init(Cipher.ENCRYPT_MODE, key);

            byte[] buffer = new byte[SIZE];
            int count = -1;
            while ((count = fis.read(buffer)) != -1) {
                if (count == SIZE) {
                    fos.write(c.doFinal(buffer), 0, SIZE);
                } else {
                    byte[] b = new byte[count];
                    for (int i = 0; i < count; i++) {
                        b[i] = buffer[i];
                    }
                    fos.write(c.doFinal(b));
                }
            }
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
            ret = -4;
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                ret = -4;
                e.printStackTrace();
            }
        }

        return ret;
    }

    /**
     * �����ļ�����-���ļ���ʱ�ϳ�
     *
     * @param readPath  ��ȡ���ļ�λ�ã����ļ���
     * @param writePath ������ļ�λ�ã����ļ���
     * @param strKey    KEYֵ
     * @return �ɹ���񣬳ɹ�����true�����򷵻�false
     */
    public static boolean decryptFile(String readPath, String writePath, String strKey) {
        int result = decryptFile(readPath, writePath, strKey, "DES");
        return result == 0;
    }

    /**
     * �����ļ�����-���ļ���ʱ�ϳ�
     *
     * @param readPath  ��ȡ���ļ�λ�ã����ļ���
     * @param writePath ������ļ�λ�ã����ļ���
     * @param strKey    KEYֵ
     * @param alg       DES|DESede|AES
     * @return 0����ɹ�������ʧ��
     */
    public static int decryptFile(String readPath, String writePath, String strKey, String alg) {
        if (!(alg.equals("DES") || alg.equals("DESede") || alg.equals("AES"))) {
            System.out.println("alg type not find: " + alg);
            return -1;
        }

        if (readPath == null || writePath == null) {
            System.out.println("file path is null");
            return -2;
        }

        File readFile = new File(readPath);
        File writeFile = new File(writePath);
        if (!readFile.exists()) {
            System.out.println("readPath (" + readPath + ") is error");
            return -3;
        }

        if (writeFile.exists()) {
            writeFile.delete();
            System.out.println("writePath (" + writePath
                    + ") is exists, delete it");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        int ret = 0;

        try {
            fis = new FileInputStream(readFile);
            fos = new FileOutputStream(writeFile);
            Key key = getKey(strKey.getBytes(), alg);
            Cipher c;
            c = Cipher.getInstance(alg + "/ECB/NoPadding");
            c.init(Cipher.DECRYPT_MODE, key);

            byte[] buffer = new byte[SIZE];
            int count = -1;
            while ((count = fis.read(buffer)) != -1) {
                if (count == SIZE) {
                    fos.write(c.doFinal(buffer));
                } else {
                    byte[] b = new byte[count];
                    for (int i = 0; i < count; i++) {
                        b[i] = buffer[i];
                    }
                    fos.write(c.doFinal(b));
                }
            }
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException e) {
            ret = -4;
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                ret = -4;
                e.printStackTrace();
            }
        }

        return ret;
    }

    /**
     * �ֽڷ�ʽ����2
     *
     * @param bytes  Ҫ���ܵ��ֽ�����
     * @param strKey ����
     * @param alg    DES|DESede|AES
     * @return ���ܺ���ֽ�����
     */
    public static byte[] encryptBytes(byte[] bytes, String strKey, String alg) {
        if (!(alg.equals("DES") || alg.equals("DESede") || alg.equals("AES"))) {
            System.out.println("alg type not find: " + alg);
            return null;
        }

        byte[] r = null;
        if (bytes == null) return null;
        try {
            Key key = getKey(strKey.getBytes(), alg);
            Cipher c;
            c = Cipher.getInstance(alg);
            c.init(Cipher.ENCRYPT_MODE, key);
            r = c.doFinal(bytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | NoSuchPaddingException | BadPaddingException e) {
            e.printStackTrace();
        }

        return r;
    }

    /**
     * �ֽڷ�ʽ����2
     *
     * @param code   Ҫ���ܵ��ֽ�����
     * @param strKey �ܳ�
     * @param alg    DES|DESede|AES
     * @return ���ܺ���ֽ�����
     */
    public static byte[] decryptBytes(byte[] code, String strKey, String alg) {
        if (!(alg.equals("DES") || alg.equals("DESede") || alg.equals("AES"))) {
            System.out.println("alg type not find: " + alg);
            return null;
        }

        byte[] r = null;
        if (code == null) return null;
        try {
            Key key = getKey(strKey.getBytes(), alg);
            Cipher c;
            c = Cipher.getInstance(alg);
            c.init(Cipher.DECRYPT_MODE, key);
            r = c.doFinal(code);
        } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | NoSuchPaddingException | BadPaddingException e) {
            e.printStackTrace();
        }

        return r;
    }

    /**
     * �ֽ���ʽ�� DES����
     *
     * @param bytes  Ҫ���ܵ��ֽ�����
     * @param strKey �ܳ�
     * @return ���ܺ���ֽ�����
     */
    public static byte[] encryptBytes(byte[] bytes, String strKey) {
        return encrypt(bytes, strKey.getBytes());
    }

    /**
     * �ֽ���ʽ�� DES����
     *
     * @param code   Ҫ���ܵ��ֽ�����
     * @param strKey �ܳ�
     * @return ���ܺ���ֽ�����
     */
    public static byte[] decryptBytes(byte[] code, String strKey) {
        return decrypt(code, strKey.getBytes());
    }


    /**
     * DES����
     *
     * @param src ����Դ
     * @param key ��Կ�����ȱ�����8�ı���
     * @return ���ؼ��ܺ���ֽ�
     */
    private static byte[] encrypt(byte[] src, byte[] key) {
        if (src == null) return null;
        try {
            // DES�㷨Ҫ����һ�������ε������Դ
            SecureRandom sr = new SecureRandom();
            // ��ԭʼ�ܳ����ݴ���DESKeySpec����
            DESKeySpec dks = new DESKeySpec(key);
            // ����һ���ܳ׹�����Ȼ��������DESKeySpecת����
            // һ��SecretKey����
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher����ʵ����ɼ��ܲ���
            Cipher cipher = Cipher.getInstance("DES");
            // ���ܳ׳�ʼ��Cipher����
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            // ���ڣ���ȡ���ݲ�����
            // ��ʽִ�м��ܲ���
            return cipher.doFinal(src);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeySpecException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * DES����
     *
     * @param src ����Դ
     * @param key ��Կ�����ȱ�����8�ı���
     * @return ���ؽ��ܺ���ֽ�
     */
    private static byte[] decrypt(byte[] src, byte[] key) {
        if (src == null) return null;
        try {
            // DES�㷨Ҫ����һ�������ε������Դ
            SecureRandom sr = new SecureRandom();
            // ��ԭʼ�ܳ����ݴ���һ��DESKeySpec����
            DESKeySpec dks = new DESKeySpec(key);
            // ����һ���ܳ׹�����Ȼ��������DESKeySpec����ת����
            // һ��SecretKey����
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher����ʵ����ɽ��ܲ���
            Cipher cipher = Cipher.getInstance("DES");
            // ���ܳ׳�ʼ��Cipher����
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            // ���ڣ���ȡ���ݲ�����
            // ��ʽִ�н��ܲ���
            return cipher.doFinal(src);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

}
