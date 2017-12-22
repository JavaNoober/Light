package com.light.core;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.light.core.listener.ICompressProxy;
import com.light.core.listener.OnAsyncCompressFinishListener;
import com.light.proxy.FileCompressProxy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xiaoqi on 2017/11/25.
 */

public class AsyncCompressExecutor {

	private static final int COMPRESS = 0x001;
	private static final int COMPRESS_FOR_LIST = 0x002;

	private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	private OnAsyncCompressFinishListener listener;
	private List<String> imageList;
	private String path;

	private static class CompressHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

		}
	}

	private static final CompressHandler mHandler = new CompressHandler();


	public class Build {
		OnAsyncCompressFinishListener listener;
		List<String> imageList;
		String path;

		public Build onFinishListener(OnAsyncCompressFinishListener listener){
			this.listener = listener;
			return this;
		}

		public Build imageList(List<String> imageList){
			this.imageList = imageList;
			return this;
		}

		public Build path(String path){
			this.path = path;
			return this;
		}

		public AsyncCompressExecutor Build(){
			AsyncCompressExecutor executor = new AsyncCompressExecutor();
			executor.listener = listener;
			executor.imageList = imageList;
			executor.path = path;
			return executor;
		}
	}

	public void compress(){
		final ICompressProxy iCompressProxy = new FileCompressProxy();
		if(imageList != null){
			for(final String path : imageList){
				cachedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						iCompressProxy.compress(path);
						if(cachedThreadPool.isShutdown() && listener != null){
							Message message = new Message();
							message.what = COMPRESS;
							Bundle bundle = new Bundle();
							bundle.putBoolean("Result", true);
							message.setData(bundle);
							mHandler.sendMessage(message);
						}
					}
				});
			}
		}else {
			cachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					boolean result = iCompressProxy.compress(path);
					if(cachedThreadPool.isShutdown() && listener != null){
						Message message = new Message();
						message.what = COMPRESS;
						Bundle bundle = new Bundle();
						bundle.putBoolean("Result", result);
						message.setData(bundle);
						mHandler.sendMessage(message);
					}
				}
			});
		}

	}

}
