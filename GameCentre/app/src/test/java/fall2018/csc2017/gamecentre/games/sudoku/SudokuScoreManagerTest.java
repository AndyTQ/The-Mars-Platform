package fall2018.csc2017.gamecentre.games.sudoku;

import android.content.Context;

import org.junit.Test;

import fall2018.csc2017.gamecentre.User;

import static org.junit.Assert.*;

/**
 * test for sudoku score manager.
 */
public class SudokuScoreManagerTest {
    private User user;
    private Context context;

    /**
     * test for culculating score.
     */
    @Test
    public void testcalculateScore() {

        SudokuScoreManager SSM = new SudokuScoreManager(user, context);
        SSM.setPlayTime(200);
        assertEquals(100,SSM.calculateScore());


    }
}