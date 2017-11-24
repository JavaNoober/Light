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
import com.light.core.NativeCompressEngine;
import com.light.core.Utils.MemoryComputeUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.xiaoqi.libjpegcompress.ImageUtils;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView imageView = findViewById(R.id.image);
//		Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//		imageView.setImageBitmap(new MatrixUtil.Build().bitmap(bitmap2).scale(3, 3).postTranslate(200, 200).build());

		LightConfig config = new LightConfig();
		config.setMaxFileSize(2222222);
		config.setMaxHeight(1024);
		config.setMaxWidth(1024);
		Light.getInstance().setConfig(config);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.d1);
		Log.e("MemorySize", MemoryComputeUtil.getMemorySize(bitmap) + "字节");
		try {
			String pathRoot = Environment.getExternalStorageDirectory().getCanonicalPath()+"/bitmap.jpg";
			String pathRoot2 = Environment.getExternalStorageDirectory().getCanonicalPath()+"/test_1920_1200_o.jpg";
			ArrayList<String> list = new ArrayList<>();
			for(int i = 0;i < 10 ;i ++){
				list.add(i+"");
			}
			boolean bitmap2 = new NativeCompressEngine().compress2File(bitmap, pathRoot);

//			boolean bitmap1 = new LightCompressEngine(Light.getInstance(this)).compress2File(bitmap, pathRoot2);
//			imageView.setImageBitmap(bitmap1);
//			Log.e("MemorySize", MemoryComputeUtil.getMemorySize(bitmap1) + "字节");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
