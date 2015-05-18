package com.example.admin2015.student_rating;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by admin2015 on 18.05.2015.
 */
public class StudentInfo extends Activity {
    DatabaseHandler dbhandler;
    TextView name_surname;
    private EditText name;
    private EditText surname;
    private EditText studentId;
    private EditText gpa;
    private EditText faculty;
    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        name = (EditText) findViewById(R.id.nameField);
        surname = (EditText) findViewById(R.id.surnameField);
        studentId = (EditText) findViewById(R.id.studentIdField);
        gpa = (EditText) findViewById(R.id.gpaField);
        faculty = (EditText) findViewById(R.id.facultyField);
        photo = (ImageView) findViewById(R.id.imageView);
        Student student = (Student) getIntent().getExtras().getSerializable("student");

        name.setText(student.name);
        surname.setText(student.surname);
        studentId.setText(student.studentId + "");
        gpa.setText(student.gpa + "");
        faculty.setText(student.faculty);
        File file = new File(student.photo);
        if (file.exists()) {
            photo.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }
    }
}
