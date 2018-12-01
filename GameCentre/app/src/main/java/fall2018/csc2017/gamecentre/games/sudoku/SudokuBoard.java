package fall2018.csc2017.gamecentre.games.sudoku;

import java.io.Serializable;

/*
 * Excluded from tests because it's a model class.
 */
class SudokuBoard implements Serializable {
    /**
     * The cells on the board.
     */
    private SudokuCell[][] cells;

    /**
     * The number of cells that have been filled.
     */
    private int numCellFilled;


    /**
     * The constructor.
     *
     * @param numMissingDigit the number of digits to be filled by the user.
     */
    SudokuBoard(int numMissingDigit) {
        BoardGenerator boardGenerator = new BoardGenerator(numMissingDigit);
        this.cells = boardGenerator.getCells();
        this.numCellFilled = 81 - numMissingDigit;
    }

    /**
     * Return the value of cell at position (i, j).
     *
     * @param i the cell row.
     * @param j the cell column.
     * @return the number of cell at position (i, j).
     */
    int getCellNum(int i, int j) {
        return cells[i][j].getCellValue();
    }

    /**
     * Update the cell at position (i, j) with num.
     *
     * @param i   the row index of the cell at which the player chooses to fill.
     * @param j   the column index of the cell at which the player chooses to fill.
     * @param num the number between 1 to 9 that the player chooses to fill.
     */
    void updateCellNum(int i, int j, int num) {
        cells[i][j].setCellValue(num);
    }

    /**
     * Return the number of cells that have been filled.
     *
     * @return the number of cells that have been filled.
     */
    int getNumCellFilled() {
        return numCellFilled;
    }

    /**
     * Set the number of cells filled.
     *
     * @param numCellFilled the number of cells filled.
     */
    void setNumCellFilled(int numCellFilled) {
        this.numCellFilled = numCellFilled;
    }

    /**
     * Increase the number of cells filled by 1.
     */
    void incrementNumCellFilled() {
        numCellFilled++;
    }


    /**
     * Set the cells of the board.
     *
     * @param cells the array of cells.
     */
    void setCells(SudokuCell[][] cells) {
        this.cells = cells;

    }


    /**
     * Return the cell at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    SudokuCell getCell(int row, int col) {
        return cells[row][col];
    }


    /**
     * Return if the cell can be edited.
     *
     * @param position the index of the cell chosen.
     * @return if the cell can be edited.
     */
    boolean isValidPosition(int position) {
        int row = position / 9;
        int col = position % 9;
        SudokuCell cell = cells[row][col];
        return cell.isToBeFilled();
    }

}
