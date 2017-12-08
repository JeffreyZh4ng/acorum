package edu.illinois.finalproject.tabfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.illinois.finalproject.R;

public class ForumFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private TextView testview;
    private String courseKey;

    public ForumFragment() {}

    public static ForumFragment newInstance(String courseKey) {
        Bundle args = new Bundle();
        args.putString("courseKey", courseKey);
        ForumFragment fragment = new ForumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseKey = getArguments().getString("courseKey");
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        testview = (TextView) view.findViewById(R.id.testview);
        testview.setText(courseKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }
}
