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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private void resetPassword(final String email) {
        mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                try {
                    if (task.isComplete() && task.getResult().getProviders().isEmpty()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Email has not yet been registered", Toast.LENGTH_LONG).show();
                    } else {
                        sendEmail(email);
                    }
                } catch (RuntimeException e) {
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid email entered", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Helper method for the reset password method that will send the new password email to the specified
     * email address if the email is registered in the authentication database
     *
     * @param email The email you want to send the password reset to
     */
    private void sendEmail(String email) {
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ForgotPasswordActivity.this, "Email sent!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPasswordActivity.this, "Failed to send email", Toast.LENGTH_LONG).show();
            }
        });
    }
}
