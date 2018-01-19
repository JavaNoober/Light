package com.light.proxy;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.light.body.CompressArgs;
import com.light.core.listener.ICompressProxy;

/**
 * Created by xiaoqi on 2018/1/9
 */

public class CompressFactory {

	public enum Compress {
		Bitmap, Bytes, File, Resource, Uri
	}

	public static ICompressProxy createCompress(Compress compressCategory, CompressArgs compressArgs, Object object) {
		ICompressProxy proxy;
		switch (compressCategory){
			case Uri:
				if(compressArgs == null){
					proxy = new UriCompressProxy.Builder().uri((Uri) object).build();
				}else {
					proxy = new UriCompressProxy.Builder().height(compressArgs.getHeight()).width(compressArgs.getWidth())
							.quality(compressArgs.getQuality()).uri((Uri) object).build();
				}
				break;
			case File:
				if(compressArgs == null){
					proxy = new FileCompressProxy.Builder().path((String) object).build();
				}else {
					proxy = new FileCompressProxy.Builder().height(compressArgs.getHeight()).width(compressArgs.getWidth())
							.quality(compressArgs.getQuality()).path((String) object).build();
				}
				break;
			case Bytes:
				if(compressArgs == null){
					proxy = new BytesCompressProxy.Builder().bytes((byte[]) object).build();
				}else {
					proxy = new BytesCompressProxy.Builder().height(compressArgs.getHeight()).width(compressArgs.getWidth())
							.quality(compressArgs.getQuality()).bytes((byte[]) object).build();
				}
				break;
			case Bitmap:
				if(compressArgs == null){
					proxy = new BitmapCompressProxy.Builder().bitmap((Bitmap) object).build();
				}else {
					proxy = new BitmapCompressProxy.Builder().height(compressArgs.getHeight()).width(compressArgs.getWidth())
							.quality(compressArgs.getQuality()).bitmap((Bitmap) object).build();
				}
				break;
			case Resource:
				ResourcesCompressProxy.Builder builder = new ResourcesCompressProxy.Builder();

				//TODo test
				if(object instanceof Drawable){
					builder.drawable((Drawable) object);
				}else {
					builder.resource((int) object);
				}

				if(compressArgs == null){
					proxy = builder.build();
				}else {
					proxy = builder.height(compressArgs.getHeight()).width(compressArgs.getWidth()).build();
				}
				break;
			default:
				proxy = null;
		}
		return proxy;
	}
}
