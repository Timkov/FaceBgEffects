package opu.timkov.effects.filters;

import android.content.Context;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import opu.timkov.effects.Effect;

public class BlackAndWhite implements Effect {

    private Mat grey;

    private Mat dst;

    private int tresh = 127;

    private int maxVal = 255;

    @Override
    public void init(Size size, Context context) {
        grey = new Mat((int) size.height, (int) size.width, CvType.CV_8U);
        dst = new Mat();
    }

    @Override
    public Mat process(Mat src) {
        System.out.println(src.type() + " " +  + dst.type() + " " + CvType.CV_8UC3);
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2RGB);
        System.out.println(src.type() + " " + dst.type() + " " + CvType.CV_8UC3);
        Imgproc.applyColorMap(dst, dst, Imgproc.COLORMAP_WINTER);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_RGB2BGRA);
        return dst;
    }
}
