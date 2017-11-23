package com.light.core;

import android.content.Context;
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
import java.util.UUID;

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

	public Bitmap compress2Bitmap(Context context, Bitmap bitmap){
		String temp = context.getApplicationContext().getCacheDir().getAbsolutePath()
				+ UUID.randomUUID().toString() + ".jpg";
		Bitmap resultBitmap;
		if(compress2File(bitmap, temp)){
			resultBitmap = BitmapFactory.decodeFile(temp);

		}
		return null;
	}


	public boolean compress2File(Bitmap bitmap, String outputPath){
		return compress2File(bitmap, outputPath, lightConfig.getMaxWidth(), lightConfig.getMaxHeight());
	}

	public boolean compress2File(Bitmap bitmap, String outputPath, int width, int height){
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		float scale = 1;
		if(bitmapWidth > width || bitmapHeight > height){
			float widthScale = (float) width / bitmapWidth;
			float heightScale = (float) height / bitmapHeight;
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