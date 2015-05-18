package com.example.admin2015.student_rating;

import android.graphics.Bitmap;
import android.media.FaceDetector;

/**
 * Created by admin2015 on 23.04.2015.
 */
public class FaceDetectionHelper {


    private static final int MAX_FACES = 5;

    private static void makeImageWidthEven(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        // Make width of the image even:
        if(width % 2 != 0) {
            image = ImageHelper.cropImage(image, 0, 0, width - 1, height);
        }
    }

    public static FaceDetector.Face[] detectFaces(Bitmap image) {
        // We need an even image for face detection:
        makeImageWidthEven(image);
        // Try to detect faces:
        FaceDetector.Face[] facesInImage = new FaceDetector.Face[MAX_FACES];
        FaceDetector faceDetector = new FaceDetector(image.getWidth(), image.getHeight(), MAX_FACES);
        int numFaces = faceDetector.findFaces(image, facesInImage);
        // Only return the faces we have found:
        FaceDetector.Face[] result = new FaceDetector.Face[numFaces];
        for(int pos = 0; pos < numFaces; pos++) {
            result[pos] = facesInImage[pos];
        }
        return result;
    }
}
