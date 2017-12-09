package com.light.proxy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;

import com.light.body.Light;
import com.light.core.Utils.UriPraser;
import com.light.core.listener.ICompressProxy;

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

	@Override
	public boolean compress(String outPath) {
		if(compressProxy != null){
			return compressProxy.compress(outPath);
		}else {
			return false;
		}
	}

	@Override
	public Bitmap compress() {

		if(UriPraser.isLocalFileUri(uri)){
			String filePath = UriPraser.getPathFromFileUri(uri);
			compressProxy = new FileCompressProxy.Build().width(width).height(height).quality(quality).path(filePath).build();
		}else if(UriPraser.isLocalContentUri(uri)){
			String filePath = UriPraser.getPathFromContentUri(uri);
			compressProxy = new FileCompressProxy.Build().width(width).height(height).quality(quality).path(filePath).build();
		}else if(UriPraser.isLocalAnroidResourceUri(uri)){
			try {
				InputStream input = Light.getInstance().getContext().getContentResolver().openInputStream(uri);
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				compressProxy = new BitmapCompressProxy.Build().width(width).height(height).bitmap(bitmap).build();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else if(UriPraser.isNetworkUri(uri)){
			if(Looper.getMainLooper() == Looper.myLooper()){
				throw new RuntimeException("network uri can't compressed on UI Thread");
			}
//			compressProxy = new FileCompressProxy.Build().width(width).height(height).path().build();
		}

		return compressProxy.compress();
	}

	public static class Build {
		private Uri uri;
		private int width;
		private int height;
		private int quality;

		public Build uri(Uri uri) {
			this.uri = uri;
			return this;
		}

		public Build width(int width) {
			this.width = width;
			return this;
		}

		public Build height(int height) {
			this.height = height;
			return this;
		}

		public Build quality(int quality) {
			this.quality = quality;
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
			return proxy;
		}
	}
}
