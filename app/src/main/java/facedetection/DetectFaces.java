package facedetection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin2015.student_rating.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DetectFaces extends Activity {

	private Button backButton, selectFaces;
	private byte[] imageByteArray;
	private Bitmap imageBitmap;
	private ImageView capturedImage;
	private static final int MAX_FACES = 5;
	private ArrayList<Bitmap> extractedFaces = new ArrayList<Bitmap>();
	private static final String TAG = "Detected Faces";
	private ArrayList<String> imageDimensions;

	int facesFound = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.displayfaces);

		// Recieve the image byte array from camera class
		Bundle extras = getIntent().getExtras();

		backButton = (Button) findViewById(R.id.bBack);
		selectFaces = (Button) findViewById(R.id.bSelectFaces);
		capturedImage = (ImageView) findViewById(R.id.ivCapturedImage);

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

		// If no faces are found
		if (facesFound == 0) {
			Toast.makeText(getApplicationContext(),
                    "No faces found, kindly try again...!!!",
                    Toast.LENGTH_SHORT).show();
		} else if (facesFound != 0) {
			Toast.makeText(getApplicationContext(),
                    facesFound + " faces detected", Toast.LENGTH_SHORT).show();
		}

		// Back to camera
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		// Send the cropped faces to next activity in the form of
		// ArrayList<Bitmap>
		selectFaces.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (extractedFaces.size() != 0) {
					// Try to connect to the server

					Intent myIntent = new Intent(DetectFaces.this,
							DisplayDetectedFaces.class);
					Bundle bundle = new Bundle();
					bundle.putParcelableArrayList("ExtractedFaces",
							extractedFaces);
					myIntent.putExtras(bundle);
					myIntent.putStringArrayListExtra("ImageDimensions",
							imageDimensions);
					startActivity(myIntent);
					Log.i("Select Faces Click", extractedFaces.size() + "");
				} else {
					Log.e("Select Faces Click", extractedFaces.size()
                            + " faces extracted");
				}

			}
		});
	}

	private void detectFaces() {
		if (null != imageBitmap) {
			int width = imageBitmap.getWidth();
			int height = imageBitmap.getHeight();

			// Call face detector object
			FaceDetector detector = new FaceDetector(width, height,
					DetectFaces.MAX_FACES);
			Face[] faces = new Face[DetectFaces.MAX_FACES];

			Bitmap bitmap565 = Bitmap.createBitmap(width, height,
                    Config.RGB_565);
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
						/*
						 * Toast.makeText( getApplicationContext(),
						 * "Too many faces/face size is too small, kindly go back and try again..."
						 * , Toast.LENGTH_SHORT).show();
						 */

					}
				}
			}

			Log.i("Number of Faces detected", extractedFaces.size() + "");

			String filepath = Environment.getExternalStorageDirectory()
					+ "/facedetect" + System.currentTimeMillis() + ".jpg";

			try {
				FileOutputStream fos = new FileOutputStream(filepath);
				bitmap565.compress(CompressFormat.JPEG, 90, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			ImageView imageView = (ImageView) findViewById(R.id.ivCapturedImage);

			// Set the image view in this activity with the detected faces
			// bitmap
			imageView.setImageBitmap(bitmap565);
		}
	}
}
