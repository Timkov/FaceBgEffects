package opu.timkov.effects.filters;

import android.content.Context;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

import opu.timkov.R;
import opu.timkov.effects.Effect;

public class VirtualBgEffect implements Effect {

    private Mat dst;

    protected int bgImageId = R.drawable.nature;

    @Override
    public void init(Size size, Context context) {
        try {
            dst = Utils.loadResource(context, bgImageId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Imgproc.resize(dst, dst, size);
        Core.flip(dst, dst, 0);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_RGB2BGRA);
    }

    @Override
    public Mat process(Mat src) {
        src.release();
        return dst;
    }
}
