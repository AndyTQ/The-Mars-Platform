package fall2018.csc2017.gamecentre.games.slidingtiles;

/*
 Adapted from:
  https://abhiandroid.com/ui/chronometer
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.games.CustomAdapter;

import static java.lang.Math.round;

/**
 * The game activity.
 */
public class SlidingTilesGameActivity extends AppCompatActivity implements Observer {
    /**
     * The file name for saving game states.
     */
    private String saveFileName;
    /**
     * A temporary save file.
     */
    private String tempSaveFileName;

    /**
     * The board manager.
     */
    private SlidingTilesBoardManager boardManager;

    /**
     * The score manager.
     */
    private SlidingTilesScoreManager scoreManager;

    /**
     * The movement controller.
     */
    private SlidingTilesMovementController mController;

    /**
     * The buttons to be displayed.
     */
    private ArrayList<Button> tileButtons;

    /**
     * The grid view
     */
    private SlidingTilesGestureDetectGridView gridView;

    /**
     * Column height and width calculated based on device size
     */
    private int columnWidth, columnHeight;

    /**
     * The clock to record the playing time.
     */
    private Chronometer cmTimer;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    private void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the undo limit from StartingActivity
        Intent intent = getIntent();
        int undoLimitPlayerSet = intent.getIntExtra("undo limit", 3);
        saveFileName = intent.getStringExtra("file name");
        tempSaveFileName = intent.getStringExtra("temp file name");
        loadFromFile(tempSaveFileName);
        mController = new SlidingTilesMovementController();
        mController.setBoardManager(boardManager);
        mController.setGameActivity(this);
        mController.setUndoLimit(undoLimitPlayerSet);
        createTileButtons(this);
        setContentView(R.layout.activity_slidingtiles_main);
        // Initialize the clock
        initializeClock();
        // Observer sets up desired dimensions as well as calls our display function
        boardManager.getBoard().addObserver(this);
        // Add grid view to activity and set its attributes
        scoreManager = new SlidingTilesScoreManager(boardManager.getUser(),
                boardManager.getComplexity(), this);
        initializeGridView();
        // Add undo button to the game
        addUndoButtonListener();
        saveToFile(saveFileName);
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
     * Add View to activity and set its attributes.
     */
    private void initializeGridView() {
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(boardManager.getBoard().getNumCols());
        gridView.setMovementController(mController);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();
                        int smallerSide = Math.min(displayHeight, displayWidth);
                        columnWidth = smallerSide / boardManager.getBoard().getNumCols();
                        columnHeight = smallerSide / boardManager.getBoard().getNumRows();
                        display();
                    }
                });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        tileButtons = new ArrayList<>();
        for (int row = 0; row != boardManager.getBoard().getNumRows(); row++) {
            for (int col = 0; col != boardManager.getBoard().getNumCols(); col++) {
                Button tmp = new Button(context);
                if (!boardManager.isCustomized()) {
                    tmp.setBackgroundResource(boardManager.getBoard().getTile(row, col).getBackground());
                } else {
                    Integer wrappedID = boardManager.getBoard().getTile(row, col).getId();
                    tmp.setBackground(loadTileFromStorage(wrappedID.toString()));
                }
                this.tileButtons.add(tmp);
            }
        }
    }

    private Drawable loadTileFromStorage(String id) {
        try {
            File file = new File(getFilesDir(), "square_tile_" + id + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return new BitmapDrawable(getResources(), bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        SlidingTilesBoard board = boardManager.getBoard();
        int nextPos = 0;
        for (Button btn : tileButtons) {
            int row = nextPos / boardManager.getBoard().getNumRows();
            int col = nextPos % boardManager.getBoard().getNumCols();
            if (!boardManager.isCustomized()) {
                btn.setBackgroundResource(board.getTile(row, col).getBackground());
            } else {
                Integer wrappedID = board.getTile(row, col).getId();
                btn.setBackground(loadTileFromStorage(wrappedID.toString()));
            }
            nextPos++;
        }
        boardManager.setDuration(SystemClock.elapsedRealtime() - cmTimer.getBase());
        saveToFile(saveFileName);
    }



    /**
     * Activate the undo button.
     */
    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.UndoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.undo();
            }
        });
    }

    /**
     * Display that the undo limit is exceeded.
     */
    void MakeToastUndoLimitExceeded() {
        Toast.makeText(this, "Undo limit exceeded!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Display that the state was back to original.
     */
    void MakeToastNoMoreSteps() {
        Toast.makeText(this, "No more steps to undo!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Display that a step was undid successfully.
     */
    void makeToastUndoText() {
        Toast.makeText(this, "Step Undid", Toast.LENGTH_SHORT).show();
    }

    void makeInvalidTapText() {
        Toast.makeText(this, "Invalid Tap", Toast.LENGTH_SHORT).show();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        cmTimer.stop();
        boardManager.setDuration(SystemClock.elapsedRealtime() - cmTimer.getBase());
        saveToFile(tempSaveFileName);
    }

    void onGameOver() {
        cmTimer.stop();
        scoreManager.setNumOfSwaps(boardManager.getNumOfSwaps());
        long playTime = round((SystemClock.elapsedRealtime() - cmTimer.getBase()) / 1000);
        scoreManager.setPlayTime(playTime);
        int score = scoreManager.calculateScore();
        scoreManager.updateHighScore();
        Intent intent = new Intent(this, SlidingTilesWinningActivity.class);
        intent.putExtra("GAME_SCORE", score);
        this.startActivity(intent);
    }

    /**
     * Display that a player has no history.
     */
    private void makeNoHistoryText() {
        Toast.makeText(this, "You have no history. " +
                "Please start a new game", Toast.LENGTH_SHORT).show();
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
                boardManager = (SlidingTilesBoardManager) input.readObject();
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

    @Override
    public void update(Observable o, Object arg) {
        display();
    }
}
