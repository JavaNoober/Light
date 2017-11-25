package com.light.core.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;

/**
 * Created by xiaoqi on 2017/11/21.
 */

public class MatrixUtil {

	public static class Build {
		//放大缩小系数
		private Float scaleX;
		private Float scaleY;

		private Float postScaleX;
		private Float postScaleY;
		//旋转角度
		private Float degrees;

		private Float postDegrees;
		//旋转中心
		private Float rotateX;
		private Float rotateY;

		private Float postRotateX;
		private Float postRotateY;
		//平移
		private Float translateX;
		private Float translateY;

		private Float postTranslateX;
		private Float postTranslateY;
		//倾斜比例
		private Float skewX;
		private Float skewY;

		private Float postSkewX;
		private Float postSkewY;
		private Bitmap bitmap;
		private boolean recycle = true;

		public Build scale(float scaleX, float scaleY){
			this.scaleX = scaleX;
			this.scaleY = scaleY;
			return this;
		}

		public Build postScale(float postScaleX, float postScaleY){
			this.postScaleX = postScaleX;
			this.postScaleY = postScaleY;
			return this;
		}

		public Build rotate(float degrees){
			this.degrees = degrees;
			return this;
		}

		public Build postRotate(float postDegrees){
			this.postDegrees = postDegrees;
			return this;
		}

		public Build rotate(float degrees, float rotateX, float rotateY){
			this.degrees = degrees;
			this.rotateX = rotateX;
			this.rotateY = rotateY;
			return this;
		}

		public Build postRotate(float postDegrees, float postRotateX, float postRotateY){
			this.postDegrees = postDegrees;
			this.postRotateX = postRotateX;
			this.postRotateY = postRotateY;
			return this;
		}

		public Build translate(float translateX, float translateY){
			this.translateX = translateX;
			this.translateY = translateY;
			return this;
		}

		public Build postTranslate(float postTranslateX, float postTranslateY){
			this.postTranslateX = postTranslateX;
			this.postTranslateY = postTranslateY;
			return this;
		}

		public Build skew(float skewX, float skewY){
			this.skewX = skewX;
			this.skewY = skewY;
			return this;
		}

		public Build postSkew(float postSkewX, float postSkewY){
			this.postSkewX = postSkewX;
			this.postSkewY = postSkewY;
			return this;
		}

		public Build bitmap(Bitmap bitmap){
			this.bitmap = bitmap;
			return this;
		}

		public Build recycle(boolean recycle){
			this.recycle = recycle;
			return this;
		}

		@Nullable
		public Bitmap build(){
			if(bitmap == null){
				return null;
			}
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			if(scaleX != null && scaleY != null){
				matrix.setScale(scaleX, scaleY);
				width = (int) (width * scaleX);
				height = (int) (height * scaleY);
			}
			if(postScaleX != null && postScaleY != null){
				matrix.postScale(postScaleX, postScaleY);
				width = (int) (width * postScaleX);
				height = (int) (height * postScaleY);
			}
			if(degrees != null){
				matrix.setRotate(degrees);
			}
			if(postDegrees != null){
				matrix.postRotate(postDegrees);
			}
			if(degrees != null && rotateX != null && rotateY != null){
				matrix.setRotate(degrees, rotateX, rotateY);
			}
			if(postDegrees != null && postRotateX != null && postRotateY != null){
				matrix.postRotate(postDegrees, postRotateX, postRotateY);
			}
			if(translateX != null && translateY != null){
				matrix.setTranslate(translateX, translateY);
				width = (int) (width + translateX);
				height = (int) (height + translateY);
			}
			if(postTranslateX != null && postTranslateY != null){
				matrix.postTranslate(postTranslateX, postTranslateY);
				width = (int) (width + postTranslateX);
				height = (int) (height + postTranslateY);
			}
			if(skewX != null && skewY != null){
				matrix.setSkew(skewX, skewY);
			}
			if(postSkewX != null && postSkewY != null){
				matrix.postSkew(postSkewX, postSkewY);
			}
			Bitmap afterBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
			Canvas canvas = new Canvas(afterBitmap);
			canvas.drawBitmap(bitmap, matrix, paint);
			if(recycle){
				bitmap.recycle();
			}
			return afterBitmap;
		}
	}

	public static float getScale(int width, int height, int currentWidth, int currentHeight) {
		float scale = 1;
		if((width > 0 && height > 0) && (currentWidth > width || currentHeight > height)){
			float widthScale = (float) width / currentWidth;
			float heightScale = (float) height / currentHeight;
			scale = Math.min(widthScale, heightScale);
		}
		return scale;
	}

}
