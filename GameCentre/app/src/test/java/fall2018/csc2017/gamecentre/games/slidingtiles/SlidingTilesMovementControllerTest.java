package fall2018.csc2017.gamecentre.games.slidingtiles;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import fall2018.csc2017.gamecentre.User;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * test for sliding tiles movement controller.
 */
public class SlidingTilesMovementControllerTest {
    private final User user = new User();
    /**
     * create a 3 by 3 slding tiles board.
     * @return a complete 3 by 3 board.
     */
    private SlidingTilesBoard createThreeBoard() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(0));
        tiles.add(new Tile(1));
        tiles.add(new Tile(2));
        tiles.add(new Tile(3));
        tiles.add(new Tile(4));
        tiles.add(new Tile(5));
        tiles.add(new Tile(7));
        tiles.add(new Tile(24));
        tiles.add(new Tile(8));
        return new SlidingTilesBoard(tiles, 3);
    }

    /**
     * create a 3 by 3 slding tiles board.
     * @return a complete 3 by 3 board.
     */
    private SlidingTilesBoard createThreeBoardv2() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(0));
        tiles.add(new Tile(1));
        tiles.add(new Tile(2));
        tiles.add(new Tile(3));
        tiles.add(new Tile(4));
        tiles.add(new Tile(5));
        tiles.add(new Tile(24));
        tiles.add(new Tile(7));
        tiles.add(new Tile(8));
        return new SlidingTilesBoard(tiles, 3);
    }

    /**
     *  test for processTap and if movement is valid tap.
     */
    @Test
    public void testProcessTapMovementInvalidTap() {
        SlidingTilesBoard board = createThreeBoard();
        SlidingTilesBoardManager boardManager = new SlidingTilesBoardManager(user, 3);
        boardManager.setBoard(board);
        SlidingTilesGameActivity gameActivity = mock(SlidingTilesGameActivity.class);
        SlidingTilesMovementController mController = new SlidingTilesMovementController();
        mController.setBoardManager(boardManager);
        mController.setGameActivity(gameActivity);
        mController.processTapMovement(0);
        verify(gameActivity).makeInvalidTapText();
    }

    /**
     * test for checking if puzzle is solved and check the true branch.
     */
    @Test
    public void testProcessTapMovementPuzzleSolved() {
        SlidingTilesBoard board = createThreeBoard();
        SlidingTilesBoardManager boardManager = new SlidingTilesBoardManager(user, 3);
        boardManager.setBoard(board);
        SlidingTilesGameActivity gameActivity = mock(SlidingTilesGameActivity.class);
        SlidingTilesMovementController mController = new SlidingTilesMovementController();
        mController.setBoardManager(boardManager);
        mController.setGameActivity(gameActivity);
        mController.processTapMovement(8);
        verify(gameActivity).onGameOver();

    }

    /**
     * test for checking if puzzle is solved and check the false branch.
     */
    @Test
    public void testProcessTapMovementPuzzleNotSolved() {
        SlidingTilesBoard board = createThreeBoardv2();
        SlidingTilesBoardManager boardManager = new SlidingTilesBoardManager(user, 3);
        boardManager.setBoard(board);
        SlidingTilesGameActivity gameActivity = mock(SlidingTilesGameActivity.class);
        SlidingTilesMovementController mController = new SlidingTilesMovementController();
        mController.setBoardManager(boardManager);
        int oldNumOfSwaps = boardManager.getNumOfSwaps();
        int oldUndoLimit = mController.getUndoLimitCurrent();
        mController.setGameActivity(gameActivity);
        mController.processTapMovement(7);
        assertEquals(oldUndoLimit, mController.getUndoLimitCurrent());
        assertEquals(oldNumOfSwaps + 1, boardManager.getNumOfSwaps());
        assertEquals(1, mController.getStates().size());
    }

    /**
     * test if there is anymore step to undo.
     */
    @Test
    public void testNoMoreStepsToUndo() {
        SlidingTilesBoard board = createThreeBoardv2();
        SlidingTilesBoardManager boardManager = new SlidingTilesBoardManager(user, 3);
        boardManager.setBoard(board);
        SlidingTilesGameActivity gameActivity = mock(SlidingTilesGameActivity.class);
        SlidingTilesMovementController mController = new SlidingTilesMovementController();
        mController.setBoardManager(boardManager);
        mController.setGameActivity(gameActivity);
        mController.setUndoLimit(1);
        mController.undo();
        verify(gameActivity).MakeToastNoMoreSteps();

    }

    /**
     * test if there is anymore step to undo.
     */
    @Test
    public void testStepsCanUndo() {
        SlidingTilesBoard board = createThreeBoardv2();
        SlidingTilesBoardManager boardManager = new SlidingTilesBoardManager(user, 3);
        boardManager.setBoard(board);
        SlidingTilesGameActivity gameActivity = mock(SlidingTilesGameActivity.class);
        SlidingTilesMovementController mController = new SlidingTilesMovementController();
        mController.setBoardManager(boardManager);
        mController.setGameActivity(gameActivity);
        mController.setUndoLimit(3);

        mController.processTapMovement(7);
        mController.undo();
        assertEquals(25, boardManager.getBoard().getTile(2, 0).getId());
        verify(gameActivity).makeToastUndoText();
    }

    /**
     * test if the undo limit is exceeded.
     */
    @Test
    public void testUndoLimitExceeded() {
        SlidingTilesBoard board = createThreeBoardv2();
        SlidingTilesBoardManager boardManager = new SlidingTilesBoardManager(user, 3);
        boardManager.setBoard(board);
        SlidingTilesGameActivity gameActivity = mock(SlidingTilesGameActivity.class);
        SlidingTilesMovementController mController = new SlidingTilesMovementController();
        mController.setBoardManager(boardManager);
        mController.setGameActivity(gameActivity);
        mController.processTapMovement(7);
        mController.undo();
        verify(gameActivity).MakeToastUndoLimitExceeded();

    }


    /**
     * test if the undo limit can be increased.
     */
    @Test
    public void testIncreaseUndoLimitCurrent() {
        SlidingTilesMovementController mController = new SlidingTilesMovementController();
        mController.setUndoLimit(3);
        mController.setUndoLimitCurrent(2);
        mController.increaseUndoLimitCurrent();
        assertEquals(3, mController.getUndoLimitCurrent());

    }

    /**
     * est if the undo limit can be decreased.
     */
    @Test
    public void testDecreaseUndoLimitCurrent() {
        SlidingTilesMovementController mController = new SlidingTilesMovementController();
        mController.setUndoLimit(3);
        mController.decreaseUndoLimitCurrent();
        assertEquals(2, mController.getUndoLimitCurrent());

    }

}