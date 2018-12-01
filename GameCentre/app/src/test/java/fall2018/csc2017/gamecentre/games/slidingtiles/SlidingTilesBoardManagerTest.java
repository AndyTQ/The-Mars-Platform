package fall2018.csc2017.gamecentre.games.slidingtiles;

import org.junit.Test;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import fall2018.csc2017.gamecentre.User;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * test for Slding tiles board manager.
 */
@RunWith(Parameterized.class)
public class SlidingTilesBoardManagerTest {
    private final User user = new User();

    /**
     *parameter for test if Puzzle is Solved .
     * @return set of the parameter.
     */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {3, true}, {4, true}, {5, true}
        });
    }

    /**
     * complexity for the current game.
     */

    private final int compl;
    /**
     * the expexted value.
     */
    private final boolean Expected;
    /**
     * SlidingTiles boards for testing.
     */
    private final SlidingTilesBoard board3;
    private final SlidingTilesBoard board4;
    private final SlidingTilesBoard board5;

    /**
     * create a 3 by 3 sliding tiles board.
     * @return a complete 3 by 3 board.
     */
    private SlidingTilesBoard createThreeBoard() {
        SlidingTilesBoardManager bmsol = new SlidingTilesBoardManager(user, 3);
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(0));
        tiles.add(new Tile(2));
        tiles.add(new Tile(1));
        tiles.add(new Tile(4));
        tiles.add(new Tile(24));
        tiles.add(new Tile(5));
        tiles.add(new Tile(7));
        tiles.add(new Tile(6));
        tiles.add(new Tile(3));
        return new SlidingTilesBoard(tiles, 3);

    }
    /**
     * create a 4 by 4 slding tiles board.
     * @return a complete 4 by 4 board.
     */
    private SlidingTilesBoard createFourBoard() {
        SlidingTilesBoardManager bm4 = new SlidingTilesBoardManager(user, 4);
        List<Tile> tiles4 = new ArrayList<>();
        final int numTiles = 16;
        for (int tileNum = 0; tileNum != numTiles - 1; tileNum++) {
            tiles4.add(new Tile(tileNum));
        }
        tiles4.add(new Tile(24));
        tiles4.set(14, new Tile(24));
        tiles4.set(15, new Tile(14));
        return new SlidingTilesBoard(tiles4, 4);

    }

    /**
     * create a 5 by 5 slding tiles board.
     * @return a complete 5 by 5 board.
     */
    private SlidingTilesBoard createFiveBoard() {
        SlidingTilesBoardManager bm5 = new SlidingTilesBoardManager(user, 5);
        List<Tile> tiles5 = new ArrayList<>();
        int numTiles5 = 25;
        for (int tileNum = 0; tileNum != numTiles5 - 1; tileNum++) {
            tiles5.add(new Tile(tileNum));
        }
        tiles5.add(new Tile(24));
        tiles5.set(7, new Tile(24));
        tiles5.set(24, new Tile(7));
        return new SlidingTilesBoard(tiles5, 5);

    }

    /**
     * constructor for sliding tiles board manager test.
     * @param compl complexity
     * @param Expected expected outcome
     */
    public SlidingTilesBoardManagerTest(int compl, boolean Expected) {
        this.compl = compl;
        this.Expected = Expected;
        this.board3 = createThreeBoard();
        this.board4 = createFourBoard();
        this.board5 = createFiveBoard();
    }

    /**
     * test if we can get the correct user.
     */
    @Test
    public void testGetUser() {
        SlidingTilesBoardManager bm = new SlidingTilesBoardManager(user, 3, false);
        assertEquals(user, bm.getUser());
    }

    /**
     * test if the puzzle is solved.
     */
    @Test
    public void testPuzzleSolvedTrue() {
        SlidingTilesBoardManager bm = new SlidingTilesBoardManager(user, compl);
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = compl * compl;
        for (int tileNum = 0; tileNum != numTiles - 1; tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        tiles.add(new Tile(24));

        bm.setBoard(new SlidingTilesBoard(tiles, compl));
        assertEquals(Expected, bm.puzzleSolved());


    }


    /**
     * check if the 3 by 3 board is solvable.
     */
    @Test
    public void testCheckSolvableThree() {
        SlidingTilesBoardManager bmsol = new SlidingTilesBoardManager(user, 3);

        assertTrue(bmsol.checkSolvable(board3));
    }

    /**
     * check if the 4 by 4 board is solvable.
     */
    @Test
    public void testCheckSolvableFour() {
        SlidingTilesBoardManager bm4 = new SlidingTilesBoardManager(user, 4);
        assertTrue(bm4.checkSolvable(board4));

    }


    /**
     * check if the 5 by 5 board is solvable.
     */
    @Test
    public void testCheckSolvableFive() {
        SlidingTilesBoardManager bm5 = new SlidingTilesBoardManager(user, 5);
        assertTrue(bm5.checkSolvable(board5));


    }

    /**
     * test if is valid tap method for 3 by 3 board.
     */
    @Test
    public void testIsValidTapThree() {
        SlidingTilesBoardManager bm = new SlidingTilesBoardManager(user, 3);
        bm.setBoard(board3);
        int position = 5;
        assertTrue(bm.isValidTap(position));
    }


    /**
     * test if is valid tap method for 4 by 4 board.
     */
    @Test
    public void testIsValidTapTFour() {
        SlidingTilesBoardManager bm = new SlidingTilesBoardManager(user, 4);
        bm.setBoard(board4);
        int position = 5;
        assertFalse(bm.isValidTap(position));
    }


    /**
     * test if is valid tap method for 5 by 5 board.
     */
    @Test
    public void testIsValidTapFive() {
        SlidingTilesBoardManager bm = new SlidingTilesBoardManager(user, 5);
        bm.setBoard(board5);
        int position = 6;
        assertTrue(bm.isValidTap(position));
    }

    /**
     * test touch move method for 3 by 3 board.
     */
    @Test
    public void testTouchMoveThree() {
        int position = (compl - 1) * compl - 1;
        SlidingTilesBoardManager bm = new SlidingTilesBoardManager(user, 3);
        bm.setBoard(this.board3);

        assertEquals(4, bm.touchMove(1));

    }


    /**
     * test touch move method for 4 by 4 board.
     */
    @Test
    public void testTouchMovefour() {
        SlidingTilesBoardManager bm4 = new SlidingTilesBoardManager(user, 4);
        bm4.setBoard(board4);
        assertEquals(14, bm4.touchMove(15));

    }


    /**
     * test touch move method for 5 by 5 board.
     */
    @Test
    public void testTouchMovefive() {
        SlidingTilesBoardManager bm5 = new SlidingTilesBoardManager(user, 5);
        bm5.setBoard(board5);
        assertEquals(7, bm5.touchMove(6));

    }


}