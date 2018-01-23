package com.light.core.listener;

import android.graphics.Bitmap;

/**
 * Created by xiaoqi on 2017/11/25
 */

public interface ICompressProxy {

	boolean compress(String outPath);

	Bitmap compress();
}
