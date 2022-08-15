package opu.timkov.core;

import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.opencv.core.Mat;
import org.opencv.core.Size;

import opu.timkov.R;
import opu.timkov.effects.manager.EffectsManager;
import opu.timkov.segmentation.SegmentationProvider;
import opu.timkov.ui.UiController;
import opu.timkov.ui.ActivityProxy;

public class Core {

    private final Size size = new Size(1056, 704);

    private EffectsManager effectsManager;

    private SegmentationProvider segmentationProvider;

    private FrameProcessor frameProcessor;

    private UiController uiController;

    private CameraSwitcher cameraSwitcher;

    private Recorder recorder;

    public Core(ActivityProxy proxy) {
        effectsManager = new EffectsManager(size, proxy.getContext());
        segmentationProvider = new SegmentationProvider(proxy.getAssets(), proxy.getHandler());
        frameProcessor = new FrameProcessor(segmentationProvider, effectsManager, size);
        recorder = new Recorder(size);
        cameraSwitcher = new CameraSwitcher(proxy.getCameraView(), recorder);
        uiController = new UiController(effectsManager, recorder, cameraSwitcher, proxy);
    }

    public Mat process(Mat frame) {
        Mat frameRes = frameProcessor.process(frame);
        recorder.write(frameRes);
        return frameRes;
    }
}
