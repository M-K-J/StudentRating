package com.example.admin2015.student_rating;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import facedetection.CameraActivity;

/**
 * Created by admin2015 on 25.04.2015.
 */
public class AddActivity extends Activity {
    EditText fn, ln;
    DatabaseHandler dbHandler;
    Button addButton;
    private EditText name;
    private EditText surname;
    private EditText studentId;
    private EditText gpa;
    private EditText faculty;
    private Button addPhoto;
    private ImageView capturedImage;
    private byte[] imageByteArray;
    private Bitmap imageBitmap;
    private int facesFound;
    private List<Bitmap> extractedFaces;
    private List imageDimensions;
    private String filepath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student_actvity);

        name = (EditText) findViewById(R.id.nameField);
        surname = (EditText) findViewById(R.id.surnameField);
        studentId = (EditText) findViewById(R.id.studentIdField);
        gpa = (EditText) findViewById(R.id.gpaField);
        faculty = (EditText) findViewById(R.id.facultyField);
        capturedImage = (ImageView) findViewById(R.id.imageView);

        addButton = (Button) findViewById(R.id.addStudent);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student = new Student();
                student.name = name.getText().toString();
                student.surname = surname.getText().toString();
                student.studentId = Integer.parseInt(studentId.getText().toString());
                student.gpa = Float.parseFloat(gpa.getText().toString());
                student.faculty = faculty.getText().toString();
                student.photo = filepath;
                DatabaseHandler.getInstance(getApplicationContext()).addStudent(student);
                Toast.makeText(getApplicationContext(), "Transaction successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        addPhoto = (Button) findViewById(R.id.addPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, CameraActivity.class);
                startActivityForResult(intent, 123);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Recieve the image byte array from camera class
        Bundle extras = data.getExtras();

        if (extras != null) {
            imageByteArray = extras.getByteArray("ImageByteArray");

            // Convert the image byte array to bitmap
            imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0,
                    imageByteArray.length);

            // Set the bitmap to image view
            capturedImage.setImageBitmap(imageBitmap);
        }

        // Call face detection function
        detectFaces();

    }

    private void detectFaces() {
        if (null != imageBitmap) {
            int width = imageBitmap.getWidth();
            int height = imageBitmap.getHeight();

            // Call face detector object
            FaceDetector detector = new FaceDetector(width, height, 1);
            FaceDetector.Face[] faces = new FaceDetector.Face[1];

            Bitmap bitmap565 = Bitmap.createBitmap(width, height,
                    Bitmap.Config.RGB_565);
            Paint ditherPaint = new Paint();
            Paint drawPaint = new Paint();

            // Set properties of the rectangle that should appear for a detected
            // face
            ditherPaint.setDither(true);
            drawPaint.setColor(Color.GREEN);
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeWidth(3);

            // Create a canvas and draw the imagebitmap over the canvas
            Canvas canvas = new Canvas();
            canvas.setBitmap(bitmap565);
            canvas.drawBitmap(imageBitmap, 0, 0, ditherPaint);

            // trigger the face detector function and store the number of faces
            // detected as int paramter faceFound
            facesFound = detector.findFaces(bitmap565, faces);

            // This parameters find the midpoint, eyeDistance and confidence in
            // a face
            PointF midPoint = new PointF();
            float eyeDistance = 0.0f;
            float confidence = 0.0f;

            Log.i("FaceDetector", "Number of faces found: " + facesFound);

            if (facesFound > 0) {

                // Select detected faces, one after one
                for (int index = 0; index < facesFound; ++index) {

                    // Get mid point, eye distance and confidence of the face
                    faces[index].getMidPoint(midPoint);
                    eyeDistance = faces[index].eyesDistance();
                    confidence = faces[index].confidence();

                    Log.i("FaceDetector", "Confidence: " + confidence
                            + ", Eye distance: " + eyeDistance
                            + ", Mid Point: (" + midPoint.x + ", " + midPoint.y
                            + ")");

                    try {
                        // Draw a rectange depending upon face parameters.
                        // Kindly note that this is just for displaying the
                        // rectangle
                        canvas.drawRect((float) midPoint.x - (eyeDistance * 2),
                                (float) midPoint.y - (eyeDistance * 2),
                                (int) midPoint.x + (eyeDistance * 2),
                                (int) midPoint.y + (eyeDistance * 2), drawPaint);

                        // And this is for cropping the detected face from the
                        // image
                        Bitmap croppedBitmap = Bitmap.createBitmap(imageBitmap,
                                (int) (midPoint.x - (eyeDistance * 2)),
                                (int) (midPoint.y - (eyeDistance * 2)),
                                (int) (eyeDistance * 4),
                                (int) (eyeDistance * 4));

                        extractedFaces.add(croppedBitmap);

                        imageDimensions.add(midPoint.x + "," + midPoint.y + ","
                                + (eyeDistance * 2) + "," + (eyeDistance * 3)
                                + "," + "IMGPNG");
                    } catch (Exception e) {
						 Toast.makeText( getApplicationContext(),
                                 "Too many faces/face size is too small, " +
                                         "kindly go back and try again...",
                                 Toast.LENGTH_SHORT).show();
                    }
                }
            }

//            Log.i("faces", extractedFaces.size() + "");

            filepath = Environment.getExternalStorageDirectory()
                    + "/facedetect" + System.currentTimeMillis() + ".jpg";
            if (extractedFaces!=null && extractedFaces.size()>0)
                bitmap565 = extractedFaces.get(0);

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

            // Set the image view in this activity with the detected faces
            // bitmap
            capturedImage.setImageBitmap(bitmap565);
        }
    }
}
