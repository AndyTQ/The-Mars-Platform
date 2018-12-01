package fall2018.csc2017.gamecentre.games.sudoku;

import android.widget.Button;

class SudokuMovementController {
    /**
     * The position that is previously chosen by the user.
     */
    private int prevSelectedPosition = -1;

    /**
     * Return the previously chosen position.
     *
     * @return the previously chosen position.
     */
    int getPrevSelectedPosition() {
        return prevSelectedPosition;
    }

    /**
     * Return the position of the cell.
     *
     * @return the position of the cell.
     */
    int getCellPosition() {
        return cellPosition;
    }

    /**
     * The constructor.
     *
     * @param boardManager the board manager.
     * @param gameActivity the game activity.
     */
    SudokuMovementController(SudokuBoardManager boardManager, SudokuGameActivity gameActivity) {
        this.boardManager = boardManager;
        this.gameActivity = gameActivity;
    }

    /**
     * The position of the chosen cell.
     */
    private int cellPosition = -1;

    /**
     * The board manager.
     */
    private final SudokuBoardManager boardManager;

    /**
     * The game activity.
     */
    private final SudokuGameActivity gameActivity;

    /**
     * Initialize the style of the button when creating the buttons for the grid view.
     *
     * @param cell the corresponding cell.
     * @param btn  the button.
     * @param num  the number.
     */
    void initializeButtonStyle(SudokuCell cell, Button btn, int num) {
        if (!cell.isToBeFilled()) {
            gameActivity.setButtonNum(btn, num);
            gameActivity.setButtonFixedBackground(btn);
        } else {
            gameActivity.setButtonToBeFilledBackground(btn);
            if (num != 0) {
                gameActivity.setButtonNum(btn, num);
            }
        }
    }

    /**
     * Set the position of the cell.
     *
     * @param cellPosition the cell position.
     */
    void setCellPosition(int cellPosition) {
        this.cellPosition = cellPosition;
    }

    /**
     * React to the tap on the cell at position.
     *
     * @param position the position.
     */
    void processTapCell(int position) {
        if (boardManager.getBoard().isValidPosition(position)) {
            prevSelectedPosition = cellPosition;
            cellPosition = cellPosition != position ? position : -1;
            if (cellPosition != -1) {
                gameActivity.makeCellRed(cellPosition);
            }
            if (prevSelectedPosition != -1) {
                gameActivity.makePrevCellColourOriginal(prevSelectedPosition);
            }
        } else {
            gameActivity.makeInvalidTapMessage();
        }
    }

    /**
     * React the tap on the digit button.
     *
     * @param digit the digit chosen.
     */
    void processTapDigit(int digit) {
        if (cellPosition != -1) {
            boardManager.fillInCell(this.cellPosition, digit);
            gameActivity.displayChange(cellPosition);
            cellPosition = -1;
            if (boardManager.puzzleSolved()) {
                gameActivity.onGameOver();
            }
        } else {
            gameActivity.makeChooseCellFirstMessage();
        }
    }

}
