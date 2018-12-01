package fall2018.csc2017.gamecentre.games.slidingtiles;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import fall2018.csc2017.gamecentre.R;

/**
 * The scoreboard activity for the sliding puzzle tile game.
 */
public class SlidingTilesScoreboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidingtiles_scoreboard);

        TextView scoreView3 = findViewById(R.id.high_scores_list3);
        TextView scoreView4 = findViewById(R.id.high_scores_list4);
        TextView scoreView5 = findViewById(R.id.high_scores_list5);

        String scoreList3 = buildScoreList("SlidingTiles3");
        String scoreList4 = buildScoreList("SlidingTiles4");
        String scoreList5 = buildScoreList("SlidingTiles5");

        scoreView3.setText(scoreList3);
        scoreView4.setText(scoreList4);
        scoreView5.setText(scoreList5);
    }

    /**
     * Return the string of formatted list of high scores.
     * @param prefsName the name for the shared preferences which stores the high scores data.
     * @return the string of formatted list of high scores.
     */
    private String buildScoreList(String prefsName) {
        SharedPreferences scorePrefs = getSharedPreferences(prefsName, 0);

        String[] highScores = scorePrefs.getString("highScores", "").split("\\|");

        StringBuilder scoreBuild = new StringBuilder();
        for (int i = 0; i < highScores.length; i++) {
            String score = highScores[i];
            if (score.equals("")) {return "Nobody has ever played this level...";}
            String rankRow = String.valueOf(i + 1) + ". " + score + "\n";
            scoreBuild.append(rankRow);
        }
        return scoreBuild.toString();
    }
}
