package com.dmcc.image_preview;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.dmcc.image_preview.base.BaseSwipeBackActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;


/**
 * Created by sll on 2016/3/10.
 */
public class ImagePreviewActivity extends BaseSwipeBackActivity<ImagePreviewContract.ImagePreviewView, ImagePreviewPresenter>
        implements ViewPager.OnPageChangeListener, ImagePreviewContract.ImagePreviewView {

    public static void startActivity(Context mContext, String extraPic, ArrayList<String> extraPics) {
        Intent intent = new Intent(mContext, ImagePreviewActivity.class);
        intent.putExtra("extraPic", extraPic);
        intent.putExtra("extraPics", extraPics);
        mContext.startActivity(intent);
    }

    public static OkHttpClient okHttpClient;
    public static String Cache_Root_Dir;

    public static final String CACHE_DIR = "http_cache_dir";
    ViewPager viewPager;
    Toolbar toolbar;

    private HashMap<Integer, ImageFragment> fragmentMap = new HashMap<>();
    private ImageViewAdapter mImageViewAdapter;
    private int mCurrentItem = 0;
    private List<String> extraPics;
    private String extraPic;

    @Override
    public int bindLayout() {
        return R.layout.activity_preview;
    }

    @Override
    public void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setPadding(0, getStatusBarHeight(getContext()), 0, 0);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        initToolBar(toolbar);
        extraPics = getIntent().getStringArrayListExtra("extraPics");
        extraPic = getIntent().getStringExtra("extraPic");
        initViewPager();
        initCurrentItem();
    }

    @Override
    public void doBusiness(Context mContext) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
    }

    private void initViewPager() {
        mImageViewAdapter = new ImageViewAdapter(getFragmentManager());
        viewPager.setAdapter(mImageViewAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    void initCurrentItem() {
        mCurrentItem = extraPics.indexOf(extraPic);
        if (mCurrentItem < 0) {
            mCurrentItem = 0;
        }
        viewPager.setCurrentItem(mCurrentItem);
        getSupportActionBar().setTitle((mCurrentItem + 1) + "/" + extraPics.size());
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentItem = position;
        getSupportActionBar().setTitle((position + 1) + "/" + mImageViewAdapter.getCount());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void endLoading() {

    }

    @Override
    public ImagePreviewPresenter initPresenter() {

        return new ImagePreviewPresenter(new OkHttpHelper(getOkHttpClient()), getContext());
    }


    public class ImageViewAdapter extends FragmentStatePagerAdapter {

        public ImageViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ImageFragment fragment = fragmentMap.get(position);
            if (fragment == null) {
                fragment = ImageFragment.newInstance(extraPics.get(position));
                fragmentMap.put(position, fragment);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return extraPics.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof Fragment) {
                fragmentMap.put(position, (ImageFragment) object);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.savePicture) {
            presenter.saveImage(extraPics.get(viewPager.getCurrentItem()));
        }  else if (id == R.id.copy) {
            presenter.copyImagePath(extraPics.get(viewPager.getCurrentItem()));
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.downloadAgain) {
            ImageFragment fragment =
                    ((ImageFragment) mImageViewAdapter.getItem(viewPager.getCurrentItem()));
            if (fragment != null) {
                fragment.doBusiness(getContext());
            }
        }
        return true;
    }

    public void initToolBar(Toolbar mToolBar) {
        if (null != mToolBar) {
            setSupportActionBar(mToolBar);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            File cacheFile = new File(Cache_Root_Dir, CACHE_DIR);
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
            okHttpClient = new OkHttpClient.Builder()
                    .cache(cache).connectTimeout(15, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }

    public int getStatusBarHeight(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
