package opu.timkov.core;

import android.os.Environment;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoWriter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import opu.timkov.ui.RecordButtonListener;

public class Recorder implements RecordButtonListener  {

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

    private Size frameSize;

    private VideoWriter videoWriter;

    private Mat rotationMatrix;

    private Mat frameBuffer;


    public Recorder(Size size) {
        videoWriter = new VideoWriter();
        frameSize = size;
        rotationMatrix = Imgproc.getRotationMatrix2D(new Point(size.width/2, size.height/2), 90,
                size.height / size.width);
        frameBuffer = new Mat();
    }

    public void setRotation(int angle) {
        rotationMatrix = Imgproc.getRotationMatrix2D(new Point(frameSize.width/2, frameSize.height/2), angle,
                frameSize.height / frameSize.width);
    }

    @Override
    public void onRecordButtonPress() {
        if (videoWriter.isOpened()) {
            stop();
        } else {
            start();
        }
    }

    private void start() {
        videoWriter.open(createOutputFile(), VideoWriter.fourcc('M','J','P','G'),
                20.0, frameSize, true);
    }

    private void stop() {
        videoWriter.release();
    }

    public void write(Mat frame) {
        if(videoWriter.isOpened()==false){
            return;
        }
        //Rotating the given image
        Imgproc.warpAffine(frame, frameBuffer, rotationMatrix, frameSize);
        videoWriter.write(frameBuffer);
    }

    private String createOutputFile() {
        File mediaDir = new File(Environment.getExternalStorageDirectory() + "/media/");
        mediaDir.mkdirs();
        Date date = new Date();
        String fileName = mediaDir + "/" + formatter.format(date) + ".avi";
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) { e.printStackTrace(); }
        }
        System.out.println("saving file " + fileName);
        return fileName;
    }
}
