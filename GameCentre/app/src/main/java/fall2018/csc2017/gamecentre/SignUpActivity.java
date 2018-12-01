package fall2018.csc2017.gamecentre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * The sign up activity of this app.
 *
 * This class consists of only UI-related code and Firebase Component. Thus,
 * it is excluded from test.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmailEntered;
    private EditText editTextPasswordEntered;
    private EditText editTextUserNameEntered;
    private FirebaseAuth mAuth;
    private ProgressBar signUpProgressBar;

    /**
     * Create the sign-up page.
     *
     * @param savedInstanceState The saved instance state from android system.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_signup);

        editTextEmailEntered = findViewById(R.id.sign_up_page_email_input);
        editTextPasswordEntered = findViewById(R.id.sign_up_page_password_input);
        editTextUserNameEntered = findViewById(R.id.sign_up_page_name_input);
        signUpProgressBar = findViewById(R.id.sign_up_progress_bar);
        signUpProgressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.sign_up_page_create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_up_page_login_link).setOnClickListener(this);
    }

    /**
     * Onclick event handler.
     *
     * @param v the view of the current context.
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_page_create_account_button:
                String username = editTextUserNameEntered.getText().toString();
                String email = editTextEmailEntered.getText().toString();
                String password = editTextPasswordEntered.getText().toString();
                addNewUser(username, email, password);
                break;
            case R.id.sign_up_page_login_link:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    /**
     * Add a new user into the Firebase Database.
     * Adapted from https://github.com/probelalkhan/GhostApp/blob/master/app/src/main/java/
     * net/simplifiedcoding/ghostapp/MainActivity.java
     *
     * @param username the user name of the user,
     * @param email    the email of the user.
     * @param password the password of the user.
     */
    private void addNewUser(final String username, final String email, final String password) {
        /*
        If the user enters validate information, store all the data into users database.
         */
        signUpProgressBar.setVisibility(View.VISIBLE);
        if (!validate(username, email, password)) {
            signUpProgressBar.setVisibility(View.GONE);
            return;
        }

        // Check if username already exists.
        FirebaseDatabase.getInstance().getReference("Users").
                orderByChild("name").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The user name is already there!
                    Toast.makeText(SignUpActivity.this,
                            "The user name already exists.", Toast.LENGTH_LONG).show();
                    signUpProgressBar.setVisibility(View.GONE);
                } else {
                    // Perform sign up.
                    firebasePerformSignUp(username, email, password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignUpActivity.this, Objects.requireNonNull(databaseError).
                        getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Perform sign up using the given username, email, and password.
     * Store the result into Firebase Database.
     *
     * @param username the user name
     * @param email    the email of the user
     * @param password the password setted by the user
     */
    private void firebasePerformSignUp(final String username, final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            setupProfile(username, email);
                        } else {
                            Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).
                                    getMessage(), Toast.LENGTH_LONG).show();
                            signUpProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * Set up the profile of the user being registered.
     * Adapted from https://github.com/probelalkhan/GhostApp/blob/master/app/src/main/java/net/
     * simplifiedcoding/ghostapp/MainActivity.java
     *
     * @param username the username of the user.
     * @param email    the email of the user.
     */
    private void setupProfile(final String username, String email) {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        User user = new User(username, email, uid);
        // Store userName and email into FireBase database.
        FirebaseDatabase.getInstance().getReference("Users")
                .child(Objects.requireNonNull(uid))
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    signUpProgressBar.setVisibility(View.GONE);
                    finish();
                    Toast.makeText(SignUpActivity.this,
                            "Congrats! You are registered.",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).
                            getMessage(), Toast.LENGTH_LONG).show();
                    signUpProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Check if the given new user's account information is valid.
     * Adapted from:
     * https://github.com/sourcey/materiallogindemo/blob/master/app/src/main/java/com/sourcey/materiallogindemo/LoginActivity.java
     *
     * @return return true if and only if:
     * - The password length is between 6-16
     * - The email address is valid
     * - Both email address and username have not been used before
     */
    private boolean validate(String username, String email, String password) {
        boolean valid = true;
        if (username.isEmpty()) {
            editTextUserNameEntered.setError("Enter a valid user name");
            valid = false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailEntered.setError("Enter a valid email address");
            valid = false;
        } else {
            editTextEmailEntered.setError(null);
        }
        if (password.isEmpty() || password.length() < 6 || password.length() > 16) {
            editTextPasswordEntered.setError("Between 6 and 16 alphanumeric characters");
            valid = false;
        } else {
            editTextPasswordEntered.setError(null);
        }
        return valid;
    }
}
