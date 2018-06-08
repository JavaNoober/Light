package com.light.body;

import android.graphics.Bitmap;

import com.light.core.Utils.DisplayUtil;

import java.io.Serializable;

/**
 * Created by xiaoqi on 2017/11/21
 */

public class LightConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private int compressFileSize = -1;//单位kb

	private String outputRootDir;

	private int maxWidth;

	private int maxHeight;

	private int defaultQuality = 70;

	private boolean needIgnoreSize = false;

	private boolean autoRotation = false;

	private boolean autoRecycle = false;

    private Bitmap.Config bitmapConfig = Bitmap.Config.RGB_565;

    public static boolean isDebug = true;

	public LightConfig() {
		maxWidth = DisplayUtil.getScreenWidth(Light.getInstance().getContext());
		maxHeight = DisplayUtil.getScreenHeight(Light.getInstance().getContext());
	}

	public LightConfig setCompressFileSize(int compressFileSize) {
		this.compressFileSize = compressFileSize;
		return this;
	}

	public LightConfig setOutputRootDir(String outputRootDir) {
		this.outputRootDir = outputRootDir;
		return this;
	}

	public int getCompressFileSize() {
		return compressFileSize;
	}

	public String getOutputRootDir() {
		return outputRootDir;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public int getDefaultQuality() {
		return defaultQuality;
	}

	public void setDefaultQuality(int defaultQuality) {
		this.defaultQuality = defaultQuality;
	}

	public boolean isNeedIgnoreSize() {
		return needIgnoreSize;
	}

	public void setNeedIgnoreSize(boolean needIgnoreSize) {
		this.needIgnoreSize = needIgnoreSize;
	}

	public boolean isAutoRotation() {
		return autoRotation;
	}

	public void setAutoRotation(boolean autoRotation) {
		this.autoRotation = autoRotation;
	}

	public boolean isAutoRecycle() {
		return autoRecycle;
	}

	public void setAutoRecycle(boolean autoRecycle) {
		this.autoRecycle = autoRecycle;
	}

    public Bitmap.Config getBitmapConfig() {
        return bitmapConfig;
    }

    public void setConfig(Bitmap.Config bitmapConfig) {
        this.bitmapConfig = bitmapConfig;
    }
}
