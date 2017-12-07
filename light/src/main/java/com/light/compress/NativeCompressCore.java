package com.light.compress;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xiaoqi on 2017/12/7.
 */

public class NativeCompressCore {

	public static boolean compress(Bitmap bitmap, String outfile, int quality, Bitmap.CompressFormat format){
		boolean isSuccess = false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outfile);
			isSuccess = bitmap.compress(format, quality, fos);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return isSuccess;
	}
}
