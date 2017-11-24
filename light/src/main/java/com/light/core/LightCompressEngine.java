package com.light.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.compress.LightCompressCore;
import com.light.core.Utils.SimpleSizeCompute;
import com.light.core.callback.ICompressEngine;
import com.light.core.callback.OnCompressFinishListener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiaoqi on 2017/11/21.
 *
 */

public class LightCompressEngine implements ICompressEngine{


	private LightConfig lightConfig;

	public LightCompressEngine(Light light){
		lightConfig = light.getConfig();
	}

	@Nullable
	@Override
	public Bitmap compress2Bitmap(Context context, Bitmap bitmap){
		int defaultQuality = lightConfig.getDefaultQuality();
		return compress2Bitmap(context, bitmap, defaultQuality);
	}

	@Nullable
	@Override
	public Bitmap compress2Bitmap(Context context, Bitmap bitmap, int quality) {
		return compress2Bitmap(context, bitmap, quality, lightConfig.getMaxWidth(), lightConfig.getMaxHeight());
	}

	@Nullable
	@Override
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
			resultBitmap = BitmapUtil.compressImageFromPath(temp, width, height);
			new File(temp).delete();
		}
		return resultBitmap;
	}

	@Nullable
	@Override
	public Bitmap compress2Bitmap(String imagePath, int width, int height){
		return BitmapUtil.compressImageFromPath(imagePath, width, height);
	}

	@Override
	public boolean compress2File(Bitmap bitmap, String outputPath){
		int defaultQuality = lightConfig.getDefaultQuality();
		return compress2File(bitmap, outputPath, defaultQuality, lightConfig.getMaxWidth(), lightConfig.getMaxHeight());
	}

	@Override
	public boolean compress2File(Bitmap bitmap, String outputPath, int quality) {
		return compress2File(bitmap, outputPath, quality, lightConfig.getMaxWidth(), lightConfig.getMaxHeight());
	}

	@Override
	public boolean compress2File(Bitmap bitmap, String outputPath, int width, int height){
		int defaultQuality = lightConfig.getDefaultQuality();
		return compress2File(bitmap, outputPath, defaultQuality, width, height);
	}

	//只会压缩文件大小，不会压缩bitmap大小
	@Override
	public boolean compress2File(Bitmap bitmap, String outputPath, int quality, int width, int height) {
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		float scale = 1;
		if((width > 0 || height > 0) && (bitmapWidth > width || bitmapHeight > height)){
			float widthScale = (float) width / bitmapWidth;
			float heightScale = (float) height / bitmapHeight;
			scale = Math.min(widthScale, heightScale);
		}
		if(scale < 1){
			Log.e("Light", "scale:"+ scale);
			Bitmap result = new MatrixUtil.Build().scale(scale, scale).bitmap(bitmap).build();
			return LightCompressCore.compressBitmap(result, quality, outputPath);
		}else {
			return LightCompressCore.compressBitmap(bitmap, quality, outputPath);
		}
	}

	@Override
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

}