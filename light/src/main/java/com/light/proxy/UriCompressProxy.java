package com.light.proxy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.light.body.CompressArgs;
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
	private ICompressProxy compressProxy = null;
	private OnCompressFinishListener compressFinishListener = null;
	private CompressArgs compressArgs;

	@Override
	public boolean compress(String outPath) {
		if(UriParser.isLocalFileUri(uri)){
			String filePath = UriParser.getPathFromFileUri(uri);
			compressProxy = new FileCompressProxy.Builder().compressArgs(compressArgs).path(filePath).build();
		}else if(UriParser.isLocalContentUri(uri)){
			String filePath = UriParser.getPathFromContentUri(uri);
			compressProxy = new FileCompressProxy.Builder().compressArgs(compressArgs).path(filePath).build();
		}else if(UriParser.isLocalAnroidResourceUri(uri)){
			try {
				InputStream input = Light.getInstance().getContext().getContentResolver().openInputStream(uri);
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				compressProxy = new BitmapCompressProxy.Builder().compressArgs(compressArgs).bitmap(bitmap).build();
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

	public void compressFromHttp(boolean openDiskCache, OnCompressFinishListener compressFinishListener) {
		if(UriParser.isNetworkUri(uri)) {
			if (Looper.getMainLooper() == Looper.myLooper()) {
				throw new RuntimeException("network uri can't compressed on UI Thread");
			}
			if(compressFinishListener != null){
				HttpDownLoader.downloadImage(openDiskCache, uri, compressFinishListener);
			}
		}
	}

	@Nullable
	@Override
	public Bitmap compress() {

		if(UriParser.isLocalFileUri(uri)){
			String filePath = UriParser.getPathFromFileUri(uri);
			compressProxy = new FileCompressProxy.Builder().compressArgs(compressArgs).path(filePath).build();
		}else if(UriParser.isLocalContentUri(uri)){
			String filePath = UriParser.getPathFromContentUri(uri);
			compressProxy = new FileCompressProxy.Builder().compressArgs(compressArgs).path(filePath).build();
		}else if(UriParser.isLocalAnroidResourceUri(uri)){
			try {
				InputStream input = Light.getInstance().getContext().getContentResolver().openInputStream(uri);
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				compressProxy = new BitmapCompressProxy.Builder().compressArgs(compressArgs).bitmap(bitmap).build();
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
		private CompressArgs compressArgs;
		private OnCompressFinishListener compressFinishListener;

		public Builder uri(Uri uri) {
			this.uri = uri;
			return this;
		}

		public Builder compressArgs(CompressArgs compressArgs) {
			this.compressArgs = compressArgs;
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
			proxy.uri = uri;
			if(compressArgs == null){
				proxy.compressArgs = CompressArgs.getDefaultArgs();
			}else {
				proxy.compressArgs = compressArgs;
			}
			proxy.compressFinishListener = compressFinishListener;
			return proxy;
		}
	}
}
