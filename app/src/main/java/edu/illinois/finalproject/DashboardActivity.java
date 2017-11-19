package edu.illinois.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private FirebaseAuth mAuth;
    private TextView emailTextView;
    private Button logoutButton;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();

        emailTextView = (TextView) findViewById(R.id.emailTextView);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        changePasswordButton = (Button) findViewById(R.id.changePassowrdButton);

        emailTextView.setText(mAuth.getCurrentUser().getEmail());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutListener();
            }
        });
    }

    /**
     * Implementation for on click listener that will log the user out when they click the logout button
     */
    public void logoutListener() {
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
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
                    Toast.makeText(DashboardActivity.this, "Password Changed!", Toast.LENGTH_LONG).show();
                } else {

                }
            }
        });
    }
}
