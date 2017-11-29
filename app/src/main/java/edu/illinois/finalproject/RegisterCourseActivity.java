package edu.illinois.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.illinois.finalproject.javaobjects.Course;

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
    private EditText courseYearField;
    private EditText courseTermField;
    private EditText studentEnrollmentKeyField;
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
        courseYearField = (EditText) findViewById(R.id.courseYearField);
        courseTermField = (EditText) findViewById(R.id.courseTermField);
        studentEnrollmentKeyField = (EditText) findViewById(R.id.studentEnrollmentKeyField);
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
        String universityName = universityNameField.getText().toString();
        String courseTitle = courseTitleField.getText().toString();
        String courseYear = courseYearField.getText().toString();
        String courseTerm = courseTermField.getText().toString();
        String studentEnrollmentKey = studentEnrollmentKeyField.getText().toString();
        String registerCourseKey = registerCourseKeyField.getText().toString();

        if (!registerCourseKey.equals(CREATE_COURSE_PERMISSION_KEY)) {
            Toast.makeText(RegisterCourseActivity.this, KEYS_DONT_MATCH, Toast.LENGTH_LONG).show();
        }
        else if (universityName.isEmpty() || courseTitle.isEmpty() || courseYear.isEmpty()
                || courseTerm.isEmpty() || studentEnrollmentKey.isEmpty() || registerCourseKey.isEmpty()) {
            Toast.makeText(RegisterCourseActivity.this, NULL_FIELDS_TOAST, Toast.LENGTH_LONG).show();
        } else {
            registerCourse(universityName, courseTitle, courseYear, courseTerm, studentEnrollmentKey);
        }
    }

    private void registerCourse(String universityName, String courseTitle, String courseYear, String courseTerm, String studentEnrollmentKey) {
        String userKey = mAuth.getCurrentUser().getUid();
        Course course = new Course(universityName, studentEnrollmentKey, courseTerm, courseYear);
        mRef.child("courses").child(courseTitle).setValue(course);
        mRef.child("courses").child(courseTitle).child("instructors").child(userKey).setValue(true);
        mRef.child("users").child(userKey).child("enrolledCourses").child(courseTitle).setValue(true);
        Toast.makeText(RegisterCourseActivity.this, COURSE_CREATION_SUCCESS, Toast.LENGTH_LONG).show();
    }
}
