package com.light.proxy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.LightCompressEngine;
import com.light.core.Utils.L;
import com.light.core.Utils.MatrixUtil;
import com.light.core.listener.ICompressEngine;
import com.light.core.listener.ICompressProxy;

import java.lang.ref.SoftReference;

/**
 * Created by xiaoqi on 2017/11/25.
 */

public class ResourcesCompressProxy implements ICompressProxy {

	private int resId;
	private int width;
	private int height;
	private LightConfig lightConfig;
	private static ResourcesCompressProxy proxy;

	public ResourcesCompressProxy() {
		lightConfig = Light.getInstance().getConfig();
	}

	@Override
	public boolean compress(String outPath) {
		return false;
	}

	@Override
	public Bitmap compress() {
		ICompressEngine compressEngine = getCompressEngine();
		int resultWidth = 0;
		int resultHeight = 0;
		if(width > 0 && height >0){
			resultWidth = width;
			resultHeight = height;
		}else if(lightConfig.getMaxHeight() > 0 && lightConfig.getMaxHeight() > 0){
			resultWidth = lightConfig.getMaxHeight();
			resultHeight = lightConfig.getMaxHeight();
		}

		Bitmap result = compressEngine.compress2Bitmap(resId, resultWidth, resultHeight);
		float scaleSize = MatrixUtil.getScale(width, height, result.getWidth(), result.getHeight());
		if(scaleSize < 1){
			L.e("scaleSize:"+ scaleSize);
			return new MatrixUtil.Build().scale(scaleSize, scaleSize).bitmap(result).build();
		}
		return result;
	}

	public ResourcesCompressProxy resource(int resId) {
		this.resId = resId;
		return this;
	}

	public ResourcesCompressProxy width(int width) {
		this.width = width;
		return this;
	}

	public ResourcesCompressProxy height(int height) {
		this.height = height;
		return this;
	}

	private ICompressEngine getCompressEngine(){
		return new LightCompressEngine(lightConfig);
	}


	public static Bitmap decodeResource(int id) {

		int densityDpi = Light.getInstance().getResources().getDisplayMetrics().densityDpi;
		Bitmap bitmap;
		TypedValue value = new TypedValue();
		Light.getInstance().getResources().openRawResource(id, value);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
		if (densityDpi > DisplayMetrics.DENSITY_HIGH) {
			opts.inTargetDensity = value.density;
			bitmap = BitmapFactory.decodeResource(Light.getInstance().getResources(), id, opts);
		}else{
			bitmap = BitmapFactory.decodeResource(Light.getInstance().getResources(), id);
		}

		return bitmap;
	}
}
