package com.light.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.compress.LightCompressCore;
import com.light.core.Utils.SimpleSizeCompute;
import com.light.core.callback.ICompressEngine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by xiaoqi on 2017/11/21.
 *
 * 只会压缩文件大小，不会压缩bitmap大小
 */

public class LightCompressEngine implements ICompressEngine{


	private LightConfig lightConfig;


	public LightCompressEngine(Light light){
		lightConfig = light.getConfig();
	}

	public Bitmap compress2Bitmap(Bitmap bitmap){
		return null;
	}


	public boolean compress2File(Bitmap bitmap, String outputPath){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scale = 1;
		if(width > lightConfig.getMaxWidth() || height > lightConfig.getMaxHeight()){
			float widthScale = (float) lightConfig.getMaxWidth() / width;
			float heightScale = (float) lightConfig.getMaxHeight() / height;
			scale = Math.min(widthScale, heightScale);
		}
		if(scale < 1){
			Log.e("scale", scale+"");
//			Bitmap result = new MatrixUtil.Build().scale(scale, scale).bitmap(bitmap).build();
			return LightCompressCore.compressBitmap(compressImageFromPath(bitmap, 1024, 1024), outputPath);
		}else {
			return LightCompressCore.compressBitmap(bitmap, outputPath);
		}
	}

	public Bitmap compressImageFromPath(Bitmap bitmap, int pixelW, int pixelH) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
//		BitmapFactory.decodeFile(imgPath,options);
		BitmapFactory.decodeStream(bitmap2InputSream(bitmap),null, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = SimpleSizeCompute.computeSampleSize(options , pixelH > pixelW ? pixelH : pixelW ,
				pixelW * pixelH );
		Bitmap bitmap2 = BitmapFactory.decodeStream(bitmap2InputSream(bitmap),null, options);
		return bitmap2;
	}

	private InputStream bitmap2InputSream(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		return  isBm;
	}
}