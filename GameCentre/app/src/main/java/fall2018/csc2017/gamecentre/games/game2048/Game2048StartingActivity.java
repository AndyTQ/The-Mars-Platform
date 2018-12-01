package fall2018.csc2017.gamecentre.games.game2048;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.User;

/**
 * A activity class that is excluded from the tests since it contains minimal amount of logic.
 * <p>
 * The Starting activity of the game 2048
 */
public class Game2048StartingActivity extends AppCompatActivity {

    /**
     * Initialize the size of the map that the user chose
     * The default is set to 4, to avoid crash when the user didn't touch the seek bar.
     */
    private int size_selected = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048_starting);
        addSeekBarListener();
        final User currentUser = (User) getIntent().getSerializableExtra("UserClass");
        Button startButton = findViewById(R.id.activity_game2048_starting_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Game2048MainActivity.class);
                intent.putExtra("size", size_selected);
                intent.putExtra("game_2048_player", currentUser);
                startActivity(intent);
            }
        });
    }

    /**
     * Adds the seek bar on the starting page
     * The seek bar is used for collecting information about what size of map that the user chose.
     */
    private void addSeekBarListener() {
        SeekBar seek = findViewById(R.id.activity_game2048_size_seekbar);
        seek.setProgress(1);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Write code to perform some action when progress is changed.
                size_selected = progress + 3;
                updateTextView();
            }

            /**
             * Anonymous method
             * Updates the text view to tell the user about current board size.
             */
            private void updateTextView() {
                TextView textView = findViewById(R.id.map_size_text);
                if (size_selected != 7) {
                    textView.setText(String.format("The current map size is %s ʕ •ᴥ•ʔ", "" +
                            size_selected));
                } else {
                    textView.setText(String.format("The current map size is %s ᶘ ᵒᴥᵒᶅ", "" +
                            size_selected));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Write code to perform some action when touch is started.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Write code to perform some action when touch is stopped.
            }

        });
    }
}
