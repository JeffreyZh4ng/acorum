package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.javaobjects.ForumPost;
import edu.illinois.finalproject.javaobjects.UserInformation;

public class ForumDetailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private LinearLayout forumDetailList;
    private String courseKey;
    private String postKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        forumDetailList = (LinearLayout) findViewById(R.id.forumDetailList);

        courseKey = getIntent().getExtras().getString(Constants.COURSE_KEY_ARG);
        postKey = getIntent().getExtras().getString(Constants.POST_KEY_ARG);

        setPostElement();
    }

    private void setPostElement() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                View view = LayoutInflater.from(forumDetailList.getContext()).inflate
                        (R.layout.activity_forum_detail_header_element, forumDetailList, false);
                ForumPost forumPost = dataSnapshot.child(Constants.FORUM_POSTS_CHILD)
                        .child(courseKey).child(postKey).getValue(ForumPost.class);
                setPostText(view, forumPost, dataSnapshot);
                forumDetailList.addView(view);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setPostText(View view, ForumPost forumPost, DataSnapshot dataSnapshot) {
        TextView forumDetailTitleTextView = (TextView) view.findViewById(R.id.forumDetailTitleTextView);
        forumDetailTitleTextView.setText(forumPost.getPostTitle());

        TextView forumDetailInfoTextView = (TextView) view.findViewById(R.id.forumDetailInfoTextView);
        String userName = getName(forumPost.getPosterKey(), dataSnapshot);
        forumDetailInfoTextView.setText("Posted on: " + forumPost.getDatePosted() + " By: " + userName);

        TextView forumDetailMessageTextView = (TextView) view.findViewById(R.id.forumDetailMessageTextView);
        forumDetailMessageTextView.setText(forumPost.getPostMessage());
    }

    private String getName(String userKey, DataSnapshot dataSnapshot) {
        StringBuilder userName = new StringBuilder();
        String userFirstName = dataSnapshot.child(Constants.USERS_CHILD).child(userKey).getValue(UserInformation.class).getFirstName();
        userName.append(userFirstName);
        userName.append(" ");
        String userLastName = dataSnapshot.child(Constants.USERS_CHILD).child(userKey).getValue(UserInformation.class).getLastName();
        userName.append(userLastName);
        return userName.toString();

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
                startActivity(new Intent(ForumDetailActivity.this, ProfileActivity.class));
                finish();
                break;
            case R.id.profileSettingsButton:
                startActivity(new Intent(ForumDetailActivity.this, ProfileSettingsActivity.class));
                finish();
                break;
            case R.id.logoutButton:
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ForumDetailActivity.this, LoginActivity.class));
                }
                break;
            case R.id.backButton:
                finish();
                break;
        }
        return true;
    }
}
