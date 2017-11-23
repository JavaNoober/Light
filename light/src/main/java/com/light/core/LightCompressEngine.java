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
			Bitmap result = new MatrixUtil.Build().scale(scale, scale).bitmap(bitmap).build();
			return LightCompressCore.compressBitmap(result, outputPath.replace("test", "test2"));
		}else {
			return LightCompressCore.compressBitmap(bitmap, outputPath);
		}
	}

}