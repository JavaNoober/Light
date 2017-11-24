package com.light.core.callback;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by xiaoqi on 2017/11/22.
 */

public interface ICompressEngine {

	Bitmap compress2Bitmap(Context context, Bitmap bitmap);

	Bitmap compress2Bitmap(Context context, Bitmap bitmap, int quality);

	Bitmap compress2Bitmap(Context context, Bitmap bitmap, int width, int height);

	Bitmap compress2Bitmap(Context context, Bitmap bitmap, int quality, int width, int height);

	Bitmap compress2Bitmap(String imagePath, int width, int height);

	boolean compress2File(Bitmap bitmap, String outputPath);

	boolean compress2File(Bitmap bitmap, String outputPath, int quality);

	boolean compress2File(Bitmap bitmap, String outputPath, int width, int height);

	boolean compress2File(Bitmap bitmap, String outputPath, int quality, int width, int height);

	void compress(List<String> pathList, String outputPath, int fileSize, OnCompressFinishListener listener);
}
