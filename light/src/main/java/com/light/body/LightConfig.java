package com.light.body;


import java.io.Serializable;

/**
 * Created by xiaoqi on 2017/11/21.
 */

public class LightConfig implements Serializable{

	private static final long serialVersionUID = 1L;

	private int maxFileSize = -1;

	private String outputRootDir;

	private int maxWidth;

	private int maxHeight;

	public LightConfig setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
		return this;
	}

	public LightConfig setOutputRootDir(String outputRootDir) {
		this.outputRootDir = outputRootDir;
		return this;
	}

	public int getMaxFileSize() {
		return maxFileSize;
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
}
