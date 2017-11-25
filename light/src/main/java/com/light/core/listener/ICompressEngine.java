package com.light.core.listener;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by xiaoqi on 2017/11/22.
 */

public interface ICompressEngine {

	Bitmap compress2Bitmap(Context context, Bitmap bitmap, int quality, int width, int height);

	Bitmap compress2Bitmap(String imagePath, int width, int height);

	Bitmap compress2Bitmap(int resId, int width, int height);

	boolean compress2File(Bitmap bitmap, String outputPath, int quality, int width, int height);

}
