package fall2018.csc2017.gamecentre;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass that displays the per-user scoreboard.
 *
 * This class consists of only UI and Firebase Component. Thus, it is excluded from test.
 */
public class ScoreboardFragment extends Fragment {
    /**
     *  The default selected game
     */
    private String gameSelected = "The Real 2048";

    /**
     * The ViewPager Adapter for scoreboard page.
     */
    private ScoreboardPagerAdapter scoreboardPagerAdapter;

    /**
     * The difficulty Mapping for each game. Key is the game name and value is
     * a string array of difficulties.
     */
    private HashMap<String, String[]> difficultyMap;
    /**
     * The current user of the centre.
     */
    private User user;
    /**
     * The view of the fragment.
     */
    private View view;
    private FragmentManager fragmentManager;
    private ViewPager viewPager;

    /**
     * Required Empty ScoreboardFragment Constructor.
     */
    public ScoreboardFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        difficultyMap = new HashMap<>();
        difficultyMap.put("Sliding Tiles", new String[]{"3 X 3", "4 X 4", "5 X 5"});
        difficultyMap.put("The Real 2048", new String[]{"3 X 3", "4 X 4", "5 X 5", "6 X 6", "7 X 7"});
        difficultyMap.put("Sudoku", new String[]{"All levels"});

        // Get the current user.
        user = MainActivity.currentUser;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_scoreboard, container, false);
        setHasOptionsMenu(true);

        updateGameTitle(gameSelected);

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = view.findViewById(R.id.viewpager);

        fragmentManager = getChildFragmentManager();

        scoreboardPagerAdapter = new ScoreboardPagerAdapter(fragmentManager);
        firebaseUpdateInfo();
        //Set up tab layouts
        viewPager.setOffscreenPageLimit(10);
        viewPager.setAdapter(scoreboardPagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.difficulty_select_tabs);
        tabLayout.setupWithViewPager(viewPager);

        addSelectScoreboardListener();
        addPerUserScoreboardButtonListener();

        return view;
    }

    /**
     * Update the score information into view for the corresponding selected game.
     */
    private void firebaseUpdateInfo() {
        int tabPosition = 0;

        for (String difficultyName : difficultyMap.get(gameSelected)) {
            Bundle b = new Bundle();
            b.putStringArray("info", new String[]{gameSelected, difficultyName});
            final Fragment fragment = new ScoreTabPageFragment();
            fragment.setArguments(b);
            scoreboardPagerAdapter.addFragment(fragment, difficultyName, tabPosition);
            tabPosition++;
        }
        scoreboardPagerAdapter.notifyDataSetChanged();
    }

    /**
     * Add listener for the scoreboard select button.
     */
    private void addSelectScoreboardListener() {
        TextView selectButton = view.findViewById(R.id.fragment_scoreboard_button_select);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseScoreboardDialog();
            }
        });
    }

    /**
     * Choose the game
     */
    private void showChooseScoreboardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Scoreboard");
        final String[] items = {"Sliding Tiles", "The Real 2048", "Sudoku"};
        builder.setNegativeButton("OK", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameSelected = items[which];
                updateGameTitle(gameSelected);
                clearFragmentManager();
                scoreboardPagerAdapter.clearAllFragment();
                scoreboardPagerAdapter.notifyDataSetChanged();
                firebaseUpdateInfo();
            }
        });
        builder.show();
    }

    /**
     * Add listener for the per user scoreboard button.
     */
    private void addPerUserScoreboardButtonListener(){
        TextView perUserButton = view.findViewById(R.id.fragment_scoreboard_show_per_user);
        perUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PerUserScoreboardActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Update the title of to the given name
     * @param name the name of the game.
     */
    private void updateGameTitle(String name) {
        TextView gameTitle = view.findViewById(R.id.fragment_user_scoreboard_game_name);
        gameTitle.setText(name);
    }

    /**
     * Clear all tabs in the TabView.
     */
    private void clearFragmentManager() {
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragmentManager.beginTransaction().remove(fragment).commitNow();
        }
    }
}
