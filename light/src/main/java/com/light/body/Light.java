package com.light.body;

import android.content.Context;

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
	}

	public LightConfig getConfig(){
		return config;
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
		return context;
	}
}
