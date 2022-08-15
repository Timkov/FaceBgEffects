package opu.timkov.effects.filters;

import android.content.Context;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import opu.timkov.core.FrameProcessor;

public class Cartoon extends LutEffect {

    private Mat dst;

    private final int NUM_RED = 10;

    private final int NUM_GREEN = 15;

    private final int NUM_BLUE = 80;


    @Override
    public void init(Size size, Context context) {
        dst = new Mat(size, FrameProcessor.MAT_TYPE_4_CH);
    }

    @Override
    public Mat process(Mat src) {
        dst = cartoon(src, NUM_RED, NUM_GREEN, NUM_BLUE);
        dst = reduceColors(dst, 0, NUM_GREEN, NUM_RED);
        return dst;
    }

    private Mat cartoon(Mat img, int numRed, int numGreen, int numBlue) {
        Mat reducedColorImage = reduceColors(img, numRed, numGreen, numBlue);

        Mat result = new Mat();
        Imgproc.cvtColor(img, result, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.medianBlur(result, result, 15);

        Imgproc.adaptiveThreshold(result, result, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY, 15, 2);

        Imgproc.cvtColor(result, result, Imgproc.COLOR_GRAY2BGRA);

        Core.bitwise_and(reducedColorImage, result, result);

        return result;
    }
}
