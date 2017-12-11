package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.javaobjects.Announcement;
import edu.illinois.finalproject.javaobjects.AnnouncementList;
import edu.illinois.finalproject.javaobjects.Course;
import edu.illinois.finalproject.javaobjects.UserInformation;

/**
 * The activity that is created when the user wants to create a new course
 */
public class RegisterCourseActivity extends AppCompatActivity {

    private static final String KEYS_DONT_MATCH = "The create course permission key you entered doesn't match";
    private static final String COURSE_CREATION_SUCCESS = "Course successfully registered!";
    private static final String CREATE_COURSE_PERMISSION_KEY = "123456";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private EditText universityNameField;
    private EditText courseTitleField;
    private EditText courseSectionField;
    private EditText courseYearField;
    private EditText courseTermField;
    private EditText registerCourseKeyField;
    private Button registerCourseButton;
    private String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_course);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        userKey = mAuth.getCurrentUser().getUid();

        universityNameField = (EditText) findViewById(R.id.universityNameField);
        courseTitleField = (EditText) findViewById(R.id.courseTitleField);
        courseSectionField = (EditText) findViewById(R.id.courseSectionField);
        courseYearField = (EditText) findViewById(R.id.courseYearField);
        courseTermField = (EditText) findViewById(R.id.courseTermField);
        registerCourseKeyField = (EditText) findViewById(R.id.registerCourseKeyField);
        registerCourseButton = (Button) findViewById(R.id.registerCourseButton);

        registerCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCourse();
            }
        });
    }

    /**
     * Helper method that will validate all the fields entered. If the create course permission key
     * is not correct, or if any of the fields are empty, a coruse will not be made.
     */
    private void validateCourse() {
        final String universityName = universityNameField.getText().toString();
        final String courseTitle = courseTitleField.getText().toString();
        final String courseSection = courseSectionField.getText().toString();
        final String courseYear = courseYearField.getText().toString();
        final String courseTerm = courseTermField.getText().toString();
        String registerCourseKey = registerCourseKeyField.getText().toString();

        if (!registerCourseKey.equals(CREATE_COURSE_PERMISSION_KEY)) {
            Toast.makeText(RegisterCourseActivity.this, KEYS_DONT_MATCH, Toast.LENGTH_LONG).show();
        }
        else if (universityName.isEmpty() || courseTitle.isEmpty() || courseYear.isEmpty() || courseSection.isEmpty()
                || courseTerm.isEmpty() || registerCourseKey.isEmpty()) {
            Toast.makeText(RegisterCourseActivity.this, Constants.EMPTY_FIELDS_TOAST, Toast.LENGTH_LONG).show();
        } else {
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // Gets the name for the user and formats it for ease of display later
                    UserInformation userInformation = dataSnapshot.child(Constants.USERS_CHILD)
                            .child(mAuth.getCurrentUser().getUid()).getValue(UserInformation.class);
                    String userName = userInformation.getFirstName() + " " + userInformation.getLastName();

                    Course course = new Course(courseTitle, universityName, courseTerm, courseSection, courseYear, userName, userKey);
                    registerCourse(course);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    
                }
            });
        }
    }

    /**
     * Helper method that will register the course into the database and will add the course into
     * creator's enrolled courses
     *
     * @param course The course that is being created
     */
    private void registerCourse(Course course) {
        String courseKey = mRef.child(Constants.COURSES_CHILD).push().getKey();
        mRef.child(Constants.COURSES_CHILD).child(courseKey).setValue(course);
        mRef.child(Constants.USERS_CHILD).child(userKey).child(Constants.ENROLLED_COURSES_CHILD).child(courseKey).setValue(true);
        mRef.child(Constants.COURSE_ANNOUNCEMENTS_CHILD).child(courseKey).setValue(new AnnouncementList());

        Toast.makeText(RegisterCourseActivity.this, COURSE_CREATION_SUCCESS, Toast.LENGTH_LONG).show();
        startActivity(new Intent(RegisterCourseActivity.this, UserDashboardActivity.class));
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
                startActivity(new Intent(RegisterCourseActivity.this, ProfileActivity.class));
                finish();
                break;
            case R.id.profileSettingsButton:
                startActivity(new Intent(RegisterCourseActivity.this, ProfileSettingsActivity.class));
                finish();
                break;
            case R.id.logoutButton:
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(RegisterCourseActivity.this, LoginActivity.class));
                }
                break;
            case R.id.backButton:
                startActivity(new Intent(RegisterCourseActivity.this, UserDashboardActivity.class));
                break;
        }
        return true;
    }
}
