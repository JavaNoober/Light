package com.light.body;

import android.content.Context;

import com.light.core.SimpleCache;

import java.io.File;

/**
 * Created by xiaoqi on 2017/11/21.
 *
 * 1:设置文件最大值
 * 2:图片的旋转缩放
 * 3:异步并发压缩
 * 4:同步压缩
 */

public class Light {

	private static Light light;
	private LightConfig config;
	private Context context;
	private static SimpleCache cache;

	private Light(){

	}

	private Light(Context context){
		this.context = context;
	}

	public static Light getInstance(Context context){
		if(light == null){
			synchronized (Light.class){
				if (light == null){
					context = context.getApplicationContext();
					light = new Light(context);
					cache = SimpleCache.get(new File(context.getCacheDir(), "LightCache"));
				}
			}
		}
		return light;
	}

	public void setConfig(LightConfig config){
		this.config = config;
		cache.put("LightConfig", config);
	}

	public LightConfig getConfig(){
		if(config != null){
			return config;
		}else {
			config = (LightConfig) cache.getAsObject("LightConfig");
			return config;
		}
	}

	public void clearCache(){
		cache.clear();
	}


	public static boolean compressDrawableRes(){
		return true;
	}

	public static boolean compressDrawable(){
		return true;
	}

	public static boolean compressFile(){
		return true;
	}

	public static boolean compressBitmap(){
		return true;
	}

	public static boolean compress(){
		return true;
	}
}
