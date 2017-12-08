package edu.illinois.finalproject.tabfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.illinois.finalproject.R;

public class CourseWorkFragment extends Fragment {

    private String courseKey;

    public CourseWorkFragment() {}

    public static CourseWorkFragment newInstance(String courseKey) {
        CourseWorkFragment fragment = new CourseWorkFragment();
        Bundle args = new Bundle();
        args.putString("courseKey", courseKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseKey = getArguments().getString("courseKey");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_work, container, false);
    }
}
