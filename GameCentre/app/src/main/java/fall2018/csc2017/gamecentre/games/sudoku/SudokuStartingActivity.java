package fall2018.csc2017.gamecentre.games.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.User;

/**
 * Excluded from tests because it's a view class.
 */
public class SudokuStartingActivity extends AppCompatActivity {
    /**
     * The user playing the game.
     */
    private User user;

    /**
     * The board manager.
     */
    private SudokuBoardManager boardManager;

    /**
     * whether the board is solved.
     */
    private boolean is_solved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_starting);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        this.user = (User) getIntent().getSerializableExtra("UserClass");
        boardManager = new SudokuBoardManager(this.user, 50);
        saveToFile(getTempSaveFileName());
        addStartButtonListener();
        addLoadButtonListener();
    }

    /**
     * Activate the load button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.sudoku_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardManager = new SudokuBoardManager(user, 50);
                switchToGame();
            }
        });
    }


    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.sudoku_load);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shallowLoadFromFile(getSaveFileName());
                if (is_solved) {
                    makeToastAlreadySolved();
                } else {
                    loadFromFile(getSaveFileName());
                    saveToFile(getTempSaveFileName());
                    makeToastLoadedText();
                    switchToGame();
                }
            }
        });
    }

    /**
     * Switch from the current to the game activity.
     */
    private void switchToGame() {
        Intent intent = new Intent(SudokuStartingActivity.this, SudokuGameActivity.class);
        intent.putExtra("file name sudoku", getSaveFileName());
        intent.putExtra("temp file name sudoku", getTempSaveFileName());
        saveToFile(getTempSaveFileName());
        startActivity(intent);
    }

    /**
     * Save the current board manager to the file with fileName.
     *
     * @param fileName the name of the file to be saved to.
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
     * Load the board manager from the file with fileName.
     *
     * @param fileName the file name.
     */
    private void loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (SudokuBoardManager) input.readObject();
                is_solved = boardManager.puzzleSolved();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Only load the winning status of the last saved game to avoid loading an already solved game.
     *
     * @param fileName the file name
     */
    private void shallowLoadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                SudokuBoardManager tempManager = (SudokuBoardManager) input.readObject();
                is_solved = tempManager.puzzleSolved();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Return the name for the temp save file for the current user.
     *
     * @return the name for the temp save file.
     */
    private String getTempSaveFileName() {
        return String.format("sudoku_temp_save_file_%s.ser", user.toString());
    }

    /**
     * Return the name for the save file for the current user.
     *
     * @return Return the name for the save file.
     */
    private String getSaveFileName() {
        return String.format("sudoku_save_file_%s.ser", user.toString());
    }

    /**
     * Display that a game was already solved.
     */
    private void makeToastAlreadySolved() {
        Toast.makeText(this,
                "The previously saved game is already solved! So start a new game.",
                Toast.LENGTH_SHORT).show();
    }


    /**
     * Display that a game was loaded successfully.
     */
    private void makeToastLoadedText() {
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    }

}
