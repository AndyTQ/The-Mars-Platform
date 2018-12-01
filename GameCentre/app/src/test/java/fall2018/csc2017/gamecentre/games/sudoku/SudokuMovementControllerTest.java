package fall2018.csc2017.gamecentre.games.sudoku;

import android.widget.Button;


import org.junit.Test;

import static org.junit.Assert.*;


import fall2018.csc2017.gamecentre.User;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * test for sudoku Movement controller.
 */
public class SudokuMovementControllerTest {
    @Test
    public void testInitializeButtonStyleFixed() {
        SudokuGameActivity gameActivity = mock(SudokuGameActivity.class);

        //This boardManager is only used to construct the movement controller.
        SudokuBoardManager boardManager = mock(SudokuBoardManager.class);
        Button btn = mock(Button.class);
        // By default a button is set to be fixed(not editable by the user).
        SudokuCell cell = new SudokuCell();
        int num = 1;

        SudokuMovementController mController = new SudokuMovementController(boardManager, gameActivity);
        mController.initializeButtonStyle(cell, btn, num);
        verify(gameActivity).setButtonNum(btn, num);
        verify(gameActivity).setButtonFixedBackground(btn);
    }

    @Test
    public void testInitializeButtonStyleToBeFilled() {
        SudokuGameActivity gameActivity = mock(SudokuGameActivity.class);
        SudokuBoardManager boardManager = mock(SudokuBoardManager.class);
        Button btn = mock(Button.class);
        SudokuCell cell = new SudokuCell();
        cell.setToBeFilled();
        int num = 1;

        SudokuMovementController mController = new SudokuMovementController(boardManager, gameActivity);
        mController.initializeButtonStyle(cell, btn, num);
        verify(gameActivity).setButtonNum(btn, num);
        verify(gameActivity).setButtonToBeFilledBackground(btn);
    }

    @Test
    public void testProcessTapCellInvalidPosition() {
        SudokuGameActivity gameActivity = mock(SudokuGameActivity.class);
        User user = mock(User.class);
        SudokuBoardManager boardManager = new SudokuBoardManager(user, 0);
        boardManager.getBoard().getCell(0, 0).setToBeFilled();

        SudokuMovementController mController = new SudokuMovementController(boardManager, gameActivity);
        mController.processTapCell(1);
        verify(gameActivity).makeInvalidTapMessage();
    }

    @Test
    public void testProcessTapCellValidPositionChangeCellToCell() {
        SudokuGameActivity gameActivity = mock(SudokuGameActivity.class);
        User user = mock(User.class);
        SudokuBoardManager boardManager = new SudokuBoardManager(user, 0);
        SudokuBoard board = boardManager.getBoard();
        board.getCell(0, 0).setToBeFilled();
        board.getCell(0, 1).setToBeFilled();
        board.updateCellNum(0, 0, 0);
        board.updateCellNum(0, 1, 0);
        board.setNumCellFilled(79);

        SudokuMovementController mController = new SudokuMovementController(boardManager, gameActivity);
        mController.setCellPosition(0);
        mController.processTapCell(1);

        verify(gameActivity).makeCellRed(1);
        verify(gameActivity).makePrevCellColourOriginal(0);
        assertEquals(mController.getCellPosition(), 1);
        assertEquals(mController.getPrevSelectedPosition(), 0);
    }

    @Test
    public void testProcessTapDigitCellNotChosen() {
        SudokuGameActivity gameActivity = mock(SudokuGameActivity.class);
        SudokuBoardManager boardManager = mock(SudokuBoardManager.class);

        SudokuMovementController mController = new SudokuMovementController(boardManager, gameActivity);
        mController.processTapDigit(3);

        verify(gameActivity).makeChooseCellFirstMessage();
    }

    @Test
    public void testProcessTapDigitPuzzleNotSolved() {
        SudokuGameActivity gameActivity = mock(SudokuGameActivity.class);
        SudokuBoardManager boardManager = mock(SudokuBoardManager.class);

        SudokuMovementController mController = new SudokuMovementController(boardManager, gameActivity);
        mController.setCellPosition(80);
        mController.processTapDigit(3);

        verify(gameActivity).displayChange(80);
        verify(boardManager).fillInCell(80, 3);
    }

    @Test
    public void testProcessTapDigitPuzzleSolved() {
        SudokuGameActivity gameActivity = mock(SudokuGameActivity.class);
        User user = mock(User.class);
        SudokuBoardManager boardManager = new SudokuBoardManager(user, 0);
        int correctNum = boardManager.getBoard().getCellNum(8, 8);
        int fillNum = 1 == correctNum ? 2 : 1;
        boardManager.getBoard().updateCellNum(8, 8, fillNum);

        SudokuMovementController mController = new SudokuMovementController(boardManager, gameActivity);
        mController.setCellPosition(80);
        mController.processTapDigit(correctNum);
        verify(gameActivity).onGameOver();
    }


}