package fall2018.csc2017.gamecentre.games.sudoku;

import java.io.Serializable;

import fall2018.csc2017.gamecentre.User;

class SudokuBoardManager implements Serializable {
    /**
     * The board being managed.
     */
    private SudokuBoard sudokuBoard;

    /**
     * The user. This will be currently logged in user.
     */
    private final User user;

    /**
     * The duration of the current game play.
     */
    private long duration = 0;

    /**
     * The constructor.
     *
     * @param user            the user playing the game.
     * @param numMissingDigit the number of digits to be filled by the user.
     */
    SudokuBoardManager(User user, int numMissingDigit) {
        this.user = user;
        this.sudokuBoard = new SudokuBoard(numMissingDigit);
    }


    /**
     * Return the current board.
     *
     * @return the board
     */
    SudokuBoard getBoard() {
        return sudokuBoard;
    }

    void setSudokuBoard(SudokuBoard board) {
        this.sudokuBoard = board;

    }

    /**
     * Getting the currently logged in user.
     *
     * @return the user of the gameplay
     */
    public User getUser() {
        return user;
    }

    /**
     * set the current elapsed time .
     *
     * @param duration The duration
     */
    void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * get the current elapsed time.
     *
     * @return the duration
     */
    long getDuration() {
        return this.duration;
    }


    /**
     * Return if the puzzle is already solved.
     *
     * @return if the puzzle is already solved.
     */
    boolean puzzleSolved() {
        if (sudokuBoard.getNumCellFilled() != 81) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int num = sudokuBoard.getCellNum(i, j);
                if (!CheckIfSafe(i, j, num)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean CheckIfSafe(int row, int col, int num) {
        return (safeInRow(row, col, num) &&
                safeInCol(row, col, num) &&
                safeInBox(row, col, num));
    }

    private boolean safeInRow(int row, int col, int num) {
        for (int j = 0; j < 9; j++)
            if (sudokuBoard.getCellNum(row, j) == num && (col != j))
                return false;
        return true;
    }

    private boolean safeInCol(int row, int col, int num) {
        for (int i = 0; i < 9; i++)
            if (sudokuBoard.getCellNum(i, col) == num && (row != i))
                return false;
        return true;
    }

    private boolean safeInBox(int row, int col, int num) {
        int rowStart = row - row % 3;
        int colStart = col - col % 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (sudokuBoard.getCellNum(rowStart + i, colStart + j) == num &&
                        row != rowStart + i && col != colStart + j)
                    return false;

        return true;
    }

    /**
     * Fill in the cell at position with num.
     *
     * @param position the index of the cell to fill in the num.
     * @param num      the number between 1 to 9 that the player chooses to fill.
     */
    void fillInCell(int position, int num) {
        int row = position / 9;
        int col = position % 9;
        if (sudokuBoard.getCellNum(row, col) == 0) {
            sudokuBoard.incrementNumCellFilled();
        }
        sudokuBoard.updateCellNum(row, col, num);
    }


}
