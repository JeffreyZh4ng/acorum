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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.activities.ForumPostActivity;
import edu.illinois.finalproject.javaobjects.ForumPost;
import edu.illinois.finalproject.recycleradapters.ForumPostRecyclerAdapter;

public class ForumFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private RecyclerView forumPostRecycler;
    private Button createForumPostButton;
    private TextView forumAlert;
    private String courseKey;

    public ForumFragment() {}

    /**
     * Sets the arguments of the fragment given by the user dashboard activity
     *
     * @param courseKey The course key argument of the course
     * @param isInstructor Argument that tells if the user is an instructor of the course or not
     * @return The fragment containing the arguments
     */
    public static ForumFragment newInstance(String courseKey, boolean isInstructor) {
        Bundle args = new Bundle();
        args.putString(Constants.COURSE_KEY_ARG, courseKey);
        ForumFragment fragment = new ForumFragment();
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
        }

        mAuth = FirebaseAuth.getInstance();
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
        createForumPostButton = (Button) view.findViewById(R.id.createForumPostButton);
        forumPostRecycler = (RecyclerView) view.findViewById(R.id.forumPostRecycler);
        forumAlert = (TextView) view.findViewById(R.id.forumAlert);

        createForumPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ForumPostActivity.class)
                        .putExtra(Constants.COURSE_KEY_ARG, courseKey));
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setForumPostElements(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * Helper method that sets the text and the recycler view adapter for the forum posts
     *
     * @param dataSnapshot The dataSnapshot of the database
     */
    private void setForumPostElements(DataSnapshot dataSnapshot) {
        HashMap<String, ForumPost> forumPostHashMap = new HashMap<>();
        for (DataSnapshot forumPostSnapshot: dataSnapshot.child(Constants.FORUM_POSTS_CHILD)
                .child(courseKey).getChildren()) {
            ForumPost post = forumPostSnapshot.getValue(ForumPost.class);
            forumPostHashMap.put(forumPostSnapshot.getKey(), post);
        }
        if (forumPostHashMap.size() != 0) {
            forumAlert.setVisibility(View.GONE);
        }

        ForumPostRecyclerAdapter adapter = new ForumPostRecyclerAdapter(forumPostHashMap, courseKey);
        forumPostRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        forumPostRecycler.setLayoutManager(layoutManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }
}
