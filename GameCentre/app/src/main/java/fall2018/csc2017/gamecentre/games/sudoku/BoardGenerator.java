package fall2018.csc2017.gamecentre.games.sudoku;

/* Adapted from:
   https://www.geeksforgeeks.org/program-sudoku-generator/ */

import java.lang.*;

class BoardGenerator {
    private final SudokuCell[][] cells;
    private final int numMissingDigit; // No. Of missing digits

    // Constructor
    BoardGenerator(int numMissingDigit) {
        this.numMissingDigit = numMissingDigit;
        cells = new SudokuCell[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new SudokuCell();
            }
        }
        fillValues();
    }

    SudokuCell[][] getCells() {
        return cells;
    }

    // Sudoku Generator
    private void fillValues() {
        // Fill the diagonal of SRN x SRN matrices
        fillDiagonal();

        // Fill remaining blocks
        fillRemaining(0, 3);

        // Remove Randomly K digits to make game
        removeKDigits();
    }

    // Fill the diagonal SRN number of SRN x SRN matrices
    private void fillDiagonal() {

        for (int i = 0; i < 9; i = i + 3)

            // for diagonal box, start coordinates->i==j
            fillBox(i, i);
    }

    // Returns false if given 3 x 3 block contains num.
    private boolean unUsedInBox(int rowStart, int colStart, int num) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (cells[rowStart + i][colStart + j].getCellValue() == num)
                    return false;

        return true;
    }

    // Fill a 3 x 3 matrix.
    private void fillBox(int row, int col) {
        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do {
                    num = randomGenerator(9);
                }
                while (!unUsedInBox(row, col, num));

                cells[row + i][col + j].setCellValue(num);
            }
        }
    }

    // Random generator
    private int randomGenerator(int num) {
        return (int) Math.floor((Math.random() * num + 1));
    }

    // Check if safe to put in cell
    private boolean CheckIfSafe(int i, int j, int num) {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i - i % 3, j - j % 3, num));
    }

    // check in the row for existence
    private boolean unUsedInRow(int i, int num) {
        for (int j = 0; j < 9; j++)
            if (cells[i][j].getCellValue() == num)
                return false;
        return true;
    }

    // check in the row for existence
    private boolean unUsedInCol(int j, int num) {
        for (int i = 0; i < 9; i++)
            if (cells[i][j].getCellValue() == num)
                return false;
        return true;
    }

    // A recursive function to fill remaining
    // matrix
    private boolean fillRemaining(int i, int j) {
        // System.out.println(i+" "+j);
        if (j >= 9 && i < 9 - 1) {
            i = i + 1;
            j = 0;
        }
        if (i >= 9 && j >= 9)
            return true;

        if (i < 3) {
            if (j < 3)
                j = 3;
        } else if (i < 9 - 3) {
            if (j == (i / 3) * 3)
                j = j + 3;
        } else {
            if (j == 9 - 3) {
                i = i + 1;
                j = 0;
                if (i >= 9)
                    return true;
            }
        }

        for (int num = 1; num <= 9; num++) {
            if (CheckIfSafe(i, j, num)) {
                cells[i][j].setCellValue(num);
                if (fillRemaining(i, j + 1))
                    return true;

                cells[i][j].setCellValue(0);
            }
        }
        return false;
    }

    // Remove the K no. of digits to
    // complete game
    private void removeKDigits() {
        int count = numMissingDigit;
        while (count != 0) {
            int cellId = randomGenerator(9 * 9) - 1;

            // System.out.println(cellId);
            // extract coordinates i and j
            int i = (cellId / 9);
            int j = cellId % 9;

            SudokuCell cell = cells[i][j];

            // System.out.println(i+" "+j);
            if (cell.getCellValue() != 0) {
                count--;
                cell.setCellValue(0);
                cell.setToBeFilled();
            }
        }
    }
}
