package com.light.compress;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.light.core.Utils.L;

/**
 * Created by xiaoqi on 2017/11/21
 */

public class LightCompressCore {

	static {
		System.loadLibrary("jpegbither");
		System.loadLibrary("light");
	}

	private final static int DEFAULT_QUALITY = 85;

	public static boolean compressBitmap(Bitmap bit, String fileName) {
		return compressBitmap(bit, DEFAULT_QUALITY, fileName);
	}

	public static boolean compressBitmap(Bitmap bit, int quality, String fileName) {
		long startTime = System.currentTimeMillis();
		if (quality < 0 || quality > 100) {
			throw new IllegalArgumentException("quality must be 0..100");
		}
		Bitmap.Config config = bit.getConfig();
		if(config.equals(Bitmap.Config.ARGB_8888)){
			try {
				return saveBitmap(bit, quality, fileName);
			}finally {
				long time = System.currentTimeMillis() - startTime;
				L.e("Light", "JNI time:" + time);
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
					L.e("Light", "JNI time:" + time);
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
	 * @param bit bitmap
	 * @param w the width of image to generate
	 * @param h the height of image to generate
	 * @param quality the quality of compress
	 * @param fileNameBytes out path
	 * @param optimize Whether or not the optimal compression is opened
	 * @return true:success
	 */
	private static native boolean compressBitmap(Bitmap bit, int w, int h, int quality, byte[] fileNameBytes, boolean
			optimize);
}
