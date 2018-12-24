package fall2018.csc2017.gamecentre;

import java.io.Serializable;
import java.util.Objects;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * The main GameCentre activity.
 * Bottom navigation bar adapted from
 * https://medium.com/@hitherejoe/exploring-the-android-design-support-library-bottom-navigation-drawer-548de699e8e0
 *
 *  This class consists of only UI and Firebase Component. Thus, it is excluded from test.
 */
public class MainActivity extends AppCompatActivity implements Serializable {
    public static User currentUser;
    private FirebaseAuth mAuth;
    private BottomNavigationView mainNav;


    /**
     * Create the main game centre window and associated widgets.
     *
     * @param savedInstanceState The saved instance state from android system.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_main);

        //Set default fragment being displayed
        currentUser = (User) getIntent().getSerializableExtra("UserClass");
        mainNav = findViewById(R.id.bottom_navigation);

        setFragment(new ProfileFragment());

        //Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();


        mainNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return setFragmentById(item.getItemId());
            }
        });

        addInfoUpdateListener();
    }

    /**
     * Set up the given fragment to display.
     *
     * @param fragment the given fragment.
     */
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_centre_main_frame, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * Refresh the user's information from server when the activity is returned to foreground.
     */
    @Override
    protected void onResume(){
        super.onResume();
    }

    /**
     * Update the user information by querying firebase database.
     */
    private void addInfoUpdateListener(){
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users");
        ValueEventListener userDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                //Update UI
                setFragmentById(mainNav.getSelectedItemId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        dataRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).
                addValueEventListener(userDataListener);
    }

    /**
     * Set the fragment to foreground by id.
     *
     * @param id the id of the fragment.
     * @return whether the id corresponds to an existing fragment.
     */
    private boolean setFragmentById(int id) {
        Fragment fragment;
        switch (id){
            case R.id.activity_centre_profile:
                fragment = new ProfileFragment();
                setFragment(fragment);
                return true;
            case R.id.activity_centre_games:
                fragment = new GamesFragment();
                setFragment(fragment);
                return true;
            case R.id.activity_centre_rankings:
                fragment = new ScoreboardFragment();
                setFragment(fragment);
                return true;
            default:
                return false;
        }
    }

}

