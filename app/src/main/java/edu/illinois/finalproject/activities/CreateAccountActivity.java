package edu.illinois.finalproject.activities;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.javaobjects.UserInformation;

/**
 * An activity that is opened when the user wants to create a new account
 */
public class CreateAccountActivity extends AppCompatActivity {

    private static final String PASSWORDS_DONT_MATCH = "The passwords you entered don't match";
    private static final String ACCOUNT_CREATION_SUCCESS = "Account successfully created!";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private Button registerButton;
    private Button returnToLoginButton;
    private Button forgotPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        firstNameField = (EditText) findViewById(R.id.firstNameField);
        lastNameField = (EditText) findViewById(R.id.lastNameField);
        emailField = (EditText) findViewById(R.id.emailFeild);
        passwordField = (EditText) findViewById(R.id.passwordField);
        confirmPasswordField = (EditText) findViewById(R.id.confirmPasswordField);
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
                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                finish();
            }
        });
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });
    }

    /**
     * Implementation for the register button action listener. Attempts to register the user if
     * all conditions are satisfied.
     */
    private void registerListener() {
        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(CreateAccountActivity.this, PASSWORDS_DONT_MATCH, Toast.LENGTH_LONG).show();
        }
        else if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(CreateAccountActivity.this, Constants.EMPTY_FIELDS_TOAST, Toast.LENGTH_LONG).show();
        } else {
            signUpUser(firstName, lastName, email, password);
        }
    }

    /**
     * Helper method that makes a request to the FireBase Database and creates a users account
     *
     * @param email The email the user inputted
     * @param password The password the user inputted
     */
    private void signUpUser(final String firstName, final String lastName, final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateAccountActivity.this, ACCOUNT_CREATION_SUCCESS, Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    mRef.child(Constants.USERS_CHILD).child(user.getUid()).setValue(new UserInformation(firstName, lastName, email));
                    startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Error: " + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
