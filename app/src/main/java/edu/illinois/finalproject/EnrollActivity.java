package edu.illinois.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EnrollActivity extends AppCompatActivity {

    private static final String EMPTY_FIELD_ERROR = "Please enter a valid course key";
    private static final String COURSE_DOESNT_EXIST = "A course with the specified key does not exist!";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private EditText courseEnrollKeyField;
    private Button joinCourseButton;
    private String courseKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        courseEnrollKeyField = (EditText) findViewById(R.id.courseEnrollKeyField);
        joinCourseButton = (Button) findViewById(R.id.joinCourseButton);

        joinCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinCourseListener();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        getMenuInflater().inflate(R.menu.menu_back_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileButton:
                startActivity(new Intent(EnrollActivity.this, ProfileActivity.class));
                finish();
                break;
            case R.id.profileSettingsButton:
                startActivity(new Intent(EnrollActivity.this, ProfileSettingsActivity.class));
                finish();
                break;
            case R.id.logoutButton:
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(EnrollActivity.this, LoginActivity.class));
                }
                break;
            case R.id.backButton:
                startActivity(new Intent(EnrollActivity.this, UserDashboardActivity.class));
                break;
        }
        return true;
    }

    private void joinCourseListener() {
        courseKey = courseEnrollKeyField.getText().toString();
        if (courseKey.isEmpty()) {
            Toast.makeText(EnrollActivity.this, EMPTY_FIELD_ERROR, Toast.LENGTH_LONG).show();
        } else {
            enrollStudent();
        }
    }

    private void enrollStudent() {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean doesCourseExist = dataSnapshot.child("courses").child(courseEnrollKeyField.getText()
                        .toString()).exists();
                if (doesCourseExist) {
                    String userKey = mAuth.getCurrentUser().getUid();
                    mRef.child("users").child(userKey).child("enrolledCourses").child(courseKey).setValue(true);
                    startActivity(new Intent(EnrollActivity.this, UserDashboardActivity.class));
                } else {
                    Toast.makeText(EnrollActivity.this, COURSE_DOESNT_EXIST, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
