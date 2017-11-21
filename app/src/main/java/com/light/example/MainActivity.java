package com.light.example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.MatrixUtil;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView imageView = findViewById(R.id.image);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
		imageView.setImageBitmap(new MatrixUtil.Build().bitmap(bitmap).scale(3, 3).postTranslate(200, 200).build());

		LightConfig config = new LightConfig();
		config.setMaxFileSize(2222222);
		Light.getInstance(this).setConfig(config);
		int i = Light.getInstance(this).getConfig().getMaxFileSize();

	}
}
