package com.light.body;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.File;

/**
 * Created by xiaoqi on 2018/3/9
 */

public class LightBuilder {
	private Object imageSource;

	private int width;
	private int height;
	private int quality;
	private boolean autoRotation = Light.getInstance().getConfig().isAutoRotation();
	private boolean ignoreSize = Light.getInstance().getConfig().isNeedIgnoreSize();
	private boolean autoRecycle = Light.getInstance().getConfig().isAutoRecycle();
	private CompressArgs args;

	public LightBuilder(Uri uri) {
		this.imageSource = uri;
	}

	public LightBuilder(File file) {
		this.imageSource = file;
	}

	public LightBuilder(byte[] bytes) {
		this.imageSource = bytes;
	}

	public LightBuilder(String path) {
		this.imageSource = path;
	}

	public LightBuilder(Bitmap bitmap) {
		this.imageSource = bitmap;
	}

	public LightBuilder(int resId) {
		this.imageSource = resId;
	}

	public LightBuilder(Drawable drawable) {
		this.imageSource = drawable;
	}

	public LightBuilder width(int width) {
		this.width = width;
		return this;
	}

	public LightBuilder height(int height) {
		this.height = height;
		return this;
	}

	public LightBuilder quality(int quality) {
		this.quality = quality;
		return this;
	}

	public LightBuilder ignoreSize(boolean ignoreSize) {
		this.ignoreSize = ignoreSize;
		return this;
	}

	public LightBuilder autoRotation(boolean autoRotation) {
		this.autoRotation = autoRotation;
		return this;
	}

	public LightBuilder autoRecycle(boolean autoRecycle) {
		this.autoRecycle = autoRecycle;
		return this;
	}

	public LightBuilder compressArgs(CompressArgs args) {
		this.args = args;
		return this;
	}

	public boolean compress(String outPath){
		CompressArgs compressArgs;
		if(args != null){
			compressArgs = args;
		}else {
			compressArgs = new CompressArgs.Builder().width(width).height(height)
					.quality(quality).ignoreSize(ignoreSize)
					.autoRecycle(autoRecycle).autoRotation(autoRotation).build();
		}
		if(imageSource == null){
			throw new NullPointerException("imageSource is Null!");
		}
		if(imageSource instanceof String){
			return Light.getInstance().compress((String) imageSource, compressArgs, outPath);
		}else if(imageSource instanceof Uri){
			return Light.getInstance().compress((Uri) imageSource, compressArgs, outPath);
		}else if(imageSource instanceof Bitmap){
			return Light.getInstance().compress((Bitmap) imageSource, compressArgs, outPath);
		}else if(imageSource instanceof byte[]){
			return Light.getInstance().compress((byte[]) imageSource, compressArgs, outPath);
		}else if(imageSource instanceof Drawable){
			return Light.getInstance().compress((Drawable) imageSource, compressArgs, outPath);
		}else if(imageSource instanceof Integer){
			return Light.getInstance().compress((Integer) imageSource, compressArgs, outPath);
		}else {
			throw new RuntimeException("Only support image types are String, Uri, Bitmap, byte[], Drawable and " +
					"drawable resourceId");
		}
	}

	public Bitmap compress(){
		CompressArgs compressArgs;
		if(args != null){
			compressArgs = args;
		}else {
			compressArgs = new CompressArgs.Builder().width(width).height(height)
					.quality(quality).ignoreSize(ignoreSize)
					.autoRecycle(autoRecycle).autoRotation(autoRotation).build();
		}
		if(imageSource == null){
			throw new NullPointerException("imageSource is Null!");
		}
		if(imageSource instanceof String){
			return Light.getInstance().compress((String) imageSource, compressArgs);
		}else if(imageSource instanceof Uri){
			return Light.getInstance().compress((Uri) imageSource, compressArgs);
		}else if(imageSource instanceof Bitmap){
			return Light.getInstance().compress((Bitmap) imageSource, compressArgs);
		}else if(imageSource instanceof byte[]){
			return Light.getInstance().compress((byte[]) imageSource, compressArgs);
		}else if(imageSource instanceof Drawable){
			return Light.getInstance().compress((Drawable) imageSource, compressArgs);
		}else if(imageSource instanceof Integer){
			return Light.getInstance().compress((Integer) imageSource, compressArgs);
		}else {
			throw new RuntimeException("Only support image types are String, Uri, Bitmap, byte[], Drawable and " +
					"drawable resourceId");
		}
	}
}
