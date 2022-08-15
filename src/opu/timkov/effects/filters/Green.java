package opu.timkov.effects.filters;

import android.content.Context;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import opu.timkov.effects.Effect;

import static org.opencv.core.Core.LUT;

public class Green extends LutEffect {


    private Mat dst;

    @Override
    public void init(Size size, Context context) {

        dst = new Mat((int) size.width, (int) size.height, CvType.CV_8UC3);
    }

    @Override
    public Mat process(Mat src) {
        System.out.println(src.type() + " " +  + dst.type() + " " + CvType.CV_8UC3);
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2RGB);
        System.out.println(src.type() + " " + dst.type() + " " + CvType.CV_8UC3);
        Imgproc.applyColorMap(dst, dst, Imgproc.COLORMAP_PINK);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_RGB2BGRA);
        return dst;
    }
}
