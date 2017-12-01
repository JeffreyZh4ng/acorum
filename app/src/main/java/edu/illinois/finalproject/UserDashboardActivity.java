package edu.illinois.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import edu.illinois.finalproject.javaobjects.UserInformation;

/**
 * An activity that is created when the user has successfully logged in
 */
public class UserDashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String userKey;
    private LinearLayout courseListLayout;
    private TextView enrollAlert;
    private Button enrollButton;
    private Button registerCourseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        userKey = mAuth.getCurrentUser().getUid();

        courseListLayout = (LinearLayout) findViewById(R.id.courseList);
        enrollAlert = (TextView) findViewById(R.id.enrollAlert);
        enrollButton = (Button) findViewById(R.id.enrollButton);
        registerCourseButton = (Button) findViewById(R.id.registerCourseButton);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setWelcomeMessage(dataSnapshot);
                setEnrollAlert(dataSnapshot);
                setClassList(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enrollListener();
            }
        });
        registerCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createClassListener();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileButton:
                startActivity(new Intent(UserDashboardActivity.this, ProfileActivity.class));
                finish();
                break;
            case R.id.profileSettingsButton:
                startActivity(new Intent(UserDashboardActivity.this, ProfileSettingsActivity.class));
                finish();
                break;
            case R.id.logoutButton:
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(UserDashboardActivity.this, LoginActivity.class));
                }
                return true;
        }
        return true;
    }

    private void enrollListener() {
        startActivity(new Intent(UserDashboardActivity.this, EnrollActivity.class));
        finish();
    }

    private void createClassListener() {
        startActivity(new Intent(UserDashboardActivity.this, RegisterCourseActivity.class));
        finish();
    }

    private void setWelcomeMessage(DataSnapshot dataSnapshot) {
        StringBuilder message = new StringBuilder("Welcome, ");
        String userFirstName = dataSnapshot.child("users").child(userKey).getValue(UserInformation.class).getFirstName();
        message.append(userFirstName);
        message.append(" ");
        String userLastName = dataSnapshot.child("users").child(userKey).getValue(UserInformation.class).getLastName();
        message.append(userLastName);
        setTitle(message);
    }

    private void setEnrollAlert(DataSnapshot dataSnapshot) {
        int enrolledClassesCount = dataSnapshot.child("users").child(userKey)
                .getValue(UserInformation.class).getEnrolledCourses().size();
        if (enrolledClassesCount == 1) {
            enrollAlert.setVisibility(View.VISIBLE);
        } else {
            enrollAlert.setVisibility(View.GONE);
        }
    }

    private void setClassList(DataSnapshot dataSnapshot) {
        HashMap<String, Boolean> courseList = dataSnapshot.child("users").child(userKey).
                getValue(UserInformation.class).getEnrolledCourses();
        for (String key: courseList.keySet()) {
            if (courseList.get(key)) {
                //courseListLayout.add
            }
        }
    }
}
