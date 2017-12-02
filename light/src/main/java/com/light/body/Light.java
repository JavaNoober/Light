package com.light.body;

import android.content.Context;
import android.content.res.Resources;

import com.light.core.Utils.DisplayUtil;

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

	public Context getContext() {
		return applicationContext;
	}

	public Resources getResources() {
		return resources;
	}
}
