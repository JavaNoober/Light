package com.light.proxy;

import android.graphics.Bitmap;

import com.light.core.listener.ICompressProxy;

/**
 * Created by xiaoqi on 2017/11/25.
 */

public class BitmapCompressProxy implements ICompressProxy {
	@Override
	public boolean compress(String outPath) {
		return false;
	}

	@Override
	public Bitmap compress() {
		return null;
	}
}
