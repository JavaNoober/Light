package com.light.proxy;

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
				proxy = new UriCompressProxy.Builder().compressArgs(compressArgs).uri((Uri) object).build();
				break;
			case File:
				proxy = new FileCompressProxy.Builder().compressArgs(compressArgs).path((String) object).build();
				break;
			case Bytes:
				proxy = new BytesCompressProxy.Builder().compressArgs(compressArgs).bytes((byte[]) object).build();
				break;
			case Bitmap:
				proxy = new BitmapCompressProxy.Builder().compressArgs(compressArgs).bitmap((Bitmap) object).build();
				break;
			case Resource:
				ResourcesCompressProxy.Builder builder = new ResourcesCompressProxy.Builder();
				if(object instanceof Drawable){
					builder.drawable((Drawable) object);
				}else {
					builder.resource((int) object);
				}
				proxy = builder.compressArgs(compressArgs).build();
				break;
			default:
				proxy = null;
		}
		return proxy;
	}
}
