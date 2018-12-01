package fall2018.csc2017.gamecentre.games;
/*
 We implemented firebase in this class to query data from the internet.
 Thus, the class is excluded from testing.
 */


import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fall2018.csc2017.gamecentre.MainActivity;

import fall2018.csc2017.gamecentre.User;

/**
 * Manage scores in a game, including calculate score and update score to the scoreboards.
 */
public abstract class ScoreManager {
    /**
     * The name of the game.
     */
    private final String gameName;

    /**
     * The complexity of the game.
     */
    private String complexity;

    /**
     * Firebase Authentication Instance
     */
    private FirebaseAuth mAuth;
    /**
     * Firebase Authentication Listener
     */
    FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * Firebase Database Reference for scoreboard
     */
    private DatabaseReference mScoreDatabase;

    /**
     * The owner of the score.
     */
    private final User user;

    /**
     * The constructor.
     *
     * @param user the user.
     */
    protected ScoreManager(User user, String gameName, String complexity) {
        this.user = user;
        this.gameName = gameName;
        this.complexity = complexity;
    }

    /**
     * Return the score that the player gets in game.
     *
     * @return the score that the player gets in game.
     */
    protected abstract int calculateScore();

    /**
     * Update the user's highest score if the score is higher than the user's previous record and
     * update the highest score list if the score is higher than any previous top scores on the
     * score board.
     */
    public void updateHighScore() {

        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();
        final int newScoreNum = calculateScore();

        mScoreDatabase =
                FirebaseDatabase.getInstance().getReference()
                        .child("Scoreboards").child(this.gameName).child(this.complexity).child(uid);

        mScoreDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            //private int myExscoreValue;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Score exScore = dataSnapshot.getValue(Score.class);
                updateNewScore(exScore, newScoreNum);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Update the new score to database, if it is higher than the user's previous score.
     *
     * @param exScore the previous score of the user
     * @param newScoreNum the new score of the user
     */
    private void updateNewScore(Score exScore, int newScoreNum) {
        int myExScoreValue;
        if (exScore == null) {
            myExScoreValue = 0;
        } else {
            myExScoreValue = exScore.getScoreNum();
        }
        if (newScoreNum >= myExScoreValue) {
            /* update personal highest score. */
            mScoreDatabase.setValue(new Score(MainActivity.currentUser.getName(),
                    newScoreNum));
        }
    }

    /**
     * Set the complexity description of this score manager.
     *
     * @param complexity the current complexity description of the game.
     */
    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }
}
