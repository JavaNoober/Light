package com.light.core.callback;

import android.graphics.Bitmap;

/**
 * Created by xiaoqi on 2017/11/22.
 */

public interface ICompressEngine {

	Bitmap compress2Bitmap(Bitmap bitmap);

	boolean compress2File(Bitmap bitmap, String outputPath);
}
