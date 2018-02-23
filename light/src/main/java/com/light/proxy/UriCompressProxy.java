package com.light.proxy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.light.body.Light;
import com.light.core.Utils.UriParser;
import com.light.core.Utils.http.HttpDownLoader;
import com.light.core.listener.ICompressProxy;
import com.light.core.listener.OnCompressFinishListener;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by xiaoqi on 2017/11/25
 */

public class UriCompressProxy implements ICompressProxy {

	private Uri uri;
	private int width;
	private int height;
	private int quality;
	private ICompressProxy compressProxy = null;
	private OnCompressFinishListener compressFinishListener = null;
	private boolean needIgnoreSize;
	private boolean autoRotation;

	@Override
	public boolean compress(String outPath) {
		if(UriParser.isLocalFileUri(uri)){
			String filePath = UriParser.getPathFromFileUri(uri);
			compressProxy = new FileCompressProxy.Builder().width(width).height(height).quality(quality)
					.autoRotation(autoRotation).path(filePath).build();
		}else if(UriParser.isLocalContentUri(uri)){
			String filePath = UriParser.getPathFromContentUri(uri);
			compressProxy = new FileCompressProxy.Builder().width(width).height(height).quality(quality)
					.autoRotation(autoRotation).path(filePath).build();
		}else if(UriParser.isLocalAnroidResourceUri(uri)){
			try {
				InputStream input = Light.getInstance().getContext().getContentResolver().openInputStream(uri);
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				compressProxy = new BitmapCompressProxy.Builder().quality(quality).width(width).height(height).bitmap(bitmap).build();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else if(UriParser.isNetworkUri(uri)){
			throw new RuntimeException("please use method of 'Light.getInstance().compressFromHttp()'");

		}else {
			return false;
		}
		return compressProxy.compress(outPath);
	}

	public void compressFromHttp(OnCompressFinishListener compressFinishListener) {
		if(UriParser.isNetworkUri(uri)) {
			if (Looper.getMainLooper() == Looper.myLooper()) {
				throw new RuntimeException("network uri can't compressed on UI Thread");
			}
			if(compressFinishListener != null){
				HttpDownLoader.downloadImage(uri, compressFinishListener);
			}
		}
	}

	@Nullable
	@Override
	public Bitmap compress() {

		if(UriParser.isLocalFileUri(uri)){
			String filePath = UriParser.getPathFromFileUri(uri);
			compressProxy = new FileCompressProxy.Builder().width(width).height(height).quality(quality).ignoreSize(needIgnoreSize)
					.path(filePath).autoRotation(autoRotation).build();
		}else if(UriParser.isLocalContentUri(uri)){
			String filePath = UriParser.getPathFromContentUri(uri);
			compressProxy = new FileCompressProxy.Builder().width(width).height(height).quality(quality).ignoreSize(needIgnoreSize)
					.path(filePath).autoRotation(autoRotation).build();
		}else if(UriParser.isLocalAnroidResourceUri(uri)){
			try {
				InputStream input = Light.getInstance().getContext().getContentResolver().openInputStream(uri);
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				compressProxy = new BitmapCompressProxy.Builder().width(width).height(height).bitmap(bitmap)
						.ignoreSize(needIgnoreSize).build();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else if(UriParser.isNetworkUri(uri)){
			if(Looper.getMainLooper() == Looper.myLooper()){
				throw new RuntimeException("network uri can't compressed on UI Thread");
			}
//			compressProxy = new FileCompressProxy.Builder().width(width).height(height).path().build();
		}else {
			return null;
		}

		return compressProxy.compress();
	}

	public static class Builder {
		private Uri uri;
		private int width;
		private int height;
		private int quality;
		private boolean ignoreSize = Light.getInstance().getConfig().isNeedIgnoreSize();
		private boolean autoRotation = Light.getInstance().getConfig().isAutoRotation();
		private OnCompressFinishListener compressFinishListener;

		public Builder uri(Uri uri) {
			this.uri = uri;
			return this;
		}

		public Builder width(int width) {
			this.width = width;
			return this;
		}

		public Builder height(int height) {
			this.height = height;
			return this;
		}

		public Builder quality(int quality) {
			this.quality = quality;
			return this;
		}

		public Builder ignoreSize(boolean ignoreSize) {
			this.ignoreSize = ignoreSize;
			return this;
		}

		public Builder autoRotation(boolean autoRotation) {
			this.autoRotation = autoRotation;
			return this;
		}

		public Builder compressListener(OnCompressFinishListener compressFinishListener) {
			this.compressFinishListener = compressFinishListener;
			return this;
		}

		public UriCompressProxy build(){
			if(uri == null){
				throw new RuntimeException("resId is not exists");
			}
			UriCompressProxy proxy = new UriCompressProxy();
			proxy.width = width;
			proxy.height = height;
			proxy.uri = uri;
			proxy.quality = quality;
			proxy.needIgnoreSize = ignoreSize;
			proxy.autoRotation = autoRotation;
			proxy.compressFinishListener = compressFinishListener;
			return proxy;
		}
	}
}
