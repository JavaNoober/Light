package com.light.example;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.light.body.CompressArgs;
import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.Utils.DisplayUtil;
import com.light.core.Utils.L;
import com.light.core.Utils.MatrixUtil;
import com.light.core.Utils.MemoryComputeUtil;
import com.light.proxy.FileCompressProxy;
import com.light.proxy.UriCompressProxy;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
	ImageView imageView;
	ImageView imageView2;
	Uri imageUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageView = findViewById(R.id.image);
		imageView2 = findViewById(R.id.image2);
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
//		imageView.setImageBitmap(new MatrixUtil.Builder().bitmap(bitmap2).scale(3, 3).postTranslate(200, 200).build());
		Light.getInstance().init(this);



		String path  = Environment.getExternalStorageDirectory().getAbsolutePath()+"/pic.jpg";
		CompressArgs args = new CompressArgs.Builder()
				.height(DisplayUtil.dip2px(this, 800))
				.width(DisplayUtil.dip2px(this, 400))
				.build();
		Uri uri = Uri.fromFile(new File(path));
		Bitmap bitmap1 = Light.getInstance().compress(uri, args);
		imageView.setImageBitmap(bitmap1);
		Bitmap bitmap2 = BitmapFactory.decodeFile(path);
		imageView2.setImageBitmap(bitmap2);
		Light.setImage(imageView, bitmap1);
		L.e(DisplayUtil.dip2px(this, 800) + "");
		L.e(MemoryComputeUtil.getMemorySize(bitmap1) + "");
		L.e(MemoryComputeUtil.getMemorySize(bitmap2) + "");
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
		Bitmap b = new UriCompressProxy.Builder().uri(originalUri).width(1024).height(768).build().compress();
		imageView.setImageBitmap(b);
//		imageView.setImageURI(originalUri);
	}
}
