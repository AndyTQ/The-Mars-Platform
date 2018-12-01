package fall2018.csc2017.gamecentre.games.sudoku;

import java.io.Serializable;

/*
 * Excluded from tests because it's a model class.
 */
class SudokuCell implements Serializable {
    /**
     * if the cell is to be filled by the user.
     */
    private boolean toBeFilled = false;

    /**
     * The value that is filled on this cell either by the user or initialized by the generator.
     */
    private int cellValue;

    /**
     * Return if the cell is to be filled by the user.
     *
     * @return if the cell is to be filled by the user.
     */
    boolean isToBeFilled() {
        return toBeFilled;
    }

    /**
     * Set that the cell can be filled by the user. (This method is used by the board generator.)
     */
    void setToBeFilled() {
        this.toBeFilled = true;
    }

    /**
     * Return the value of cell.
     *
     * @return the value of the cell.
     */
    int getCellValue() {
        return cellValue;
    }

    /**
     * Set the value of the cell.
     *
     * @param cellValue the number filled on the cell.
     */
    void setCellValue(int cellValue) {
        this.cellValue = cellValue;
    }

}
