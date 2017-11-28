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
import com.light.core.Utils.DisplayUtil;
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
		config.setMaxHeight(1024);
		config.setMaxWidth(1024);
		Light.getInstance().init(this).setConfig(config);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_1920_1200);

		Bitmap bb = new ResourcesCompressProxy().decodeResource(R.drawable.test_4608_3456, config);
		Log.e("MemorySize", MemoryComputeUtil.getMemorySize(bitmap) + "字节");
		Log.e("MemorySize", MemoryComputeUtil.getMemorySize(bb) + "字节");
		try {
			String pathRoot = Environment.getExternalStorageDirectory().getCanonicalPath()+"/bitmap.jpg";
			String pathRoot2 = Environment.getExternalStorageDirectory().getCanonicalPath()+"/test_1920_1200_o.jpg";
			ArrayList<String> list = new ArrayList<>();
			for(int i = 0;i < 10 ;i ++){
				list.add(i+"");
			}
//			Bitmap b = new ResourcesCompressProxy().resource(R.drawable.test_1920_1200)
//					.height(DisplayUtil.dip2px(this, 960))
//					.width(DisplayUtil.dip2px(this, 600))
//					.compress();
//
////			boolean bitmap1 = new LightCompressEngine(L'ight.getInstance(this)).compress2File(bitmap, pathRoot2);
//			imageView.setImageBitmap(b);
//			Log.e("MemorySize", MemoryComputeUtil.getMemorySize(b) + "字节");
//			Log.e("MemorySize", b.getWidth() + "  " + b.getHeight());
//			Log.e("MemorySize", DisplayUtil.px2dip(this, b.getWidth()) + "  " + DisplayUtil.px2dip(this, b
//					.getHeight()));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
