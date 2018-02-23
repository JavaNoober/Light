package com.light.core.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;

/**
 * Created by xiaoqi on 2017/11/21
 */

public class MatrixUtil {

	public static class Build {
		//放大缩小系数
		private Float scaleX;
		private Float scaleY;

		private Float postScaleX;
		private Float postScaleY;

		private Float preScaleX;
		private Float preScaleY;

		//旋转角度
		private Float rotate;

		private Float postRotate;

		private Float preRotate;
		//旋转中心
		private Float rotateX;
		private Float rotateY;

		private Float postRotateX;
		private Float postRotateY;

		private Float preRotateX;
		private Float preRotateY;

		//平移
		private Float translateX;
		private Float translateY;

		private Float postTranslateX;
		private Float postTranslateY;

		private Float preTranslateX;
		private Float preTranslateY;

		//倾斜比例
		private Float skewX;
		private Float skewY;

		private Float postSkewX;
		private Float postSkewY;

		private Float preSkewX;
		private Float preSkewY;

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

		public Build preScale(float preScaleX, float preScaleY){
			this.preScaleX = preScaleX;
			this.preScaleY = preScaleY;
			return this;
		}

		public Build rotate(float degrees){
			this.rotate = degrees;
			return this;
		}

		public Build postRotate(float postDegrees){
			this.postRotate = postDegrees;
			return this;
		}

		public Build preRotate(float preRotate){
			this.preRotate = preRotate;
			return this;
		}

		public Build preRotate(float preRotate, float preRotateX, float preRotateY){
			this.preRotate = preRotate;
			this.preRotateX = preRotateX;
			this.preRotateY = preRotateY;
			return this;
		}

		public Build rotate(float degrees, float rotateX, float rotateY){
			this.rotate = degrees;
			this.rotateX = rotateX;
			this.rotateY = rotateY;
			return this;
		}

		public Build postRotate(float postDegrees, float postRotateX, float postRotateY){
			this.postRotate = postDegrees;
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

		public Build preTranslate(float preTranslateX, float preTranslateY){
			this.preTranslateX = preTranslateX;
			this.preTranslateY = preTranslateY;
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

		public Build preSkew(float preSkewX, float preSkewY){
			this.preSkewX = preSkewX;
			this.preSkewY = preSkewY;
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

			if(preScaleX != null && preScaleY != null){
				matrix.preScale(preScaleX, preScaleY);
				width = (int) (width * preScaleX);
				height = (int) (height * preScaleY);
			}

			if(rotate != null){
				matrix.setRotate(rotate);
			}
			if(postRotate != null){
				matrix.postRotate(postRotate);
				if(postRotate == 90 || postRotate == 270){
					int tmp = width;
					width = height;
					height = tmp;
				}
			}
			if(preRotate != null){
				matrix.preRotate(preRotate);
				if(preRotate == 90 || preRotate == 270){
					int tmp = width;
					width = height;
					height = tmp;
				}
			}

			if(rotate != null && rotateX != null && rotateY != null){
				matrix.setRotate(rotate, rotateX, rotateY);
			}

			if(postRotate != null && postRotateX != null && postRotateY != null){
				matrix.postRotate(postRotate, postRotateX, postRotateY);
			}

			if(rotate != null && preRotateX != null && preRotateY != null){
				matrix.preRotate(rotate, preRotateX, preRotateY);
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

			if(preTranslateX != null && preTranslateY != null){
				matrix.preTranslate(preTranslateX, preTranslateY);
				width = (int) (width + preTranslateX);
				height = (int) (height + preTranslateY);
			}

			if(skewX != null && skewY != null){
				matrix.setSkew(skewX, skewY);
			}

			if(postSkewX != null && postSkewY != null){
				matrix.postSkew(postSkewX, postSkewY);
			}

			if(preSkewX != null && preSkewY != null){
				matrix.preSkew(preSkewX, preSkewY);
			}

			Bitmap afterBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
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
