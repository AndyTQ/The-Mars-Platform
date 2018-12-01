package fall2018.csc2017.gamecentre.games.slidingtiles;

import android.content.Context;

import java.lang.Math;

import fall2018.csc2017.gamecentre.games.ScoreManager;
import fall2018.csc2017.gamecentre.User;

/*
Adapted from:
https://code.tutsplus.com/tutorials/android-sdk-create-an-arithmetic-game-high-scores-and-state-data--mobile-18825
*/

/**
 * Manage the scores of the Sliding tiles game.
 */
public class SlidingTilesScoreManager extends ScoreManager {
    private static final String GAME_NAME = "Sliding Tiles";

    /**
     * the complexity of the game.
     */
    private final int complexity;

    /**
     * Number of swaps performed in this game.
     */
    private int numOfSwaps;

    /**
     * Total time in seconds that takes player to finish this game.
     */
    private long playTime;

    SlidingTilesScoreManager(User user, int complexity, Context context) {
        super(user, GAME_NAME, String.valueOf(complexity) + " X " +
                String.valueOf(complexity));
        this.complexity = complexity;
    }

    /**
     * Set numOfSwaps to be num.
     * @param num the value that numOfSwaps should be.
     */
    void setNumOfSwaps(int num) {
        numOfSwaps = num;
    }

    /**
     * Set playTime to be time.
     * @param time the value that playTime should be.
     */
    void setPlayTime(long time) {
        playTime = time;
    }

    @Override
    protected int calculateScore() {
        long BEST_TIME = getBestTime();
        int BEST_NUM_SWAPS = getBestNumSwaps();

        int timeScore = (int) (50 * Math.pow(0.997, playTime - BEST_TIME));
        int swapScore = (int) (50 * Math.pow(0.95, numOfSwaps - BEST_NUM_SWAPS));

        return timeScore + swapScore;
    }

    /**
     * Return the best time to complete the game at the given complexity.
     *
     * @return Return the best time to complete the game at the given complexity.
     */
    private long getBestTime() {
        switch (complexity) {
            case 3:
                return 30;
            case 4:
                return 60;
            case 5:
                return 90;
            default:
                return 60;
        }
    }

    /**
     * Return the best number of swaps that need to perform to win the game.
     *
     * @return the best number of swaps that need to perform to win the game.
     */
    private int getBestNumSwaps() {
        switch (complexity) {
            case 3:
                return 30;
            case 4:
                return 60;
            case 5:
                return 120;
            default:
                return 60;
        }

    }

}
