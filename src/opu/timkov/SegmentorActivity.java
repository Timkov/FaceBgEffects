package opu.timkov;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.util.Collections;
import java.util.List;

import opu.timkov.core.Core;
import opu.timkov.ui.ActivityProxy;

public class SegmentorActivity extends CameraActivity implements CvCameraViewListener2 {
    private static final String TAG = "OPU::Timkov::FaceView";

    private CameraBridgeViewBase mCameraView;

    private Core core;

    private Handler handler;

    private HandlerThread handlerThread;

    private void initCore() {
        if (core != null) { return; }
        core = new Core(new ActivityProxy(this, handler, mCameraView));
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        return core.process(inputFrame.rgba());
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mCameraView.setCameraIndex(1);
                    mCameraView.enableView();
                    initCore();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.tutorial1_surface_view);
        mCameraView = (CameraBridgeViewBase) findViewById(R.id.segmentor_activity_java_surface_view);
        mCameraView.setVisibility(SurfaceView.VISIBLE);
        mCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mCameraView != null)
            mCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        handlerThread = new HandlerThread("front");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mCameraView);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mCameraView != null)
            mCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) { }

    @Override
    public void onCameraViewStopped() { }

}
