package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.illinois.finalproject.R;

public class ProfileSettingsActivity extends AppCompatActivity {

    private static final String PASSWORD_CHANGED_SUCCESSFUL = "Password Changed!";
    private static final String INVALID_PASSWORD_ERROR = "Please enter a valid password";
    private static final String TITLE_STRING = "Profile Settings";
    private static final int MIN_PASSWORD_LENGTH = 6;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private EditText changePasswordField;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        setTitle(TITLE_STRING);

        changePasswordField = (EditText) findViewById(R.id.newPasswordField);
        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordListener();;
            }
        });
    }
    /**
     * Implementation for on the change password button listener that will try to set the users new password
     */
    private void changePasswordListener() {
        String newPassword = changePasswordField.getText().toString();
        if (newPassword.length() < MIN_PASSWORD_LENGTH) {
            Toast.makeText(ProfileSettingsActivity.this, INVALID_PASSWORD_ERROR, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ProfileSettingsActivity.this, PASSWORD_CHANGED_SUCCESSFUL, Toast.LENGTH_LONG).show();
                }
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
            case R.id.logoutButton:
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ProfileSettingsActivity.this, LoginActivity.class));
                }
                break;
            case R.id.backButton:
                startActivity(new Intent(ProfileSettingsActivity.this, UserDashboardActivity.class));

                break;
        }
        return true;
    }
}
