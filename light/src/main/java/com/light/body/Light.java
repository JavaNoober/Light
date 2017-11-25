package com.light.body;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by xiaoqi on 2017/11/21.
 *
 */

public class Light {

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

	public Light setConfig(LightConfig config){
		this.config = config;
		return this;
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
