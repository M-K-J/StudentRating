package com.example.admin2015.student_rating;

import android.graphics.Bitmap;
import android.media.FaceDetector;

/**
 * Created by admin2015 on 23.04.2015.
 */
public class FaceRecognitionHelper {
    public static String recognizeFace(Bitmap bitmap, FaceDetector.Face face) throws Exception {
        Bitmap faceBitmap = ImageHelper.cropFace(bitmap, face);
        return RecognitionAPI.requestRecognition(faceBitmap);
    }
}
