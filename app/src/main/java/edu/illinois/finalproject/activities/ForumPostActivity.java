package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.javaobjects.ForumPost;

public class ForumPostActivity extends AppCompatActivity {

    private static final String FORUM_POST_POSTED_TOAST = "Forum post posted";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private TextView forumPostTitleTextEdit;
    private TextView forumPostMessageTextEdit;
    private Button postForumPostButton;
    private String courseKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        courseKey = getIntent().getExtras().getString(Constants.COURSE_KEY_ARG);

        forumPostTitleTextEdit = (TextView) findViewById(R.id.forumPostTitleTextEdit);
        forumPostMessageTextEdit = (TextView) findViewById(R.id.forumPostMessageTextEdit);
        postForumPostButton = (Button) findViewById(R.id.postForumPostButton);

        postForumPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postForumPost();
            }
        });
    }

    private void postForumPost() {
        String postTitle = forumPostTitleTextEdit.getText().toString();
        String postMessage = forumPostMessageTextEdit.getText().toString();
        if (postTitle.isEmpty() || postMessage.isEmpty()) {
            Toast.makeText(ForumPostActivity.this, Constants.EMPTY_FIELDS_TOAST, Toast.LENGTH_LONG).show();
        } else {
            addForumPostToDatabase(postTitle, postMessage);
        }
    }

    private void addForumPostToDatabase(String postTitle, String postMessage) {
        String datePosted = Calendar.getInstance().getTime().toString();
        final ForumPost forumPost = new ForumPost(postTitle, datePosted, postMessage);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int forumPostCount = 0;
                for (DataSnapshot forumPostSnapshot: dataSnapshot.child(Constants.FORUM_POSTS_CHILD)
                        .child(courseKey).getChildren()) {
                    forumPostCount++;
                }
                mRef.child(Constants.FORUM_POSTS_CHILD).child(courseKey)
                        .child(forumPostCount + "_key").setValue(forumPost);
                Toast.makeText(ForumPostActivity.this, FORUM_POST_POSTED_TOAST, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Override method that will create the settings icon and the back button in the menu bar
     *
     * @param menu The menu that the icons are being set to
     * @return True
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        getMenuInflater().inflate(R.menu.menu_back_button, menu);
        return true;
    }

    /**
     * Override method that controls what happens when you click on one of the icons in the menu bar
     *
     * @param item The item in the menu that was clicked on
     * @return True
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileButton:
                startActivity(new Intent(ForumPostActivity.this, ProfileActivity.class));
                finish();
                break;
            case R.id.profileSettingsButton:
                startActivity(new Intent(ForumPostActivity.this, ProfileSettingsActivity.class));
                finish();
                break;
            case R.id.logoutButton:
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ForumPostActivity.this, LoginActivity.class));
                }
                break;
            case R.id.backButton:
                finish();
                break;
        }
        return true;
    }
}
