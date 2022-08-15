package opu.timkov.effects;

import android.content.Context;

import org.opencv.core.Mat;
import org.opencv.core.Size;

public interface Effect {
    void init(Size size, Context context);
    Mat process(Mat src);
}
