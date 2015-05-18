package com.example.admin2015.student_rating;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity{
    Button start,help,about;
    EditText editEmail,editPass;
    TextView admin;
    int password = 123;
    //private static final int CAPTURE_IMAGE_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button)findViewById(R.id.bstart);
        help = (Button)findViewById(R.id.bhelp);
        about = (Button)findViewById(R.id.about);
        admin = (TextView)findViewById(R.id.textView);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(about);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentstart = new Intent(MainActivity.this,CaptureActivity.class);
                startActivity(intentstart);


            }
        });



    }


    private void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        alertDialog.setView(inflater.inflate(R.layout.custom_layout,null));

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editEmail = (EditText)findViewById(R.id.username);
                editPass = (EditText)findViewById(R.id.password);
                Intent intent = new Intent(MainActivity.this,AddNewStudent.class);

                startActivity(intent);}



        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish(); // Your custom code
            }
        });
        alertDialog.show();
    }
    }



