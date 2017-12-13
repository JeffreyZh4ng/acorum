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
import edu.illinois.finalproject.javaobjects.Announcement;
import edu.illinois.finalproject.javaobjects.ForumResponsePost;

public class ForumPostResponseActivity extends AppCompatActivity {

    public static final String RESPONSE_POSTED_TOAST = "Response Posted";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private TextView forumPostResponseMessageTextEdit;
    private Button postForumPostResponseButton;
    private String courseKey;
    private String postKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post_response);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        courseKey = getIntent().getExtras().getString(Constants.COURSE_KEY_ARG);
        postKey = getIntent().getExtras().getString(Constants.POST_KEY_ARG);

        forumPostResponseMessageTextEdit = (TextView) findViewById(R.id.forumPostResponseMessageTextEdit);
        postForumPostResponseButton = (Button) findViewById(R.id.postForumPostResponseButton);

        postForumPostResponseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addResponseToDatabase();
            }
        });
    }

    private void addResponseToDatabase() {
        final String postMessage = forumPostResponseMessageTextEdit.getText().toString();
        String currentDate = Calendar.getInstance().getTime().toString();
        String userKey = mAuth.getCurrentUser().getUid();
        final ForumResponsePost response = new ForumResponsePost(currentDate, postMessage, userKey);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int responseCount = 0;
                for (DataSnapshot forumResponseSnapshot: dataSnapshot.child(Constants.FORUM_RESPONSES_CHILD)
                        .child(courseKey).child(postKey).getChildren()) {
                    responseCount++;
                }
                mRef.child(Constants.FORUM_RESPONSES_CHILD).child(courseKey).child(postKey)
                        .child(responseCount + "_key").setValue(response);
                Toast.makeText(ForumPostResponseActivity.this, RESPONSE_POSTED_TOAST, Toast.LENGTH_LONG).show();
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
                startActivity(new Intent(ForumPostResponseActivity.this, ProfileActivity.class));
                finish();
                break;
            case R.id.profileSettingsButton:
                startActivity(new Intent(ForumPostResponseActivity.this, ProfileSettingsActivity.class));
                finish();
                break;
            case R.id.logoutButton:
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ForumPostResponseActivity.this, LoginActivity.class));
                }
                break;
            case R.id.backButton:
                finish();
                break;
        }
        return true;
    }
}
