package edu.illinois.finalproject.tabfragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.javaobjects.AnnouncementList;
import edu.illinois.finalproject.recycleradapters.AnnouncementRecyclerAdapter;

public class AnnouncementFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private RecyclerView announcementRecycler;
    private Button postAnnouncementButton;
    private String courseKey;
    private boolean isInstructor;

    public AnnouncementFragment() {}

    public static AnnouncementFragment newInstance(String courseKey, boolean isInstructor) {
        Bundle args = new Bundle();
        args.putString("courseKey", courseKey);
        args.putBoolean("isInstructor", isInstructor);
        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseKey = getArguments().getString("courseKey");
            isInstructor = getArguments().getBoolean("isInstructor");
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        postAnnouncementButton = (Button) view.findViewById(R.id.postAnnouncementButton);
        if (!isInstructor) {
            postAnnouncementButton.setVisibility(View.GONE);
        }

        /*mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AnnouncementList announcementList = dataSnapshot.child("courseAnnouncements")
                        .child(courseKey).getValue(AnnouncementList.class);
                AnnouncementRecyclerAdapter adapter = new AnnouncementRecyclerAdapter(announcementList);
                announcementRecycler.setAdapter(adapter);
                announcementRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_announcement, container, false);
    }
}
