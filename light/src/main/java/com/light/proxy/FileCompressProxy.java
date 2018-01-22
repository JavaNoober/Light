package com.light.proxy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.LightCompressEngine;
import com.light.core.Utils.MatrixUtil;
import com.light.core.listener.ICompressEngine;
import com.light.core.listener.ICompressProxy;

import java.io.File;

/**
 * Created by xiaoqi on 2017/11/25
 */

public class FileCompressProxy implements ICompressProxy {

	private String path;
	private int width;
	private int height;
	private int quality;
	private LightConfig lightConfig;
	private ICompressEngine compressEngine;

	public FileCompressProxy(){
		lightConfig = Light.getInstance().getConfig();
		compressEngine = new LightCompressEngine();
	}

	@Override
	public boolean compress(String outPath) {
		if(quality <= 0 || quality > 100){
			quality = lightConfig.getDefaultQuality();
		}
		if(outPath == null){
			outPath = lightConfig.getOutputRootDir();
		}
		return compressEngine.compress2File(compress(), outPath, quality);
	}

	@Override
	public Bitmap compress() {
		int resultWidth;
		int resultHeight;
		if(width > 0 && height >0){
			resultWidth = width;
			resultHeight = height;
		}else {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			resultWidth = Math.min(lightConfig.getMaxWidth(), options.outWidth);
			resultHeight = Math.min(lightConfig.getMaxHeight(), options.outHeight);
		}
		Bitmap result = compressEngine.compress2Bitmap(path, resultWidth, resultHeight);
		float scaleSize = MatrixUtil.getScale(resultWidth, resultHeight, result.getWidth(), result.getHeight());
		if(scaleSize < 1){
			return new MatrixUtil.Build().scale(scaleSize, scaleSize).bitmap(result).build();
		}
		return result;
	}

	public static class Builder {
		private String path;
		private int width;
		private int height;
		private int quality;

		public FileCompressProxy.Builder path(String path) {
			this.path = path;
			return this;
		}

		public FileCompressProxy.Builder width(int width) {
			this.width = width;
			return this;
		}

		public FileCompressProxy.Builder height(int height) {
			this.height = height;
			return this;
		}

		public FileCompressProxy.Builder quality(int quality) {
			this.quality = quality;
			return this;
		}

		public FileCompressProxy build(){
			if(path == null || !new File(path).exists()){
				throw new RuntimeException("image path is wrong");
			}
			FileCompressProxy proxy = new FileCompressProxy();
			proxy.width = width;
			proxy.height = height;
			proxy.path = path;
			proxy.quality = quality;
			return proxy;
		}
	}
}
