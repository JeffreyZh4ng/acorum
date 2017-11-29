package edu.illinois.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.illinois.finalproject.javaobjects.UserInformation;

/**
 * An activity that is created when the user has successfully logged in
 */
public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private TextView enrollAlert;
    private Button enrollButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        enrollAlert = (TextView) findViewById(R.id.enrollAlert);
        enrollButton = (Button) findViewById(R.id.enrollButton);

        setTitle("Acorum - Dashboard");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setWelcomeMessage(dataSnapshot);
                setEnrollAlert(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
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
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                finish();
                break;
            case R.id.profileSettingsButton:
                startActivity(new Intent(DashboardActivity.this, ProfileSettingsActivity.class));
                finish();
                break;
            case R.id.logoutButton:
                logoutListener();
                return true;
        }
        return true;
    }

    /**
     * Implementation for on click listener that will log the user out when they click the logout button
     */
    private void logoutListener() {
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
        }
    }

    private void setWelcomeMessage(DataSnapshot dataSnapshot) {
        StringBuilder message = new StringBuilder("Welcome, ");
        String userKey = mAuth.getCurrentUser().getUid();
        String userFirstName = dataSnapshot.child("users").child(userKey).getValue(UserInformation.class).getFirstName();
        message.append(userFirstName);
        message.append(" ");
        String userLastName = dataSnapshot.child("users").child(userKey).getValue(UserInformation.class).getLastName();
        message.append(userLastName);
        setTitle(message);
    }

    private void setEnrollAlert(DataSnapshot dataSnapshot) {
        String userKey = mAuth.getCurrentUser().getUid();
        int enrolledClassesCount = dataSnapshot.child("users").child(userKey)
                .getValue(UserInformation.class).getEnrolledCourses().size();
        if (enrolledClassesCount == 1) {
            enrollAlert.setVisibility(View.VISIBLE);
        } else {
            enrollAlert.setVisibility(View.GONE);
        }
    }
}
