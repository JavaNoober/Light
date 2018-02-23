package com.light.core.Utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by xiaoqi on 2018/2/23
 */

public class DegreeHelper {

	/**
	 * @param imagePath
	 * @return degree of image
	 */
	public static int getBitmapDegree(String imagePath) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(imagePath);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
				case ExifInterface.ORIENTATION_TRANSPOSE:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
				case ExifInterface.ORIENTATION_FLIP_VERTICAL:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
				case ExifInterface.ORIENTATION_TRANSVERSE:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}
