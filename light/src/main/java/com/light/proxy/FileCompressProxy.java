package com.light.proxy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;

import com.light.body.CompressArgs;
import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.LightCompressEngine;
import com.light.core.Utils.DegreeHelper;
import com.light.core.Utils.FileUtils;
import com.light.core.Utils.L;
import com.light.core.Utils.MatrixUtil;
import com.light.core.Utils.http.HttpDownLoader;
import com.light.core.listener.ICompressEngine;
import com.light.core.listener.ICompressProxy;
import com.light.core.listener.OnCompressFinishListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by xiaoqi on 2017/11/25
 */

public class FileCompressProxy implements ICompressProxy {

	private String path;
	private LightConfig lightConfig;
	private ICompressEngine compressEngine;
	private CompressArgs compressArgs;

	public FileCompressProxy(){
		lightConfig = Light.getInstance().getConfig();
		compressEngine = new LightCompressEngine();
	}

	@Override
	public boolean compress(String outPath) {
		int quality = compressArgs.getQuality();
		if(quality <= 0 || quality > 100){
			quality = lightConfig.getDefaultQuality();
		}
		if(outPath == null){
			outPath = lightConfig.getOutputRootDir();
		}
        int compressFileSize = lightConfig.getCompressFileSize();
		if(compressArgs.getCompressFileSize() > 0){
            compressFileSize = compressArgs.getCompressFileSize();
        }

		if(compressFileSize > 0 && new File(path).length() / 1024 < compressFileSize){
            try {
                L.d("copyFile");
                return FileUtils.copyFile(new File(path), new File(outPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // gif格式不压缩
        if(FileUtils.getCompressFormat(new File(path)) == null){
            try {
                L.d("copyFile");
                return FileUtils.copyFile(new File(path), new File(outPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bitmap bitmap = compress();
        try {
            return compressEngine.compress2File(bitmap, outPath, quality);
        }finally {
            if(bitmap != null && !bitmap.isRecycled()){
                bitmap.recycle();
            }
        }

	}

	@Override
	public Bitmap compress() {
		int resultWidth;
		int resultHeight;
		if(!compressArgs.isIgnoreSize() && compressArgs.getWidth() > 0 && compressArgs.getHeight() >0){
			resultWidth = compressArgs.getWidth();
			resultHeight = compressArgs.getHeight();
		}else {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			if(compressArgs.isIgnoreSize()){
				resultWidth = options.outWidth;
				resultHeight = options.outHeight;
			}else {
				resultWidth = Math.min(lightConfig.getMaxWidth(), options.outWidth);
				resultHeight = Math.min(lightConfig.getMaxHeight(), options.outHeight);
			}

		}
		Bitmap result = compressEngine.compress2Bitmap(path, resultWidth, resultHeight, compressArgs.getConfig());
		float scaleSize = MatrixUtil.getScale(resultWidth, resultHeight, result.getWidth(), result.getHeight());
		if(scaleSize < 1){
			MatrixUtil.Build build = new MatrixUtil.Build().scale(scaleSize, scaleSize).bitmap(result);
			if(compressArgs.isAutoRotation()){
				int degree = DegreeHelper.getBitmapDegree(path);
				if(degree != 0){
					build.preRotate(degree);
				}
			}
			return build.build();
		}
		if(compressArgs.isAutoRotation()){
			int degree = DegreeHelper.getBitmapDegree(path);
			if(degree != 0){
				return new MatrixUtil.Build().preRotate(degree).bitmap(result).build();
			}
		}
		return result;
	}

	public void compressFromHttp(OnCompressFinishListener compressFinishListener) {
		if (Looper.getMainLooper() == Looper.myLooper()) {
			throw new RuntimeException("network uri can't compressed on UI Thread");
		}
		if (compressFinishListener != null) {
			HttpDownLoader.downloadImage(path, compressFinishListener);
		}
	}

	public static class Builder {
		private String path;
		private CompressArgs compressArgs;

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Builder compressArgs(CompressArgs compressArgs) {
			this.compressArgs = compressArgs;
			return this;
		}

		public FileCompressProxy build(){
			if(path == null){
				throw new RuntimeException("This path does not exist.");
			}
			FileCompressProxy proxy = new FileCompressProxy();
			proxy.path = path;
			if(compressArgs == null){
				proxy.compressArgs = CompressArgs.getDefaultArgs();
			}else {
				proxy.compressArgs = compressArgs;
			}
			return proxy;
		}
	}
}
