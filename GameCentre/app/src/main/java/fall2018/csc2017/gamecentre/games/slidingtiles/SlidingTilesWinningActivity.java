package fall2018.csc2017.gamecentre.games.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import fall2018.csc2017.gamecentre.MainActivity;
import fall2018.csc2017.gamecentre.R;


/**
 * The winning activity of the game, displayed after the player wins the game.
 */
public class SlidingTilesWinningActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidingtiles_winning);
        Integer score = getIntent().getIntExtra("GAME_SCORE", 0);
        String scoreText = score.toString();
        TextView scoreDisplay = findViewById(R.id.sliding_tiles_winning_score);
        scoreDisplay.setText(scoreText);
        findViewById(R.id.slidingtiles_winning_quit).setOnClickListener(this);
        findViewById(R.id.slidingtiles_winning_return_to_menu).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slidingtiles_winning_quit:
                Intent intentBackToCentre = new Intent(SlidingTilesWinningActivity.this,
                        MainActivity.class);
                intentBackToCentre.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intentBackToCentre, 0);
                break;
            case R.id.slidingtiles_winning_return_to_menu:
                Intent intentBackToGameMenu = new Intent(SlidingTilesWinningActivity.this,
                        SlidingTilesStartingActivity.class);
                intentBackToGameMenu.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intentBackToGameMenu, 0);
                break;
        }
    }
}
