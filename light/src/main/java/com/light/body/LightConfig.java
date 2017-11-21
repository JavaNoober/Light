package com.light.body;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by xiaoqi on 2017/11/21.
 */

public class LightConfig implements Serializable{

	private static final long serialVersionUID = 1L;

	private int maxFileSize;

	private String outputRootDir;


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
}
