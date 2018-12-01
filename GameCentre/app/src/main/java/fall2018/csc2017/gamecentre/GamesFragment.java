package fall2018.csc2017.gamecentre;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fall2018.csc2017.gamecentre.games.game2048.Game2048StartingActivity;
import fall2018.csc2017.gamecentre.games.slidingtiles.SlidingTilesStartingActivity;
import fall2018.csc2017.gamecentre.games.sudoku.SudokuStartingActivity;


/**
 * A simple {@link Fragment} subclass that displays the games available.
 */
public class GamesFragment extends Fragment {

    /**
     * Required empty constructor.
     */
    public GamesFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_games, container, false);
        addSlidingTilesCardListener(view);
        addGame2048CardListener(view);
        addSudokuCardListener(view);
        return view;
    }

    /**
     * Add listener for game 2048 card.
     * @param view the view of the activity.
     */
    private void addGame2048CardListener(View view) {
        CardView game_2048_card = view.findViewById(R.id.game_2048_card);
        game_2048_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Game2048StartingActivity.class);
                User currentUser = MainActivity.currentUser;
                intent.putExtra("UserClass", currentUser);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });
    }

    /**
     * Add listener for sliding tiles card.
     * @param view the view of the activity.
     */
    private void addSlidingTilesCardListener(View view) {
        CardView sliding_tiles_card = view.findViewById(R.id.sliding_tiles_card);
        sliding_tiles_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SlidingTilesStartingActivity.class);
                User currentUser = MainActivity.currentUser;
                intent.putExtra("UserClass", currentUser);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });
    }

    /**
     * Add listener for Sudoku card.
     * @param view the view of the activity.
     */
    private void addSudokuCardListener(View view) {
        CardView sliding_tiles_card = view.findViewById(R.id.sudoku_card);
        sliding_tiles_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SudokuStartingActivity.class);
                User currentUser = MainActivity.currentUser;
                intent.putExtra("UserClass", currentUser);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });
    }


}