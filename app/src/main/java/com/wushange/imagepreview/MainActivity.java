package com.wushange.imagepreview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dmcc.image_preview.ImagePreviewActivity;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> extraPics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFrescoConfig();
        for (int i = 0; i < 5; i++) {
            extraPics.add("http://img2081.poco.cn/mypoco/myphoto/20130102/15/37946792201301021535501010906275817_001.jpg");
            extraPics.add("http://image6.huangye88.com/2013/03/28/2a569ac6dbab1216.jpg");
            extraPics.add("http://i.epetbar.com/2014-06/07/a08a0303f1e8e41b880588891c453b16.jpg");
            extraPics.add("http://www.shifenkafei.com/data/upload/553deb1621af2.jpg");
            extraPics.add("http://zhanhui.3158.cn/data/attachment/exhibition/data/attachment/exhibition/article/2015/12/17/4d80cdd4500e52ff5c7fbd600c0a7a84.jpg");
            extraPics.add("http://www.meiwai.net/uploads/allimg/c150907/14416140N1GZ-51951.jpg");
            extraPics.add("http://d6.yihaodianimg.com/N03/M04/16/3D/CgQCs1N5MUiATsxVAADJLWXi5mk84700.jpg");
            extraPics.add("http://www.51pinwei.com/uploads/allimg/140415/1322-140415112HNZ.jpg");
        }

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePreviewActivity.startActivity(MainActivity.this, extraPics.get(0), extraPics);
            }
        });
    }


    //建议在application中初始化
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
}
