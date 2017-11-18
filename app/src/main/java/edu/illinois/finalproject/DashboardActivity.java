package edu.illinois.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * An activity that is created when the user has successfully logged in
 */
public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView emailTextView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();

        emailTextView = (TextView) findViewById(R.id.emailTextView);
        logoutButton = (Button) findViewById(R.id.logoutButton);

        emailTextView.setText(mAuth.getCurrentUser().getEmail());
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutListener();
            }
        });
    }

    /**
     * Helper method that will log the user out when they click the logout button
     */
    public void logoutListener() {
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
        }
    }
}
