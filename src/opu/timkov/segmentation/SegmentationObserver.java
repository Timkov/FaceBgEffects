package opu.timkov.segmentation;

import android.graphics.Bitmap;

public interface SegmentationObserver {
    void onMaskUpdated(Bitmap newMask);
}
