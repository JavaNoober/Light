# Light
a lightweight image compress framework for Android based on libJpeg.
一个基于libJpeg的压缩图片框架。(逐步更新中...)

support types:
    File,String,Uri,Bytes,Bitmap,DrawableResourceID,Drawable

 * 1:设置文件最大值 //todo
 * 2:图片的旋转缩放
 * 3:异步并发压缩 //todo
 * 4:同步压缩

 ### 版本记录: 
       
       0.0.1 完成大致功能
       //未发布
       0.0.2 修复从uri获取路径错误的bug;
             增加ignoreSize的设置，以便用于压缩图片保持原尺寸;
 ### 使用方法: 
 
    引入
    compile 'com.noober.light:core:0.0.1'
    
    android {
        ...
        ndk {
            abiFilters 'armeabi-v7a', 'armeabi', 'x86'
        }
    }
    
 #### 设置压缩参数:
 CompressArgs这个类用于设置每次所要压缩到的指定的宽高以及压缩质量。使用如下:
 
    CompressArgs args = new CompressArgs.Builder().width(width).height(height).quality(70).ignoreSize(false).build();
 
 ##### 参数说明：
  1.width: 要压缩到的图片的宽度，单位px
  2.height: 要压缩到的图片的高度，单位px
  3.quality: 压缩质量, 可选范围是0-100。
  4.ignoreSize: 是否要忽略压缩后图片宽高的限制, 如果设为true，压缩的时候会根据原图本身大小进行压缩，设置的width和height就会失效，默认false。
 
 ##### 默认参数：
 调用compress方法的时候也可以不初始化这个参数，框架会自动初始化一个默认的压缩设置。
 
**默认规则**如下：
    压缩图片的最大默认高度是屏幕高度；
    图片的最大宽度是屏幕宽度；
    默认压缩质量是85；
    ignoreSize 为false。
    
 ##### 设置全局的压缩参数，以便不需要每次都去设置：    
 建议在Application的onCreate中进行初始化，使用如下：
 
    LightConfig lightConfig = new LightConfig();
    lightConfig.setDefaultQuality(xx);
    lightConfig.setMaxWidth(xx);
    lightConfig.setMaxHeight(xx);
    lightConfig.setMaxHeight(xx);
    lightConfig.setNeedIgnoreSize(xx);
    Light.getInstance().setConfig(lightConfig);
    
    
 #### 压缩图片，获取Bitmap：images -> Bitmap
 
 支持**File,String,Uri,Bytes,Bitmap,DrawableResourceID,Drawable**这5种类型压缩为Bitmap。
    
    
    //String,Uri,Bytes,Bitmap,File -->bitmap
    Bitmap bitmap = Light.getInstance().compress(path);
    //args为CompressArgs压缩参数，不传入，即用默认参数
    Bitmap bitmap = Light.getInstance().compress(path, args);
    .....
    
 #### 压缩图片并且保存到本地：images -> boolean    
 支持**File,String,Uri,Bytes,Bitmap**这5种类型保存为文件。
 想对于压缩成Bitmap的方法只是多了一个输出路径。
 
    //images --> boolean //保存图片，返回值代表保存是否成功
    //img为要压缩的图片，args是压缩参数，不传即默认, outPath为输出路径
    Light.getInstance().compress(img, args, outPath);
    Light.getInstance().compress(img, outPath);
    //支持类型Bitmap, Bytes, String, Resource, Uri, Drawable
    
 #### 其他
 
 * 1:获取bitmap占用内存的大小，返回值单位是kb：
 
 
    MemoryComputeUtil.getMemorySize(compressBitmap)
    
    
    
 ### TODO:


    //todo
    List<Integer> list = new ArrayList<>();
    ...
    Light.getInstance().compress(list).execute().onFinish(new OnCompressFinishListener(){});
    Light.getInstance().compress(list).enqueue();