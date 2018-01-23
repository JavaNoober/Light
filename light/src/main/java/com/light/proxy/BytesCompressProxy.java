package com.light.proxy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.LightCompressEngine;
import com.light.core.Utils.MatrixUtil;
import com.light.core.listener.ICompressEngine;
import com.light.core.listener.ICompressProxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xiaoqi on 2017/11/25
 */

public class BytesCompressProxy implements ICompressProxy {
	private byte[] bytes;
	private int width;
	private int height;
	private int quality;
	private LightConfig lightConfig;
	private ICompressEngine compressEngine;
	private boolean needIgnoreSize;

	private BytesCompressProxy() {
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
		if(!needIgnoreSize && width > 0 && height >0){
			resultWidth = width;
			resultHeight = height;
		}else {
			InputStream input = null;
			try {
				input = new ByteArrayInputStream(bytes);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(input, null, options);
				if(needIgnoreSize){
					resultWidth = options.outWidth;
					resultHeight = options.outHeight;
				}else {
					resultWidth = Math.min(lightConfig.getMaxWidth(), options.outWidth);
					resultHeight = Math.min(lightConfig.getMaxHeight(), options.outHeight);
				}

			}finally {
				if(input != null){
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		Bitmap result = compressEngine.compress2Bitmap(bytes, resultWidth, resultHeight);
		float scaleSize = MatrixUtil.getScale(resultWidth, resultHeight, result.getWidth(), result.getHeight());
		if(scaleSize < 1){
			return new MatrixUtil.Build().scale(scaleSize, scaleSize).bitmap(result).build();
		}
		return result;
	}

	public static class Builder {
		private byte[] bytes;
		private int width;
		private int height;
		private int quality;
		private boolean ignoreSize;

		public Builder bytes(byte[] bytes) {
			this.bytes = bytes;
			return this;
		}

		public Builder width(int width) {
			this.width = width;
			return this;
		}

		public Builder height(int height) {
			this.height = height;
			return this;
		}

		public Builder quality(int quality) {
			this.quality = quality;
			return this;
		}

		public Builder ignoreSize(boolean ignoreSize) {
			this.ignoreSize = ignoreSize;
			return this;
		}

		public BytesCompressProxy build(){
			if(bytes == null || bytes.length == 0){
				throw new RuntimeException("bytes is empty");
			}
			BytesCompressProxy proxy = new BytesCompressProxy();
			proxy.width = width;
			proxy.height = height;
			proxy.bytes = bytes;
			proxy.quality = quality;
			proxy.needIgnoreSize = ignoreSize;
			return proxy;
		}
	}
}
