package com.dmcc.image_preview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;
import java.io.InputStream;

import okio.BufferedSink;
import okio.Okio;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/4/29.
 */
public class ImagePreviewPresenter extends BasePresenter<ImagePreviewContract.ImagePreviewView> implements ImagePreviewContract.ImagePreviewPresenter {
    private Context mContext;

    private OkHttpHelper mOkHttpHelper;

    public ImagePreviewPresenter(OkHttpHelper okHttpHelper, Context context) {
        mOkHttpHelper = okHttpHelper;
        mContext = context;
    }

    @Override
    public void shareImage(String url) {
        shareSingleImage(url);
    }

    @Override
    public void saveImage(final String url) {
        Observable.just(url)
                .map(new Func1<String, InputStream>() {
                    @Override
                    public InputStream call(String s) {
                        return getImageBytesFromLocal(Uri.parse(s));
                    }
                })
                .map(new Func1<InputStream, File>() {
                    @Override
                    public File call(InputStream in) {
                        if (in != null) {
                            String fileName = getFileNameFromUrl(url);
                            File target = new File(getPicSavePath(), fileName);
                            if (target.exists()) {
                                return target;
                            }
                            try {
                                BufferedSink sink = Okio.buffer(Okio.sink(target));
                                sink.writeAll(Okio.source(in));
                                sink.close();
                                return target;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                })
                .map(new Func1<File, File>() {
                    @Override
                    public File call(File file) {
                        if (file != null && file.exists()) {
                            return file;
                        }
                        try {
                            mOkHttpHelper.httpDownload(url, file);
                            return file;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .doOnNext(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        if (file != null && file.exists()) {
                            scanPhoto(file);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        if (file != null && file.exists()) {
                            Toast.makeText(mContext, R.string.save_ok, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, R.string.sava_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private InputStream getImageBytesFromLocal(Uri loadUri) {
        if (loadUri == null) {
            return null;
        }
        CacheKey cacheKey =
                DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
        try {
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                return ImagePipelineFactory.getInstance()
                        .getMainDiskStorageCache()
                        .getResource(cacheKey)
                        .openStream();
            }
            if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                return ImagePipelineFactory.getInstance()
                        .getSmallImageDiskStorageCache()
                        .getResource(cacheKey)
                        .openStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void scanPhoto(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void copyImagePath(String url) {
        copy(url);
    }

    public static String getFileNameFromUrl(String url) {
        int index = url.lastIndexOf('?');
        String filename;
        if (index > 1) {
            filename = url.substring(url.lastIndexOf('/') + 1, index);
        } else {
            filename = url.substring(url.lastIndexOf('/') + 1);
        }
        return filename;
    }

    private static String picSavePath;

    public String getPicSavePath() {
        if (!TextUtils.isEmpty(picSavePath)) {
            return picSavePath;
        }
        picSavePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + getPicSavePath1()
                + File.separator;
        File file = new File(picSavePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return picSavePath;
    }

    public String getPicSavePath1() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString("PicSavePath", "gzsll");
    }

    public void shareSingleImage(String url) {
        Uri imageUri = Uri.fromFile(new File(picSavePath));
        Log.d("share", "uri:" + imageUri);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        mContext.startActivity(Intent.createChooser(shareIntent, mContext.getResources().getString(R.string.shareto)));
    }

    public void copy(String stripped) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard =
                    (android.text.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(stripped);
        } else {
            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("content", stripped);
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(mContext, R.string.copy_ok, Toast.LENGTH_SHORT).show();
    }

}
