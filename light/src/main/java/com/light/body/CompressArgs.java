package com.light.body;


/**
 * Created by xiaoqi on 2018/1/10
 */

public class CompressArgs {

	private int width;
	private int height;
	private int quality;
	private boolean ignoreSize;
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

	public boolean isIgnoreSize() {
		return ignoreSize;
	}

	public static class Builder {
		private int width;
		private int height;
		private int quality;
		private boolean ignoreSize = Light.getInstance().getConfig().isNeedIgnoreSize();

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

		public Builder ignoreSize(boolean ignoreSize) {
			this.ignoreSize = ignoreSize;
			return this;
		}

		public CompressArgs build(){
			CompressArgs args = new CompressArgs();
			args.width = width;
			args.height = height;
			args.quality = quality;
			args.ignoreSize = ignoreSize;
			return args;
		}
	}

}
