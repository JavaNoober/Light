package com.light.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.light.body.Light;
import com.light.compress.LightCompressCore;
import com.light.compress.NativeCompressCore;
import com.light.core.Utils.L;
import com.light.core.Utils.SimpleSizeCompute;
import com.light.core.listener.ICompressEngine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by xiaoqi on 2017/11/21
 */

public class LightCompressEngine implements ICompressEngine{
	private final static String TAG = Light.TAG + "-LightCompressEngine";

	@Override
	public Bitmap compress2Bitmap(Bitmap bitmap, int width, int height) {
		String temp = Light.getInstance().getContext().getCacheDir().getAbsolutePath()
				+ UUID.randomUUID().toString() + ".jpg";
		Bitmap resultBitmap = null;
		if(LightCompressCore.compressBitmap(bitmap, 100, temp)){
			resultBitmap = compress2Bitmap(temp, width, height);
			new File(temp).delete();
		}
		return resultBitmap;
	}

	@Override
	public Bitmap compress2Bitmap(String imagePath, int width, int height){
		long start = System.currentTimeMillis();
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imagePath, options);
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inJustDecodeBounds = false;
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
				options.inPurgeable = true;
				options.inInputShareable = true;
			}
			options.inSampleSize = SimpleSizeCompute.computeSampleSize(options , Math.max(width, height),
					width * height);
			L.e(TAG, "sampleSize:"+ options.inSampleSize);
			return BitmapFactory.decodeFile(imagePath, options);
		}finally {
			L.e(TAG, "time:"+ (System.currentTimeMillis() - start));
		}
	}

	@Override
	public Bitmap compress2Bitmap(int resId, int width, int height) {
		long start = System.currentTimeMillis();
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeResource(Light.getInstance().getResources(),resId,options);
			options.inJustDecodeBounds = false;
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
				options.inPurgeable = true;
				options.inInputShareable = true;
			}
			options.inSampleSize = SimpleSizeCompute.computeSampleSize(options , Math.max(width, height),
					width * height);
			L.e("sampleSize:"+options.inSampleSize);
			InputStream is = Light.getInstance().getResources().openRawResource(resId);
			return BitmapFactory.decodeStream(is,null,options);
		}finally {
			L.e("MemorySize", "time:"+ (System.currentTimeMillis() - start));
		}
	}

	@Override
	public Bitmap compress2Bitmap(byte[] bytes, int width, int height) {
		long start = System.currentTimeMillis();
		InputStream input = null;
		try {
			input = new ByteArrayInputStream(bytes);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(input, null, options);
			try {
				input.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
				options.inPurgeable = true;
				options.inInputShareable = true;
			}
			options.inSampleSize = SimpleSizeCompute.computeSampleSize(options , Math.max(width, height),
					width * height);
			L.e("sampleSize:"+options.inSampleSize);
			input = new ByteArrayInputStream(bytes);
			return BitmapFactory.decodeStream(input, null, options);
		} finally {
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			L.e("MemorySize", "time:"+ (System.currentTimeMillis() - start));
		}
	}

	//只会压缩文件大小，不会压缩bitmap大小
	@Override
	public boolean compress2File(Bitmap bitmap, String outputPath, int quality) {
		if (bitmap.hasAlpha()) {
			L.e(TAG, "compress by NativeCompressCore");
			return NativeCompressCore.compress(bitmap, outputPath, quality, Bitmap.CompressFormat.JPEG);
		} else {
//			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
				L.e(TAG, "compress by LightCompressCore");
				return LightCompressCore.compressBitmap(bitmap, quality, outputPath);
//			} else {
//				L.e(TAG, "compress by NativeCompressCore");
//				return NativeCompressCore.compress(bitmap, outputPath, 100, Bitmap.CompressFormat.JPEG);
//			}
		}
	}

}