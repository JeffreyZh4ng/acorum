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
 * An activity that is opened when the user wants to create a new account
 */
public class CreateAccountActivity extends AppCompatActivity {

    private static final String NULL_CREDENTIALS_TOAST = "Please enter an Email/Password";

    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;
    private Button registerButton;
    private Button returnToLoginButton;
    private Button forgotPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();

        emailField = (EditText) findViewById(R.id.emailFeild);
        passwordField = (EditText) findViewById(R.id.passwordField);
        registerButton = (Button) findViewById(R.id.registerButton);
        returnToLoginButton = (Button) findViewById(R.id.returnToLoginButton);
        forgotPasswordButton = (Button) findViewById(R.id.forgotPasswordButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerListener();
            }
        });

        returnToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToLoginListener();
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
     * Implementation for the register button action listener
     */
    public void registerListener() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(CreateAccountActivity.this, NULL_CREDENTIALS_TOAST, Toast.LENGTH_LONG).show();
        } else {
            signUpUser(email, password);
        }
    }

    /**
     * Implementation for the return to login button action listener
     */
    public void returnToLoginListener() {
        startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * Implementation for the forgot password button action listener
     */
    public void forgotPasswordListener() {
        startActivity(new Intent(CreateAccountActivity.this, ForgotPasswordActivity.class));
        finish();
    }

    /**
     * Makes a request to the FireBase Database and creates a users account
     *
     * @param email The email the user inputted
     * @param password The password the user inputted
     */
    private void signUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateAccountActivity.this, "Account successfully created!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Error: " + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
