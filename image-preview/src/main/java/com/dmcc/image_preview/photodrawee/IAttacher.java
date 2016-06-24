package com.dmcc.image_preview.photodrawee;

import android.view.GestureDetector;
import android.view.View;


public interface IAttacher {

  public static final float DEFAULT_MAX_SCALE = 3.0f;
  public static final float DEFAULT_MID_SCALE = 1.75f;
  public static final float DEFAULT_MIN_SCALE = 1.0f;
  public static final long ZOOM_DURATION = 200L;

  float getMinimumScale();

  float getMediumScale();

  float getMaximumScale();

  void setMaximumScale(float maximumScale);

  void setMediumScale(float mediumScale);

  void setMinimumScale(float minimumScale);

  float getScale();

  void setScale(float scale);

  void setScale(float scale, boolean animate);

  void setScale(float scale, float focalX, float focalY, boolean animate);

  void setZoomTransitionDuration(long duration);

  void setAllowParentInterceptOnEdge(boolean allow);

  void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener listener);

  void setOnScaleChangeListener(OnScaleChangeListener listener);

  void setOnLongClickListener(View.OnLongClickListener listener);

  void setOnPhotoTapListener(OnPhotoTapListener listener);

  void setOnViewTapListener(OnViewTapListener listener);

  OnPhotoTapListener getOnPhotoTapListener();

  OnViewTapListener getOnViewTapListener();

  void update(int imageInfoWidth, int imageInfoHeight);
}
