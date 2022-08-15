package opu.timkov.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;

import android.os.Handler;

import org.opencv.android.CameraBridgeViewBase;

public class ActivityProxy {

    private Activity activity;

    private Handler handler;

    private CameraBridgeViewBase cameraView;

    public ActivityProxy(Activity activity, Handler handler, CameraBridgeViewBase camView) {
        this.activity = activity;
        this.handler = handler;
        this.cameraView = camView;
    }

    public Context getContext() {
        return (Context) activity;
    }

    public View findViewById(int id) {
        return activity.findViewById(id);
    }

    public AssetManager getAssets() {
        return activity.getAssets();
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraBridgeViewBase getCameraView() {
        return cameraView;
    }

}
