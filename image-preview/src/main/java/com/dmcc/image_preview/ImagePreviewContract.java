package com.dmcc.image_preview;


/**
 * Created by sll on 2016/5/11.
 */
public interface ImagePreviewContract {
    interface ImagePreviewView extends BaseView {

    }

    interface ImagePreviewPresenter {
        void shareImage(String url);
        void saveImage(String url);

        void copyImagePath(String url);
    }
}
