package opu.timkov.effects.filters;

import android.content.Context;
import android.graphics.Color;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import opu.timkov.core.FrameProcessor;
import opu.timkov.effects.Effect;

public class Draw implements Effect {

    private final int BLOCK_SIZE = 9;
    private final int C = 2;
    private final int K_SIZE = 5;
    private Mat dst;

    @Override
    public void init(Size size, Context context) {
         dst = new Mat((int)size.height, (int)size.width, FrameProcessor.MAT_TYPE_4_CH);
    }

    @Override
    public Mat process(Mat src) {
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.adaptiveThreshold(dst, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY, BLOCK_SIZE, C);
        Imgproc.medianBlur(dst, dst, K_SIZE);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_GRAY2BGRA);
        return dst;
    }
}
