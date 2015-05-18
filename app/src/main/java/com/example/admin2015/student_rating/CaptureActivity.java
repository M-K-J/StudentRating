package com.example.admin2015.student_rating;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin2015 on 23.04.2015.
 */
public class CaptureActivity extends Activity {

    private static final int CAPTURE_IMAGE_REQUEST_CODE = 100;
    ImageView imageView;
    Button detectFace;
    private static final int MAX_FACES = 5;

    private Bitmap cameraBitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognition_activity);
        //imageView = (ImageView)findViewById(R.id.image);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAPTURE_IMAGE_REQUEST_CODE){
           if(resultCode==RESULT_OK){
               Toast.makeText(this,"Image captured",Toast.LENGTH_SHORT).show();

               processCameraImage(data);
           }
           else {
              Toast.makeText(this,"ELSE ",Toast.LENGTH_SHORT).show();
           }
        }


    }

    private void processCameraImage(Intent intent) {
        detectFace = (Button)findViewById(R.id.detect);
        detectFace.setOnClickListener(btnclicklistener);
        imageView = (ImageView)findViewById(R.id.recognition_results);

        cameraBitmap = (Bitmap)intent.getExtras().get("data");

        imageView.setImageBitmap(cameraBitmap);


    }
    private void detectFace(){

    if(null != cameraBitmap){
        int width = cameraBitmap.getWidth();
        int height = cameraBitmap.getHeight();


        FaceDetector detector = new FaceDetector(width, height,CaptureActivity.MAX_FACES);
        Face[] faces = new Face[CaptureActivity.MAX_FACES];
        Bitmap  bitmap565 = Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565);
        Paint ditherPaint = new Paint();
        Paint drawPaint = new Paint();

        ditherPaint.setDither(true);
        drawPaint.setColor(Color.RED);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(2);

        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap565);
        canvas.drawBitmap(cameraBitmap, 0, 0, ditherPaint);

        int facesFound = detector.findFaces(bitmap565, faces);
        PointF midPoint = new PointF();
        float eyeDistance = 0.0f;
        float confidence = 0.0f;
        Log.i("FaceDetector", "Number of faces found: " + facesFound);

        if(facesFound > 0)
        {
            for(int index=0; index<facesFound; ++index){
                faces[index].getMidPoint(midPoint);
                eyeDistance = faces[index].eyesDistance();
                confidence = faces[index].confidence();

                Log.i("FaceDetector",
                        "Confidence: " + confidence +
                                ", Eye distance: " + eyeDistance +
                                ", Mid Point: (" + midPoint.x + ", " + midPoint.y + ")");

                canvas.drawRect((int)midPoint.x - eyeDistance ,
                        (int)midPoint.y - eyeDistance ,
                        (int)midPoint.x + eyeDistance,
                        (int)midPoint.y + eyeDistance, drawPaint);
            }
        }
        String filepath = Environment.getExternalStorageDirectory() + "/facedetect" + System.currentTimeMillis() + ".jpg";

        try {
            FileOutputStream fos = new FileOutputStream(filepath);

            bitmap565.compress(Bitmap.CompressFormat.JPEG, 90, fos);

            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageView imageView = (ImageView)findViewById(R.id.recognition_results)
                ;


        imageView.setImageBitmap(bitmap565);
    }
    }




    private View.OnClickListener btnclicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.detect:
                    detectFace();break;
            }
        }
    };



}
