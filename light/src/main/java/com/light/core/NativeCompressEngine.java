package com.light.core;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.light.compress.LightCompressCore;
import com.light.core.Utils.SimpleSizeCompute;
import com.light.core.callback.ICompressEngine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by xiaoqi on 2017/11/23.
 */

public class NativeCompressEngine implements ICompressEngine {
	@Override
	public Bitmap compress2Bitmap(Context context, Bitmap bitmap) {
		return null;
	}

	@Override
	public boolean compress2File(Bitmap bitmap, String outputPath) {
		return LightCompressCore.compressBitmap(compressImageFromPath(bitmap, 1024, 1024), outputPath);
	}

	/**
	 * 速度比compress2File快，但是尺寸不能精准
	 * @param bitmap
	 * @param pixelW
	 * @param pixelH
	 * @return
	 */
	public Bitmap compressImageFromPath(Bitmap bitmap, int pixelW, int pixelH) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
//		BitmapFactory.decodeFile(imgPath,options);
		BitmapFactory.decodeStream(bitmap2InputStream(bitmap),null, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = SimpleSizeCompute.computeSampleSize(options , pixelH > pixelW ? pixelH : pixelW ,
				pixelW * pixelH );
		Log.e("MemorySize", options.inSampleSize + "");
		Bitmap bitmap2 = BitmapFactory.decodeStream(bitmap2InputStream(bitmap),null, options);
		return bitmap2;
	}

	private InputStream bitmap2InputStream(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return new ByteArrayInputStream(baos.toByteArray());
	}
}
