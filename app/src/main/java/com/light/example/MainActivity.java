package com.light.example;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.body.RxLight;
import com.light.core.Utils.MemoryComputeUtil;
import com.light.core.Utils.UriParser;
import com.light.core.Utils.http.HttpDownLoader;
import com.light.core.listener.OnCompressFinishListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
	ImageView ivCompress;
	TextView tvInfo;
	Uri imageUri;

	final static String info = "原图片:\n高度：%d，宽度：%d，占用内存：%dKB\n显示的图片(压缩后)：\n高度：%d, 宽度：%d，占用内存：%dKB";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ivCompress = findViewById(R.id.image_compress);
		tvInfo = findViewById(R.id.tv_info);
		LightConfig config = new LightConfig();
		config.setNeedIgnoreSize(true);
		Light.getInstance().setConfig(config);
//		Uri uri = Uri.parse("http://pic4.nipic.com/20091217/3885730_124701000519_2.jpg");
//		Flowable.just(uri).compose(RxLight.compressForUriHttp()).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
//		List<String> urlList = new ArrayList<>();
//		Flowable.fromIterable(urlList).compose(RxLight.compressForStringHttp()).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));

//		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pic.jpg";
//		Flowable.just(Uri.parse(path)).compose(RxLight.compress()).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && imageUri != null) {
			//效果同下
//			Light.setImage(ivCompress, imageUri);
			Bitmap compressBitmap = Light.getInstance().compress(imageUri);
//			ivCompress.setImageBitmap(compressBitmap);
			Flowable.just(imageUri).compose(RxLight.compress()).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
			//系统获取图片的方法
			String path = UriParser.getPathFromContentUri(imageUri);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			Bitmap bitmap2 = BitmapFactory.decodeFile(path);
			tvInfo.setText(String.format(Locale.CHINA, info, options.outHeight, options.outWidth,
					MemoryComputeUtil.getMemorySize(bitmap2), compressBitmap.getHeight(),
					compressBitmap.getWidth(), MemoryComputeUtil.getMemorySize(compressBitmap)));
			bitmap2.recycle();
		}else if(requestCode == 2 && data != null){
			Uri imageUri = data.getData();
			String path = UriParser.getPathFromContentUri(imageUri);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			Bitmap compressBitmap = Light.getInstance().compress(imageUri);
			ivCompress.setImageBitmap(compressBitmap);
			Bitmap bitmap2 = BitmapFactory.decodeFile(path);
			tvInfo.setText(String.format(Locale.CHINA, info, options.outHeight, options.outWidth,
					MemoryComputeUtil.getMemorySize(bitmap2), compressBitmap.getHeight(),
					compressBitmap.getWidth(), MemoryComputeUtil.getMemorySize(compressBitmap)));
			bitmap2.recycle();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("相册");
		menu.add("拍照");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if("相册".equals(item.getTitle())){
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_PICK);
			intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, 2);
		}else if("拍照".equals(item.getTitle())){
			imageUri = getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					new ContentValues());
			Intent takePhotoIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			takePhotoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(takePhotoIntent, 1);
		}
		return true;
	}
}
