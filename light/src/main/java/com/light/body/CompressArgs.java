package com.light.body;


/**
 * Created by xiaoqi on 2018/1/10
 */

public class CompressArgs {

	private int width;
	private int height;
	private int quality;
	private boolean ignoreSize;
	private boolean autoRotation;
	private boolean autoRecycle;

	private CompressArgs(){

	}

	public static CompressArgs getDefaultArgs(){
		LightConfig config = Light.getInstance().getConfig();
		return new Builder().width(config.getMaxWidth()).height(config.getMaxWidth())
				.quality(config.getDefaultQuality()).ignoreSize(config.isNeedIgnoreSize())
				.autoRecycle(config.isAutoRecycle()).autoRotation(config.isAutoRotation()).build();
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

	public boolean isAutoRotation() {
		return autoRotation;
	}

	public boolean isAutoRecycle() {
		return autoRecycle;
	}

	public static class Builder {
		private int width;
		private int height;
		private int quality;
		private boolean autoRotation = Light.getInstance().getConfig().isAutoRotation();
		private boolean ignoreSize = Light.getInstance().getConfig().isNeedIgnoreSize();
		private boolean autoRecycle = Light.getInstance().getConfig().isAutoRecycle();

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

		public Builder autoRotation(boolean autoRotation) {
			this.autoRotation = autoRotation;
			return this;
		}

		public Builder autoRecycle(boolean autoRecycle) {
			this.autoRecycle = autoRecycle;
			return this;
		}

		public CompressArgs build(){
			CompressArgs args = new CompressArgs();
			args.width = width;
			args.height = height;
			args.quality = quality;
			args.ignoreSize = ignoreSize;
			args.autoRotation = autoRotation;
			args.autoRecycle = autoRecycle;
			return args;
		}
	}

}
