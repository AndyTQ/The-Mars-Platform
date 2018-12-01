package fall2018.csc2017.gamecentre;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass that displays the current user's profile.
 *
 * This class consists of only UI and asynchronous url fetcher. Thus, it is excluded from test.
 */
public class ProfileFragment extends Fragment {
    private TextView nameText;
    private TextView emailText;
    private TextView locationText;
    private TextView bioText;
    private ImageView profileImage;
    private ProgressBar imageProgressBar;

    /**
     * Required empty public constructor for fragment class.
     */
    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        nameText = view.findViewById(R.id.activity_centre_profiles_displayname);
        emailText = view.findViewById(R.id.activity_centre_profile_email);
        locationText = view.findViewById(R.id.activity_centre_profile_location);
        bioText = view.findViewById(R.id.activity_centre_profile_bio);
        profileImage = (de.hdodenhof.circleimageview.CircleImageView)
                view.findViewById(R.id.profile_image);
        imageProgressBar = view.findViewById(R.id.fragment_profile_image_progress_bar);
        imageProgressBar.setVisibility(View.VISIBLE);
        setTexts();
        setProfileImage();

        view.findViewById(R.id.profile_edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                Objects.requireNonNull(getActivity()).finish();
                startActivity(intent);
            }
        });
        return view;
    }

    /**
     * Set the text for Profile, according to the current user.
     */
    private void setTexts() {
        nameText.setText(MainActivity.currentUser.getName());
        emailText.setText(MainActivity.currentUser.getEmail());
        locationText.setText(MainActivity.currentUser.getLocation());
        bioText.setText(MainActivity.currentUser.getBio());
    }

    /**
     * Set the profile image for the profile, according to the user.
     */
    private void setProfileImage() {
        String imageUrl = MainActivity.currentUser.getImageUrl();
        Picasso.get().load(imageUrl).into(profileImage, new Callback() {
            @Override
            public void onSuccess() {
                imageProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setTexts();
    }
}
