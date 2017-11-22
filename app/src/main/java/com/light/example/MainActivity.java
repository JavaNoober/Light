package com.light.example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.LightCompressEngine;
import com.light.core.MatrixUtil;
import com.light.core.Utils.MemoryComputeUtil;

import java.io.IOException;

import com.xiaoqi.libjpegcompress.ImageUtils;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView imageView = findViewById(R.id.image);
		Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
		imageView.setImageBitmap(new MatrixUtil.Build().bitmap(bitmap2).scale(3, 3).postTranslate(200, 200).build());
//
		LightConfig config = new LightConfig();
		config.setMaxFileSize(2222222);
		config.setMaxHeight(1024);
		config.setMaxWidth(1024);
		Light.getInstance(this).setConfig(config);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
		Log.e("MemorySize", MemoryComputeUtil.getMemorySize(bitmap) + "字节");
		try {
			String pathRoot = Environment.getExternalStorageDirectory().getCanonicalPath()+"/222.jpg";
			Log.e("MemorySize", ImageUtils.compressBitmap(bitmap, pathRoot));
			boolean b = new LightCompressEngine(Light.getInstance(this)).compress2File(bitmap, pathRoot);
			Log.e("MemorySize", MemoryComputeUtil.getMemorySize(bitmap) + "字节" + b);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
