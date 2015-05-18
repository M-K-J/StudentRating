package facedetection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin2015.student_rating.R;

import java.util.ArrayList;

public class DisplayDetectedFaces extends Activity implements
		View.OnClickListener {

	private ImageView[] m_ReceivedFace;
	private RelativeLayout[] m_ResultFields;
	private TextView[] m_TitleFields;
	// private Button button1, button2, button3, button4, button5;
	private static final String TAG = "Display Detected Faces";
	private ArrayList<Bitmap> m_aResults;
	private ArrayList<String> imageDimensions;
	Drawable buttonBackground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.displaydetectedfaces);

		// Receive the ArrayList<bitmap of images>
		Bundle extras = this.getIntent().getExtras();
		Intent imageDimIntent = getIntent();

		if (extras != null) {
			m_aResults = extras.getParcelableArrayList("ExtractedFaces");
			imageDimensions = imageDimIntent
					.getStringArrayListExtra("ImageDimensions");

			initialise();

			for (int i = 0; i < 5; i++) {
				if (i >= m_aResults.size()) {
					m_ResultFields[i].removeAllViews();
					continue;
				}
				m_ReceivedFace[i].setImageBitmap(m_aResults.get(i));
			}
		}

	}

	public void initialise() {

		m_ResultFields = new RelativeLayout[5];
		m_ResultFields[0] = (RelativeLayout) findViewById(R.id.results_1);
		m_ResultFields[1] = (RelativeLayout) findViewById(R.id.results_2);
		m_ResultFields[2] = (RelativeLayout) findViewById(R.id.results_3);
		m_ResultFields[3] = (RelativeLayout) findViewById(R.id.results_4);
		m_ResultFields[4] = (RelativeLayout) findViewById(R.id.results_5);

		m_TitleFields = new TextView[5];
		m_TitleFields[0] = (TextView) findViewById(R.id.textView1);
		m_TitleFields[1] = (TextView) findViewById(R.id.textView2);
		m_TitleFields[2] = (TextView) findViewById(R.id.textView3);
		m_TitleFields[3] = (TextView) findViewById(R.id.textView4);
		m_TitleFields[4] = (TextView) findViewById(R.id.textView5);

		m_ReceivedFace = new ImageView[5];
		m_ReceivedFace[0] = (ImageView) findViewById(R.id.imageView1);
		m_ReceivedFace[1] = (ImageView) findViewById(R.id.imageView2);
		m_ReceivedFace[2] = (ImageView) findViewById(R.id.imageView3);
		m_ReceivedFace[3] = (ImageView) findViewById(R.id.imageView4);
		m_ReceivedFace[4] = (ImageView) findViewById(R.id.imageView5);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

}
