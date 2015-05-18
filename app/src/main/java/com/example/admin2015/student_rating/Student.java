package com.example.admin2015.student_rating;

import java.io.Serializable;

/**
 * Created by admin2015 on 25.04.2015.
 */
public class Student implements Serializable {
    long id;
    public String name, surname;
    public float gpa;
    public String faculty;
    public String photo = "";
    public int studentId;

    public Student() {

    }

    public Student(String name, String surname) {

        this.name = name;
        this.surname = surname;

    }

    @Override
    public String toString() {
        return name + " " + surname;
    }

}
