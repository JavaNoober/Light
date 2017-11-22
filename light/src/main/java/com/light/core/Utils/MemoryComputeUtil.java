package com.light.core.Utils;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by xiaoqi on 2017/11/22.
 */

public class MemoryComputeUtil {

	public static int getMemorySize(Bitmap bitmap){
		Bitmap.Config config = bitmap.getConfig();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Log.e("MemoryComputeUtil", "width:" + width + "height:"+ height);
		if(config.equals(Bitmap.Config.ALPHA_8)){
			return width * height;
		}else if(config.equals(Bitmap.Config.ARGB_4444)){
			return width * height * 2;
		}else if(config.equals(Bitmap.Config.RGB_565)){
			return width * height * 2;
		}else {
			//ARGB_8888
			return width * height * 4;
		}
	}
}
