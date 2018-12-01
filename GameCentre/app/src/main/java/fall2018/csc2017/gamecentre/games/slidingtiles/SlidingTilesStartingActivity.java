package fall2018.csc2017.gamecentre.games.slidingtiles;

/*
  Adapted from:
  https://tutorialwing.com/android-discrete-seekbar-tutorial-with-example/
  https://www.tutorialspoint.com/android/android_spinner_control.htm
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.User;
import fall2018.csc2017.gamecentre.games.DifficultyChoosable;

/**
 * The initial activity for the sliding puzzle tile game.
 */

public class SlidingTilesStartingActivity extends AppCompatActivity implements DifficultyChoosable {

    /**
     * The board manager.
     */
    private SlidingTilesBoardManager boardManager;

    /**
     * limit for undo times.
     */
    private int undoLimitPlayerSet = 3;
    /**
     * complexity from the saved previous file.
     */
    private int loaded_complexity;
    /**
     * complexity choosen now.
     */
    private int complexity = 3;
    /**
     * if the sliding tiles board is solved.
     */
    private boolean is_solved;
    /**
     * The user. This will be currently logged in user.
     */
    private User user;
    /**
     * If the user choose his own picture for the profile image.
     */
    private boolean customized = false;
    /**
     * data of the image to be customized.
     */
    private Bitmap imgData;
    /**
     * default case code for the profile image.
     */
    private final int useDefault = 0;
    /**
     * case code for the custom image option.
     */
    private final int chooseImageCustom = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidingtiles_starting);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        this.user = (User) getIntent().getSerializableExtra("UserClass");
        boardManager = new SlidingTilesBoardManager(this.user, complexity);
        saveToFile(getTempSaveFileName());
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
        addSelectImageListener();
        addDifficultySelector();
        addSeekBar();
    }

    protected void addStartButtonListener() {
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardManager = new SlidingTilesBoardManager(user, complexity, customized);
                switchToGame();
            }
        });
    }

    /**
     * Activate the Difficulty selector spinner
     */
    public void addDifficultySelector() {
        Spinner difficultySpinner = findViewById(R.id.spinner);
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        complexity = 3;
                        break;
                    case 1:
                        complexity = 4;
                        break;
                    case 2:
                        complexity = 5;
                        break;
                    default:
                        complexity = 3;
                }
                if (customized) {
                    setImageToTiles(imgData);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                complexity = 3;
            }
        });
    }

    private void addSelectImageListener() {
        Button mAddImage = findViewById(R.id.activity_slidingtiles_customize_button);
        mAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });
    }

    /**
     * Choose the image
     */
    private void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your background");
        String[] items = {"Use default number", "Customize Images"};
        builder.setNegativeButton("OK", null);
        int checkItem = customized ? 1 : 0;
        builder.setSingleChoiceItems(items, checkItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                customized = which != 0;
                switch (which) {
                    case useDefault:
                        break;
                    case chooseImageCustom:
                        CropImage.startPickImageActivity(SlidingTilesStartingActivity.this);
                        break;
                }
            }
        });
        builder.show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE:
                    Uri imageUri = CropImage.getPickImageResultUri(this, data);
                    startCropImageActivity(imageUri);
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    try {
                        imgData = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                        setImageToTiles(imgData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } else if (resultCode == RESULT_CANCELED) {
            customized = false;
        }
    }

    /**
     * activity for cropping image.
     *
     * @param imageUri the data for image
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1).setRequestedSize(370, 370).
                setMinCropResultSize(380, 380).start(this);
    }

    /**
     * Process the cutted image.
     */
    private void setImageToTiles(Bitmap imgData) {

        if (imgData != null) {
            int size = complexity * complexity;
            int tileLength = 360 / complexity;
            int x = 0;
            int y = 0;
            for (Integer i = 1; i < size; i++) { // last one is the blank tile.
                Bitmap tileImg = Bitmap.createBitmap(Objects.requireNonNull(imgData), x, y, tileLength, tileLength);
                try (FileOutputStream out = new FileOutputStream(getFilesDir() +
                        "/square_tile_" + i.toString() + ".png"
                )) {
                    tileImg.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (i % complexity == 0) {
                    x = 0;
                    y += tileLength;
                } else {
                    x += tileLength;
                }
            }
        }
    }

    /**
     * Adds the seek bar on the starting page
     * The seek bar is used for collecting information about what size of map that the user chose.
     */
    private void addSeekBar() {
        SeekBar seek = findViewById(R.id.slidingtiles_difficulty_seekbar);
        seek.setProgress(3);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Write code to perform some action when progress is changed.
                if (progress == 11) {
                    undoLimitPlayerSet = Integer.MAX_VALUE;
                } else undoLimitPlayerSet = progress;
                Integer current_limit = undoLimitPlayerSet;
                if (current_limit != Integer.MAX_VALUE) {
                    updateTextViewNumber(current_limit);
                } else updateTextViewUnlimited();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Write code to perform some action when touch is started.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Write code to perform some action when touch is stopped.
                Integer current_limit = undoLimitPlayerSet;
                if (current_limit != Integer.MAX_VALUE) {
                    updateTextViewNumber(current_limit);
                } else updateTextViewUnlimited();
            }

        });
    }

    /**
     * Update the text view, displaying the allowed number of undo.
     *
     * @param current_limit the string to update.
     */
    private void updateTextViewNumber(Integer current_limit) {
        TextView textView = findViewById(R.id.allow_max);
        String thisString = "Undo limit is now " + current_limit.toString() + " ʕ •ᴥ•ʔ";
        textView.setText(thisString);
    }

    /**
     * Update the text view, displaying the allowed number of undo.
     */
    private void updateTextViewUnlimited() {
        TextView textView = findViewById(R.id.allow_max);
        String thisString = "Undo limit is now infinity ᶘ ᵒᴥᵒᶅ";
        textView.setText(thisString);
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
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shallowLoadFromFile(getSaveFileName());
                if (is_solved) {
                    makeToastAlreadySolved();
                } else {
                    if (loaded_complexity != complexity) {
                        complexity = loaded_complexity;
                    }
                    loadFromFile(getSaveFileName());
                    saveToFile(getTempSaveFileName());
                    makeToastLoadedText();
                    switchToGame();
                }
            }
        });
    }


    /**
     * Display that a game was loaded successfully.
     */
    private void makeToastLoadedText() {
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    }

    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(getSaveFileName());
                saveToFile(getTempSaveFileName());
                makeToastSavedText();
            }
        });
    }

    /**
     * Display that a game was saved successfully.
     */
    private void makeToastSavedText() {
        Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        boolean flag = false;
        super.onResume();
        AssetManager assetManager = this.getAssets();
        try {
            assetManager.open(getSaveFileName());
        } catch (Exception ignored) {
            flag = true;
        }
        if (flag) {
            loadFromFile(getTempSaveFileName());
        } else {
            loadFromFile(getSaveFileName());
        }
    }


    protected void switchToGame() {
        Intent intent = new Intent(SlidingTilesStartingActivity.this, SlidingTilesGameActivity.class);
        intent.putExtra("file name", getSaveFileName());
        intent.putExtra("undo limit", undoLimitPlayerSet);
        intent.putExtra("temp file name", getTempSaveFileName());
        saveToFile(getTempSaveFileName());
        startActivity(intent);
    }

    /**
     * Return the name for the save file for the current user.
     *
     * @return Return the name for the save file.
     */
    private String getSaveFileName() {
        return String.format("sliding_tiles_save_file_%s.ser", user.toString());
    }

    /**
     * Return the name for the temp save file for the current user.
     *
     * @return the name for the temp save file.
     */
    private String getTempSaveFileName() {
        return String.format("sliding_tiles_temp_save_file_%s.ser", user.toString());
    }


    protected void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    protected void loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (SlidingTilesBoardManager) input.readObject();
                is_solved = boardManager.puzzleSolved();
                loaded_complexity = boardManager.getComplexity();
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
                SlidingTilesBoardManager tempManager = (SlidingTilesBoardManager) input.readObject();
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
}
