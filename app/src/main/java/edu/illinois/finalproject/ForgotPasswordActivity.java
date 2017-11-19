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

    private static final String EMPTY_FIELD_ERROR = "Please enter a valid email";
    private static final String MALFORMED_EMAIL_ERROR = "Invalid email entered";
    private static final String EMAIL_DOESNT_EXIST_ERROR = "Email has not yet been registered";
    private static final String EMAIL_SUCCESSFUL = "Email sent!";
    private static final String UNABLE_TO_SEND_EMAIL_ERROR = "Failed to send email";

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
            Toast.makeText(ForgotPasswordActivity.this, EMPTY_FIELD_ERROR, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ForgotPasswordActivity.this, EMAIL_DOESNT_EXIST_ERROR, Toast.LENGTH_LONG).show();
                    } else {
                        sendEmail(email);
                    }
                } catch (RuntimeException e) {
                    Toast.makeText(ForgotPasswordActivity.this, MALFORMED_EMAIL_ERROR, Toast.LENGTH_LONG).show();
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
                Toast.makeText(ForgotPasswordActivity.this, EMAIL_SUCCESSFUL, Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPasswordActivity.this, UNABLE_TO_SEND_EMAIL_ERROR, Toast.LENGTH_LONG).show();
            }
        });
    }
}
