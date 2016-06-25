项目中用到图片浏览 拆分出来 以后方便使用
module使用 rxjava + okhttp3 +  fresco
所以项目中引用以后  会增大安装包体积2m左右
如果你的项目中没有使用rxjava 和 okhttp3 和fresco的图片加载器的话 请慎用，介意安装包大小慎用，适用 rxjava + okhttp + retrofit 构建的项目

[源码下载](https://github.com/wushge11/ImagePreview)
 支持加载本地图片和网络图片 手势放大缩小查看， 保存本地 复制链接 优化缓存， 
 看效果，
![这里写图片描述](http://img.blog.csdn.net/20160625111900812)

使用方法：
在你的项目中添加如下依赖

```
    compile 'com.android.support:appcompat-v7:23.4.0'
    compile 'com.android.support:support-v13:23.3.0'
    compile 'com.android.support:design:23.4.0'
    compile 'com.facebook.fresco:fresco:0.10.0'
    compile 'com.facebook.fresco:imagepipeline-okhttp3:0.10.0'
    compile 'com.facebook.fresco:animated-gif:0.10.0'
    compile 'io.reactivex:rxjava:1.1.0'
    compile 'io.reactivex:rxandroid:1.1.0'
    compile 'com.squareup.okhttp3:okhttp:3.3.1'
    compile 'com.github.castorflex.smoothprogressbar:library:1.1.0'
    compile 'com.wushange:image-preview:1.0'
```



然后再manifest 中添加activity

```
 <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
  <activity android:name="com.dmcc.image_preview.ImagePreviewActivity"/>
```

然后记得初始化 fresco， 在application中

```
 private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    public static final int MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 8;

    private void initFrescoConfig() {
        final MemoryCacheParams bitmapCacheParams =
                new MemoryCacheParams(MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                        Integer.MAX_VALUE,                     // Max entries in the cache
                        MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                        Integer.MAX_VALUE,                     // Max length of eviction queue
                        Integer.MAX_VALUE);
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(this, ImagePreviewActivity.getOkHttpClient())
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setBitmapMemoryCacheParamsSupplier(new Supplier<MemoryCacheParams>() {
                    public MemoryCacheParams get() {
                        return bitmapCacheParams;
                    }
                })
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(this).setMaxCacheSize(MAX_DISK_CACHE_SIZE).build())
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);
    }
```


然后在需要使用的地方 填充图片地址集合 和index

```
 ImagePreviewActivity.startActivity(MainActivity.this, extraPics.get(0), extraPics);
```


[源码下载](https://github.com/wushge11/ImagePreview)
自身项目有的就不用添加了

这就是我自己为了方便写的小玩意， 介意安装包大小慎用，适用 rxjava + okhttp + retrofit 构建的项目
