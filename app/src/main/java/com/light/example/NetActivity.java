package com.light.example;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.light.body.Light;
import com.light.body.RxLight;
import com.light.core.Utils.MatrixUtil;
import com.light.core.Utils.MemoryComputeUtil;
import com.light.core.Utils.http.HttpDownLoader;
import com.light.core.listener.OnCompressFinishListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NetActivity extends AppCompatActivity {
    ImageView ivCompress;
    ImageView ivImage;
    TextView tvInfo;
    TextView tvInfo1;
    TextView tvInfo2;
    TextView tvSize;
    Button button1;
    Button button2;
    Button button3;
    String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/1.jpg";//压缩后
    String path2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/2.jpg";//原图片
    final static String info1 = "压缩后:\n高度：%d，宽度：%d，占用内存：%dKB";
    final static String info2 = "原图片:\n高度：%d，宽度：%d，占用内存：%dKB";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        ivCompress = findViewById(R.id.image_compress);
        ivImage = findViewById(R.id.image);
        tvInfo = findViewById(R.id.tv_info);
        tvInfo1 = findViewById(R.id.tv_info1);
        tvInfo2 = findViewById(R.id.tv_info2);
        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        button3 = findViewById(R.id.btn3);
        tvSize = findViewById(R.id.tv_size);
        tvInfo1.setVisibility(View.VISIBLE);
        tvInfo2.setVisibility(View.VISIBLE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("图片加载中");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Uri uri = Uri.parse("http://img52.fooww.com:9999/group5/M01/00/99/ZyjCGlrtH9uEPlSIAAAAAN9Juaw140.jpg");
                showImg(uri);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Uri uri = Uri.parse("http://k.zol-img.com.cn/sjbbs/7692/a7691515_s.jpg");
                showImg(uri);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Uri uri = Uri.parse("http://pic31.nipic.com/20130705/9527735_231540074000_2.jpg");
                showImg(uri);
            }
        });
    }

    private void showImg(Uri uri) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = HttpDownLoader.downloadImage(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                File file = new File(path2);
                OutputStream output = null;
                try {
                    output = new FileOutputStream(file);
                    BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
                    bufferedOutput.write(bytes);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        float scaleX = MatrixUtil.getScale(1920, 1080, options.outWidth, options.outHeight);
                        ivImage.setImageBitmap(new MatrixUtil.Build().scale(scaleX, scaleX).bitmap(bitmap).build());
                        Light.getInstance().compress(path2, path1);
                        tvInfo2.setText(String.format(Locale.CHINA, info2, options.outHeight,
                                options.outWidth, MemoryComputeUtil.getMemorySize(bitmap)));
                        tvSize.setText("原图片文件大小:" + file.length() / 1024 + "KB;压缩后图片文件大小:" +
                                new File(path1).length() / 1024 + "KB");
                    }
                });
            }
        }).start();
        Flowable.just(uri).compose(RxLight.compressForUriHttp())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    progressDialog.dismiss();
                    ivCompress.setImageBitmap(bitmap);
                    tvInfo1.setText(String.format(Locale.CHINA, info1, bitmap.getHeight(),
                            bitmap.getWidth(), MemoryComputeUtil.getMemorySize(bitmap)));
                });

        //缓存测试
//        new Thread(() -> Light.getInstance().compressFromHttp(uri, true, new OnCompressFinishListener() {
//            @Override
//            public void onFinish(byte[] bytes) {
//                Bitmap bitmap = Light.getInstance().compress(bytes);
//                runOnUiThread(() -> {
//                    progressDialog.dismiss();
//                    ivCompress.setImageBitmap(bitmap);
//                    tvInfo1.setText(String.format(Locale.CHINA, info1, bitmap.getHeight(),
//                            bitmap.getWidth(), MemoryComputeUtil.getMemorySize(bitmap)));
//                });
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//        })).start();
    }

}
