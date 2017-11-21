package edu.illinois.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * An activity that is created when the user has successfully logged in
 */
public class DashboardActivity extends AppCompatActivity {

    private static final String PASSWORD_CHANGED_SUCCESSFUL = "Password Changed!";
    private static final String INVALID_PASSWORD_ERROR = "Please enter a valid password";

    private FirebaseAuth mAuth;
    private TextView emailTextView;
    private EditText changePasswordField;
    private Button logoutButton;
    private Button changePasswordButton;
    private Menu dashboardMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();

        emailTextView = (TextView) findViewById(R.id.emailTextView);
        changePasswordField = (EditText) findViewById(R.id.newPasswordField);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        changePasswordButton = (Button) findViewById(R.id.changePassowrdButton);

        emailTextView.setText(mAuth.getCurrentUser().getEmail());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutListener();
            }
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
        if (newPassword.length() < 6) {
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
