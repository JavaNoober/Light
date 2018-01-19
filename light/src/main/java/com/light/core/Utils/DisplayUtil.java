package com.light.core.Utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;

import com.light.body.Light;

public class DisplayUtil {
	private static final String TAG = Light.TAG + "-DisplayUtil";

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static Point getScreenMetrics(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
		L.i(TAG, "Screen---Width = " + w_screen + " Height = " + h_screen + " densityDpi = " + dm.densityDpi);
		return new Point(w_screen, h_screen);

	}

	/**
	 *
	 * @param context
	 * @return
	 */
	public static float getScreenRate(Context context) {
		Point P = getScreenMetrics(context);
		float H = P.y;
		float W = P.x;
		return (H / W);
	}


	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}


	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}


	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	public static double pt2px(float spValue) {
		return (double) spValue * 2.22;
	}

	public static int[] getViewSize(View view) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(width, height);
		return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
	}
}
