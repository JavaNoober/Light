package com.light.body;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.light.core.listener.ICompressProxy;
import com.light.proxy.CompressFactory;

/**
 * Created by xiaoqi on 2018/1/10.
 */

public class CompressArgs {

	private int width;
	private int height;
	private int quality;

	private CompressArgs(){

	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getQuality() {
		return quality;
	}

	class Builder {
		private int width;
		private int height;
		private int quality;

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

		public CompressArgs build(){
			CompressArgs args = new CompressArgs();

//			if(count == 0){
//				throw new IllegalArgumentException("You must choose a type of image to be compressed.");
//			}else if(count > 1){
//				throw new IllegalArgumentException("Only one type of image to be compressed can be selected");
//			}
			args.width = width;
			args.height = height;
			args.quality= quality;
			return args;
		}
	}

}
