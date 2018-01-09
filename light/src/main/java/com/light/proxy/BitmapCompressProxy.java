package com.light.proxy;

import android.graphics.Bitmap;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.LightCompressEngine;
import com.light.core.Utils.MatrixUtil;
import com.light.core.listener.ICompressEngine;
import com.light.core.listener.ICompressProxy;

/**
 * Created by xiaoqi on 2017/11/25.
 */

public class BitmapCompressProxy implements ICompressProxy {

	private Bitmap bitmap;
	private int width;
	private int height;
	private int quality;
	private LightConfig lightConfig;
	private ICompressEngine compressEngine;

	public BitmapCompressProxy() {
		lightConfig = Light.getInstance().getConfig();
		compressEngine = new LightCompressEngine();
	}

	@Override
	public boolean compress(String outPath) {
		if(quality <= 0 || quality > 100){
			quality = lightConfig.getDefaultQuality();
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
			resultWidth = Math.min(lightConfig.getMaxWidth(), bitmap.getWidth());
			resultHeight = Math.min(lightConfig.getMaxHeight(), bitmap.getHeight());
		}
		Bitmap result = compressEngine.compress2Bitmap(bitmap, resultWidth, resultHeight);
		float scaleSize = MatrixUtil.getScale(resultWidth, resultHeight, result.getWidth(), result.getHeight());
		if(scaleSize < 1){
			return new MatrixUtil.Build().scale(scaleSize, scaleSize).bitmap(result).build();
		}
		return result;
	}

	public static class Builder {
		private Bitmap bitmap;
		private int width;
		private int height;

		public BitmapCompressProxy.Builder bitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
			return this;
		}

		public BitmapCompressProxy.Builder width(int width) {
			this.width = width;
			return this;
		}

		public BitmapCompressProxy.Builder height(int height) {
			this.height = height;
			return this;
		}

		public BitmapCompressProxy.Builder quality(int quality) {
			this.height = quality;
			return this;
		}

		public BitmapCompressProxy build(){
			if(bitmap == null){
				throw new RuntimeException("bitmap is empty");
			}
			BitmapCompressProxy proxy = new BitmapCompressProxy();
			proxy.width = width;
			proxy.height = height;
			proxy.bitmap = bitmap;
			return proxy;
		}
	}
}
