package edu.illinois.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import edu.illinois.finalproject.javaobjects.Course;
import edu.illinois.finalproject.javaobjects.UserInformation;

public class RegisterCourseActivity extends AppCompatActivity {

    private static final String NULL_FIELDS_TOAST = "Please complete all the empty fields";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_course);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

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
            Toast.makeText(RegisterCourseActivity.this, NULL_FIELDS_TOAST, Toast.LENGTH_LONG).show();
        } else {
            final String[] userName = new String[1];
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInformation userInformation = dataSnapshot.child("users").child
                            (mAuth.getCurrentUser().getUid()).getValue(UserInformation.class);
                    userName[0] = userInformation.getFirstName() + " " + userInformation.getLastName();
                    registerCourse(universityName, courseTerm, courseSection, courseYear, userName[0], courseTitle);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void registerCourse(String universityName, String courseTerm, String courseSection, String courseYear, String userName, String courseTitle) {
        String userKey = mAuth.getCurrentUser().getUid();
        Course course = new Course(courseTitle, universityName, courseTerm, courseSection, courseYear, userName, userKey);
        String key = mRef.child("courses").push().getKey();
        mRef.child("courses").child(key).setValue(course);
        mRef.child("users").child(userKey).child("enrolledCourses").child(key).setValue(true);
        Toast.makeText(RegisterCourseActivity.this, COURSE_CREATION_SUCCESS, Toast.LENGTH_LONG).show();
        startActivity(new Intent(RegisterCourseActivity.this, UserDashboardActivity.class));
    }
}
