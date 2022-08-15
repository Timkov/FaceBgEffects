package opu.timkov.core;

import org.opencv.android.CameraBridgeViewBase;

public class CameraSwitcher {

    private CameraBridgeViewBase cameraView;

    private Recorder recorder;

    private int cameraId;

    public CameraSwitcher(CameraBridgeViewBase camView, Recorder rec) {
        cameraView = camView;
        cameraId = 1;
        recorder = rec;
    }

    public void switchCamera() {
        cameraId ^= 1;
        cameraView.disableView();
        cameraView.setCameraIndex(cameraId);
        cameraView.enableView();
        recorder.setRotation(cameraId == 1 ? 90 : -90);
        if (cameraId == 0) {
            cameraView.setFixParameters(90, 1);
        } else {
            cameraView.setFixParameters(270, -1);
        }
    }
}
