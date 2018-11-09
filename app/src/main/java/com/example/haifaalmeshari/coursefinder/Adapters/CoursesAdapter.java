package com.example.haifaalmeshari.coursefinder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haifaalmeshari.coursefinder.Activities.NameCourse;
import com.example.haifaalmeshari.coursefinder.App.AppConfig;
import com.example.haifaalmeshari.coursefinder.Models.Course;
import com.example.haifaalmeshari.coursefinder.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseHolder> {

    private Context context;
    private ArrayList<Course> courseArrayList;

    public CoursesAdapter(Context context, ArrayList<Course> courseArrayList) {
        this.context = context;
        this.courseArrayList = courseArrayList;
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(context).inflate(R.layout.row_item, viewGroup, false);
        return new CourseHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseHolder courseHolder, int i) {
        final Course course = courseArrayList.get(i);
        courseHolder.courseTitle.setText(course.getTitle());
        courseHolder.courseDesc.setText(course.getDesc());
        courseHolder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCourseActivity(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseArrayList.size();
    }

    class CourseHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_row_layout)
        LinearLayout rowLayout;
        @BindView(R.id.title_view)
        TextView courseTitle;
        @BindView(R.id.desc_view)
        TextView courseDesc;

        CourseHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void goToCourseActivity(Course course) {
        Intent intent = new Intent(context, NameCourse.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConfig.INTENT_COURSE_KEY, course);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
