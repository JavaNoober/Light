package com.light.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.light.core.Utils.SimpleSizeCompute;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by xiaoqi on 2017/11/24.
 */

public class BitmapUtil {

	/**
	 * 尺寸不能精准,但是内存占用会比较小
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap compressImageFromBitmap(Bitmap bitmap, int width, int height) {
		long start = System.currentTimeMillis();
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeStream(bitmap2InputStream(bitmap),null, options);
			options.inJustDecodeBounds = false;
			options.inSampleSize = SimpleSizeCompute.computeSampleSize(options , Math.max(width, height),
					width * height);
			Log.e("MemorySize", options.inSampleSize + "");
			return BitmapFactory.decodeStream(bitmap2InputStream(bitmap),null, options);
		}finally {
			Log.e("MemorySize", "耗时："+ (System.currentTimeMillis() - start));
		}

	}

	public static Bitmap compressImageFromPath(String path, int pixelW, int pixelH) {
		long start = System.currentTimeMillis();
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeFile(path,options);
			options.inJustDecodeBounds = false;
			options.inSampleSize = SimpleSizeCompute.computeSampleSize(options , pixelH > pixelW ? pixelH : pixelW ,
					pixelW * pixelH );
			Log.e("MemorySize", options.inSampleSize + "");
			return BitmapFactory.decodeFile(path, options);
		}finally {
			Log.e("MemorySize", "耗时："+ (System.currentTimeMillis() - start));
		}

	}

	private static InputStream bitmap2InputStream(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos);
		return new ByteArrayInputStream(baos.toByteArray());
	}
}
