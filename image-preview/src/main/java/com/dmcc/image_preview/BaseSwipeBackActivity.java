package com.dmcc.image_preview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dmcc.image_preview.swipebacklayout.activity.SwipeBackActivity;

import java.lang.ref.WeakReference;



/**
 *
 * @author wushange
 * @version 1.0
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public abstract class BaseSwipeBackActivity<V, T extends BasePresenter<V>> extends SwipeBackActivity implements IBaseActivity {
    public T presenter;
    protected WeakReference<Activity> context = null;
    protected View mContextView = null;
    boolean autoDissIm = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = new WeakReference<Activity>(this);
        mContextView = LayoutInflater.from(this).inflate(bindLayout(), null);
        setContentView(mContextView);
        initP();
        initView(mContextView);
        doBusiness(this);

    }

    public abstract T initPresenter();


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        detachP();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeP();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachP();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachP();

    }


    private void initP() {
        presenter = initPresenter();
        if (presenter != null)
            presenter.attachView((V) this);
    }

    private void resumeP() {
        if (presenter != null)
            presenter.attachView((V) this);
    }

    private void detachP() {
        if (presenter != null)
            presenter.detachView();
    }

    public boolean isAutoDissIm() {
        return autoDissIm;
    }

    public void setAutoDissIm(boolean autoDissIm) {
        this.autoDissIm = autoDissIm;
    }


    protected Activity getContext() {
        if (null != context)
            return context.get();
        else
            return null;
    }


}
