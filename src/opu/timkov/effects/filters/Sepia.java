package opu.timkov.effects.filters;

import android.content.Context;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;


import opu.timkov.effects.Effect;

public class Sepia implements Effect {

    private Mat kernel;

    private Mat dst;

    @Override
    public void init(Size size, Context context) {
        kernel = new Mat(4, 4, CvType.CV_32F);
        kernel.put(0, 0, 0.189f, 0.769f, 0.393f, 0f);
        kernel.put(1, 0, 0.168f, 0.686f, 0.349f, 0f);
        kernel.put(2, 0,0.131f, 0.534f, 0.272f, 0f);
        kernel.put(3, 0, 0.000f, 0.000f, 0.000f, 1f);

        dst = new Mat();
    }

    @Override
    public Mat process(Mat src) {
        Core.transform(src, dst, kernel);
        return dst;
    }
}
