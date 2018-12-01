package fall2018.csc2017.gamecentre.games.sudoku;

import org.junit.Test;

import fall2018.csc2017.gamecentre.User;

import static org.junit.Assert.*;

/**
 * test class for SudokuBoardManager.
 */
public class SudokuBoardManagerTest {
    private final User user = new User();

    private final SudokuBoard board;

    /**
     * create and return a complete correct sudokuCells set.
     * @return sudokucells arrays
     */
    private SudokuCell[][] createSudokuCells() {
        int[] firstRow = {8, 9, 3, 2, 7, 6, 4, 5, 1};
        int[] secondRow = {2, 7, 6, 4, 5, 1, 8, 9, 3};
        int[] thirdRow = {4, 5, 1, 8, 9, 3, 2, 7, 6};
        int[] fourRow = {5, 1, 8, 9, 3, 2, 7, 6, 4};
        int[] fiveRow = {9, 3, 2, 7, 6, 4, 5, 1, 8};
        int[] sixRow = {7, 6, 4, 5, 1, 8, 9, 3, 2};
        int[] sevenRow = {6, 4, 5, 1, 8, 9, 3, 2, 7};
        int[] eightRow = {1, 8, 9, 3, 2, 7, 6, 4, 5};
        int[] nineRow = {3, 2, 7, 6, 4, 5, 1, 8, 9};
        SudokuCell[][] cells;
        cells = new SudokuCell[9][9];
        for (int j = 0; j < 9; j++) {
            cells[0][j] = new SudokuCell();
            cells[0][j].setCellValue(firstRow[j]);

            cells[1][j] = new SudokuCell();
            cells[1][j].setCellValue(secondRow[j]);

            cells[2][j] = new SudokuCell();
            cells[2][j].setCellValue(thirdRow[j]);

            cells[3][j] = new SudokuCell();
            cells[3][j].setCellValue(fourRow[j]);

            cells[4][j] = new SudokuCell();
            cells[4][j].setCellValue(fiveRow[j]);

            cells[5][j] = new SudokuCell();
            cells[5][j].setCellValue(sixRow[j]);

            cells[6][j] = new SudokuCell();
            cells[6][j].setCellValue(sevenRow[j]);

            cells[7][j] = new SudokuCell();
            cells[7][j].setCellValue(eightRow[j]);

            cells[8][j] = new SudokuCell();
            cells[8][j].setCellValue(nineRow[j]);
        }
        return cells;
    }

    /**
     * changes cell in the cells set created in the ;ast method to construct a wrong board.
     */
    private SudokuCell[][] createWrongCells() {
        SudokuCell[][] changed = createSudokuCells();
        changed[0][2].setCellValue(1);
        return changed;
    }


    /**
     * constructor before the test for the SudokuBoard Manager.
     */
    public SudokuBoardManagerTest() {
        // Fixture setup
        SudokuBoard board = new SudokuBoard(2);
        board.setCells(createSudokuCells());
        this.board = board;


    }

    /**
     * test if the suoku board in the current state is a solved, correct board and test the true branch.
     */
    @Test
    public void testpuzzleSolved() {
        // Fixture setup
        SudokuBoardManager bm = new SudokuBoardManager(user, 81);
        board.setNumCellFilled(81);
        bm.setSudokuBoard(board);
        // Verify Outcome
        assertTrue(bm.puzzleSolved());
    }

    /**
     * test if the sudoku board in the current state is a solved, correct board and test the false branch.
     */
    @Test
    public void testpuzzleSolvedWrong() {
        // Fixture setup
        SudokuBoardManager bm = new SudokuBoardManager(user, 81);
        board.setCells(createWrongCells());
        board.setNumCellFilled(81);
        bm.setSudokuBoard(board);
        // Verify Outcome
        assertFalse(bm.puzzleSolved());
    }


    /**
     * set the board which take out two cells from the complete board for the fill in cells' test and return the new sudoku board.
     * @return SudokuCells array
     */
    private SudokuCell[][] takeOutCells() {
        // Fixture setup
        SudokuCell[][] oriCells = createSudokuCells();
        oriCells[0][2].setCellValue(0);
        oriCells[8][7].setCellValue(0);
        return oriCells;
    }

    /**
     * test if the up certain cell method can work.
     */
    @Test
    public void testfillInCell1() {
        // Fixture setup
        SudokuBoardManager bm = new SudokuBoardManager(user, 81);
        board.setNumCellFilled(79);
        SudokuCell[][] cells = takeOutCells();
        board.setCells(cells);

        bm.setSudokuBoard(board);
        bm.fillInCell(2, 3);
        //verify Outcome
        assertEquals(80, board.getNumCellFilled());
        assertEquals(3, board.getCellNum(0, 2));


    }

    /**
     * tests for fill in the second cell.
     */
    @Test
    public void testfillInCell2() {
        // Fixture setup
        SudokuBoardManager bm = new SudokuBoardManager(user, 81);
        board.setNumCellFilled(80);
        SudokuCell[][] cells = takeOutCells();
        board.setCells(cells);

        bm.setSudokuBoard(board);
        bm.fillInCell(79, 9);
        //verify Outcome
        assertEquals(81, board.getNumCellFilled());
        assertEquals(9, board.getCellNum(8, 7));


    }

    /**
     * test if we can get proper user.
     */
    @Test
    public void testGetUser() {
        // Fixture setup
        SudokuBoardManager bm = new SudokuBoardManager(user, 81);
        // Verify Outcome
        assertEquals(user, bm.getUser());
    }


    /**
     * test if we can get proper board.
     */
    @Test
    public void testGetBoard() {
        // Fixture setup
        SudokuBoardManager bm = new SudokuBoardManager(user, 81);
        bm.setSudokuBoard(board);
        // Verify Outcome
        assertEquals(board, bm.getBoard());
    }

    /**
     * test if we can get proper duration.
     */
    @Test
    public void testGetDuration() {
        // Fixture setup
        SudokuBoardManager bm = new SudokuBoardManager(user, 81);
        long duration = 0;
        // Verify Outcome
        assertEquals(duration, bm.getDuration());

    }

}