package fall2018.csc2017.gamecentre.games.game2048;

import java.util.Stack;

import fall2018.csc2017.gamecentre.User;
import fall2018.csc2017.gamecentre.games.ScoreManager;

/**
 * The score manager for Game2048
 */
public class Game2048ScoreManager extends ScoreManager {
    /**
     * Static game name, used when collecting the score from multiple games
     */
    private static final String GAME_NAME = "The Real 2048";
    /**
     * the score
     */
    private int score;
    /**
     * The stack for the scores history
     */
    private Stack<Integer> scores = new Stack<>();

    /**
     * The constructor.
     *
     * @param user the user.
     */
    Game2048ScoreManager(User user, int complexity) {
        super(user, GAME_NAME, String.valueOf(complexity) + " X "
                + String.valueOf(complexity));
    }

    /**
     * Change the score of the current game to score
     *
     * @param score the score to be replaced by
     */
    void setScoreChange(int score) {
        this.score = score;
    }

    /**
     * Increment the score by increment amount
     *
     * @param increment the amount to be incremented
     */
    void increaseScoreBy(int increment) {
        this.score += 2 * increment;
    }


    @Override
    protected int calculateScore() {
        return score;
    }

    /**
     * save the score into the stack
     *
     * @param newScore the score to be stored
     */
    void saveScore(int newScore) {
        this.scores.add(newScore);
    }

    /**
     * return the last score stored in the stack
     *
     * @return int the score
     */
    int popScore() {
        return scores.pop();
    }

    /**
     * Reset the scores stack accordingly
     */
    void resetScores() {
        this.scores = new Stack<>();
    }

    /**
     * Returns whether the scores stack is empty or not
     * This method is dedicated for use in testing
     */
    boolean isScoresEmpty() {
        return scores.isEmpty();
    }
}
