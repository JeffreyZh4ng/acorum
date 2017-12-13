package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.HashMap;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.javaobjects.Course;
import edu.illinois.finalproject.javaobjects.UserInformation;
import edu.illinois.finalproject.recycleradapters.CourseRecyclerViewAdapter;
import edu.illinois.finalproject.recycleradapters.ForumPostRecyclerAdapter;

/**
 * An activity that is created when the user has successfully logged in. The user can enroll or create
 * classes. They can also pick a class to go to its course dashboard.
 */
public class UserDashboardActivity extends AppCompatActivity {

    private static final String WELCOME_MESSAGE = "Welcome, ";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String userKey;
    private RecyclerView courseRecyclerView;
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

        courseRecyclerView = (RecyclerView) findViewById(R.id.courseRecyclerView);
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
                startActivity(new Intent(UserDashboardActivity.this, EnrollActivity.class));
                finish();
            }
        });
        registerCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDashboardActivity.this, RegisterCourseActivity.class));
                finish();
            }
        });
    }

    /**
     * Helper method that sets the title of the activity
     *
     * @param dataSnapshot The dataSnapshot needed to retrieve the users name
     */
    private void setWelcomeMessage(DataSnapshot dataSnapshot) {
        StringBuilder message = new StringBuilder(WELCOME_MESSAGE);

        String userFirstName = dataSnapshot.child(Constants.USERS_CHILD).child(userKey).getValue(UserInformation.class).getFirstName();
        message.append(userFirstName);
        message.append(" ");
        String userLastName = dataSnapshot.child(Constants.USERS_CHILD).child(userKey).getValue(UserInformation.class).getLastName();
        message.append(userLastName);

        setTitle(message);
    }

    /**
     * Helper method that will hide or show the alert message depending on if the user is enrolled
     * in any classes
     *
     * @param dataSnapshot The dataSnapshot needed to retrieve the list of courses the user is in
     */
    private void setEnrollAlert(DataSnapshot dataSnapshot) {
        int enrolledClassesCount = dataSnapshot.child(Constants.USERS_CHILD).child(userKey)
                .getValue(UserInformation.class).getEnrolledCourses().size();

        if (enrolledClassesCount == 0) {
            enrollAlert.setVisibility(View.VISIBLE);
        } else {
            enrollAlert.setVisibility(View.GONE);
        }
    }

    /**
     * Helper metho that will populate the list of courses that the user is a part of
     *
     * @param dataSnapshot The dataSnapshot needed to retrieve the list of courses the user is in
     */
    private void setClassList(DataSnapshot dataSnapshot) {
        HashMap<String, Boolean> courseHashMap = dataSnapshot.child(Constants.USERS_CHILD).child(userKey).
                getValue(UserInformation.class).getEnrolledCourses();
        ArrayList<Course> courseList = new ArrayList<>();
        ArrayList<String> courseKeyList = new ArrayList<>();
        for (String course: courseHashMap.keySet()) {
            courseList.add(dataSnapshot.child(Constants.COURSES_CHILD).child(course).getValue(Course.class));
            courseKeyList.add(course);
        }
        Log.d("SIZE OF THE THING", courseList.size() + "");

        CourseRecyclerViewAdapter adapter = new CourseRecyclerViewAdapter(courseList, courseKeyList, dataSnapshot, userKey);
        courseRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(courseRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        courseRecyclerView.setLayoutManager(layoutManager);
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
                startActivity(new Intent(UserDashboardActivity.this, ProfileSettingsActivity.class));
                finish();
                break;
            case R.id.logoutButton:
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(UserDashboardActivity.this, LoginActivity.class));
                }
                break;
        }
        return true;
    }
}
