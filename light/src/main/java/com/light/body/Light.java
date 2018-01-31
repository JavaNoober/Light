package com.light.body;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Looper;
import android.widget.ImageView;

import com.light.core.Utils.ContextUtil;
import com.light.core.Utils.DisplayUtil;
import com.light.core.Utils.http.HttpDownLoader;
import com.light.core.listener.OnCompressFinishListener;
import com.light.proxy.CompressFactory;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiaoqi on 2017/11/21
 */

public class Light {
	public final static String TAG = "Light";

	private static Light light;
	private LightConfig config;
	private Context applicationContext;
	private Resources resources;

	private Light(){
		applicationContext = ContextUtil.get();
		resources = applicationContext.getResources();
	}

	public static Light getInstance(){
		if(light == null){
			synchronized (Light.class){
				if (light == null){
					light = new Light();
				}
			}
		}
		return light;
	}

	public void setConfig(LightConfig config){
		if(config == null){
			config = new LightConfig();
		}
		this.config = config;
		if(config.getMaxWidth() <= 0){
			config.setMaxWidth(DisplayUtil.getScreenWidth(applicationContext));
		}
		if(config.getMaxHeight() <= 0){
			config.setMaxHeight(DisplayUtil.getScreenHeight(applicationContext));
		}
	}

	public LightConfig getConfig(){
		if(config == null){
			config = new LightConfig();
		}
		return config;
	}

	public Context getContext() {
		return applicationContext;
	}

	public Resources getResources() {
		return resources;
	}

	public boolean compress(Uri uri, String outPath){
		return compressImage(uri, null, outPath);
	}

	public boolean compress(String path, String outPath){
		return compressImage(path, null, outPath);
	}

	public boolean compress(File file, String outPath){
		return compressImage(file.getAbsolutePath(), null, outPath);
	}

	public boolean compress(Bitmap bitmap, String outPath){
		return compressImage(bitmap, null, outPath);
	}

	public boolean compress(byte[] bytes, String outPath){
		return compressImage(bytes, null, outPath);
	}

	public boolean compress(int resId, String outPath){
		return compressImage(resId, null, outPath);
	}

	public boolean compress(Drawable drawable, String outPath){
		return compressImage(drawable, null, outPath);
	}

	public boolean compress(Uri uri, CompressArgs compressArgs, String outPath){
		return compressImage(uri, compressArgs, outPath);
	}

	public boolean compress(String path, CompressArgs compressArgs, String outPath){
		return compressImage(path, compressArgs, outPath);
	}

	public boolean compress(File file, CompressArgs compressArgs, String outPath){
		return compressImage(file.getAbsolutePath(), compressArgs, outPath);
	}

	public boolean compress(Bitmap bitmap, CompressArgs compressArgs, String outPath){
		return compressImage(bitmap, compressArgs, outPath);
	}

	public boolean compress(byte[] bytes, CompressArgs compressArgs, String outPath){
		return compressImage(bytes, compressArgs, outPath);
	}

	public boolean compress(int resId, CompressArgs compressArgs, String outPath){
		return compressImage(resId, compressArgs, outPath);
	}

	public boolean compress(Drawable drawable, CompressArgs compressArgs, String outPath){
		return compressImage(drawable, compressArgs, outPath);
	}

	boolean compressImage(Object imageSource, CompressArgs compressArgs, String outPath){
		if(outPath == null){
			throw new NullPointerException("OutPath is Null!");
		}
		if(imageSource == null){
			throw new NullPointerException("imageSource is Null!");
		}
		if(imageSource instanceof String){
			return new ArgumentsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.File, imageSource).compress(outPath);
		}else if(imageSource instanceof Uri){
			return new ArgumentsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.Uri, imageSource).compress(outPath);
		}else if(imageSource instanceof Bitmap){
			return new ArgumentsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.Bitmap, imageSource).compress(outPath);
		}else if(imageSource instanceof byte[]){
			return new ArgumentsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.Bytes, imageSource).compress(outPath);
		}else if(imageSource instanceof Drawable || imageSource instanceof Integer){
			return new ArgumentsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.Resource, imageSource).compress(outPath);
		}else {
			throw new RuntimeException("Only support image types are String, Uri, Bitmap, byte[], Drawable and " +
					"drawable resourceId");
		}
	}


	public Bitmap compress(Uri uri, CompressArgs compressArgs){
		return compressImage(uri, compressArgs);
	}

	public Bitmap compress(String path, CompressArgs compressArgs){
		return compressImage(path, compressArgs);
	}

	public Bitmap compress(File file, CompressArgs compressArgs){
		return compressImage(file.getAbsolutePath(), compressArgs);
	}

	public Bitmap compress(Bitmap bitmap, CompressArgs compressArgs){
		return compressImage(bitmap, compressArgs);
	}

	public Bitmap compress(byte[] bytes, CompressArgs compressArgs){
		return compressImage(bytes, compressArgs);
	}

	public Bitmap compress(int resId, CompressArgs compressArgs){
		return compressImage(resId, compressArgs);
	}

	public Bitmap compress(Drawable drawable, CompressArgs compressArgs){
		return compressImage(drawable, compressArgs);
	}

	public Bitmap compress(Uri uri){
		return compressImage(uri, null);
	}

	public Bitmap compress(String path){
		return compressImage(path, null);
	}

	public Bitmap compress(File file){
		return compressImage(file.getAbsolutePath(), null);
	}

	public Bitmap compress(Bitmap bitmap){
		return compressImage(bitmap, null);
	}

	public Bitmap compress(byte[] bytes){
		return compressImage(bytes, null);
	}

	public Bitmap compress(int resId){
		return compressImage(resId, null);
	}

	public Bitmap compress(Drawable drawable){
		return compressImage(drawable, null);
	}

	Bitmap compressImage(Object imageSource, CompressArgs compressArgs){
		if(imageSource == null){
			throw new NullPointerException("imageSource is Null!");
		}
		if(imageSource instanceof File){
			return new ArgumentsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.File, ((File) imageSource).getAbsolutePath()).compress();
		}else if(imageSource instanceof String){
			return new ArgumentsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.File, imageSource).compress();
		}else if(imageSource instanceof Uri){
			return new ArgumentsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.Uri, imageSource).compress();
		}else if(imageSource instanceof Bitmap){
			return new ArgumentsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.Bitmap, imageSource).compress();
		}else if(imageSource instanceof byte[]){
			return new ArgumentsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.Bytes, imageSource).compress();
		}else if(imageSource instanceof Drawable || imageSource instanceof Integer){
			return new ArgumentsAdapter(compressArgs).getCompressProxy(CompressFactory.Compress.Resource, imageSource).compress();
		}else {
			throw new RuntimeException("Only support image types are String, Uri, Bitmap, byte[], Drawable and " +
					"drawable resourceId");
		}
	}

	/**
	 * get image from internet
	 *
	 * run on sub thread
	 *
	 * @param url http
	 * @param listener
	 */
	public void compressFromHttp(String url, OnCompressFinishListener listener){
		compressFromHttp(url, null, listener);
	}

	public void compressFromHttp(String url, CompressArgs compressArgs, OnCompressFinishListener listener){
		new ArgumentsAdapter(compressArgs).getCompressProxy(url).compressFromHttp(listener);
	}

	/**
	 * get image from internet
	 *
	 * run on sub thread
	 *
	 * @param uri http
	 * @param listener
	 */
	public void compressFromHttp(Uri uri, OnCompressFinishListener listener){
		compressFromHttp(uri, null, listener);
	}

	public void compressFromHttp(Uri uri, CompressArgs compressArgs, OnCompressFinishListener listener){
		new ArgumentsAdapter(compressArgs).getCompressProxy(uri).compressFromHttp(listener);
	}


	public static void setImage(final ImageView imageView, Object imageSource){
		int[] size = DisplayUtil.getViewSize(imageView);
		CompressArgs args = new CompressArgs.Builder()
				.height(size[1])
				.width(size[0])
				.build();
		setImage(imageView, args, imageSource);
	}

	public static void setImage(final ImageView imageView, CompressArgs args, Object imageSource){
		if(Looper.myLooper() != Looper.getMainLooper()){
			throw new RuntimeException("Only the original thread that created a view hierarchy can touch its views.");
		}
		imageView.setImageBitmap(Light.getInstance().compressImage(imageSource, args));
	}

}
