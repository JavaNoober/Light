package com.light.example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.LightCompressEngine;
import com.light.core.Utils.L;
import com.light.core.Utils.MemoryComputeUtil;
import com.light.proxy.ResourcesCompressProxy;

import java.io.IOException;
import java.util.ArrayList;

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
		Light.getInstance().init(this).setConfig(config);

//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.d1);
//
//		c
//
//		L.e("MemorySize", MemoryComputeUtil.getMemorySize(bitmap) + "字节");
//		L.e("MemorySize", MemoryComputeUtil.getMemorySize(b) + "字节");
		try {
			String pathRoot = Environment.getExternalStorageDirectory().getCanonicalPath()+"/bitmap.jpg";
			String pathRoot2 = Environment.getExternalStorageDirectory().getCanonicalPath()+"/test_1920_1200.jpg";
			ArrayList<String> list = new ArrayList<>();
			for(int i = 0;i < 10 ;i ++){
				list.add(i+"");
			}
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_1920_1200);
			L.e("MemorySize", MemoryComputeUtil.getMemorySize(bitmap) + "字节");
			Bitmap b1 = new LightCompressEngine().compress2Bitmap(pathRoot2, 1080, 1920);
			Bitmap b2 = new ResourcesCompressProxy.Build().resource(R.drawable.test_1920_1200).build().compress();
			Bitmap b3 = new LightCompressEngine().compress2Bitmap(bitmap, 1080, 1920);
			L.e("MemorySize", MemoryComputeUtil.getMemorySize(b1) + "字节");
			L.e("MemorySize", MemoryComputeUtil.getMemorySize(b2) + "字节");
			L.e("MemorySize", MemoryComputeUtil.getMemorySize(b3) + "字节");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
