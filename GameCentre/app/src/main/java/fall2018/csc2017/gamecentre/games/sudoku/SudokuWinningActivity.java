package fall2018.csc2017.gamecentre.games.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fall2018.csc2017.gamecentre.MainActivity;
import fall2018.csc2017.gamecentre.R;

/**
 * Excluded from tests because it's a view class.
 */
public class SudokuWinningActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_winning);
        Integer score = getIntent().getIntExtra("GAME_SCORE", 0);
        String scoreText = score.toString();
        TextView scoreDisplay = findViewById(R.id.sudoku_winning_score);
        scoreDisplay.setText(scoreText);
        findViewById(R.id.sudoku_winning_quit).setOnClickListener(this);
        findViewById(R.id.sudoku_winning_return_to_menu).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sudoku_winning_quit:
                Intent intentBackToCentre = new Intent(SudokuWinningActivity.this,
                        MainActivity.class);
                intentBackToCentre.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intentBackToCentre, 0);
                break;
            case R.id.sudoku_winning_return_to_menu:
                Intent intentBackToGameMenu = new Intent(SudokuWinningActivity.this,
                        SudokuStartingActivity.class);
                intentBackToGameMenu.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intentBackToGameMenu, 0);
                break;
        }
    }
}
