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
 * 加密解密 工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class EncryptUtil {

    private static int SIZE = 512 * 1024; // 加密缓冲区大小

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
     * 整个文件加密-大文件耗时较长
     *
     * @param readPath  读取的文件位置，含文件名
     * @param writePath 输出的文件位置，含文件名
     * @param strKey    KEY值
     * @return 成功与否，成功返回true，否则返回false
     */
    public static boolean encryptFile(String readPath, String writePath, String strKey) {
        int result = encryptFile(readPath, writePath, strKey, "DES");
        return result == 0;
    }

    /**
     * 整个文件加密-大文件耗时较长
     *
     * @param readPath  读取的文件位置，含文件名
     * @param writePath 输出的文件位置，含文件名
     * @param strKey    KEY值
     * @param alg       DES|DESede|AES
     * @return 0代表成功，其他失败
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
     * 整个文件解密-大文件耗时较长
     *
     * @param readPath  读取的文件位置，含文件名
     * @param writePath 输出的文件位置，含文件名
     * @param strKey    KEY值
     * @return 成功与否，成功返回true，否则返回false
     */
    public static boolean decryptFile(String readPath, String writePath, String strKey) {
        int result = decryptFile(readPath, writePath, strKey, "DES");
        return result == 0;
    }

    /**
     * 整个文件解密-大文件耗时较长
     *
     * @param readPath  读取的文件位置，含文件名
     * @param writePath 输出的文件位置，含文件名
     * @param strKey    KEY值
     * @param alg       DES|DESede|AES
     * @return 0代表成功，其他失败
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
     * 字节方式加密2
     *
     * @param bytes  要加密的字节数组
     * @param strKey 加密
     * @param alg    DES|DESede|AES
     * @return 加密后的字节数组
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
     * 字节方式解密2
     *
     * @param code   要解密的字节数组
     * @param strKey 密匙
     * @param alg    DES|DESede|AES
     * @return 解密后的字节数组
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
     * 字节形式的 DES加密
     *
     * @param bytes  要加密的字节数组
     * @param strKey 密匙
     * @return 加密后的字节数组
     */
    public static byte[] encryptBytes(byte[] bytes, String strKey) {
        return encrypt(bytes, strKey.getBytes());
    }

    /**
     * 字节形式的 DES解密
     *
     * @param code   要解密的字节数组
     * @param strKey 密匙
     * @return 解密后的字节数组
     */
    public static byte[] decryptBytes(byte[] code, String strKey) {
        return decrypt(code, strKey.getBytes());
    }


    /**
     * DES加密
     *
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return 返回加密后的字节
     */
    private static byte[] encrypt(byte[] src, byte[] key) {
        if (src == null) return null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(src);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeySpecException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * DES解密
     *
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return 返回解密后的字节
     */
    private static byte[] decrypt(byte[] src, byte[] key) {
        if (src == null) return null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            // 现在，获取数据并解密
            // 正式执行解密操作
            return cipher.doFinal(src);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

}
