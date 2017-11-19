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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The initial activity that will be created when the user has to log in
 */
public class LoginActivity extends AppCompatActivity {

    private static final String INVALID_CREDENTIALS_TOAST = "Invalid Email/Password";
    private static final String NULL_CREDENTIALS_TOAST = "Please enter an Email/Password";

    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private Button createAccountButton;
    private Button forgotPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        }

        emailField = (EditText) findViewById(R.id.emailFeild);
        passwordField = (EditText) findViewById(R.id.passwordField);
        loginButton = (Button) findViewById(R.id.registerButton);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        forgotPasswordButton = (Button) findViewById(R.id.forgotPasswordButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginListener();
            }
        });
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccountListener();
            }
        });
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordListener();
            }
        });
    }

    /**
     * Implementation for the create account button action listener that will open the create account activity
     */
    private void createAccountListener() {
        startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
        finish();
    }

    /**
     * Implementation for the forgot password button action listener that will open up the forgot
     * password activity
     */
    private void forgotPasswordListener() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        finish();
    }

    /**
     * Implementation for the login button action listener that will try to open the dashboard activity
     */
    private void loginListener() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, NULL_CREDENTIALS_TOAST, Toast.LENGTH_LONG).show();
        } else {
            loginUser(email, password);
        }
    }

    /**
     * Helper method that makes a request to the FireBase Database to log the user in
     *
     * @param email The email the user inputted
     * @param password The password the user inputted
     */
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, INVALID_CREDENTIALS_TOAST, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
