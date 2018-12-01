package fall2018.csc2017.gamecentre.games.game2048;

import android.content.Context;

import org.junit.Test;

import fall2018.csc2017.gamecentre.User;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;

public class Game2048BoardManagerTest {
    private final User user = mock(User.class);
    private final int complexity = 4;
    private final Context context = mock(Context.class);
    private final Game2048BoardManager boardManager = new Game2048BoardManager(user, complexity, context);
    private final OnGame2048Listener onGame2048Listener = mock(OnGame2048Listener.class);

    /**
     * Initialize the board, empty
     */
    private void initializeBoard() {
        boardManager.createNewEmptyBoard();
    }

    /**
     * Initialize the lister
     */
    private void initializeListener() {
        boardManager.setGame2048Listener(onGame2048Listener);
    }

    /**
     * Fill the board with zeros
     */
    private void fillBoardWithZero() {
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                Game2048Item game2048Item0 = new Game2048Item(context);
                game2048Item0.setNumber(0);
                boardManager.setGame2048ItemWithIndex(game2048Item0, i, j);
            }
        }
    }

    /**
     * Fill the board with two
     */
    private void fillBoardWithTwo() {
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                Game2048Item game2048Item0 = new Game2048Item(context);
                game2048Item0.setNumber(2);
                boardManager.setGame2048ItemWithIndex(game2048Item0, i, j);
            }
        }
    }

    /**
     * Fill the board with mysterious numbers
     */
    private void fillBoardWithAllNumber() {
        int power = 1;
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                Game2048Item game2048Item2 = new Game2048Item(context);
                int number = (int) Math.pow(2, power);
                if (number <= 2048) {
                    power++;
                } else {
                    number = 2;
                }
                game2048Item2.setNumber(number);
                game2048Item2.getBackGroundColor();
                boardManager.setGame2048ItemWithIndex(game2048Item2, i, j);
            }
        }
    }

    /**
     * Test that the board could be successfully initialized
     */
    @Test
    public void testBoardManagerInit() {
        initializeBoard();
        assertFalse(boardManager.game2048ItemIsNull());
        assertTrue(boardManager.isFirst());
    }

    /**
     * test whether or not the isWinning() function call of a board manager is correct
     */
    @Test
    public void testIsWinning() {
        initializeBoard();
        fillBoardWithZero();
        Game2048Item game2048Item2 = new Game2048Item(context);
        game2048Item2.setNumber(2);
        boardManager.setGame2048ItemWithIndex(game2048Item2, 2, 2);
        assertFalse(boardManager.isWinning());
        Game2048Item game2048Item2048 = new Game2048Item(context);
        game2048Item2048.setNumber(2048);
        boardManager.setGame2048ItemWithIndex(game2048Item2048, 1, 1);
        assertTrue(boardManager.isWinning());
        boardManager.resetSameSizeBoard();
        assertFalse(boardManager.isWinning());
    }

    /**
     * Test whether or not the game is over
     */
    @Test
    public void testIsGameOver() {
        initializeBoard();
        fillBoardWithZero();
        assertFalse(boardManager.isGameOver());
        fillBoardWithAllNumber();
        assertFalse(boardManager.isGameOver());
    }

    /**
     * Test whether or not the generating view is correctly working
     */
    @Test
    public void testGenerateView() {
        initializeBoard();
        fillBoardWithZero();
        boardManager.setFirst();
        boardManager.generateViewAuto();
        assertFalse(boardManager.isFirst());
    }

    /**
     * Test whether or not a board manager is able to successfully restart
     */
    @Test
    public void testReStart() {
        initializeBoard();
        fillBoardWithZero();
        Game2048Item game2048Item = new Game2048Item(context);
        game2048Item.setNumber(2048);
        boardManager.setGame2048ItemWithIndex(game2048Item, 0, 0);
        assertTrue(boardManager.isWinning());
        boardManager.setGame2048Listener(onGame2048Listener);
        boardManager.reStart();
        assertFalse(boardManager.isWinning());
    }

    /**
     * Tests for the saving and undoing for a board manager
     */
    @Test
    public void testSaveAndUndo() {
        initializeBoard();
        initializeListener();
        fillBoardWithZero();
        boardManager.saveStates(context);
        boardManager.getGame2048ScoreManager().saveScore(100);
        Game2048Item game2048Item = new Game2048Item(context);
        game2048Item.setNumber(2048);
        boardManager.setGame2048ItemWithIndex(game2048Item, 0, 0);
        assertTrue(boardManager.isWinning());
        boardManager.undo(context);
        assertFalse(boardManager.isWinning());
    }

    /**
     * Test whether or not the getUser() function call can correctly return the current playing user
     */
    @Test
    public void testGetUser() {
        initializeBoard();
        initializeListener();
        assertNull(boardManager.getUser().getName());
    }

    /**
     * Test the action function, responsible for
     */
    @Test
    public void testAction() {
        initializeBoard();
        initializeListener();
        testBoardManagerInit();
        fillBoardWithZero();
        Game2048Layout.Action leftAction = Game2048Layout.Action.LEFT;
        Game2048Layout.Action rightAction = Game2048Layout.Action.RIGHT;
        Game2048Layout.Action upAction = Game2048Layout.Action.UP;
        Game2048Layout.Action downAction = Game2048Layout.Action.DOWN;
        boardManager.action(leftAction);
        assertFalse(boardManager.isGameOver());
        Game2048Item game2048Item1 = new Game2048Item(context);
        game2048Item1.setNumber(1024);
        Game2048Item game2048Item2 = new Game2048Item(context);
        game2048Item2.setNumber(512);
        boardManager.setGame2048ItemWithIndex(game2048Item1, 0, 0);
        boardManager.setGame2048ItemWithIndex(game2048Item2, 0, 1);
        boardManager.action(rightAction);
        assertFalse(boardManager.isGameOver());
        boardManager.resetSameSizeBoard();
        fillBoardWithTwo();
        boardManager.action(upAction);
        boardManager.action(downAction);
        assertFalse(boardManager.isWinning());
    }

}
