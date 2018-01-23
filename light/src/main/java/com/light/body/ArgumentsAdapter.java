package com.light.body;

import com.light.core.listener.ICompressProxy;
import com.light.proxy.CompressFactory;

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
}
