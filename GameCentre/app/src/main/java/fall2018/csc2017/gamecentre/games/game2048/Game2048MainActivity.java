package fall2018.csc2017.gamecentre.games.game2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.User;

/**
 * A activity class that is excluded from the tests since it contains minimal amount of logic.
 * <p>
 * The activity of the game
 */
public class Game2048MainActivity extends Activity implements OnGame2048Listener {
    /**
     * The real time updated score
     */
    private TextView scoreView;
    /**
     * The customized GridLayout for the game
     */
    private Game2048Layout game2048Layout;
    /**
     * the score of the current game
     */
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048_main);
        addUndoListener();
        Intent intent = getIntent();
        int size = intent.getIntExtra("size", 4);
        User user = (User) intent.getSerializableExtra("game_2048_player");
        // Set the name, the size of the board
        game2048Layout = findViewById(R.id.id_game2048);
        game2048Layout.setCurrentUser(user);
        game2048Layout.setSize(size);
        game2048Layout.setOnGame2048Listener(this);
        scoreView = findViewById(R.id.game_2048_score);
    }

    /**
     * Add the listener for the undo button
     */
    private void addUndoListener() {
        Button undoButton = findViewById(R.id.undo2048);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game2048Layout.onUndo();
            }
        });
    }

    @Override
    public void onScoreChange(int score) {
        this.score = score;
        scoreView.setText("Score : " + score);
    }

    @Override
    public void onGameOver() {
        new AlertDialog.Builder(this).setTitle("YOU LOST")
                .setMessage("Your " + scoreView.getText()).setCancelable(false)
                .setPositiveButton("Play again!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        game2048Layout.reStart();
                    }
                }).setNegativeButton("Return to menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }

    @Override
    public void onGameWinning() {
        Intent intent = new Intent(this, Game2048WinningActivity.class);
        intent.putExtra("game_2048_score", this.score);
        startActivity(intent);
    }

}
