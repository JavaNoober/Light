package com.light.core;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.light.compress.LightCompressCore;
import com.light.core.Utils.SimpleSizeCompute;
import com.light.core.callback.ICompressEngine;
import com.light.core.callback.OnCompressFinishListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by xiaoqi on 2017/11/23.
 */

public class NativeCompressEngine implements ICompressEngine {
	@Override
	public Bitmap compress2Bitmap(Context context, Bitmap bitmap) {
		return null;
	}

	@Override
	public Bitmap compress2Bitmap(Context context, Bitmap bitmap, int quality) {
		return null;
	}

	@Override
	public Bitmap compress2Bitmap(Context context, Bitmap bitmap, int width, int height) {
		return null;
	}

	@Override
	public Bitmap compress2Bitmap(Context context, Bitmap bitmap, int quality, int width, int height) {
		return null;
	}

	@Override
	public Bitmap compress2Bitmap(String imagePath, int width, int height) {
		return null;
	}

	@Override
	public boolean compress2File(Bitmap bitmap, String outputPath) {
		File file = new File(outputPath);
		try {
			FileOutputStream out = new FileOutputStream(file);
			BitmapUtil.compressImageFromBitmap(bitmap, 1024, 1024).compress(Bitmap.CompressFormat.WEBP, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean compress2File(Bitmap bitmap, String outputPath, int quality) {
		return false;
	}

	@Override
	public boolean compress2File(Bitmap bitmap, String outputPath, int width, int height) {
		return false;
	}

	@Override
	public boolean compress2File(Bitmap bitmap, String outputPath, int quality, int width, int height) {
		return false;
	}

	@Override
	public void compress(List<String> pathList, String outputPath, int fileSize, OnCompressFinishListener listener) {

	}

}
