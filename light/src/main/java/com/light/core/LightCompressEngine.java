package com.light.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.compress.LightCompressCore;
import com.light.core.Utils.L;
import com.light.core.Utils.MatrixUtil;
import com.light.core.Utils.SimpleSizeCompute;
import com.light.core.listener.ICompressEngine;
import com.light.core.listener.OnCompressFinishListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
	private final static String TAG = Light.TAG + "-LightCompressEngine";

	private LightConfig lightConfig;

	public LightCompressEngine(){
		this.lightConfig = Light.getInstance().getConfig();
	}

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
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeFile(imagePath, options);
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inSampleSize = SimpleSizeCompute.computeSampleSize(options , Math.max(width, height),
					width * height);
			L.e(TAG, "inSampleSize:"+options.inSampleSize);
			return BitmapFactory.decodeFile(imagePath, options);
		}finally {
			L.e(TAG, "耗时："+ (System.currentTimeMillis() - start));
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
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inSampleSize = SimpleSizeCompute.computeSampleSize(options , Math.max(width, height),
					width * height);
			L.e("samplesize:"+options.inSampleSize);
			InputStream is = Light.getInstance().getResources().openRawResource(resId);
			return BitmapFactory.decodeStream(is,null,options);
		}finally {
			L.e("MemorySize", "耗时："+ (System.currentTimeMillis() - start));
		}
	}

	//只会压缩文件大小，不会压缩bitmap大小
	@Override
	public boolean compress2File(Bitmap bitmap, String outputPath, int quality, int width, int height) {
//		int bitmapWidth = bitmap.getWidth();
//		int bitmapHeight = bitmap.getHeight();
//		float scale = MatrixUtil.getScale(width, height, bitmapWidth, bitmapHeight);
//		if(scale < 1){
//			L.e("Light", "scale:"+ scale);
//			Bitmap result = new MatrixUtil.Build().scale(scale, scale).bitmap(bitmap).build();
//			return LightCompressCore.compressBitmap(result, quality, outputPath);
//		}else {
//			return LightCompressCore.compressBitmap(bitmap, quality, outputPath);
//		}
		if (bitmap.hasAlpha()) {
			return compress(bitmap, outputPath, quality, Bitmap.CompressFormat.PNG);
		} else {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
				return LightCompressCore.compressBitmap(bitmap, quality, outputPath);
			} else {
				return compress(bitmap, outputPath, quality, Bitmap.CompressFormat.JPEG);
			}
		}

	}

	static boolean compress(Bitmap bitmap, String outfile, int quality, Bitmap.CompressFormat format){
		boolean isSuccess = false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outfile);
			isSuccess = bitmap.compress(format, quality, fos);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return isSuccess;
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
}