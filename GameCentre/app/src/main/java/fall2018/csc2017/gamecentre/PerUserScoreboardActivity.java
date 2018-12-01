package fall2018.csc2017.gamecentre;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import fall2018.csc2017.gamecentre.games.Score;

/**
 * Activity for per-user scoreboard.
 *
 * This class consists of only UI and Firebase Component. Thus, it is excluded from test.
 */
public class PerUserScoreboardActivity extends AppCompatActivity {
    private HashMap<String, String[]> difficultyMap;
    private FirebaseAuth mAuth;
    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_user_scoreboard);

        mAuth = FirebaseAuth.getInstance();
        uId = FirebaseAuth.getInstance().getUid();

        difficultyMap = new HashMap<>();
        difficultyMap.put("Sliding Tiles", new String[]{"3 X 3", "4 X 4", "5 X 5"});
        difficultyMap.put("The Real 2048", new String[]{"3 X 3", "4 X 4", "5 X 5", "6 X 6", "7 X 7"});
        difficultyMap.put("Sudoku", new String[]{"All levels"});
        final LinearLayout linearLayout = findViewById(R.id.activity_per_user_scoreboard_table);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(3, 3, 3, 3);
        for (Map.Entry<String, String[]> item : difficultyMap.entrySet()) {
            final String gameName = item.getKey();
            String[] gameDifficulties = item.getValue();
            final boolean[] titleSetted = {false};
            for (final String gameDifficulty : gameDifficulties) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                        getReference("Scoreboards").child(gameName).child(gameDifficulty).
                        child(uId);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!titleSetted[0]) {
                            TextView gameNameTextView = new TextView(getApplicationContext());
                            gameNameTextView.setText(gameName);
                            gameNameTextView.setLayoutParams(params);
                            gameNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                            gameNameTextView.setTextColor(Color.rgb(255, 255, 255));
                            linearLayout.addView(gameNameTextView);
                            titleSetted[0] = true;
                        }
                        String scoreString;
                        TextView scoreText = new TextView(getApplicationContext());
                        if (dataSnapshot.exists()) {
                            Score score = dataSnapshot.getValue(Score.class);
                            assert score != null;
                            scoreString = gameDifficulty + ": " + score.getScoreNum();
                            scoreText.setText(scoreString);
                            scoreText.setLayoutParams(params);
                            scoreText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                            scoreText.setTextColor(Color.rgb(255, 255, 255));
                            linearLayout.addView(scoreText);
                        } else {
                            scoreString = gameDifficulty + ": " + "You have not played this level yet...";
                            scoreText.setText(scoreString);
                            scoreText.setLayoutParams(params);
                            scoreText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                            scoreText.setTextColor(Color.rgb(255, 255, 255));
                            linearLayout.addView(scoreText);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

        }
    }
}
