package edu.illinois.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailField;
    private Button sendButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();

        emailField = (EditText) findViewById(R.id.recoveryEmailField);
        sendButton = (Button) findViewById(R.id.sendButton);
        backButton = (Button) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButtonListener();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPasswordListener();
            }
        });
    }

    /**
     * Implementation for the back button listener that will open the login activity
     */
    private void backButtonListener() {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * Implementation for the password reset listener that will try to send an email to the user to
     * try to reset their password
     */
    private void resetPasswordListener() {
        String email = emailField.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email", Toast.LENGTH_LONG).show();
        } else {
            resetPassword(email);
        }
    }

    /**
     * Helper method that makes a request to FireBase that will send an email to the specified user
     * to try to reset their password
     *
     * @param email The email you want to send the reset password to
     */
    private void resetPassword(String email) {
        final boolean[] emailExists = new boolean[1];
        mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                try {
                    if (task.getResult().getProviders().isEmpty()) {
                        emailExists[0] = false;
                        Toast.makeText(ForgotPasswordActivity.this, "Email has not yet been registered", Toast.LENGTH_LONG).show();
                    } else {
                        emailExists[0] = true;
                    }
                } catch (RuntimeException e) {
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid email entered", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (emailExists[0]) {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Email sent!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Failed to send email", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
