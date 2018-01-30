# Light

[![license](https://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](https://github.com/JavaNoober/Light)
[![JCenter](https://img.shields.io/badge/JCenter-Light-green.svg?style=flat)](https://bintray.com/noober/maven/Light)

a lightweight image compress framework for Android based on libJpeg.
一个基于libJpeg的压缩图片框架, 支持配合rxjava使用。
### demo效果
 
 先展示一下压缩前后的效果对比，以及文件大小和占用内存的大小
 demo比较简单，运行的时候请打开sd卡权限和相册拍照权限。
 
 ![](https://raw.githubusercontent.com/JavaNoober/Light/master/demo.jpg)

### 基本功能:
支持的压缩类型:
    File,String,Uri,Bytes,Bitmap,DrawableResourceID,Drawable

 * 1:图片的旋转、缩放、平移操作
 * 2:异步和同步压缩处理
 * 3:同步压缩
 * 4:可以配合RxJava2使用
 
 ### 版本记录: 
       
       1.0.0 完成大致功能
       1.1.0 修复从uri获取路径错误的bug;
             增加ignoreSize的设置，以便用于压缩图片保持原尺寸;
             支持配合rxjava2使用;
       1.0.1 RxLight去除线程切换控制，让开发者自己去指定线程;
             
 ### 使用方法: 
   
	    android {
	        ...
	        ndk {
	            abiFilters 'armeabi-v7a', 'armeabi', 'x86'
	        }
	    }
	    
	    //引入
	    implementation 'com.noober.light:core:1.1.0'
	    
	    //如果要配合rxjava2,加入rxjava2的依赖
	    implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
	    implementation 'io.reactivex.rxjava2:rxjava:2.1.7'
   
    
 #### 设置压缩参数:
 CompressArgs这个类用于设置每次所要压缩到的指定的宽高以及压缩质量。使用如下:
 
    CompressArgs args = new CompressArgs.Builder().width(width).height(height).quality(70).ignoreSize(false).build();
 
 ##### 参数说明：
  1.width: 要压缩到的图片的宽度，单位px
  2.height: 要压缩到的图片的高度，单位px
  3.quality: 压缩质量, 可选范围是0-100。
  4.ignoreSize: 是否要忽略压缩后图片宽高的限制, 如果设为true，压缩的时候会根据原图本身大小进行压缩，设置的width和height就会失效，默认false。
  因为从网络下载图片保存到本地，中间默认会自动压缩图片，如果不想对图片进行压缩，并保持宽高的话,设置如下参数即可：
  
    CompressArgs args = new CompressArgs.Builder().quality(100).ignoreSize(true).build();
  
 
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
    
#### 配合RxJava2使用
 关于配合RxJava2使用的类都在RxLight这个类中。
 通过与rxJava的配合，一行代码就能实现从网络下载->压缩->显示，这个过程，非常的方便。
 
 使用方法如下：
##### 从网络获取资源
 
 RxLight会自动对图片进行下载 -> 压缩 -> 显示或保存到本地
###### 从网络获取资源 -> Bitmap:
    
    //uri类型的网络资源
    Flowable.just(uri).compose(RxLight.compressForUriHttp()).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
    List<Uri> urlList = new ArrayList<>();
    ....
    Flowable.fromIterable(urlList).compose(RxLight.compressForUriHttp()).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
    //url类型的网络资源
    Flowable.just(url).compose(RxLight.compressForStringHttp()).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
    List<String> urlList = new ArrayList<>();
    ....
    Flowable.fromIterable(urlList).compose(RxLight.compressForStringHttp()).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
    //或者传入自定义的压缩参数只需要在compressForUriHttp() 和 compressForStringHttp()传入CompressArgs即可
    CompressArgs args = new CompressArgs();
    ....
    Flowable.just(uri).compose(RxLight.compressForUriHttp(args)).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
    
###### 从网络获取资源 -> boolean(保存到本地):
 与上述方法类似，只需要compressForUri() 和 compressForString()传入要保存到的路径即可。同样支持uri和string的网络地址类型。
           
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pic.jpg";
    Flowable.just(uri).compose(RxLight.compressForUriHttp(path)).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
    //自定义压缩参数
    CompressArgs args = new CompressArgs();
    ....
    Flowable.just(uri).compose(RxLight.compressForUriHttp(path, args)).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
        
##### 通过本地资源异步压缩
 同样都支持 File,String,Uri,Bytes,Bitmap,DrawableResourceID,Drawable这几种类型
###### 图片资源 -> Bitmap
 通过RxLight.compress()方法即可:
 
    Flowable.just(image).compose(RxLight.compress()).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
 
###### 图片资源 -> Boolean(保存到本地):
 通过RxLight.compress(outPath)方法即可:
 
    Flowable.just(image).compose(RxLight.compress(outPath)).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
 
#### 其他
 * 1:MemoryComputeUtil工具类，获取bitmap占用内存的大小，返回值单位是kb。
 

	     MemoryComputeUtil.getMemorySize(compressBitmap)
    
 * 2:关于so文件的编译，可以移步[https://github.com/JavaNoober/LibJpegCompress](https://github.com/JavaNoober/LibJpegCompress)。
 如何去编译，我在很久之前的写过一个博客介绍过:[http://blog.csdn.net/qq_25412055/article/details/53878655](http://blog.csdn.net/qq_25412055/article/details/53878655)
 
 * 3:MatrixUtil工具类
 可以很方便的对Bitmap进行放大缩小、旋转、缩放、平移等处理，使用也很方便，支持Matrix的大部分方法。
 

	    Bitmap result = new MatrixUtil.Build().scale(scaleSize, scaleSize).rotate(90f).bitmap(bitmap).build();
    
 ### 总结
 
 版本还在逐步更新中，欢迎各位大佬star，以及提出建议。

 GitHub demo下载地址：[https://github.com/JavaNoober/Light](https://github.com/JavaNoober/Light)