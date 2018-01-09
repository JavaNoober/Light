package com.light.body;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.light.core.Utils.DisplayUtil;
import com.light.core.listener.ICompressProxy;
import com.light.proxy.FileCompressProxy;

/**
 * Created by xiaoqi on 2017/11/21.
 *
 */

public class Light {
	public final static String TAG = "Light";

	public final static int NO_RES_ID = -1;

	private static Light light;
	private LightConfig config;
	private Context applicationContext;
	private Resources resources;

	private Light(){

	}

	public static Light getInstance(){
		if(light == null){
			synchronized (Light.class){
				if (light == null){
					light = new Light();
				}
			}
		}
		return light;
	}

	public void setConfig(LightConfig config){
		this.config = config;
		if(config.getMaxWidth() <= 0){
			config.setMaxWidth(DisplayUtil.getScreenWidth(applicationContext));
		}
		if(config.getMaxHeight() <= 0){
			config.setMaxHeight(DisplayUtil.getScreenHeight(applicationContext));
		}

	}

	public LightConfig getConfig(){
		return config;
	}

	public Light init(Context context){
		this.applicationContext = context.getApplicationContext();
		this.resources = applicationContext.getResources();
		return this;
	}

	public Context getContext() {
		return applicationContext;
	}

	public Resources getResources() {
		return resources;
	}

	public boolean compress(String path){
		return true;
	}

	class Builder {
		private String path;
		private Bitmap bitmap;
		private byte[] bytes;
		private int resId = NO_RES_ID;
		private Drawable drawable;
		private Uri uri;
		private int width;
		private int height;
		private int quality;
		private ICompressProxy compressProxy;

		public Builder path(String path) {
			this.path = path;
//			this.compressProxy =
			return this;
		}

		public Builder bitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
			return this;
		}

		public Builder bytes(byte[] bytes) {
			this.bytes = bytes;
			return this;
		}

		public Builder resource(int resId) {
			this.resId = resId;
			return this;
		}

		public Builder drawable(Drawable drawable) {
			this.drawable = drawable;
			return this;
		}

		public Builder uri(Uri uri) {
			this.uri = uri;
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

		public void build(){
			Builder builder = new Builder();

			short count = 0;
			if(path != null){
				count ++;
			}
			if(bitmap != null){
				count ++;
			}
			if(bytes != null){
				count ++;
			}
			if(resId != NO_RES_ID){
				count ++;
			}
			if(drawable != null){
				count ++;
			}
			if(uri != null){
				count ++;
			}
			if(count == 0){
				throw new IllegalArgumentException("You must choose a type of image to be compressed.");
			}else if(count > 1){
				throw new IllegalArgumentException("Only one type of image to be compressed can be selected");
			}
			builder.path = path;
			builder.bitmap = bitmap;
			builder.bytes = bytes;
			builder.resId = resId;
			builder.drawable = drawable;
			builder.uri = uri;
			builder.width = width;
			builder.height = height;
			builder.quality= quality;
		}
	}
}
