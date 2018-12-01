package fall2018.csc2017.gamecentre.games.sudoku;

import android.content.Context;

import fall2018.csc2017.gamecentre.User;
import fall2018.csc2017.gamecentre.games.ScoreManager;

class SudokuScoreManager extends ScoreManager {

    /**
     * The name of the sudoku game.
     */
    private static final String GAME_NAME = "Sudoku";

    /**
     * Total time in seconds that takes player to finish this game.
     */
    private long playTime;

    /**
     * The constructor.
     *
     * @param user the user.
     */
    SudokuScoreManager(User user, Context context) {
        super(user, GAME_NAME, "All levels");
    }

    /**
     * Set playTime to be time.
     *
     * @param time the value that playTime should be.
     */
    void setPlayTime(long time) {
        playTime = time;
    }


    @Override
    protected int calculateScore() {
        long BEST_TIME = 200;

        return (int) (100 * Math.pow(0.997, playTime - BEST_TIME));
    }
}
