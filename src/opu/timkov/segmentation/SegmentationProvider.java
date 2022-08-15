package opu.timkov.segmentation;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Handler;

import java.lang.reflect.Array;

public class SegmentationProvider {
    private Handler handler;

    private volatile boolean isRunning = false;

    private Bitmap bmp;
    private Bitmap actualSegMask;
    private final int color = Color.argb(255, 255, 255, 255);
    private final int noColor = Color.argb(0, 0, 0, 0);
    private float[][][] resultPixels;
    private int[] pixels;

    private static final int TF_OD_API_INPUT_WIDTH = 257;
    private static final int TF_OD_API_INPUT_HEIGHT = 257;
    private static final int TF_OD_API_NUM_CLASS = 21;
    private static final int TF_OD_API_NUM_OUTPUT = 21;
    private static final String TF_OD_API_MODEL_FILE = "deeplabv3_257_mv_gpu.tflite";
    private static final String TF_OD_API_LABELS_FILE = "file:///android_asset/pascal_voc_labels_list.txt";

    private Segmentor segmentor;

    public SegmentationProvider(AssetManager assetManager, Handler handler) {
        try {
            segmentor = TFLiteObjectSegmentationAPIModel.create(
                    assetManager,
                    TF_OD_API_MODEL_FILE,
                    TF_OD_API_LABELS_FILE,
                    TF_OD_API_INPUT_WIDTH,
                    TF_OD_API_INPUT_HEIGHT,
                    TF_OD_API_NUM_CLASS,
                    TF_OD_API_NUM_OUTPUT);
        } catch (Exception ex) {
            System.err.println("Unable to initialize segmentor");
            ex.printStackTrace();
        }
        this.handler = handler;
    }

    public Bitmap getSegmentationMask(Bitmap image) {
        if (!isRunning) {
            if (actualSegMask == null) {
                segment(image);
            } else {
                runInBackground(image);
            }
        }
        return actualSegMask;
    }

    private synchronized void runInBackground(final Bitmap image) {
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    isRunning = true;
                    segment(image);
                    isRunning = false;
                }
            });
        }
    }

    private synchronized void segment(Bitmap image) {
        Bitmap scaledImage = getResizedBitmap(image, TF_OD_API_INPUT_WIDTH, TF_OD_API_INPUT_HEIGHT);
        handleSegmentation(segmentor.segmentImage(scaledImage));
        actualSegMask = getResizedBitmap(bmp, image.getWidth(), image.getHeight());
    }

    private void handleSegmentation(final Segmentor.Segmentation potential) {

        int width = potential.getWidth();
        int height = potential.getHeight();
        if(bmp == null) {
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        if(pixels == null) {
            pixels = new int[bmp.getHeight()*bmp.getWidth()];
        }
        resultPixels = potential.getPixels();

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                int maxV = 0;
                int classNo = 0;
                for (int z = 0; z < TF_OD_API_NUM_CLASS; ++z) {
                    int v = (int)resultPixels[j][i][z];
                    if (z == 0 || v > maxV) {
                        maxV = v;
                        classNo = z;
                    }
                }
                if (classNo == 15) {
                    pixels[j*bmp.getWidth()+i] = color;

                } else {
                    pixels[j*bmp.getWidth()+i] = noColor;
                }
            }
        }
        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }
}
