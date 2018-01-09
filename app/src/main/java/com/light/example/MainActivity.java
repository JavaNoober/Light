package com.light.example;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.LightCompressEngine;
import com.light.core.Utils.L;
import com.light.core.Utils.MemoryComputeUtil;
import com.light.proxy.BitmapCompressProxy;
import com.light.proxy.BytesCompressProxy;
import com.light.proxy.FileCompressProxy;
import com.light.proxy.ResourcesCompressProxy;
import com.light.proxy.UriCompressProxy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
	ImageView imageView;
	Uri imageUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageView = findViewById(R.id.image);
		Button button = findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(Intent.ACTION_PICK,
//						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//				startActivityForResult(intent, 1);
//
				imageUri = getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						new ContentValues());
				Intent takePhotoIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				takePhotoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,imageUri);
				startActivityForResult(takePhotoIntent, 1);
			}
		});
//		Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//		imageView.setImageBitmap(new MatrixUtil.Build().bitmap(bitmap2).scale(3, 3).postTranslate(200, 200).build());

		LightConfig config = new LightConfig();
		config.setMaxFileSize(2222222);
		config.setDefaultQuality(75);
		Light.getInstance().init(this).setConfig(config);

//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_1920_1200);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//		byte[] datas = baos.toByteArray();
//		Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" +R.drawable.d1);
//		c
//
//		L.e("MemorySize", MemoryComputeUtil.getMemorySize(bitmap) + "字节");
//		L.e("MemorySize", MemoryComputeUtil.getMemorySize(b) + "字节");
		try {
			String pathRoot2 = Environment.getExternalStorageDirectory().getCanonicalPath()+"/img_img.jpg";
			String path = Environment.getExternalStorageDirectory().getCanonicalPath()+"/img1.jpg";
			new FileCompressProxy.Build().path(pathRoot2).height(1920).width(1440).quality(73).build().compress(path);

//			Uri uri = Uri.fromFile(new File(pathRoot2));
//			new ResourcesCompressProxy.Build().resource(R.drawable.test_1920_1200).build()
//					.compress(pathRoot2);
//			Bitmap b = new UriCompressProxy.Build().uri(uri).width(1024).height(768).build().compress();
//			imageView.setImageBitmap(b);
//			L.e("MemorySize", MemoryComputeUtil.getMemorySize(b) + "字节");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		ContentResolver resolver = getContentResolver();
		Uri originalUri = data.getData(); // 获得图片的uri
//		try {
//			Bitmap bm = MediaStore.Images.Media.getBitmap(resolver,
//					originalUri);
//			String[] proj = { MediaStore.Images.Media.DATA };
//			Cursor cursor = managedQuery(originalUri, proj, null, null,
//					null);
//			int column_index = cursor
//					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//			cursor.moveToFirst();
//			final String path = cursor.getString(column_index);
//			System.out.println(path);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		Bitmap b = new UriCompressProxy.Build().uri(originalUri).width(1024).height(768).build().compress();
		imageView.setImageBitmap(b);
//		imageView.setImageURI(originalUri);
	}
}
