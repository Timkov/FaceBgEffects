package opu.timkov.segmentation;

import android.graphics.Bitmap;

import java.util.Vector;


public interface Segmentor {

    public class Segmentation {

        private final float[][][] pixels;
        private final int numClass;
        private final int width;
        private final int height;
        private final long inferenceTime;
        private final long nativeTime;

        public Segmentation(
                final float[][][] pixels2,
                final int numClass,
                final int width, final int height, final long inferenceTime, final long nativeTime) {
            this.pixels = pixels2;
            this.numClass = numClass;
            this.width = width;
            this.height = height;
            this.inferenceTime = inferenceTime;
            this.nativeTime = nativeTime;
        }

        public float[][][] getPixels() { return pixels; }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public long getInferenceTime() {
            return inferenceTime;
        }

        public long getNativeTime() {
            return nativeTime;
        }

        public int getNumClass() {
            return numClass;
        }
    }

    Segmentation segmentImage(Bitmap bitmap);

    Vector<String> getLabels();
}
