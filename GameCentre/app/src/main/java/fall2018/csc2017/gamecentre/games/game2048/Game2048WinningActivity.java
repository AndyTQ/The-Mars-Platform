package fall2018.csc2017.gamecentre.games.game2048;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import fall2018.csc2017.gamecentre.MainActivity;
import fall2018.csc2017.gamecentre.R;

/**
 * A activity class that is excluded from the tests since it contains minimal amount of logic.
 *
 * The activity when the game 2048 wins
 */
public class Game2048WinningActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048_winning);
        Integer int_score = getIntent().getIntExtra("game_2048_score", 0);
        String scoreText = int_score.toString();
        TextView scoreDisplay = findViewById(R.id.game2048_winning_score);
        scoreDisplay.setText(scoreText);
        findViewById(R.id.game2048_winning_quit).setOnClickListener(this);
        findViewById(R.id.game2048_winning_return_to_menu).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game2048_winning_quit:
                Intent intentBackToCentre = new Intent(Game2048WinningActivity.this,
                        MainActivity.class);
                intentBackToCentre.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intentBackToCentre, 0);
                break;
            case R.id.game2048_winning_return_to_menu:
                Intent intentBackToGameMenu = new Intent(Game2048WinningActivity.this,
                        Game2048StartingActivity.class);
                intentBackToGameMenu.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intentBackToGameMenu, 0);
                break;
        }
    }
}
