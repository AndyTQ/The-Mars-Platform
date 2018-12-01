package fall2018.csc2017.gamecentre;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

/**
 * Activity for editing profile, and updating to Firebase Database.
 * Adapted from https://www.youtube.com/watch?v=ytoOw5ZDQkU
 *
 * This class consists of only UI and Firebase Component. Thus, it is excluded from test.
 */
public class ProfileEditActivity extends AppCompatActivity {
    /**
     * Fields for edit texts.
     */
    private EditText userNameEditText;
    private EditText userBioEditText;
    private EditText userLocationEditText;
    /**
     * Field for profile pic image view.
     */
    private ImageView userImageProfileView;
    /**
     * Field for buttons.
     */
    private Button saveProfileBtn;
    private Button cancelProfileBtn;
    /**
     * Firebase Authentication field.
     */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    /**
     * Firebase Database field.
     */
    private DatabaseReference mUserDatabase;
    private StorageReference mStorageRef;
    /**
     * Uri for the image imported.
     */
    private Uri imageHoldUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // For Android API >= 27
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // Assign IDs
        userNameEditText = findViewById(R.id.profile_edit_username);
        userLocationEditText = findViewById(R.id.profile_edit_location);
        userBioEditText = findViewById(R.id.profile_edit_bio);

        // Bind EditTexts
        userNameEditText.setText(MainActivity.currentUser.getName());
        userLocationEditText.setText(MainActivity.currentUser.getLocation());
        userBioEditText.setText(MainActivity.currentUser.getBio());

        // Bind ImageView
        userImageProfileView = findViewById(R.id.profile_edit_photo);
        Picasso.get().load(MainActivity.currentUser.getImageUrl()).into(userImageProfileView);

        // Bind buttons
        saveProfileBtn = findViewById(R.id.profile_edit_save);
        cancelProfileBtn = findViewById(R.id.profile_edit_cancel);

        // Assign instance to firebase auth.
        mAuth = FirebaseAuth.getInstance();

        // Set up Listener for authentication state: If state changes to log off, go back to login.
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Logic check user.
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    finish();
                    Intent moveToHome = new Intent(
                            ProfileEditActivity.this, LoginActivity.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);
                }

            }
        };

        // Firebase Database instance.
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(
                mAuth.getCurrentUser().getUid()
        );

        // Firebase Database Storage reference.
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // Save profile onclick listener.
        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic for saving user profile.
                saveUserProfile();
            }
        });

        // Cancel save onclick listener.
        cancelProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // User imageview onclick listener.
        userImageProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(ProfileEditActivity.this);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            startCropImageActivity(imageUri);
        }

        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();
                userImageProfileView.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    /**
     * Starts the activity for cropping image.
     *
     * @param imageUri The Uri for image given.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
    }

    /**
     * Saves the image of the user into Firebase Database.
     */
    private void saveUserProfile() {

        final String username, userLocation, userBio;

        username = userNameEditText.getText().toString().trim();
        userLocation = userLocationEditText.getText().toString().trim();
        userBio = userBioEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(userLocation)
                && !TextUtils.isEmpty(userBio)) {
            if (imageHoldUri != null) {
                StorageReference mChildStorage = mStorageRef.child(
                        "User_Profile").child(
                        imageHoldUri.getLastPathSegment());

                mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                mUserDatabase.child("imageUrl").setValue(imageUrl).addOnCompleteListener
                                        (new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                setProfileWithUniqueName(username, userLocation, userBio);
                                            }
                                        });
                            }
                        });

                    }
                });
            } else {
                setProfileWithUniqueName(username, userLocation, userBio);
            }
        } else {
            Toast.makeText(this, "Please enter valid information.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Save the modified username, email, and password of the user into database.
     *
     * @param username     the username of the user.
     * @param userLocation the location of the user.
     * @param userBio      the bio of the user.
     */
    private void setProfileWithUniqueName(final String username, final String userLocation, final String userBio) {
        FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("name").equalTo(username).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && !username.equals(MainActivity.currentUser.getName())) {
                            // The user name is already there!
                            Toast.makeText(ProfileEditActivity.this,
                                    "The user name already exists.", Toast.LENGTH_LONG).show();
                        } else {
                            mUserDatabase.child("name").setValue(username);
                            mUserDatabase.child("location").setValue(userLocation);
                            mUserDatabase.child("bio").setValue(userBio);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ProfileEditActivity.this, Objects.requireNonNull(databaseError).
                                getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
