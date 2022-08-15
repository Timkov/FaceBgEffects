

package opu.timkov.segmentation;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.os.Trace;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;


public class TFLiteObjectSegmentationAPIModel implements Segmentor {

  private final float MEAN = 128f;
  private final float STD = 128f;
  // Config values.
  private int inputWidth;
  private int inputHeight;
  private int numClass;
  public Vector<String> labels = new Vector<String>();

  // Pre-allocated buffers.
  private int[] intValues;
  private float[][][][] pixelClasses;
  protected FloatBuffer imgData = null;

  private Interpreter tfLite;

  private static MappedByteBuffer loadModelFile(AssetManager assets, String modelFilename)
      throws IOException {
    AssetFileDescriptor fileDescriptor = assets.openFd(modelFilename);
    FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
    FileChannel fileChannel = inputStream.getChannel();
    long startOffset = fileDescriptor.getStartOffset();
    long declaredLength = fileDescriptor.getDeclaredLength();
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
  }

  public static Segmentor create(
      final AssetManager assetManager,
      final String modelFilename,
      final String labelFilename,
      final int inputWidth,
      final int inputHeight,
      final int numClass, final int numOutput) throws IOException {

    final TFLiteObjectSegmentationAPIModel d = new TFLiteObjectSegmentationAPIModel();

    d.inputWidth = inputWidth;
    d.inputHeight = inputHeight;
    d.numClass = numClass;

    GpuDelegate delegate = new GpuDelegate(new GpuDelegate.Options().setQuantizedModelsAllowed(true));
    Interpreter.Options options = new Interpreter.Options().addDelegate(delegate);
    try {
      d.tfLite = new Interpreter(loadModelFile(assetManager, modelFilename), options);
      d.tfLite.setNumThreads(4);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    InputStream labelsInput = null;
    String actualFilename = labelFilename.split("file:///android_asset/")[1];
    labelsInput = assetManager.open(actualFilename);
    BufferedReader br = null;
    br = new BufferedReader(new InputStreamReader(labelsInput));
    String line;
    while ((line = br.readLine()) != null) {
      d.labels.add(line);
    }

    // Pre-allocate buffers.
    d.imgData = FloatBuffer.allocate(d.inputWidth*d.inputHeight * 3);
//    d.imgData.order(ByteOrder.nativeOrder());
    d.intValues = new int[d.inputWidth * d.inputHeight];
    d.pixelClasses = new float[1][d.inputHeight][d.inputWidth][numOutput];
    return d;
  }

  private TFLiteObjectSegmentationAPIModel() {}

  public Segmentation segmentImage(final Bitmap bitmap) {
    if (imgData != null) {
      imgData.rewind();
    }

    Trace.beginSection("segmentImage");

    Trace.beginSection("preprocessBitmap");

    bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

    for (int j = 0; j < inputHeight; ++j) {
      for (int i = 0; i < inputWidth; ++i) {
        int pixel = intValues[j*inputWidth + i];
        imgData.put((((pixel >> 16) & 0xFF) - MEAN) / STD);
        imgData.put((((pixel >> 8) & 0xFF) - MEAN) / STD);
        imgData.put(((pixel & 0xFF) - MEAN) / STD);
      }
    }
    imgData.rewind();
    Trace.endSection(); // preprocessBitmap

    // Run the inference call.
    Trace.beginSection("run");

    long startTime = SystemClock.uptimeMillis();
    tfLite.run(imgData, pixelClasses);
    long endTime = SystemClock.uptimeMillis();
    Trace.endSection(); // run

    Trace.endSection(); // segmentImage

    return new Segmentation(
                pixelClasses[0],
                numClass,
                inputWidth, inputHeight, endTime - startTime,
            0);
  }

  @Override
  public Vector<String> getLabels() {
    return null;
  }
}
