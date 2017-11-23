package com.light.compress;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by xiaoqi on 2017/11/21.
 */

public class LightCompressCore {

	static {
		System.loadLibrary("jpegbither");
		System.loadLibrary("light");
	}

	private final static int DEFAULT_QUALITY = 95;

	public static boolean compressBitmap(Bitmap bit, String fileName) {
		return compressBitmap(bit, DEFAULT_QUALITY, fileName);
	}

	public static boolean compressBitmap(Bitmap bit, int quality, String fileName) {
		long startTime = System.currentTimeMillis();
		Bitmap.Config config = bit.getConfig();
		if(config.equals(Bitmap.Config.ARGB_8888)){
			try {
				return saveBitmap(bit, quality, fileName);
			}finally {
				long time = System.currentTimeMillis() - startTime;
				Log.e("MemorySize", "耗时:" + time);
			}

		}else {
			Bitmap result = null;
			try {
				result = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(), Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(result);
				Rect rect = new Rect(0, 0, bit.getWidth(), bit.getHeight());
				canvas.drawBitmap(bit, null, rect, null);
				return saveBitmap(result, quality, fileName);
			}finally {
				long time = System.currentTimeMillis() - startTime;
				if(result != null){
					result.recycle();
					Log.e("MemorySize", "耗时:" + time);
				}
			}
		}
	}

	public static boolean saveBitmap(Bitmap bit, int quality,  int w, int h, String fileName) {
		return compressBitmap(bit, bit.getWidth(), bit.getHeight(), quality, fileName.getBytes(), true);
	}

	private static boolean saveBitmap(Bitmap bit, int quality, String fileName) {
		return compressBitmap(bit, bit.getWidth(), bit.getHeight(), quality, fileName.getBytes(), true);
	}

	/**
	 *
	 * @param bit bitmap图像
	 * @param w 要生成图片的宽度
	 * @param h 要生成图片的高度
	 * @param quality 压缩质量比例
	 * @param fileNameBytes 压缩后保存路径
	 * @param optimize 是否开启最优压缩
	 * @return true:压缩成功，false:压缩失败
	 */
	private static native boolean compressBitmap(Bitmap bit, int w, int h, int quality, byte[] fileNameBytes, boolean
			optimize);
}
