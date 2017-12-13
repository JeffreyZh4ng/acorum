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

public class PostAnnouncementActivity extends AppCompatActivity {

    private static final String ANNOUNCEMENT_POSTED_TOAST = "Announcement posted";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private TextView titleTextEdit;
    private TextView messageTextEdit;
    private Button postAnnouncementButton;
    private String courseKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_announcement);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        courseKey = getIntent().getExtras().getString(Constants.COURSE_KEY_ARG);

        titleTextEdit = (TextView) findViewById(R.id.titleTextEdit);
        messageTextEdit = (TextView) findViewById(R.id.messageTextEdit);
        postAnnouncementButton = (Button) findViewById(R.id.postAnnouncementButton);

        postAnnouncementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postAnnouncement();
            }
        });
    }

    private void postAnnouncement() {
        String titleText = titleTextEdit.getText().toString();
        String messageText = messageTextEdit.getText().toString();
        if (titleText.isEmpty() || messageText.isEmpty()) {
            Toast.makeText(PostAnnouncementActivity.this, Constants.EMPTY_FIELDS_TOAST, Toast.LENGTH_LONG).show();
        } else {
            addAnnouncementToDatabase(titleText, messageText);
        }
    }

    private void addAnnouncementToDatabase(String titleText, String messageText) {
        String currentDate = Calendar.getInstance().getTime().toString();
        final Announcement announcement = new Announcement(titleText, currentDate, messageText);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int announcementCount = 0;
                for (DataSnapshot announcementSnapshot: dataSnapshot.child(Constants.COURSE_ANNOUNCEMENTS_CHILD)
                        .child(courseKey).getChildren()) {
                    announcementCount++;
                }
                mRef.child(Constants.COURSE_ANNOUNCEMENTS_CHILD).child(courseKey)
                        .child(announcementCount + "_key").setValue(announcement);
                Toast.makeText(PostAnnouncementActivity.this, ANNOUNCEMENT_POSTED_TOAST, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
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
            case R.id.profileSettingsButton:
                startActivity(new Intent(PostAnnouncementActivity.this, ProfileSettingsActivity.class));
                finish();
                break;
            case R.id.logoutButton:
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(PostAnnouncementActivity.this, LoginActivity.class));
                }
                break;
            case R.id.backButton:
                finish();
                break;
        }
        return true;
    }
}
