# Light
a lightweight image compress framework for Android based on libjpeg

support types:
    File,Uri,Bytes,Bitmap,DrawableResource

 * 1:设置文件最大值
 * 2:图片的旋转缩放
 * 3:异步并发压缩
 * 4:同步压缩

 use:
   
    Light.getInstance().compress(R.drawable.pic);

    Light.getInstance().compress(R.drawable.pic, 75);

    Light.getInstance().compress(R.drawable.pic, 1024, 768); //传入参数是px

    List<Integer> list = new ArrayList<>();
    ...
    Light.getInstance().compress(list).execute().onFinish(new OnCompressFinishListener(){});
    Light.getInstance().compress(list).enqueue();