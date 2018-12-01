package fall2018.csc2017.gamecentre.games.sudoku;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.games.CustomAdapter;

import static java.lang.Math.round;

/**
 * Excluded from tests because it's a view class.
 */
public class SudokuGameActivity extends AppCompatActivity {
    /**
     * The file name for saving game states.
     */
    private String SAVE_FILENAME;

    /**
     * The board manager.
     */
    private SudokuBoardManager boardManager;

    /**
     * The Sudoku Score Manager.
     */
    private SudokuScoreManager scoreManager;

    /**
     * The movement controller.
     */
    private SudokuMovementController mController;

    /**
     * The clock to record the playing time.
     */
    private Chronometer cmTimer;

    /**
     * The buttons to be displayed.
     */
    private ArrayList<Button> cellButtons;

    /**
     * The colors for the cell backgrounds.
     */
    private final GradientDrawable spFixed = getGradientDrawable(0xFF96A9AE);
    private final GradientDrawable spToBeFilled = getGradientDrawable(0xFFCFC9C4);
    private final GradientDrawable spRed = getGradientDrawable(0xFFF0DDDA);


    // Grid View and calculated column height and width based on device size
    private SudokuGestureDetectGridView gridView;
    private static int columnWidth, columnHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the undo limit from StartingActivity
        Intent intent = getIntent();
        SAVE_FILENAME = intent.getStringExtra("file name sudoku");
        String TEMP_SAVE_FILENAME = intent.getStringExtra("temp file name sudoku");
        loadFromFile(TEMP_SAVE_FILENAME);
        setContentView(R.layout.activity_sudoku_main);
        // Initialize the clock
        initializeClock();
        scoreManager = new SudokuScoreManager(boardManager.getUser(), this);
        mController = new SudokuMovementController(boardManager, this);
        initializeGridView();
        createCellButtons(this);
        saveToFile(SAVE_FILENAME);
        addDigitButtonListeners();
    }

    /**
     * Activate the digit buttons.
     */
    private void addDigitButtonListeners() {
        addDigitOneButtonListener();
        addDigitTwoButtonListener();
        addDigitThreeButtonListener();
        addDigitFourButtonListener();
        addDigitFiveButtonListener();
        addDigitSixButtonListener();
        addDigitSevenButtonListener();
        addDigitEightButtonListener();
        addDigitNineButtonListener();
    }

    /**
     * Create buttons for the cells when initializing the grid.
     * @param context The context.
     */
    private void createCellButtons(Context context) {
        SudokuBoard board = boardManager.getBoard();
        cellButtons = new ArrayList<>();
        for (int row = 0; row != 9; row++) {
            for (int col = 0; col != 9; col++) {
                Button tmp = new Button(context);
                SudokuCell cell = board.getCell(row, col);
                int num = cell.getCellValue();
                mController.initializeButtonStyle(cell, tmp, num);
                this.cellButtons.add(tmp);
            }
        }
    }

    /**
     * Display the cell's number on the button.
     *
     * @param btn the button.
     * @param num the number of the corresponding cell.
     */
    void setButtonNum(Button btn, int num) {
        btn.setText(String.valueOf(num));
    }

    /**
     * Set the background color of the button which is to be filled by the user.
     *
     * @param btn the button.
     */
    void setButtonToBeFilledBackground(Button btn) {
        btn.setBackground(spToBeFilled);
    }

    /**
     * Set the background color of the button which couldn't be filled by the user.
     *
     * @param btn the button.
     */
    void setButtonFixedBackground(Button btn) {
        btn.setBackground(spFixed);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (SudokuBoardManager) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            makeNoHistoryText();
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            makeNoHistoryText();
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " +
                    e.toString());
            makeNoHistoryText();
        }
    }


    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    private void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Display that a player has no history.
     */
    private void makeNoHistoryText() {
        Toast.makeText(this, "You have no history. " +
                "Please start a new game", Toast.LENGTH_SHORT).show();
    }

    /**
     * Initialize the clock.
     */
    private void initializeClock() {
        cmTimer = findViewById(R.id.cmTimer);
        cmTimer.setFormat("%s");
        cmTimer.setBase(SystemClock.elapsedRealtime() - boardManager.getDuration());
        cmTimer.start();
    }

    /**
     * Change the color and text of the button after a number is filled on the cell.
     *
     * @param cellPosition the position of the cell.
     */
    void displayChange(int cellPosition) {
        SudokuBoard board = boardManager.getBoard();
        int row = cellPosition / 9;
        int col = cellPosition % 9;
        cellButtons.get(cellPosition).setText(String.valueOf(board.getCellNum(row, col)));
        cellButtons.get(cellPosition).setBackground(spToBeFilled);
        gridView.setAdapter(new CustomAdapter(cellButtons, columnWidth, columnHeight));
        boardManager.setDuration(SystemClock.elapsedRealtime() - cmTimer.getBase());
        saveToFile(SAVE_FILENAME);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        cmTimer.stop();
        boardManager.setDuration(SystemClock.elapsedRealtime() - cmTimer.getBase());
        saveToFile(SAVE_FILENAME);
    }

    @NonNull
    private GradientDrawable getGradientDrawable(int colorCode) {
        GradientDrawable sp = new GradientDrawable();
        sp.setShape(GradientDrawable.RECTANGLE);
        sp.setCornerRadii(new float[]{8, 8, 8, 8, 0, 0, 0, 0});
        sp.setColor(colorCode);
        sp.setStroke(3, Color.WHITE);
        return sp;
    }

    /**
     * Add View to activity and set its attributes.
     */
    private void initializeGridView() {
        gridView = findViewById(R.id.sudoku_grid);
        gridView.setNumColumns(9);
        gridView.setmController(mController);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();
                        columnWidth = displayWidth / 9;
                        columnHeight = displayHeight / 9;
                        gridView.setAdapter(new CustomAdapter(cellButtons, columnWidth, columnHeight));
                    }
                });
    }

    /**
     * Stop the timer and update the score after the game is over.
     */
    public void onGameOver() {
        cmTimer.stop();
        long playTime = round((SystemClock.elapsedRealtime() - cmTimer.getBase()) / 1000);
        scoreManager.setPlayTime(playTime);
        int score = scoreManager.calculateScore();
        scoreManager.updateHighScore();
        Intent intent = new Intent(this, SudokuWinningActivity.class);
        intent.putExtra("GAME_SCORE", score);
        this.startActivity(intent);
    }

    /**
     * Change the color of the button to red after a cell is selected by the user.
     *
     * @param cellPosition the position of the cell.
     */
    void makeCellRed(int cellPosition) {
        cellButtons.get(cellPosition).setBackground(spRed);
        gridView.setAdapter(new CustomAdapter(cellButtons, columnWidth, columnHeight));
    }

    /**
     * Change the color of the button to its original color when the cell is unselected.
     *
     * @param prevSelectedPosition the position of the previously selected button.
     */
    void makePrevCellColourOriginal(int prevSelectedPosition) {
        cellButtons.get(prevSelectedPosition).setBackground(spToBeFilled);
        gridView.setAdapter(new CustomAdapter(cellButtons, columnWidth, columnHeight));
    }

    /**
     * Activate the digit one button.
     */
    private void addDigitOneButtonListener() {
        final Button digitOne = findViewById(R.id.digit_1);
        digitOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.processTapDigit(1);
            }
        });
    }

    /**
     * Activate the digit two button.
     */
    private void addDigitTwoButtonListener() {
        final Button digitTwo = findViewById(R.id.digit_2);
        digitTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.processTapDigit(2);
            }
        });
    }

    /**
     * Activate the digit three button.
     */
    private void addDigitThreeButtonListener() {
        final Button digitThree = findViewById(R.id.digit_3);
        digitThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.processTapDigit(3);
            }
        });
    }

    /**
     * Activate the digit four button.
     */
    private void addDigitFourButtonListener() {
        final Button digitFour = findViewById(R.id.digit_4);
        digitFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.processTapDigit(4);
            }
        });
    }

    /**
     * Activate the digit five button.
     */
    private void addDigitFiveButtonListener() {
        final Button digitFive = findViewById(R.id.digit_5);
        digitFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.processTapDigit(5);
            }
        });
    }

    /**
     * Activate the digit six button.
     */
    private void addDigitSixButtonListener() {
        final Button digitSix = findViewById(R.id.digit_6);
        digitSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.processTapDigit(6);
            }
        });
    }

    /**
     * Activate the digit seven button.
     */
    private void addDigitSevenButtonListener() {
        final Button digitSeven = findViewById(R.id.digit_7);
        digitSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.processTapDigit(7);
            }
        });
    }

    /**
     * Activate the digit eight button.
     */
    private void addDigitEightButtonListener() {
        final Button digitEight = findViewById(R.id.digit_8);
        digitEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.processTapDigit(8);
            }
        });
    }

    /**
     * Activate the digit nine button.
     */
    private void addDigitNineButtonListener() {
        final Button digitNine = findViewById(R.id.digit_9);
        digitNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.processTapDigit(9);
            }
        });
    }

    /**
     * Make a toast to promote user to select a cell before enter the digit.
     */
    public void makeChooseCellFirstMessage() {
        Toast.makeText(this, "CHOOSE A CELL FIRST!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Make a toast that the tap is invalid.
     */
    public void makeInvalidTapMessage() {
        Toast.makeText(this, "INVALID TAP", Toast.LENGTH_SHORT).show();
    }
}
