package com.light.proxy;

import android.graphics.Bitmap;

import com.light.core.listener.ICompressProxy;

/**
 * Created by xiaoqi on 2017/11/25.
 */

public class UriCompressProxy implements ICompressProxy {

	private int resId;
	private int width;
	private int height;

	@Override
	public boolean compress(String outPath) {
		return false;
	}

	@Override
	public Bitmap compress() {
		return null;
	}

	public static class Build {
		private int resId;
		private int width;
		private int height;

		public Build uri(int resId) {
			this.resId = resId;
			return this;
		}

		public Build width(int width) {
			this.width = width;
			return this;
		}

		public Build height(int height) {
			this.height = height;
			return this;
		}

		public UriCompressProxy build(){
			if(resId == 0){
				throw new RuntimeException("resId is not exists");
			}
			UriCompressProxy proxy = new UriCompressProxy();
			proxy.width = width;
			proxy.height = height;
			proxy.resId = resId;
			return proxy;
		}
	}
}
