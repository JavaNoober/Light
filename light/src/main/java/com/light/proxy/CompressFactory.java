package com.light.proxy;

import com.light.core.listener.ICompressProxy;

/**
 * Created by xiaoqi on 2018/1/9
 */

public class CompressFactory {

	enum Compress {
		Bitmap, Bytes, File, Resource, Uri;
	}

	public static ICompressProxy createCompress(Compress compressCategory) {
//		switch (){
//
//		}
		return null;
	}
}
