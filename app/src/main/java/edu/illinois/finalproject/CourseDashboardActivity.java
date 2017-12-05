package edu.illinois.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CourseDashboardActivity extends AppCompatActivity {

    private String courseKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_dashboard);
        courseKey = getIntent().getExtras().getString("courseKey");
    }
}
