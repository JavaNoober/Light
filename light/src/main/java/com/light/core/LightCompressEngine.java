package com.light.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.compress.LightCompressCore;
import com.light.core.Utils.SimpleSizeCompute;
import com.light.core.callback.ICompressEngine;

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
			float widthScale = lightConfig.getMaxWidth() / width;
			float heightScale = lightConfig.getMaxHeight() / height;
			scale = Math.max(widthScale, heightScale);
		}
		if(scale > 1){
			Bitmap result = new MatrixUtil.Build().scale(scale, scale).bitmap(bitmap).build();
			return LightCompressCore.compressBitmap(result, outputPath);
		}else {
			return LightCompressCore.compressBitmap(bitmap, outputPath);
		}
	}

	public static Bitmap compressImageFromPath(String imgPath, int pixelW, int pixelH) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		BitmapFactory.decodeFile(imgPath,options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = SimpleSizeCompute.computeSampleSize(options , pixelH > pixelW ? pixelH : pixelW ,
				pixelW * pixelH );
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
		return bitmap;
	}
}
