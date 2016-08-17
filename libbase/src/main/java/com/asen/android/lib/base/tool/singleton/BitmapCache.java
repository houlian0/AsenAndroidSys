package com.asen.android.lib.base.tool.singleton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;

import com.asen.android.lib.base.tool.util.AppUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

/**
 * Bitmap�����ࣨ��ֹ�ڴ��������UI�߳��в���
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public class BitmapCache {

    static private volatile BitmapCache cache;
    /**
     * ����Cache���ݵĴ洢
     */
    private Hashtable<String, BitmapRef> bitmapRefs;
    /**
     * ����Reference�Ķ��У������õĶ����Ѿ������գ��򽫸����ô�������У�
     */
    private ReferenceQueue<Bitmap> q;

    /**
     * �̳�SoftReference��ʹ��ÿһ��ʵ�������п�ʶ��ı�ʶ��
     */
    class BitmapRef extends SoftReference<Bitmap> {
        private String _key = "";

        public BitmapRef(Bitmap bmp, ReferenceQueue<Bitmap> q, String key) {
            super(bmp, q);
            _key = key;
        }
    }

    private BitmapCache() {
        bitmapRefs = new Hashtable<>();
        q = new ReferenceQueue<>();
    }

    /**
     * ȡ�û�����ʵ��
     */
    public static BitmapCache getInstance() {
        if (cache == null) {
            synchronized (BitmapCache.class) {
                if (cache == null) {
                    cache = new BitmapCache();
                }
            }
        }
        return cache;
    }

    /**
     * �������õķ�ʽ��һ��Bitmap�����ʵ���������ò����������
     */
    private void addCacheBitmap(Bitmap bmp, String key) {
        cleanCache();// �����������
        if (bmp == null)
            return;
        BitmapRef ref = new BitmapRef(bmp, q, key);
        bitmapRefs.put(key, ref);
    }

    private void cleanCache() {
        BitmapRef ref = null;
        while ((ref = (BitmapRef) q.poll()) != null) {
            bitmapRefs.remove(ref._key);
        }
    }

    /**
     * ���Cache�ڵ�ȫ������
     */
    public void clearCache() {
        cleanCache();
        bitmapRefs.clear();
        System.gc();
        System.runFinalization();
    }

    // ��ȡsd����ͼƬ����ת�Ƕ�
    int getPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    // ��תͼƬ
    private Bitmap rotatingBitmap(int angle, Bitmap bitmap) {
        // ��תͼƬ ����
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // �����µ�ͼƬ
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }

    /**
     * ��sd���л��ͼƬ
     *
     * @param filePath �ļ�·��
     * @return ����ͼƬ��Bitmap
     */
    public Bitmap getSdBitmap(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }

        Bitmap bitmapImage = null;
        if (bitmapRefs.containsKey(filePath)) {
            BitmapRef ref = bitmapRefs.get(filePath);
            bitmapImage = ref.get();
        }

        if (bitmapImage == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            int i = 0;
            Bitmap bitmap = null;

            while (true) {
                try {
                    if ((options.outWidth >> i <= 1024) && (options.outHeight >> i <= 1024)) {
                        options.inSampleSize = (int) Math.pow(2.0D, i);
                        options.inJustDecodeBounds = false;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        bitmap = BitmapFactory.decodeFile(filePath, options);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i += 1;
            }

            int degree = getPictureDegree(filePath);
            if (degree == 0) {
                bitmapImage = bitmap;
            } else {
                bitmapImage = rotatingBitmap(degree, bitmap);
            }
            this.addCacheBitmap(bitmapImage, filePath);
        }

        return bitmapImage;
    }

    /**
     * ��assets�ļ����д�ͼƬ
     *
     * @param context    Android������
     * @param assetsFile assets�е��ļ�·��
     * @return ����ͼƬ��Bitmap
     */
    public Bitmap getAssetBitmap(Context context, String assetsFile) {

        Bitmap bitmapImage = null;
        // �������Ƿ��и�Bitmapʵ���������ã�����У�����������ȡ�á�
        if (bitmapRefs.containsKey(assetsFile)) {
            BitmapRef ref = bitmapRefs.get(assetsFile);
            bitmapImage = ref.get();
        }
        // ���û�������ã����ߴ��������еõ���ʵ����null�����¹���һ��ʵ����
        // �����������½�ʵ����������
        if (bitmapImage == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inTempStorage = new byte[16 * 1024];
            int inSampleSize = 1;

            BufferedInputStream buf = null;
            InputStream inputStream = null;
            try {
                inputStream = context.getAssets().open(assetsFile);
                buf = new BufferedInputStream(inputStream);

                while (true) {
                    try {
                        options.inSampleSize = inSampleSize;
                        bitmapImage = BitmapFactory.decodeStream(buf, null, options);
                        this.addCacheBitmap(bitmapImage, assetsFile);
                        break;
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                    inSampleSize++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (buf != null)
                        buf.close();
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmapImage;
    }

    /**
     * ��R��Դ�ļ����õ�ͼƬ
     *
     * @param context Android������
     * @param resId   ��ԴID
     * @return ����ͼƬ��Bitmap
     */
    public Bitmap getResourceBitmap(Context context, int resId) {
        String resName = AppUtil.getResourceNameById(context, resId);

        Bitmap bitmapImage = null;
        // �������Ƿ��и�Bitmapʵ���������ã�����У�����������ȡ�á�
        if (bitmapRefs.containsKey(resName)) {
            BitmapRef ref = bitmapRefs.get(resName);
            bitmapImage = ref.get();
        }
        // ���û�������ã����ߴ��������еõ���ʵ����null�����¹���һ��ʵ����
        // �����������½�ʵ����������
        if (bitmapImage == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inTempStorage = new byte[16 * 1024];

            int inSampleSize = 1;

            while (true) {
                try {
                    options.inSampleSize = inSampleSize;
                    bitmapImage = BitmapFactory.decodeResource(context.getResources(), resId, options);
                    this.addCacheBitmap(bitmapImage, resName);
                    break;
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                inSampleSize++;
            }
        }

        return bitmapImage;
    }

    /**
     * ��src�л��ͼƬ
     *
     * @param cla ͨ��Class�ļ������Դ�ļ�����·��
     * @param img ���class���ڰ��ļ���·�������·����
     * @return ����ͼƬ��Bitmap
     */
    public Bitmap getSrcBitmap(Class<?> cla, String img) {
        Bitmap bitmapImage = null;
        // �������Ƿ��и�Bitmapʵ���������ã�����У�����������ȡ�á�
        if (bitmapRefs.containsKey(img)) {
            BitmapRef ref = (BitmapRef) bitmapRefs.get(img);
            bitmapImage = (Bitmap) ref.get();
        }
        // ���û�������ã����ߴ��������еõ���ʵ����null�����¹���һ��ʵ����
        // �����������½�ʵ����������
        if (bitmapImage == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inTempStorage = new byte[16 * 1024];

            int inSampleSize = 1;

            while (true) {
                try {
                    options.inSampleSize = inSampleSize;
                    bitmapImage = BitmapFactory.decodeStream(cla.getResourceAsStream(img));
                    this.addCacheBitmap(bitmapImage, img);
                    break;
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                inSampleSize++;
            }
        }

        return bitmapImage;
    }

}