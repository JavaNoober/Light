package com.light.proxy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.light.body.CompressArgs;
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
	private LightConfig lightConfig;
	private ICompressEngine compressEngine;
	private CompressArgs compressArgs;

	private BytesCompressProxy() {
		lightConfig = Light.getInstance().getConfig();
		compressEngine = new LightCompressEngine();
	}

	@Override
	public boolean compress(String outPath) {
		int quality = compressArgs.getQuality();
		if(quality <= 0 || quality > 100){
			quality = lightConfig.getDefaultQuality();
		}
		if(outPath == null){
			outPath = lightConfig.getOutputRootDir();
		}
		Bitmap bitmap = compress();
		try {
			return compressEngine.compress2File(bitmap, outPath, quality);
		}finally {
			if(bitmap != null && !bitmap.isRecycled()){
				bitmap.recycle();
			}
		}
	}

	@Override
	public Bitmap compress() {
		int resultWidth;
		int resultHeight;
		if(!compressArgs.isIgnoreSize() && compressArgs.getWidth() > 0 && compressArgs.getHeight() >0){
			resultWidth = compressArgs.getWidth();
			resultHeight = compressArgs.getHeight();
		}else {
			InputStream input = null;
			try {
				input = new ByteArrayInputStream(bytes);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(input, null, options);
				if(compressArgs.isIgnoreSize()){
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
		if(compressArgs.isAutoRecycle()){
			bytes = null;
		}
		float scaleSize = MatrixUtil.getScale(resultWidth, resultHeight, result.getWidth(), result.getHeight());
		if(scaleSize < 1){
			return new MatrixUtil.Build().scale(scaleSize, scaleSize).bitmap(result).build();
		}
		return result;
	}

	public static class Builder {
		private byte[] bytes;
		private CompressArgs compressArgs;

		public Builder bytes(byte[] bytes) {
			this.bytes = bytes;
			return this;
		}

		public Builder compressArgs(CompressArgs compressArgs) {
			this.compressArgs = compressArgs;
			return this;
		}

		public BytesCompressProxy build(){
			if(bytes == null || bytes.length == 0){
				throw new RuntimeException("bytes is empty");
			}
			BytesCompressProxy proxy = new BytesCompressProxy();
			proxy.bytes = bytes;
			if(compressArgs == null){
				proxy.compressArgs = CompressArgs.getDefaultArgs();
			}else {
				proxy.compressArgs = compressArgs;
			}
			return proxy;
		}
	}
}
