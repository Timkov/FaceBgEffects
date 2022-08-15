package opu.timkov.core;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;


import opu.timkov.effects.manager.EffectType;
import opu.timkov.effects.manager.EffectsApplier;
import opu.timkov.segmentation.SegmentationObserver;
import opu.timkov.segmentation.SegmentationProvider;

public class FrameProcessor implements SegmentationObserver {

    public static final int MAT_TYPE_4_CH = 24;

    private SegmentationProvider segmentationProvider;
    private EffectsApplier effectsApplier;

    private Mat frame;
    private Mat face;
    private Mat segMask;
    private Mat segMaskInv;
    private Mat bg;
    private Mat faceTmp;
    private Mat bgTmp;
    private Bitmap inputBm;
    private Bitmap segBm;

    public FrameProcessor(SegmentationProvider segProvider, EffectsApplier effApplier, Size size) {
        segmentationProvider = segProvider;
        effectsApplier = effApplier;

        frame = new Mat();
        face = new Mat();
        segMask = new Mat();
        segMaskInv = new Mat();
        bg = new Mat();
        faceTmp = new Mat();
        bgTmp = new Mat();
        inputBm = Bitmap.createBitmap((int)size.width, (int)size.height, Bitmap.Config.ARGB_8888);
        segBm = Bitmap.createBitmap((int)size.width, (int)size.height, Bitmap.Config.ARGB_8888);
       // segmentationProvider.setObserver(this);
    }

    @Override
    public void onMaskUpdated(Bitmap newMask) {
        segBm = newMask;
        Utils.bitmapToMat(segBm, segMask);
        Core.bitwise_not(segMask, segMaskInv);
    }

    public Mat process(Mat input) {
        frame = input;
        Utils.matToBitmap(frame, inputBm);
        segBm = segmentationProvider.getSegmentationMask(inputBm);

        Utils.bitmapToMat(segBm, segMask);
        Core.bitwise_not(segMask, segMaskInv);

        bg = effectsApplier.applyEffects(frame.clone(), EffectType.BG);
        face = effectsApplier.applyEffects(frame, EffectType.FACE);

        Core.bitwise_and(face, segMask, faceTmp);
        Core.bitwise_and(bg, segMaskInv, bgTmp);

        Core.add(faceTmp, bgTmp, frame);

        return frame;
    }
}
