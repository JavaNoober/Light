package com.light.body;

import android.net.Uri;

import com.light.core.listener.ICompressProxy;
import com.light.proxy.CompressFactory;
import com.light.proxy.FileCompressProxy;
import com.light.proxy.UriCompressProxy;

/**
 * Created by xiaoqi on 2018/1/10
 */

public class ArgumentsAdapter {

	CompressArgs compressArgs;

	public ArgumentsAdapter(CompressArgs compressArgs){
		this.compressArgs = compressArgs;
	}

	public ICompressProxy getCompressProxy(CompressFactory.Compress compressCategory, Object imageSource){
		return CompressFactory.createCompress(compressCategory, compressArgs, imageSource);
	}

	public FileCompressProxy getCompressProxy(String imageSource){
		return (FileCompressProxy) CompressFactory.createCompress(CompressFactory.Compress.File, compressArgs, imageSource);
	}

	public UriCompressProxy getCompressProxy(Uri imageSource){
		return (UriCompressProxy) CompressFactory.createCompress(CompressFactory.Compress.Uri, compressArgs, imageSource);
	}
}
