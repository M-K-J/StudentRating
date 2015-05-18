package facedetection;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.example.admin2015.student_rating.R;

import java.io.ByteArrayOutputStream;

public class CameraActivity extends Activity implements OnClickListener,
        PictureCallback {

	private Preview mPreview;
	Camera mCamera;
	int numberOfCameras;
	int cameraCurrentlyLocked;
	private Button shutterButton;
	private Progress m_Progress;

	private static final String TAG = "Camera Activity";

	// The first rear facing camera
	int defaultCameraId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		mPreview = new Preview(this);
		setContentView(mPreview);

		inflateButtonsOnCameraView();

		// grab out shutter button so we can reference it later
		shutterButton = (Button) findViewById(R.id.shutter_button);
		shutterButton.setOnClickListener(this);

		/** On AutoFocus button click */
		Button autoFocus = (Button) findViewById(R.id.bAutoFocus);
		autoFocus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				mCamera.autoFocus(null);
			}
		});

		ImageView scanBar = (ImageView) findViewById(R.id.scanner);
		scanBar.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Open the default i.e. the first rear facing camera.
		mCamera = Camera.open();
		cameraCurrentlyLocked = defaultCameraId;
		mPreview.setCamera(mCamera);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.
		if (mCamera != null) {
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private void takePicture() {
		// mPreview.takePicture(this);
		mPreview.takePicture(this);
		m_Progress = (Progress) new Progress().execute("ASD");
	}

	/** Helps inflate camera buttons over the camera surfaceview */
	private void inflateButtonsOnCameraView() {

		LayoutInflater controlInflater = null;
		// TODO Auto-generated method stub
		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater.inflate(R.layout.camera_icons, null);
		LayoutParams layoutParamsControl = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.addContentView(viewControl, layoutParamsControl);
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		// TODO Auto-generated method stub

		//Convert image from byte[] to bitmap
		Bitmap imageBitmap = BitmapFactory
				.decodeByteArray(data, 0, data.length);

		//Downscale and compress the image bitmap
		imageBitmap = scaleDownBitmap(imageBitmap, 240, this);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		
		//Convert back the image from bitmap to byte[]
		byte[] byteArray = stream.toByteArray();

		mCamera.startPreview();
		m_Progress.EndDialog();

		//Send the image byte[] to next activity
		Intent results = new Intent();
		results.putExtra("ImageByteArray", byteArray);
		setResult(RESULT_OK, results);
        finish();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		takePicture();
	}

	public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight,
			Context context) {

		final float densityMultiplier = context.getResources()
				.getDisplayMetrics().density;

		int h = (int) (newHeight * densityMultiplier);
		int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

		photo = Bitmap.createScaledBitmap(photo, w, h, true);

		return photo;
	}

	private class Progress extends AsyncTask<String, Integer, String> {
		ProgressDialog pd;
		ImageView scanBar;

		public void onPreExecute() {
			pd = new ProgressDialog(CameraActivity.this);
			pd.show();

			TranslateAnimation anim = new TranslateAnimation(0f, 0f, -300f,
					300f);
			anim.setInterpolator(new LinearInterpolator());
			anim.setRepeatCount(Animation.INFINITE);
			anim.setDuration(500);

			scanBar = (ImageView) findViewById(R.id.scanner);
			scanBar.setVisibility(View.VISIBLE);
			scanBar.startAnimation(anim);
		}

		@Override
		protected String doInBackground(String... params) {
			publishProgress(new Integer(1));
			return null;
		}

		protected void onProgressUpdate(Integer... values) {
			pd.setMessage("Analysing image, please wait...");
		}

		protected void onPostExecute(String result) {

		}

		public void EndDialog() {
			scanBar.setVisibility(View.INVISIBLE);
			scanBar.clearAnimation();
			pd.dismiss();
		}
	}
}