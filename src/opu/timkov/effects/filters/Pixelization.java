package opu.timkov.effects.filters;

import android.content.Context;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import opu.timkov.core.FrameProcessor;
import opu.timkov.effects.Effect;

public class Pixelization implements Effect {

    private Mat dst;

    private Mat tmp;

    private Size smallerSize = new Size(50, 50);

    private Size originalSize;

    @Override
    public void init(Size size, Context context) {
        originalSize = size;
        tmp = new Mat((int) smallerSize.width, (int) smallerSize.height, FrameProcessor.MAT_TYPE_4_CH);
        dst = new Mat((int) originalSize.width, (int)originalSize.height, FrameProcessor.MAT_TYPE_4_CH);
    }

    @Override
    public Mat process(Mat src) {
        Imgproc.resize(src, tmp, smallerSize);
        Imgproc.resize(tmp, dst, originalSize);
        return dst;
    }
}
