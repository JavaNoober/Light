package com.xiaoqi.libjpegcompress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xiaoqi on 2016/12/27.
 */

public class ImageUtils {

	static {
		System.loadLibrary("jpegbither");
		System.loadLibrary("mylibjpeg");
	}

	private static int DEFAULT_QUALITY = 95;

	//libjpeg压缩
	public static String compressBitmap(Bitmap bit, String fileName) {
		return compressBitmap(bit, DEFAULT_QUALITY, fileName);
	}

	public static String compressBitmap(Bitmap bit, int quality, String fileName) {
		Bitmap result = null;
		try {
			result = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(result);
			Rect rect = new Rect(0, 0, bit.getWidth(), bit.getHeight());// original
			canvas.drawBitmap(bit, null, rect, null);
			return saveBitmap(result, quality, fileName);
		}finally {
			if(result != null){
				result.recycle();
			}
		}

	}

	private static String saveBitmap(Bitmap bit, int quality, String fileName) {
		return compressBitmap(bit, bit.getWidth(), bit.getHeight(), quality, fileName.getBytes(), true);
	}

	//返回值"1"是成功，"0"是失败
	private static native String compressBitmap(Bitmap bit, int w, int h, int quality, byte[] fileNameBytes,
	                                            boolean optimize);

	//图片压缩
	public static void compress(Bitmap bitmap , String fileName) {
		int quality = 95;
		int size = getBitmapSize(bitmap);
		if (size > 100 * 1024){
			Log.i("LIBJPEG","compress by libjpeg");
			if("1".equals(compressBitmap(bitmap,quality, fileName))){
				File file = new File(fileName);
				if(file.exists() && file.isFile()){
					long newFileSize = file.length();
					while (newFileSize > 100 * 1024 && quality > 0){
						Log.i("LIBJPEG","compress newFileSize");
						if( newFileSize > 1000 * 1024){
							quality -= 10;//每次都减少10
						}else {
							quality -= 2;//每次都减少10
						}
						compressBitmap(bitmap,quality, fileName);
						newFileSize = new File(fileName).length();
					}
				}
			}
		}
	}

	//原生的压缩方法
	public static void compressAndSave(Bitmap image , String fileName) {

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			if(getBitmapSize(image) < 100 * 1024){
				image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			}else {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
				int options = 100;
				while ( baos.toByteArray().length > 100 * 1024 && options > 0) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
					baos.reset();//重置baos即清空baos
					if( baos.toByteArray().length > 1000 * 1024){
						options -= 10;//每次都减少10
					}else {
						options -= 2;//每次都减少10
					}
					image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
				}
				ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
				Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStr
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				baos.reset();
			}
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getBitmapSize(Bitmap bitmap){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){    //API 19
			return bitmap.getAllocationByteCount();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
			return bitmap.getByteCount();
		}
		return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
	}

	public static boolean isSupportLibJpeg(){
		String[] abis;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			abis = Build.SUPPORTED_ABIS;
		} else {
			abis = new String[]{ Build.CPU_ABI, Build.CPU_ABI2 };
		}
		StringBuilder abiStr = new StringBuilder();
		for(String abi:abis){
			abiStr.append(abi).append(",");
		}
		Log.i("LIBJPEG",abiStr.toString());
		if(abiStr.toString().contains("x86") || abiStr.toString().contains("x86_64") || abiStr.toString().contains
				("mips") || abiStr.toString().contains("mips64") || abiStr.toString().contains("arm64-v8a")){
			return false;
		}else {
			return true;
		}
	}
}
