package opu.timkov.effects.filters;

import android.content.Context;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.LUT;

public class Oil extends LutEffect {

    private final int NUM_COLORS = 5;

    private Mat dst;

    private Mat lookupTable;

    @Override
    public void init(Size size, Context context) {
        lookupTable = createLUT(NUM_COLORS);
        dst = new Mat();
    }

    @Override
    public Mat process(Mat src) {
        System.out.println(src.type() + " " +  + dst.type() + " " + CvType.CV_8UC3);
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2RGB);
        System.out.println(src.type() + " " + dst.type() + " " + CvType.CV_8UC3);
        Imgproc.applyColorMap(dst, dst, Imgproc.COLORMAP_SUMMER);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_RGB2BGRA);
        return dst;
    }
}
