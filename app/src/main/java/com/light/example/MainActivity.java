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
import com.light.proxy.BitmapCompressProxy;
import com.light.proxy.FileCompressProxy;
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
		config.setDefaultQuality(75);
		Light.getInstance().init(this).setConfig(config);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_1920_1200);
//
//		c
//
//		L.e("MemorySize", MemoryComputeUtil.getMemorySize(bitmap) + "字节");
//		L.e("MemorySize", MemoryComputeUtil.getMemorySize(b) + "字节");
		try {
			String pathRoot2 = Environment.getExternalStorageDirectory().getCanonicalPath()+"/888.jpg";
//			new ResourcesCompressProxy.Build().resource(R.drawable.test_1920_1200).build()
//					.compress(pathRoot2);
			Bitmap b = new FileCompressProxy.Build().path(pathRoot2).build().compress();
			imageView.setImageBitmap(b);
			L.e("MemorySize", MemoryComputeUtil.getMemorySize(b) + "字节");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
