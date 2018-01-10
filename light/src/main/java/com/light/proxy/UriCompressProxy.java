package com.light.proxy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.light.body.Light;
import com.light.core.Utils.UriPraser;
import com.light.core.Utils.http.HttpHelper;
import com.light.core.listener.ICompressProxy;
import com.light.core.listener.OnCompressFinishListener;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by xiaoqi on 2017/11/25.
 */

public class UriCompressProxy implements ICompressProxy {

	private Uri uri;
	private int width;
	private int height;
	private int quality;
	private ICompressProxy compressProxy = null;
	private OnCompressFinishListener compressFinishListener = null;

	@Override
	public boolean compress(String outPath) {
		if(UriPraser.isLocalFileUri(uri)){
			String filePath = UriPraser.getPathFromFileUri(uri);
			compressProxy = new FileCompressProxy.Builder().width(width).height(height).quality(quality).path(filePath).build();
		}else if(UriPraser.isLocalContentUri(uri)){
			String filePath = UriPraser.getPathFromContentUri(uri);
			compressProxy = new FileCompressProxy.Builder().width(width).height(height).quality(quality).path(filePath).build();
		}else if(UriPraser.isLocalAnroidResourceUri(uri)){
			try {
				InputStream input = Light.getInstance().getContext().getContentResolver().openInputStream(uri);
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				compressProxy = new BitmapCompressProxy.Builder().quality(quality).width(width).height(height).bitmap(bitmap).build();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else if(UriPraser.isNetworkUri(uri)){
			throw new RuntimeException("network uri is not support yet");
//			if(Looper.getMainLooper() == Looper.myLooper()){
//				throw new RuntimeException("network uri can't compressed on UI Thread");
//			}
//			if(compressFinishListener != null){
//				HttpHelper.downloadImage(uri, compressFinishListener);
//			}
		}else {
			return false;
		}
		return compressProxy.compress(outPath);
	}


	//TODO
	private void compress(String outPath, OnCompressFinishListener compressFinishListener) {
		if(UriPraser.isNetworkUri(uri)) {
			if (Looper.getMainLooper() == Looper.myLooper()) {
				throw new RuntimeException("network uri can't compressed on UI Thread");
			}
			if (compressFinishListener != null) {
				HttpHelper.downloadImage(uri, compressFinishListener);
			}
		}
	}

	@Nullable
	@Override
	public Bitmap compress() {

		if(UriPraser.isLocalFileUri(uri)){
			String filePath = UriPraser.getPathFromFileUri(uri);
			compressProxy = new FileCompressProxy.Builder().width(width).height(height).quality(quality).path(filePath).build();
		}else if(UriPraser.isLocalContentUri(uri)){
			String filePath = UriPraser.getPathFromContentUri(uri);
			compressProxy = new FileCompressProxy.Builder().width(width).height(height).quality(quality).path(filePath).build();
		}else if(UriPraser.isLocalAnroidResourceUri(uri)){
			try {
				InputStream input = Light.getInstance().getContext().getContentResolver().openInputStream(uri);
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				compressProxy = new BitmapCompressProxy.Builder().width(width).height(height).bitmap(bitmap).build();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else if(UriPraser.isNetworkUri(uri)){
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
			proxy.compressFinishListener = compressFinishListener;
			return proxy;
		}
	}
}
