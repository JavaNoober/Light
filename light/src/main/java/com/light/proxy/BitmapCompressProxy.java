package com.light.proxy;

import android.graphics.Bitmap;

import com.light.body.CompressArgs;
import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.LightCompressEngine;
import com.light.core.Utils.MatrixUtil;
import com.light.core.listener.ICompressEngine;
import com.light.core.listener.ICompressProxy;

/**
 * Created by xiaoqi on 2017/11/25
 */

public class BitmapCompressProxy implements ICompressProxy {

	private Bitmap bitmap;
	private LightConfig lightConfig;
	private ICompressEngine compressEngine;
	private CompressArgs compressArgs;

	private BitmapCompressProxy() {
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
        return compressEngine.compress2File(compress(), outPath, quality);
	}

	@Override
	public Bitmap compress() {
		int resultWidth;
		int resultHeight;
		if(!compressArgs.isIgnoreSize() && compressArgs.getWidth() > 0 && compressArgs.getHeight() >0){
			resultWidth = compressArgs.getWidth();
			resultHeight = compressArgs.getHeight();
		}else if(!compressArgs.isIgnoreSize()){
			resultWidth = Math.min(lightConfig.getMaxWidth(), bitmap.getWidth());
			resultHeight = Math.min(lightConfig.getMaxHeight(), bitmap.getHeight());
		}else {
			resultWidth = bitmap.getWidth();
			resultHeight = bitmap.getHeight();
		}
		Bitmap result = compressEngine.compress2Bitmap(bitmap, resultWidth, resultHeight);
		if(compressArgs.isAutoRecycle()){
			bitmap.recycle();
		}
		float scaleSize = MatrixUtil.getScale(resultWidth, resultHeight, result.getWidth(), result.getHeight());
		if(scaleSize < 1){
			return new MatrixUtil.Build().scale(scaleSize, scaleSize).bitmap(result).build();
		}
		return result;
	}

	public static class Builder {
		private Bitmap bitmap;
		private CompressArgs compressArgs;

		public Builder bitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
			return this;
		}

		public Builder compressArgs(CompressArgs compressArgs) {
			this.compressArgs = compressArgs;
			return this;
		}

		public BitmapCompressProxy build(){
			if(bitmap == null){
				throw new RuntimeException("bitmap is empty");
			}
			BitmapCompressProxy proxy = new BitmapCompressProxy();
			proxy.bitmap = bitmap;
			if(compressArgs == null){
				proxy.compressArgs = CompressArgs.getDefaultArgs();
			}else {
				proxy.compressArgs = compressArgs;
			}
			return proxy;
		}
	}
}
