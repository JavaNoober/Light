package com.light.proxy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.light.body.CompressArgs;
import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.LightCompressEngine;
import com.light.core.Utils.L;
import com.light.core.Utils.MatrixUtil;
import com.light.core.listener.ICompressEngine;
import com.light.core.listener.ICompressProxy;


/**
 * Created by xiaoqi on 2017/11/25
 */

public class ResourcesCompressProxy implements ICompressProxy {
	private final static String TAG = Light.TAG + "-ResourcesCompressProxy";
	private int resId;
	private Drawable drawable;
	private LightConfig lightConfig;
	private ICompressEngine compressEngine;
	private CompressArgs compressArgs;

	private ResourcesCompressProxy() {
		lightConfig = Light.getInstance().getConfig();
		compressEngine = new LightCompressEngine();
	}

	@Override
	public boolean compress(String outPath) {
		Bitmap result = compress();
		if(outPath == null){
			outPath = lightConfig.getOutputRootDir();
		}
		return compressEngine.compress2File(result, outPath, lightConfig.getDefaultQuality());
	}

	@Override
	public Bitmap compress() {
		int resultWidth;
		int resultHeight;
		if(!compressArgs.isIgnoreSize() && compressArgs.getWidth() > 0 && compressArgs.getHeight() >0){
			resultWidth = compressArgs.getWidth();
			resultHeight = compressArgs.getHeight();
		}else {
			if(drawable != null){
				if(compressArgs.isIgnoreSize()){
					resultWidth = drawable.getIntrinsicWidth();
					resultHeight = drawable.getIntrinsicHeight();
				}else {
					resultWidth = Math.min(lightConfig.getMaxWidth(), drawable.getIntrinsicWidth());
					resultHeight = Math.min(lightConfig.getMaxHeight(), drawable.getIntrinsicHeight());
				}
			}else {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeResource(Light.getInstance().getResources(), resId, options);
				if(compressArgs.isIgnoreSize()){
					resultWidth = options.outWidth;
					resultHeight = options.outHeight;
				}else {
					resultWidth = Math.min(lightConfig.getMaxWidth(), options.outWidth);
					resultHeight = Math.min(lightConfig.getMaxHeight(), options.outHeight);
				}

			}
		}
		L.i(TAG, "finalWidth:"+resultWidth+" finalHeight:"+resultHeight);
		Bitmap result = compressEngine.compress2Bitmap(resId, resultWidth, resultHeight);
		float scaleSize = MatrixUtil.getScale(resultWidth, resultHeight, result.getWidth(), result.getHeight());
		if(scaleSize < 1){
			return new MatrixUtil.Build().scale(scaleSize, scaleSize).bitmap(result).build();
		}
		return result;
	}

	public static class Builder {
		private int resId;
		private Drawable drawable;
		private CompressArgs compressArgs;

		public Builder resource(int resId) {
			this.resId = resId;
			return this;
		}

		public Builder drawable(Drawable drawable) {
			this.drawable = drawable;
			return this;
		}

		public Builder compressArgs(CompressArgs compressArgs) {
			this.compressArgs = compressArgs;
			return this;
		}

		public ResourcesCompressProxy build(){
			if(resId == 0 && drawable == null){
				throw new RuntimeException("resource is not exists");
			}
			ResourcesCompressProxy proxy = new ResourcesCompressProxy();
			proxy.resId = resId;
			proxy.drawable = drawable;
			if(compressArgs == null){
				proxy.compressArgs = CompressArgs.getDefaultArgs();
			}else {
				proxy.compressArgs = compressArgs;
			}
			return proxy;
		}
	}

}
