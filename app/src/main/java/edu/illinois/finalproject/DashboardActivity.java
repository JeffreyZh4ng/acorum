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

    private static final String PASSWORD_CHANGED_SUCCESSFUL = "Password Changed!";
    private static final String INVALID_PASSWORD_ERROR = "Please enter a valid password";
    private static final int MIN_PASSWORD_LENGTH = 6;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private EditText changePasswordField;
    private Button changePasswordButton;
    private Button enrollButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        changePasswordField = (EditText) findViewById(R.id.newPasswordField);
        changePasswordButton = (Button) findViewById(R.id.changePassowrdButton);
        enrollButton = (Button) findViewById(R.id.enrollButton);

        setTitle("Acorum - Dashboard");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setWelcomeMessage(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordListener();
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
        if (item.getItemId() == R.id.profileButton) {
            startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
            finish();
            return true;
        } else if (item.getItemId() == R.id.profileSettingsButton) {
            startActivity(new Intent(DashboardActivity.this, ProfileSettingsActivity.class));
            finish();
            return true;
        } else if (item.getItemId() == R.id.logoutButton){
            logoutListener();
            return true;
        } else {
            return true;
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

    /**
     * Implementation for on click listener that will log the user out when they click the logout button
     */
    private void logoutListener() {
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
        }
    }

    /**
     * Implementation for on the change password button listener that will try to set the users new password
     */
    private void changePasswordListener() {
        String newPassword = changePasswordField.getText().toString();
        if (newPassword.length() < MIN_PASSWORD_LENGTH) {
            Toast.makeText(DashboardActivity.this, INVALID_PASSWORD_ERROR, Toast.LENGTH_LONG).show();
        } else {
            changePassword(newPassword);
        }
    }

    /**
     * Method that will change the password of the user in the Dashboard activity
     *
     * @param newPassword The new password
     */
    private void changePassword(String newPassword) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        firebaseUser.updatePassword(newPassword).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DashboardActivity.this, PASSWORD_CHANGED_SUCCESSFUL, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
