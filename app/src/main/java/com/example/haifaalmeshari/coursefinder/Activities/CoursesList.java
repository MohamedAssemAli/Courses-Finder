package com.example.haifaalmeshari.coursefinder.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.haifaalmeshari.coursefinder.R;

public class CoursesList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

    }

    public void Org(View v) {
        Intent intent = new Intent(CoursesList.this, Organization.class);
        startActivity(intent);
    }


}


