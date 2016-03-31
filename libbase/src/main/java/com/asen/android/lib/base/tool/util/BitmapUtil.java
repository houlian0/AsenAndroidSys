package com.asen.android.lib.base.tool.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by HL_SEN on 2015/9/21.
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class BitmapUtil {

	public static final String TAG = BitmapUtil.class.getSimpleName();

	/**
	 * 长宽 等比例缩放
	 * 
	 * @param bitmap
	 * @param zf
	 * @return
	 */
	public static Bitmap zoom(Bitmap bitmap, float zf) {
		return zoom(bitmap, zf, zf);
	}

	/**
	 * 长宽 缩放
	 * 
	 * @param bitmap
	 * @param wf
	 * @param hf
	 * @return
	 */
	public static Bitmap zoom(Bitmap bitmap, float wf, float hf) {
		if (bitmap == null) {
			return bitmap;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(wf, hf);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	/**
	 * 描述：缩放图片.不压缩的缩放
	 * 
	 * @param file
	 *            File对象
	 * @param newWidth
	 *            新图片的宽
	 * @param newHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap scaleImg(File file, int newWidth, int newHeight) {
		Bitmap resizeBmp = null;
		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("缩放图片的宽高设置不能小于0");
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), opts);

		int srcWidth = opts.outWidth;
		int srcHeight = opts.outHeight;

		int destWidth = srcWidth;
		int destHeight = srcHeight;

		float scale = 0;
		float scaleWidth = (float) newWidth / srcWidth;
		float scaleHeight = (float) newHeight / srcHeight;
		if (scaleWidth > scaleHeight) {
			scale = scaleWidth;
		} else {
			scale = scaleHeight;
		}
		if (scale != 0) {
			destWidth = (int) (destWidth / scale);
			destHeight = (int) (destHeight / scale);
		}

		opts.inPreferredConfig = Config.RGB_565;
		opts.inPurgeable = true;
		opts.inInputShareable = true;

		if (scale > 1) {
			opts.inSampleSize = (int) scale;
		} else {
			opts.inSampleSize = 1;
		}

		opts.outHeight = destHeight;
		opts.outWidth = destWidth;
		opts.inJustDecodeBounds = false;
		opts.inDither = false;
		resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
		if (resizeBmp != null && scale != 1) {
			resizeBmp = zoom(resizeBmp, scale);
		}
		return resizeBmp;
	}

	/**
	 * 描述：缩放图片,不压缩的缩放.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param newWidth
	 *            新图片的宽
	 * @param newHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap scaleImg(Bitmap bitmap, int newWidth, int newHeight) {

		Bitmap resizeBmp = null;
		if (bitmap == null) {
			return null;
		}
		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("缩放图片的宽高设置不能小于0");
		}
		int srcWidth = bitmap.getWidth();
		int srcHeight = bitmap.getHeight();

		if (srcWidth <= 0 || srcHeight <= 0) {
			return null;
		}
		float scale = 0;
		float scaleWidth = (float) newWidth / srcWidth;
		float scaleHeight = (float) newHeight / srcHeight;
		if (scaleWidth > scaleHeight) {
			scale = scaleWidth;
		} else {
			scale = scaleHeight;
		}
		if (bitmap != null && scale != 1) {
			resizeBmp = zoom(bitmap, scale);
		}
		return resizeBmp;
	}

	/**
	 * 描述：裁剪图片.
	 * 
	 * @param file
	 *            File对象
	 * @param newWidth
	 *            新图片的宽
	 * @param newHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap cutImg(File file, int newWidth, int newHeight) {
		Bitmap resizeBmp = null;
		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
		}

		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为true,decodeFile先不创建内存 只获取一些解码边界信息即图片大小信息
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), opts);
		// inSampleSize=2表示图片宽高都为原来的二分之一，即图片为原来的四分之一
		// 缩放可以将像素点打薄,裁剪前将图片缩放到目标图2倍大小
		int srcWidth = opts.outWidth; // 获取图片的原始宽度
		int srcHeight = opts.outHeight;// 获取图片原始高度
		int destWidth = 0;
		int destHeight = 0;

		int cutSrcWidth = newWidth * 2;
		int cutSrcHeight = newHeight * 2;

		// 缩放的比例,为了大图的缩小到2倍被裁剪的大小在裁剪
		double ratio = 0.0;
		// 任意一个不够长就不缩放
		if (srcWidth < cutSrcWidth || srcHeight < cutSrcHeight) {
			ratio = 0.0;
			destWidth = srcWidth;
			destHeight = srcHeight;
		} else if (srcWidth > cutSrcWidth) {
			ratio = (double) srcWidth / cutSrcWidth;
			destWidth = cutSrcWidth;
			destHeight = (int) (srcHeight / ratio);
		} else if (srcHeight > cutSrcHeight) {
			ratio = (double) srcHeight / cutSrcHeight;
			destHeight = cutSrcHeight;
			destWidth = (int) (srcWidth / ratio);
		}

		opts.inPreferredConfig = Config.RGB_565;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		if (ratio > 1) {
			opts.inSampleSize = (int) ratio;
		} else {
			opts.inSampleSize = 1;
		}
		opts.outHeight = destHeight;
		opts.outWidth = destWidth;
		opts.inJustDecodeBounds = false;
		opts.inDither = false;
		Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);
		if (bitmap != null) {
			resizeBmp = cutImg(bitmap, newWidth, newHeight);
		}
		return resizeBmp;
	}

	/**
	 * 描述：裁剪图片.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param newWidth
	 *            新图片的宽
	 * @param newHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap cutImg(Bitmap bitmap, int newWidth, int newHeight) {
		if (bitmap == null) {
			return null;
		}

		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
		}

		Bitmap resizeBmp = null;

		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();

			if (width <= 0 || height <= 0) {
				return null;
			}
			int offsetX = 0;
			int offsetY = 0;

			if (width > newWidth) {
				offsetX = (width - newWidth) / 2;
			} else {
				newWidth = width;
			}

			if (height > newHeight) {
				offsetY = (height - newHeight) / 2;
			} else {
				newHeight = height;
			}

			resizeBmp = Bitmap.createBitmap(bitmap, offsetX, offsetY, newWidth,
					newHeight);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resizeBmp != bitmap) {
				bitmap.recycle();
			}
		}
		return resizeBmp;
	}

	/******************************** 图片特效 ************************************/

	/**
	 * 图片设置圆角
	 * 
	 * @param bitmap
	 * @param roundPX
	 * @return
	 */
	public static Bitmap roundedCornerBitmap(Bitmap bitmap, float roundPX) {
		if (bitmap == null)
			return bitmap;
		Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(dstbmp);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return dstbmp;
	}

	/**
	 * 转换图片转换成圆形.
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return the bitmap
	 */
	public static Bitmap roundBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 描述：旋转Bitmap为一定的角度.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param degrees
	 *            the degrees
	 * @return the bitmap
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
		Bitmap mBitmap = null;
		try {
			Matrix m = new Matrix();
			m.setRotate(degrees % 360);
			mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmap;
	}

	/**
	 * 描述：旋转Bitmap为一定的角度并四周暗化处理.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param degrees
	 *            the degrees
	 * @return the bitmap
	 */
	public static Bitmap rotateBitmapTranslate(Bitmap bitmap, float degrees) {
		Bitmap mBitmap = null;
		int width;
		int height;
		try {
			Matrix matrix = new Matrix();
			if ((degrees / 90) % 2 != 0) {
				width = bitmap.getWidth();
				height = bitmap.getHeight();
			} else {
				width = bitmap.getHeight();
				height = bitmap.getWidth();
			}
			int cx = width / 2;
			int cy = height / 2;
			matrix.preTranslate(-cx, -cy);
			matrix.postRotate(degrees);
			matrix.postTranslate(cx, cy);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmap;
	}

	/**
	 * 转换图片转换成镜面效果的图片.
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return the bitmap
	 */
	public static Bitmap reflectionBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		try {
			int reflectionGap = 1;
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();

			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);

			Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
					width, height / 2, matrix, false);

			Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
					(height + height / 2), Config.ARGB_8888);

			Canvas canvas = new Canvas(bitmapWithReflection);
			canvas.drawBitmap(bitmap, 0, 0, null);
			Paint deafaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + reflectionGap,
					deafaultPaint);
			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
			Paint paint = new Paint();
			LinearGradient shader = new LinearGradient(0, bitmap.getHeight(),
					0, bitmapWithReflection.getHeight() + reflectionGap,
					0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			paint.setShader(shader);
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
					+ reflectionGap, paint);

			bitmap = bitmapWithReflection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 相反
	 * 
	 * @param bmp
	 * @param flag
	 *            0 or 1
	 * @return
	 */
	public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
		float[] floats = null;
		switch (flag) {
		case 0:
			floats = new float[] { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
			break;
		case 1:
			floats = new float[] { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
			break;
		}
		if (floats != null) {
			Matrix matrix = new Matrix();
			matrix.setValues(floats);
			return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), matrix, true);
		}

		return bmp;
	}

	/**
	 * 对 src 添加水印（watermark）
	 * 
	 * @param src
	 * @param watermark
	 * @param x
	 * @param y
	 * @return
	 */
	public static Bitmap doodleBitmap(Bitmap src, Bitmap watermark, int x, int y) {
		Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(newb);
		canvas.drawBitmap(src, x, y, null);
		canvas.drawBitmap(watermark,
				(src.getWidth() - watermark.getWidth()) / 2,
				(src.getHeight() - watermark.getHeight()) / 2, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		watermark.recycle();
		watermark = null;
		return newb;
	}

	/**
	 * src 上添加文字
	 * 
	 * @param src
	 * @param msg
	 * @param x
	 * @param y
	 * @return
	 */
	public static Bitmap doodleText(Bitmap src, String msg, int x, int y) {
		Canvas canvas = new Canvas(src);
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		canvas.drawText(msg, x, y, paint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return src;
	}

	/**
	 * 怀旧的 bitmap
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap oldBitmap(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.RGB_565);
		int pixColor = 0;
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				pixColor = pixels[width * i + k];
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
				newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
				newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
				int newColor = Color.argb(255, newR > 255 ? 255 : newR,
						newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
				pixels[width * i + k] = newColor;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 模糊bitmap
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap blurImage(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.RGB_565);
		int pixColor = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int newColor = 0;
		int[][] colors = new int[9][3];
		for (int i = 1, length = width - 1; i < length; i++) {
			for (int k = 1, len = height - 1; k < len; k++) {
				for (int m = 0; m < 9; m++) {
					int s = 0;
					int p = 0;
					switch (m) {
					case 0:
						s = i - 1;
						p = k - 1;
						break;
					case 1:
						s = i;
						p = k - 1;
						break;
					case 2:
						s = i + 1;
						p = k - 1;
						break;
					case 3:
						s = i + 1;
						p = k;
						break;
					case 4:
						s = i + 1;
						p = k + 1;
						break;
					case 5:
						s = i;
						p = k + 1;
						break;
					case 6:
						s = i - 1;
						p = k + 1;
						break;
					case 7:
						s = i - 1;
						p = k;
						break;
					case 8:
						s = i;
						p = k;
					}
					pixColor = bmp.getPixel(s, p);
					colors[m][0] = Color.red(pixColor);
					colors[m][1] = Color.green(pixColor);
					colors[m][2] = Color.blue(pixColor);
				}
				for (int m = 0; m < 9; m++) {
					newR += colors[m][0];
					newG += colors[m][1];
					newB += colors[m][2];
				}
				newR = (int) (newR / 9F);
				newG = (int) (newG / 9F);
				newB = (int) (newB / 9F);

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				newColor = Color.argb(255, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		return bitmap;
	}

	/**
	 * 模糊bitmap2
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap blurImageAmeliorate(Bitmap bmp) {
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixColor = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int delta = 16;
		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * gauss[idx]);
						newG = newG + (int) (pixG * gauss[idx]);
						newB = newB + (int) (pixB * gauss[idx]);
						idx++;
					}
				}
				newR /= delta;
				newG /= delta;
				newB /= delta;
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 素描
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap sketchBitmap(Bitmap bmp) {
		int pos, row, col, clr;
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] pixSrc = new int[width * height];
		int[] pixNvt = new int[width * height];
		bmp.getPixels(pixSrc, 0, width, 0, 0, width, height);
		for (row = 0; row < height; row++) {
			for (col = 0; col < width; col++) {
				pos = row * width + col;
				pixSrc[pos] = (Color.red(pixSrc[pos])
						+ Color.green(pixSrc[pos]) + Color.blue(pixSrc[pos])) / 3;
				pixNvt[pos] = 255 - pixSrc[pos];
			}
		}
		gaussGray(pixNvt, 5.0, 5.0, width, height);
		for (row = 0; row < height; row++) {
			for (col = 0; col < width; col++) {
				pos = row * width + col;

				clr = pixSrc[pos] << 8;
				clr /= 256 - pixNvt[pos];
				clr = Math.min(clr, 255);

				pixSrc[pos] = Color.rgb(clr, clr, clr);
			}
		}
		bmp.setPixels(pixSrc, 0, width, 0, 0, width, height);
		return bmp;
	}

	/**
	 * 浮雕
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap embossBitmap(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixColor = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;

		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				pos = i * width + k;
				pixColor = pixels[pos];
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);

				pixColor = pixels[pos + 1];
				newR = Color.red(pixColor) - pixR + 127;
				newG = Color.green(pixColor) - pixG + 127;
				newB = Color.blue(pixColor) - pixB + 127;

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[pos] = Color.argb(255, newR, newG, newB);
			}
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 锐化
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap sharpenImageAmeliorate(Bitmap bmp) {
		int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixColor = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;

		int idx = 0;
		float alpha = 0.3F;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + n) * width + k + m];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);
						newR = newR + (int) (pixR * laplacian[idx] * alpha);
						newG = newG + (int) (pixG * laplacian[idx] * alpha);
						newB = newB + (int) (pixB * laplacian[idx] * alpha);
						idx++;
					}
				}

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 电影?
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap filmBitmap(Bitmap bmp) {
		final int MAX_VALUE = 255;
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				pos = i * width + k;
				pixColor = pixels[pos];

				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);

				newR = MAX_VALUE - pixR;
				newG = MAX_VALUE - pixG;
				newB = MAX_VALUE - pixB;

				newR = Math.min(MAX_VALUE, Math.max(0, newR));
				newG = Math.min(MAX_VALUE, Math.max(0, newG));
				newB = Math.min(MAX_VALUE, Math.max(0, newB));

				pixels[pos] = Color.argb(MAX_VALUE, newR, newG, newB);
			}
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 阳光
	 * 
	 * @param bmp
	 * @param centerX
	 * @param centerY
	 * @return
	 */
	public static Bitmap sunshineBitmap(Bitmap bmp, int centerX, int centerY) {
		final int width = bmp.getWidth();
		final int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.RGB_565);
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;
		int radius = Math.min(centerX, centerY);

		final float strength = 150F;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				pos = i * width + k;
				pixColor = pixels[pos];

				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);

				newR = pixR;
				newG = pixG;
				newB = pixB;

				int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(
						centerX - k, 2));
				if (distance < radius * radius) {
					int result = (int) (strength * (1.0 - Math.sqrt(distance)
							/ radius));
					newR = pixR + result;
					newG = pixG + result;
					newB = pixB + result;
				}

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				pixels[pos] = Color.argb(255, newR, newG, newB);
			}
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/************************************* kernel ****************************************/

	private static int gaussGray(int[] psrc, double horz, double vert,
			int width, int height) {
		int[] dst, src;
		double[] n_p, n_m, d_p, d_m, bd_p, bd_m;
		double[] val_p, val_m;
		int i, j, t, k, row, col, terms;
		int[] initial_p, initial_m;
		double std_dev;
		int row_stride = width;
		int max_len = Math.max(width, height);
		int sp_p_idx, sp_m_idx, vp_idx, vm_idx;

		val_p = new double[max_len];
		val_m = new double[max_len];

		n_p = new double[5];
		n_m = new double[5];
		d_p = new double[5];
		d_m = new double[5];
		bd_p = new double[5];
		bd_m = new double[5];

		src = new int[max_len];
		dst = new int[max_len];

		initial_p = new int[4];
		initial_m = new int[4];

		if (vert > 0.0) {
			vert = Math.abs(vert) + 1.0;
			std_dev = Math.sqrt(-(vert * vert) / (2 * Math.log(1.0 / 255.0)));
			findConstants(n_p, n_m, d_p, d_m, bd_p, bd_m, std_dev);
			for (col = 0; col < width; col++) {
				for (k = 0; k < max_len; k++) {
					val_m[k] = val_p[k] = 0;
				}

				for (t = 0; t < height; t++) {
					src[t] = psrc[t * row_stride + col];
				}

				sp_p_idx = 0;
				sp_m_idx = height - 1;
				vp_idx = 0;
				vm_idx = height - 1;

				initial_p[0] = src[0];
				initial_m[0] = src[height - 1];

				for (row = 0; row < height; row++) {
					terms = (row < 4) ? row : 4;

					for (i = 0; i <= terms; i++) {
						val_p[vp_idx] += n_p[i] * src[sp_p_idx - i] - d_p[i]
								* val_p[vp_idx - i];
						val_m[vm_idx] += n_m[i] * src[sp_m_idx + i] - d_m[i]
								* val_m[vm_idx + i];
					}
					for (j = i; j <= 4; j++) {
						val_p[vp_idx] += (n_p[j] - bd_p[j]) * initial_p[0];
						val_m[vm_idx] += (n_m[j] - bd_m[j]) * initial_m[0];
					}

					sp_p_idx++;
					sp_m_idx--;
					vp_idx++;
					vm_idx--;
				}

				transferGaussPixels(val_p, val_m, dst, 1, height);

				for (t = 0; t < height; t++) {
					psrc[t * row_stride + col] = dst[t];
				}
			}
		}

		if (horz > 0.0) {
			horz = Math.abs(horz) + 1.0;
			if (horz != vert) {
				std_dev = Math.sqrt(-(horz * horz)
						/ (2 * Math.log(1.0 / 255.0)));
				findConstants(n_p, n_m, d_p, d_m, bd_p, bd_m, std_dev);
			}
			for (row = 0; row < height; row++) {
				for (k = 0; k < max_len; k++) {
					val_m[k] = val_p[k] = 0;
				}
				for (t = 0; t < width; t++) {
					src[t] = psrc[row * row_stride + t];
				}
				sp_p_idx = 0;
				sp_m_idx = width - 1;
				vp_idx = 0;
				vm_idx = width - 1;

				initial_p[0] = src[0];
				initial_m[0] = src[width - 1];

				for (col = 0; col < width; col++) {
					terms = (col < 4) ? col : 4;

					for (i = 0; i <= terms; i++) {
						val_p[vp_idx] += n_p[i] * src[sp_p_idx - i] - d_p[i]
								* val_p[vp_idx - i];
						val_m[vm_idx] += n_m[i] * src[sp_m_idx + i] - d_m[i]
								* val_m[vm_idx + i];
					}
					for (j = i; j <= 4; j++) {
						val_p[vp_idx] += (n_p[j] - bd_p[j]) * initial_p[0];
						val_m[vm_idx] += (n_m[j] - bd_m[j]) * initial_m[0];
					}

					sp_p_idx++;
					sp_m_idx--;
					vp_idx++;
					vm_idx--;
				}
				transferGaussPixels(val_p, val_m, dst, 1, width);
				for (t = 0; t < width; t++) {
					psrc[row * row_stride + t] = dst[t];
				}
			}
		}

		return 0;
	}

	private static void findConstants(double[] n_p, double[] n_m, double[] d_p,
			double[] d_m, double[] bd_p, double[] bd_m, double std_dev) {
		double div = Math.sqrt(2 * 3.141593) * std_dev;
		double x0 = -1.783 / std_dev;
		double x1 = -1.723 / std_dev;
		double x2 = 0.6318 / std_dev;
		double x3 = 1.997 / std_dev;
		double x4 = 1.6803 / div;
		double x5 = 3.735 / div;
		double x6 = -0.6803 / div;
		double x7 = -0.2598 / div;
		int i;

		n_p[0] = x4 + x6;
		n_p[1] = (Math.exp(x1)
				* (x7 * Math.sin(x3) - (x6 + 2 * x4) * Math.cos(x3)) + Math
				.exp(x0) * (x5 * Math.sin(x2) - (2 * x6 + x4) * Math.cos(x2)));
		n_p[2] = (2
				* Math.exp(x0 + x1)
				* ((x4 + x6) * Math.cos(x3) * Math.cos(x2) - x5 * Math.cos(x3)
						* Math.sin(x2) - x7 * Math.cos(x2) * Math.sin(x3)) + x6
				* Math.exp(2 * x0) + x4 * Math.exp(2 * x1));
		n_p[3] = (Math.exp(x1 + 2 * x0)
				* (x7 * Math.sin(x3) - x6 * Math.cos(x3)) + Math.exp(x0 + 2
				* x1)
				* (x5 * Math.sin(x2) - x4 * Math.cos(x2)));
		n_p[4] = 0.0;

		d_p[0] = 0.0;
		d_p[1] = -2 * Math.exp(x1) * Math.cos(x3) - 2 * Math.exp(x0)
				* Math.cos(x2);
		d_p[2] = 4 * Math.cos(x3) * Math.cos(x2) * Math.exp(x0 + x1)
				+ Math.exp(2 * x1) + Math.exp(2 * x0);
		d_p[3] = -2 * Math.cos(x2) * Math.exp(x0 + 2 * x1) - 2 * Math.cos(x3)
				* Math.exp(x1 + 2 * x0);
		d_p[4] = Math.exp(2 * x0 + 2 * x1);

		for (i = 0; i <= 4; i++) {
			d_m[i] = d_p[i];
		}

		n_m[0] = 0.0;
		for (i = 1; i <= 4; i++) {
			n_m[i] = n_p[i] - d_p[i] * n_p[0];
		}

		double sum_n_p, sum_n_m, sum_d;
		double a, b;

		sum_n_p = 0.0;
		sum_n_m = 0.0;
		sum_d = 0.0;

		for (i = 0; i <= 4; i++) {
			sum_n_p += n_p[i];
			sum_n_m += n_m[i];
			sum_d += d_p[i];
		}

		a = sum_n_p / (1.0 + sum_d);
		b = sum_n_m / (1.0 + sum_d);

		for (i = 0; i <= 4; i++) {
			bd_p[i] = d_p[i] * a;
			bd_m[i] = d_m[i] * b;
		}
	}

	private static void transferGaussPixels(double[] src1, double[] src2,
			int[] dest, int bytes, int width) {
		int i, j, k, b;
		int bend = bytes * width;
		double sum;

		i = j = k = 0;
		for (b = 0; b < bend; b++) {
			sum = src1[i++] + src2[j++];

			if (sum > 255)
				sum = 255;
			else if (sum < 0)
				sum = 0;

			dest[k++] = (int) sum;
		}
	}

	/********************************** 其他图形 **********************************/
	private static final float RADIUS_FACTOR = 8.0f;
	private static final int TRIANGLE_WIDTH = 120;
	private static final int TRIANGLE_HEIGHT = 100;
	private static final int TRIANGLE_OFFSET = 300;

	/**
	 * 将图片处理成气泡
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap processImage(Bitmap bitmap) {
		Bitmap bmp = null;

		bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Config.ARGB_8888);
		BitmapShader shader = new BitmapShader(bitmap,
				TileMode.CLAMP, TileMode.CLAMP);

		float radius = Math.min(bitmap.getWidth(), bitmap.getHeight())
				/ RADIUS_FACTOR;
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(shader);

		RectF rect = new RectF(TRIANGLE_WIDTH, 0, bitmap.getWidth(),
				bitmap.getHeight());
		canvas.drawRoundRect(rect, radius, radius, paint);

		Path triangle = new Path();
		triangle.moveTo(0, TRIANGLE_OFFSET);
		triangle.lineTo(TRIANGLE_WIDTH, TRIANGLE_OFFSET - (TRIANGLE_HEIGHT / 2));
		triangle.lineTo(TRIANGLE_WIDTH, TRIANGLE_OFFSET + (TRIANGLE_HEIGHT / 2));
		triangle.close();
		canvas.drawPath(triangle, paint);

		return bmp;
	}

	/*********************************** save bitmap ******************************************/

	public static boolean saveBitmap(Bitmap bmp, String path, String name,
			int quality) {
		boolean result = false;

		File file = new File(path);
		if (!file.exists())
			file.mkdirs();

		FileOutputStream fileOutputStream = null;
		try {
			path = file.getPath() + "/" + name;
			fileOutputStream = new FileOutputStream(path + File.separator
					+ name);
			bmp.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.flush();
					fileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/************************************ release **************************************/

	/**
	 * 释放Bitmap对象.
	 * 
	 * @param bitmap
	 *            要释放的Bitmap
	 */
	public static void releaseBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			try {
				if (!bitmap.isRecycled()) {
					bitmap.recycle();
				}
			} catch (Exception e) {
			}
			bitmap = null;
		}
	}

	/**
	 * 释放Bitmap数组.
	 * 
	 * @param bitmaps
	 *            要释放的Bitmap数组
	 */
	public static void releaseBitmapArray(Bitmap[] bitmaps) {
		if (bitmaps != null) {
			try {
				for (Bitmap bitmap : bitmaps) {
					if (bitmap != null && !bitmap.isRecycled()) {
						bitmap.recycle();
					}
				}
			} catch (Exception e) {
			}
		}
	}

}
