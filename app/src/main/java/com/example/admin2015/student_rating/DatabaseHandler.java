package com.example.admin2015.student_rating;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin2015 on 25.04.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static  final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "students";
    private static final String TABLE_STUDENTS = "Student";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_GPA = "gpa";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_FACULTY = "faculty";
    private static final String KEY_STUDENT_ID = "student_id";
    private static DatabaseHandler instance;

    public static DatabaseHandler getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHandler(context);
        return instance;
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STUDENT_TABLE ="CREATE TABLE "+ TABLE_STUDENTS+"("+ KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_NAME +" TEXT,"+ KEY_SURNAME + " TEXT,"
                + KEY_GPA + " REAL, " + KEY_FACULTY + " TEXT, " + KEY_PHOTO + " TEXT,"
                + KEY_STUDENT_ID + " INTEGER"
                + ")";
            db.execSQL(CREATE_STUDENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);


        // Create tables again
        onCreate(db);
    }
    public boolean addStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, student.name);
        values.put(KEY_SURNAME,student.surname);
        values.put(KEY_GPA,student.gpa);
        values.put(KEY_FACULTY,student.faculty);
        values.put(KEY_PHOTO,student.photo);
        values.put(KEY_STUDENT_ID,student.studentId);


        student.id = db.insert(TABLE_STUDENTS, null, values);

        db.close();
        if (student.id>0){
            return true;

        }
        return false;
    }
    public List<Student> getStudentsByName(String s) {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE " + KEY_NAME + " LIKE '%" + s + "'", null);
        if (c.moveToFirst()) {
            do {
                Student student = new Student(c.getString(c.getColumnIndex(KEY_NAME)), c.getString(c.getColumnIndex(KEY_SURNAME)));
                student.id = c.getLong(c.getColumnIndex(KEY_ID));
                student.faculty = c.getString(c.getColumnIndex(KEY_FACULTY));
                student.gpa = c.getFloat(c.getColumnIndex(KEY_GPA));
                student.photo = c.getString(c.getColumnIndex(KEY_PHOTO));
                student.studentId = c.getInt(c.getColumnIndex(KEY_STUDENT_ID));
                list.add(student);
            } while(c.moveToNext());
        }
        return list;
    }

    public int deleteStudentById(long id) {
        int i = 0;
        SQLiteDatabase db = getWritableDatabase();
        i = db.delete(TABLE_STUDENTS, KEY_ID + "=" + id, null);
        return i;
    }

}
