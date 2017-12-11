package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.javaobjects.Course;
import edu.illinois.finalproject.javaobjects.UserInformation;

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
        HashMap<String, Boolean> courseList = dataSnapshot.child(Constants.USERS_CHILD).child(userKey).
                getValue(UserInformation.class).getEnrolledCourses();

        for (final String key: courseList.keySet()) {
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    View view = LayoutInflater.from(courseListLayout.getContext()).inflate
                            (R.layout.activity_course_list_element, courseListLayout, false);
                    Course course = dataSnapshot.child(Constants.COURSES_CHILD).child(key).getValue(Course.class);

                    setElementText(key, course, view);
                    setElementOnClick(key, view, dataSnapshot);

                    courseListLayout.addView(view);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    /**
     * Helper method that will be used to set each of the course element's text
     *
     * @param key The courseKey of the course
     * @param course The course object from the database
     * @param view The view of the inflater
     */
    private void setElementText(String key, Course course, View view) {
        TextView courseTitleField = (TextView) view.findViewById(R.id.courseNameView);
        courseTitleField.setText(course.getCourseName());

        TextView courseInstructorField = (TextView) view.findViewById(R.id.courseInstructorView);
        courseInstructorField.setText("Instructor:" + course.getHeadInstructor());

        TextView courseUniversityField = (TextView) view.findViewById(R.id.courseUniversityView);
        courseUniversityField.setText(course.getUniversity());

        TextView courseInfoField = (TextView) view.findViewById(R.id.courseInfoView);
        StringBuilder courseInfo = new StringBuilder(course.getTerm());
        courseInfo.append(" ");
        courseInfo.append(course.getYear());
        courseInfo.append(", Section: ");
        courseInfo.append(course.getSection());
        courseInfoField.setText(courseInfo);
    }

    /**
     * Helper method that sets on click listeners to each of the elements in the list and adds in the
     * extra arguemnts needed for the course dashboard activity
     *
     * @param courseKey The courseKey of the course
     * @param view The view of the layout inflater
     */
    private void setElementOnClick(final String courseKey, View view, final DataSnapshot dataSnapshot) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> courseMap = dataSnapshot.child(Constants.COURSES_CHILD)
                        .child(courseKey).getValue(Course.class).getInstructors();
                boolean isInstructor = courseMap.containsKey(userKey);

                Intent intent = new Intent(UserDashboardActivity.this, CourseDashboardActivity.class)
                        .putExtra(Constants.COURSE_KEY_ARG, courseKey).putExtra(Constants.IS_INSTRUCTOR_ARG, isInstructor);
                startActivity(intent);
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
                break;
        }
        return true;
    }
}
