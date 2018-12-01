package fall2018.csc2017.gamecentre;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Objects;

/**
 * The initial login activity of this app;
 * a login page that allows the user
 * to enter username and passwords, or register as a new user.
 * Adapted from https://github.com/probelalkhan/GhostApp/blob/master/app/src/main/java/
 * net/simplifiedcoding/ghostapp/MainActivity.java
 *
 * This class consists of only UI and Firebase Component. Thus, it is excluded from test.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Serializable {
    private FirebaseAuth mAuth;
    private EditText editTextEmailEntered;
    private EditText editTextPasswordEntered;
    private ProgressBar loginProgressBar;
    private CheckBox mCheckBoxRemember;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    /**
     * Create the login page.
     *
     * @param savedInstanceState The saved instance state from android system.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_login);
        //==================================================================================//
        findViewById(R.id.sign_up_button).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        editTextEmailEntered = findViewById(R.id.login_user_name_entered);
        editTextPasswordEntered = findViewById(R.id.login_password_entered);
        mAuth = FirebaseAuth.getInstance();
        loginProgressBar = findViewById(R.id.login_progress_bar);
        loginProgressBar.setVisibility(View.GONE);
        mCheckBoxRemember = findViewById(R.id.login_remember_checkbox);
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        getPreferencesData();
    }

    /**
     * Onclick event handler; handles sign up button and login button.
     *
     * @param v the view of the context.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_button:
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.login_button:
                String email = editTextEmailEntered.getText().toString();
                String password = editTextPasswordEntered.getText().toString();
                performLogin(email, password);
                break;
        }
    }

    /**
     * Attempt a login for the given email and password.
     *
     * @param email    the email of the user
     * @param password the password of the user
     */
    private void performLogin(final String email, final String password) {
        if (!validate(email, password)) return;
        loginProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mCheckBoxRemember.isChecked()) {
                                Boolean boolIsChecked = mCheckBoxRemember.isChecked();
                                SharedPreferences.Editor editor = mPrefs.edit();
                                editor.putString("pref_name", email);
                                editor.putString("pref_pass", password);
                                editor.putBoolean("pref_check", boolIsChecked);
                                editor.apply();
                            } else {
                                mPrefs.edit().clear().apply();
                            }
                            switchToGameCentre();
                        } else {
                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            loginProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * Invoke the game centre activity.
     */
    private void switchToGameCentre() {
        DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference("Users");

        dataBaseRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("UserClass", currentUser);
                        loginProgressBar.setVisibility(View.GONE);
                        finish();
                        startActivity(intent);

                        editTextEmailEntered.getText().clear();
                        editTextPasswordEntered.getText().clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        loginProgressBar.setVisibility(View.GONE);
                    }
                });



        editTextEmailEntered.getText().clear();
        editTextPasswordEntered.getText().clear();
    }

    /**
     * Check whether the given email and password is valid.
     *
     * @param email    the given email from user
     * @param password the given password from user.
     * @return whether the email and password are valid.
     */
    private boolean validate(String email, String password) {
        boolean valid = true;
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

    /**
     * Get the sharedPreferences data for remembered name, password, and whether the check
     * box is selected.
     */
    private void getPreferencesData() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_name")) {
            String u = sp.getString("pref_name", "not found.");
            editTextEmailEntered.setText(u);
        }
        if (sp.contains("pref_pass")) {
            String p = sp.getString("pref_pass", "not found.");
            editTextPasswordEntered.setText(p);
        }
        if (sp.contains("pref_check")) {
            Boolean b = sp.getBoolean("pref_check", false);
            mCheckBoxRemember.setChecked(b);
        }
    }
}
