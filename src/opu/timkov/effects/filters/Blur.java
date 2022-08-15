package opu.timkov.effects.filters;

import android.content.Context;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import opu.timkov.core.FrameProcessor;
import opu.timkov.effects.Effect;

public class Blur implements Effect {

    private Mat dst;

    private Mat tmp;

    private int resizeFactor = 4;

    private Size smallerSize;

    private Size originalSize;

    private int kSize = 45;

    @Override
    public void init(Size size, Context context) {
        smallerSize = new Size(size.width / resizeFactor, size.height / resizeFactor);
        originalSize = size;
        tmp = new Mat((int) smallerSize.width, (int) smallerSize.height, FrameProcessor.MAT_TYPE_4_CH);
        dst = new Mat((int) originalSize.width, (int) originalSize.height, FrameProcessor.MAT_TYPE_4_CH);
    }

    @Override
    public Mat process(Mat src) {
        Imgproc.resize(src, tmp, smallerSize);
        Imgproc.medianBlur(tmp, tmp, kSize);
        Imgproc.resize(tmp, dst, originalSize);
        return dst;
    }
}
