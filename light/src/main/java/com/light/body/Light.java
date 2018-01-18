package com.light.body;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.light.core.Utils.DisplayUtil;
import com.light.proxy.CompressFactory;

/**
 * Created by xiaoqi on 2017/11/21.
 *
 */

public class Light {
	public final static String TAG = "Light";

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
		if(config == null){
			config = new LightConfig();
		}
		this.config = config;
		if(config.getMaxWidth() <= 0){
			config.setMaxWidth(DisplayUtil.getScreenWidth(applicationContext));
		}
		if(config.getMaxHeight() <= 0){
			config.setMaxHeight(DisplayUtil.getScreenHeight(applicationContext));
		}
	}

	public LightConfig getConfig(){
		if(config == null){
			config = new LightConfig();
		}
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


	public static boolean compress(Uri uri, CompressArgs compressArgs, String outPath){
		return new ArguementsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.Uri, uri).compress(outPath);
	}

	public boolean compress(String path){
		return true;
	}

	public boolean compress(Bitmap bitmap){
		return true;
	}

	public boolean compress(byte[] bytes){
		return true;
	}

	public boolean compress(int resId){
		return true;
	}

	public boolean compress(Drawable drawable){
		return true;
	}

	public Bitmap compress(Object object, CompressArgs compressArgs){
		if(object instanceof String){
			return new ArguementsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.File, object).compress();
		}else if(object instanceof Uri){
			return new ArguementsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.Uri, object).compress();
		}
		return null;
	}
}
