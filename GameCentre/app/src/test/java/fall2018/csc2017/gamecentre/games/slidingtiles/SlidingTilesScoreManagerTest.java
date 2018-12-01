package fall2018.csc2017.gamecentre.games.slidingtiles;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;


import fall2018.csc2017.gamecentre.User;

/**
 * test for Sliding tiles score manager class.
 */
public class SlidingTilesScoreManagerTest {
    /**
     * user current logging in.
     */
    private User user;
    /**
     * game complexity
     */
    private int complexity = 3;
    /**
     * the context
     */

    private Context context;

    /**
     * test for calculating score method for 3 by 3 board.
     */
    @Test
    public void testcalculateScoreThree() {
        SlidingTilesScoreManager ST = new SlidingTilesScoreManager(user, 3, context);
        int numOfSwaps = 30;
        ST.setNumOfSwaps(numOfSwaps);
        long playTime = 30;
        ST.setPlayTime(playTime);

        int expected = 100;
        Assert.assertEquals(expected, ST.calculateScore());

    }


    /**
     * test for calculating score method for 4 by 4 board.
     */
    @Test
    public void testcalculateScoreFour() {
        SlidingTilesScoreManager ST = new SlidingTilesScoreManager(user, 5, context);
        int numOfSwaps = 120;
        ST.setNumOfSwaps(numOfSwaps);
        long playTime = 90;
        ST.setPlayTime(playTime);

        int expected = 100;
        Assert.assertEquals(expected, ST.calculateScore());

    }

    /**
     * test for calculating score method for 5 by 5 board.
     */
    @Test
    public void testcalculateScoreFive() {
        SlidingTilesScoreManager ST = new SlidingTilesScoreManager(user, 4, context);
        int numOfSwaps = 60;
        ST.setNumOfSwaps(numOfSwaps);
        long playTime = 60;
        ST.setPlayTime(playTime);

        int expected = 100;
        Assert.assertEquals(expected, ST.calculateScore());

    }

}