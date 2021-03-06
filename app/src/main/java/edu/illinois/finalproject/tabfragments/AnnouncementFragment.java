package edu.illinois.finalproject.tabfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.activities.PostAnnouncementActivity;
import edu.illinois.finalproject.javaobjects.Announcement;
import edu.illinois.finalproject.recycleradapters.AnnouncementRecyclerAdapter;

/**
 * The fragment that displays the announcements for a class
 */
public class AnnouncementFragment extends Fragment {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private RecyclerView announcementRecycler;
    private Button createAnnouncementPostButton;
    private TextView announcementAlert;
    private String courseKey;
    private boolean isInstructor;

    public AnnouncementFragment() {}

    /**
     * Sets the arguments of the fragment given by the user dashboard activity
     *
     * @param courseKey The course key argument of the course
     * @param isInstructor Argument that tells if the user is an instructor of the course or not
     * @return The fragment containing the arguments
     */
    public static AnnouncementFragment newInstance(String courseKey, boolean isInstructor) {
        Bundle args = new Bundle();
        args.putString(Constants.COURSE_KEY_ARG, courseKey);
        args.putBoolean(Constants.IS_INSTRUCTOR_ARG, isInstructor);
        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Sets the instance variables that need to be initialized when the fragment is created
     *
     * @param savedInstanceState A savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseKey = getArguments().getString(Constants.COURSE_KEY_ARG);
            isInstructor = getArguments().getBoolean(Constants.IS_INSTRUCTOR_ARG);
        }

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    /**
     * Initializes and sets the text of components of the fragment when it is created
     *
     * @param view The view that the components of the fragment are held
     * @param savedInstanceState A savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        createAnnouncementPostButton = (Button) view.findViewById(R.id.createAnnouncementPostButton);
        announcementRecycler = (RecyclerView) view.findViewById(R.id.announcementRecycler);
        announcementAlert = (TextView) view.findViewById(R.id.announcementAlert);

        if (!isInstructor) {
            createAnnouncementPostButton.setVisibility(View.GONE);
        }
        createAnnouncementPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PostAnnouncementActivity.class)
                        .putExtra(Constants.COURSE_KEY_ARG, courseKey));
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setAnnouncementElements(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * Helper method that will compile all the announcements and will create a recycler adapter for them
     *
     * @param dataSnapshot The dataSnapshot of database
     */
    private void setAnnouncementElements(DataSnapshot dataSnapshot) {
        HashMap<String, Announcement> announcementHashMap = new HashMap<>();
        for (DataSnapshot announcementSnapshot: dataSnapshot.child(Constants.COURSE_ANNOUNCEMENTS_CHILD)
                .child(courseKey).getChildren()) {
            Announcement announcement = announcementSnapshot.getValue(Announcement.class);
            announcementHashMap.put(announcementSnapshot.getKey(), announcement);
        }
        if (announcementHashMap.size() != 0) {
            announcementAlert.setVisibility(View.GONE);
        }

        AnnouncementRecyclerAdapter adapter = new AnnouncementRecyclerAdapter(announcementHashMap);
        announcementRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        announcementRecycler.setLayoutManager(layoutManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_announcement, container, false);
    }
}
