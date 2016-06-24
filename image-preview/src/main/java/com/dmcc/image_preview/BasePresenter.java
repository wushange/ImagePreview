package com.dmcc.image_preview;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<T> {
    public T mView;
    protected Subscription mSubscription;
    protected CompositeSubscription mCompositeSubscription;

    public void attachView(T view) {
        this.mView = view;
        if (mCompositeSubscription == null)
            mCompositeSubscription = new CompositeSubscription();
    }


    public void detachView() {
        mCompositeSubscription.unsubscribe();
        mView = null;
    }

}
