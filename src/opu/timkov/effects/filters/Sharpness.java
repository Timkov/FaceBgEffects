package opu.timkov.effects.filters;

import android.content.Context;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import opu.timkov.core.FrameProcessor;
import opu.timkov.effects.Effect;

public class Sharpness implements Effect {

    private Mat dst = new Mat();

    private Mat kernel;

    @Override
    public void init(Size size, Context context) {
        kernel = new Mat(3, 3, CvType.CV_32F);
        kernel.put(0, 0, 0, -1, 0);
        kernel.put(1, 0, -1, 5, -1);
        kernel.put(2, 0,0, -1, 0);
    }

    @Override
    public Mat process(Mat src) {
        Imgproc.filter2D(src, dst, -1, kernel);
        return dst;
    }
}
