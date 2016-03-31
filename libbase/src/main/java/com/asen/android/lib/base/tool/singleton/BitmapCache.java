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
 * Created by ASEN on 2016/3/31.
 * Bitmap缓存类（防止内存溢出），UI线程中操作
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public class BitmapCache {

    static private volatile BitmapCache cache;
    /**
     * 用于Chche内容的存储
     */
    private Hashtable<String, BtimapRef> bitmapRefs;
    /**
     * 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中）
     */
    private ReferenceQueue<Bitmap> q;

    /**
     * 继承SoftReference，使得每一个实例都具有可识别的标识。
     */
    class BtimapRef extends SoftReference<Bitmap> {
        private String _key = "";

        public BtimapRef(Bitmap bmp, ReferenceQueue<Bitmap> q, String key) {
            super(bmp, q);
            _key = key;
        }
    }

    private BitmapCache() {
        bitmapRefs = new Hashtable<String, BtimapRef>();
        q = new ReferenceQueue<Bitmap>();
    }

    /**
     * 取得缓存器实例
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
     * 以软引用的方式对一个Bitmap对象的实例进行引用并保存该引用
     */
    private void addCacheBitmap(Bitmap bmp, String key) {
        cleanCache();// 清除垃圾引用
        if (bmp == null)
            return;
        BtimapRef ref = new BtimapRef(bmp, q, key);
        bitmapRefs.put(key, ref);
    }

    private void cleanCache() {
        BtimapRef ref = null;
        while ((ref = (BtimapRef) q.poll()) != null) {
            bitmapRefs.remove(ref._key);
        }
    }

    // 清除Cache内的全部内容
    private void clearCache() {
        cleanCache();
        bitmapRefs.clear();
        System.gc();
        System.runFinalization();
    }

    public int getPictureDegree(String path) {
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

    private Bitmap rotaingBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }

    /**
     * 从sd卡中获得图片
     *
     * @param filename
     * @return
     */
    public Bitmap getSdBitmap(String filename) {
        if (TextUtils.isEmpty(filename)) {
            return null;
        }

        Bitmap bitmapImage = null;
        if (bitmapRefs.containsKey(filename)) {
            BtimapRef ref = (BtimapRef) bitmapRefs.get(filename);
            bitmapImage = (Bitmap) ref.get();
        }

        if (bitmapImage == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filename, options);
            int i = 0;
            Bitmap bitmap = null;

            while (true) {
                try {
                    if ((options.outWidth >> i <= 1024) && (options.outHeight >> i <= 1024)) {
                        options.inSampleSize = (int) Math.pow(2.0D, i);
                        options.inJustDecodeBounds = false;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        bitmap = BitmapFactory.decodeFile(filename, options);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i += 1;
            }

            int degree = getPictureDegree(filename);
            if (degree == 0) {
                bitmapImage = bitmap;
            } else {
                bitmapImage = rotaingBitmap(degree, bitmap);
            }
            this.addCacheBitmap(bitmapImage, filename);
        }

        return bitmapImage;
    }

    /**
     * 从Asset文件夹中打开图片
     *
     * @param context
     * @param assetfile
     * @return
     */
    public Bitmap getAssetBitmap(Context context, String assetfile) {

        Bitmap bitmapImage = null;
        // 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
        if (bitmapRefs.containsKey(assetfile)) {
            BtimapRef ref = (BtimapRef) bitmapRefs.get(assetfile);
            bitmapImage = (Bitmap) ref.get();
        }
        // 如果没有软引用，或者从软引用中得到的实例是null，重新构建一个实例，
        // 并保存对这个新建实例的软引用
        if (bitmapImage == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inTempStorage = new byte[16 * 1024];
            int inSampleSize = 1;

            BufferedInputStream buf = null;
            InputStream inputStream = null;
            try {
                inputStream = context.getAssets().open(assetfile);
                buf = new BufferedInputStream(inputStream);

                while (true) {
                    try {
                        options.inSampleSize = inSampleSize;
                        bitmapImage = BitmapFactory.decodeStream(buf, null, options);
                        this.addCacheBitmap(bitmapImage, assetfile);
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
     * 从R资源文件中拿到图片
     *
     * @param context
     * @param resId
     * @return
     */
    public Bitmap getResourceBitmap(Context context, int resId) {
        String resName = AppUtil.getResourceNameById(context, resId);

        Bitmap bitmapImage = null;
        // 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
        if (bitmapRefs.containsKey(resName)) {
            BtimapRef ref = (BtimapRef) bitmapRefs.get(resName);
            bitmapImage = (Bitmap) ref.get();
        }
        // 如果没有软引用，或者从软引用中得到的实例是null，重新构建一个实例，
        // 并保存对这个新建实例的软引用
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
     * 从src中获得图片
     *
     * @param cla 通过Class文件获得资源文件所在路径
     * @param img 相对class所在包文件的路径（相对路径）
     * @return
     */
    public Bitmap getSrcBitmap(Class<?> cla, String img) {
        Bitmap bitmapImage = null;
        // 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
        if (bitmapRefs.containsKey(img)) {
            BtimapRef ref = (BtimapRef) bitmapRefs.get(img);
            bitmapImage = (Bitmap) ref.get();
        }
        // 如果没有软引用，或者从软引用中得到的实例是null，重新构建一个实例，
        // 并保存对这个新建实例的软引用
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