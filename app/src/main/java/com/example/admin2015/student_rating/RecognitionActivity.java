package com.example.admin2015.student_rating;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by admin2015 on 23.04.2015.
 */
public class RecognitionActivity extends Activity {

    private static final String TAG = RecognitionActivity.class.getName();

    private static final int maxNumberOfFaces = 5;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognition_activity);



    }


}
