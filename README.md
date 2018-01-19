# Light
a lightweight image compress framework for Android based on libjpeg

support types:
    File,Uri,Bytes,Bitmap,DrawableResource

 * 1:设置文件最大值
 * 2:图片的旋转缩放
 * 3:异步并发压缩
 * 4:同步压缩

 use:
    
    
    
    自动获取控件宽高来裁剪图片,进行显示
    String path  = Environment.getExternalStorageDirectory().getAbsolutePath()+"/pic.jpg";
    Light.setImage(imageView, path);
    自定义压缩参数
    CompressArgs args = new CompressArgs.Builder().width(width).height(height).quality(70).build();
    Light.setImage(imageView, null, path);
    
    
    path -->bitmap
    Bitmap bitmap = Light.getInstance().compress(path);
    
    Bitmap bitmap = Light.getInstance().compress(path, args);
    
    bitmap --> path //保存图片输出
    Light.getInstance().compress(path, args, outPath);
    
    //支持类型Bitmap, Bytes, String, Resource, Uri, Drawable
    
    
    CompressArgs args = new CompressArgs.Builder().width(width).height(height).quality(70).build();
    Light.getInstance().compress(path, args);
    
    
    Light.getInstance().compress(R.drawable.pic);

    Light.getInstance().compress(R.drawable.pic, 75);

    Light.getInstance().compress(R.drawable.pic, 1024, 768); //传入参数是px

    List<Integer> list = new ArrayList<>();
    ...
    Light.getInstance().compress(list).execute().onFinish(new OnCompressFinishListener(){});
    Light.getInstance().compress(list).enqueue();