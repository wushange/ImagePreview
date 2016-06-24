package com.dmcc.image_preview.photodrawee;


public interface OnScaleDragGestureListener {
  void onDrag(float dx, float dy);

  void onFling(float startX, float startY, float velocityX, float velocityY);

  void onScale(float scaleFactor, float focusX, float focusY);

  void onScaleEnd();
}
