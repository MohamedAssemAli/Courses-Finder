package com.example.haifaalmeshari.coursefinder.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haifaalmeshari.coursefinder.Adapters.CoursesAdapter;
import com.example.haifaalmeshari.coursefinder.App.AppConfig;
import com.example.haifaalmeshari.coursefinder.Models.Course;
import com.example.haifaalmeshari.coursefinder.R;
import com.example.haifaalmeshari.coursefinder.Utils.BuildViews;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityActivity extends AppCompatActivity {

    private static final String TAG = CityActivity.class.getSimpleName();
    // Firebase
    private DatabaseReference mRef;
    // Views
    @BindView(R.id.city_activity_recycler_view)
    RecyclerView coursesRecyclerView;
    @BindView(R.id.city_activity_empty_recycler)
    TextView emptyRecyclerPlaceHolder;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.progress_layout)
    RelativeLayout progressLayout;
    // Vars
    private CoursesAdapter coursesAdapter;
    private ArrayList<Course> courseArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            init();
            getCourses(intent.getStringExtra(AppConfig.INTENT_CITY_KEY));
        } else {
            // Sandwich data unavailable
            closeOnError();
        }
    }

    private void init() {
        courseArrayList = new ArrayList<>();
        // Firebase
        mRef = FirebaseDatabase.getInstance().getReference();
        coursesAdapter = new CoursesAdapter(this, courseArrayList);
        coursesRecyclerView.setAdapter(coursesAdapter);
        new BuildViews().setupLinearVerticalRecView(coursesRecyclerView, this);
        toggleLayout(false);
    }

    private void getCourses(String cityKey) {
        mRef.child(AppConfig.COURSES)
                .child(AppConfig.CITY)
                .child(cityKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        courseArrayList.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Course course = snapshot.getValue(Course.class);
                                courseArrayList.add(course);
                                coursesAdapter.notifyDataSetChanged();
                            }
                            Collections.reverse(courseArrayList);
                        }
                        toggleLayout(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage());
                        Log.d(TAG, databaseError.getDetails());
                        Toast.makeText(CityActivity.this, R.string.error_getting_data, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void toggleLayout(boolean flag) {
        if (flag) {
            progressLayout.setVisibility(View.GONE);
            progressBar.hide();
            if (courseArrayList.isEmpty()) {
                emptyRecyclerPlaceHolder.setVisibility(View.VISIBLE);
                coursesRecyclerView.setVisibility(View.GONE);
            } else {
                emptyRecyclerPlaceHolder.setVisibility(View.GONE);
                coursesRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            progressLayout.setVisibility(View.VISIBLE);
            progressBar.show();
            coursesRecyclerView.setVisibility(View.GONE);
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }
}

