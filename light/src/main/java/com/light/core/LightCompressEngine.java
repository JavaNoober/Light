package com.light.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.compress.LightCompressCore;
import com.light.core.Utils.MatrixUtil;
import com.light.core.Utils.SimpleSizeCompute;
import com.light.core.listener.ICompressEngine;
import com.light.core.listener.OnCompressFinishListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiaoqi on 2017/11/21.
 *
 */

public class LightCompressEngine implements ICompressEngine{


	private LightConfig lightConfig;

	public LightCompressEngine(LightConfig lightConfig){
		this.lightConfig = lightConfig;
	}

	@Nullable
	public Bitmap compress2Bitmap(Context context, Bitmap bitmap){
		int defaultQuality = lightConfig.getDefaultQuality();
		return compress2Bitmap(context, bitmap, defaultQuality);
	}

	@Nullable
	public Bitmap compress2Bitmap(Context context, Bitmap bitmap, int quality) {
		return compress2Bitmap(context, bitmap, quality, lightConfig.getMaxWidth(), lightConfig.getMaxHeight());
	}

	@Nullable
	public Bitmap compress2Bitmap(Context context, Bitmap bitmap, int width, int height){
		int defaultQuality = lightConfig.getDefaultQuality();
		return compress2Bitmap(context, bitmap, defaultQuality, width,  height);
	}

	@Nullable
	@Override
	public Bitmap compress2Bitmap(Context context, Bitmap bitmap, int quality, int width, int height) {
		String temp = context.getApplicationContext().getCacheDir().getAbsolutePath()
				+ UUID.randomUUID().toString() + ".jpg";
		Bitmap resultBitmap = null;
		if(compress2File(bitmap, temp, quality)){
			resultBitmap = compress2Bitmap(temp, width, height);
			new File(temp).delete();
		}
		return resultBitmap;
	}

	@Nullable
	@Override
	public Bitmap compress2Bitmap(String imagePath, int width, int height){
		return compress2Bitmap(imagePath, width, height);
	}

	public boolean compress2File(Bitmap bitmap, String outputPath){
		int defaultQuality = lightConfig.getDefaultQuality();
		return compress2File(bitmap, outputPath, defaultQuality, lightConfig.getMaxWidth(), lightConfig.getMaxHeight());
	}

	public boolean compress2File(Bitmap bitmap, String outputPath, int quality) {
		return compress2File(bitmap, outputPath, quality, lightConfig.getMaxWidth(), lightConfig.getMaxHeight());
	}

	public boolean compress2File(Bitmap bitmap, String outputPath, int width, int height){
		int defaultQuality = lightConfig.getDefaultQuality();
		return compress2File(bitmap, outputPath, defaultQuality, width, height);
	}

	//只会压缩文件大小，不会压缩bitmap大小
	@Override
	public boolean compress2File(Bitmap bitmap, String outputPath, int quality, int width, int height) {
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		float scale = MatrixUtil.getScale(width, height, bitmapWidth, bitmapHeight);
		if(scale < 1){
			Log.e("Light", "scale:"+ scale);
			Bitmap result = new MatrixUtil.Build().scale(scale, scale).bitmap(bitmap).build();
			return LightCompressCore.compressBitmap(result, quality, outputPath);
		}else {
			return LightCompressCore.compressBitmap(bitmap, quality, outputPath);
		}
	}

	public void compress(List<String> pathList, String outputPath, int fileSize,
	                     final OnCompressFinishListener listener) {
		Observable.fromIterable(pathList)
				.map(new Function<String, Boolean>() {
					@Override
					public Boolean apply(String path) throws Exception {
						File file = new File(path);
						if(file.isDirectory()){
							throw new RuntimeException("This path does not refer to a file");
						}
//						if(file.length() < ){
//
//						}
						return true;
					}
				}).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.ignoreElements()
				.subscribe(new Action() {
					@Override
					public void run() throws Exception {
						if(listener != null){
							listener.onFinish();
						}
					}
				});
	}

	public Bitmap compress2Bitmap(Bitmap bitmap, int width, int height) {
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

	public Bitmap compress2Bitmap(int resId, int width, int height) {

		long start = System.currentTimeMillis();
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeResource(Light.getInstance().getResources(),resId,options);
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inSampleSize = SimpleSizeCompute.computeSampleSize(options , Math.max(width, height),
					width * height);
			InputStream is = Light.getInstance().getResources().openRawResource(resId);
			return BitmapFactory.decodeStream(is,null,options);
		}finally {
			Log.e("MemorySize", "耗时："+ (System.currentTimeMillis() - start));
		}
	}

	public Bitmap compressImageFromPath(String path, int pixelW, int pixelH) {
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

	private InputStream bitmap2InputStream(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos);
		return new ByteArrayInputStream(baos.toByteArray());
	}

}