package fall2018.csc2017.gamecentre.games.game2048;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import fall2018.csc2017.gamecentre.User;

/**
 * The board manager for game 2048, contains most of the logic for game 2048
 */
class Game2048BoardManager implements Serializable {
    /**
     * Initializing static constants
     */
    private static final int WINNING_THRESHOLD = 2048;
    private static final float PIVOT_VALUE = 0.5f;
    private static final float PROBABILITY_THRESHOLD = 0.75f;
    private static final int ANIMATION_DURATION = 200;
    /**
     * The context of the activity
     */
    private final Context activityContext;
    /**
     * The user currently playing
     */
    private final User user;
    /**
     * The collection of the current items
     */
    private Game2048Item[][] game2048Items = null;
    /**
     * The complexity of the current game
     */
    private final int complexity;
    /**
     * booleans used as flags during the operation
     */
    private boolean isMoveHappen = false;
    private boolean isMergeHappen = false;
    private boolean isFirst = true;
    /**
     * Stack that stores the states of the Game2048Items
     */
    private Stack<Game2048Item[][]> states = new Stack<>();
    /**
     * The OnGame2048Listener, process updates to the activity
     */
    private OnGame2048Listener onGame2048Listener;
    /**
     * The score manager of the game, responsible for the managing the score.
     */
    private final Game2048ScoreManager game2048ScoreManager;

    /**
     * Constructor...
     *
     * @param user       the user playing the game
     * @param complexity the complexity that the user choose
     * @param context    the context of the activity
     */
    Game2048BoardManager(User user, int complexity, Context context) {
        this.user = user;
        this.complexity = complexity;
        this.activityContext = context;
        game2048ScoreManager = new Game2048ScoreManager(user, complexity);
    }

    /**
     * Rrturn whether or not this is the first tile to be created
     *
     * @return boolean
     */
    boolean isFirst() {
        return isFirst;
    }

    /**
     * Setter for the boolean isFirst, used by testing
     */
    void setFirst() {
        this.isFirst = true;
    }


    /**
     * Return the current score manager
     *
     * @return Game2048ScoreManager
     */
    Game2048ScoreManager getGame2048ScoreManager() {
        return game2048ScoreManager;
    }

    /**
     * Return the user
     *
     * @return User
     */
    public User getUser() {
        return user;
    }

    /**
     * Create a new empty board, used when the game is starting
     */
    void createNewEmptyBoard() {
        this.game2048Items = new Game2048Item[complexity][complexity];
    }

    /**
     * Set the newItem at row index row, column index col
     *
     * @param newItem the new item to be setted
     * @param row     the row index
     * @param col     the column index
     */
    void setGame2048ItemWithIndex(Game2048Item newItem, int row, int col) {
        this.game2048Items[row][col] = newItem;
    }

    /**
     * Return whether or not the tiles in yet null
     *
     * @return boolean
     */
    boolean game2048ItemIsNull() {
        return game2048Items == null;
    }

    /**
     * Replace the existing board with a empty one of same size, used by reStart()
     */
    void resetSameSizeBoard() {
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                Game2048Item item = game2048Items[i][j];
                item.setNumber(0);
            }
        }
    }

    /**
     * Return whether or not the game has won
     *
     * @return boolean
     */
    boolean isWinning() {
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                Game2048Item item = game2048Items[i][j];
                if (item.getNumber() == WINNING_THRESHOLD) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return whether or not the game is already lost
     * Partially adaped from https://blog.csdn.net/yin_zihao/article/details/50782110
     *
     * @return boolean
     */
    boolean isGameOver() {
        // If the grid is not filled with non-zero numbers
        boolean flag = true;
        if (hasEmpty()) {
            flag = false;
        }
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                Game2048Item item = game2048Items[i][j];
                // If this is not the last column
                if ((j + 1) != complexity) {
                    Game2048Item itemRight = game2048Items[i][j + 1];
                    if (item.getNumber() == itemRight.getNumber())
                        flag = false;
                }
                // If this is not the last row
                if ((i + 1) != complexity) {
                    Game2048Item itemBottom = game2048Items[i + 1][j];
                    if (item.getNumber() == itemBottom.getNumber())
                        flag = false;
                }
                // If this is not the first column
                if (j != 0) {
                    Game2048Item itemLeft = game2048Items[i][j - 1];
                    if (itemLeft.getNumber() == item.getNumber())
                        flag = false;
                }
                // If this is not the first row
                if (i != 0) {
                    Game2048Item itemTop = game2048Items[i - 1][j];
                    if (item.getNumber() == itemTop.getNumber())
                        flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * Merge the item in the rowList
     *
     * @param rowList List<Game2048Item>
     */
    private void mergeItem(List<Game2048Item> rowList) {
        if (rowList.size() < 2) {
            return;
        }
        for (int i = 0; i < rowList.size() - 1; i++) {
            Game2048Item item1 = rowList.get(i);
            Game2048Item item2 = rowList.get(i + 1);
            if (item1.getNumber() == item2.getNumber()) {
                isMergeHappen = true;
                game2048ScoreManager.increaseScoreBy(item1.getNumber());
                item1.setNumber(item1.getNumber() * 2);
                onGame2048Listener.onScoreChange(game2048ScoreManager.calculateScore());
                for (int j = i + 1; j < rowList.size() - 1; j++) {
                    // push the items afterwards
                    rowList.get(j).setNumber(rowList.get(j + 1).getNumber());
                }
                // The last item must be zero
                rowList.get(rowList.size() - 1).setNumber(0);
                return;
            }
        }
    }

    /**
     * Return whether or not the baord has empty slot
     *
     * @return boolean
     */
    private boolean hasEmpty() {
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                Game2048Item game2048Item = game2048Items[i][j];
                if (game2048Item.getNumber() == 0)
                    return true;
            }
        }
        return false;
    }

    /**
     * Process the action taken by the player.
     * Partially adapted from https://blog.csdn.net/yin_zihao/article/details/50782110
     *
     * @param action the action to be processed
     */
    void action(Game2048Layout.Action action) {
        saveStates(activityContext);
        game2048ScoreManager.saveScore(getGame2048ScoreManager().calculateScore());
        for (int i = 0; i < complexity; i++) {
            List<Game2048Item> rowList = new ArrayList<>();
            for (int j = 0; j < complexity; j++) {
                int rowIndex = getRowIndexByAction(action, i, j);
                int colIndex = getColIndexByAction(action, i, j);
                Game2048Item item = game2048Items[rowIndex][colIndex];
                if (item.getNumber() != 0) {
                    rowList.add(item);  // Store the non-zero items
                }
            }
            for (int j = 0; j < rowList.size(); j++) {
                int rowIndex = getRowIndexByAction(action, i, j);
                int colIndex = getColIndexByAction(action, i, j);
                Game2048Item item = game2048Items[rowIndex][colIndex];
                if (item.getNumber() != rowList.get(j).getNumber()) {
                    isMoveHappen = true;
                }
            }
            // Merge the same numbers in the temporary array list
            mergeItem(rowList);
            for (int j = 0; j < complexity; j++) {
                // Sequentially fill in the row/column, the rest must be zero
                if (rowList.size() > j) {
                    switch (action) {
                        case LEFT:
                            game2048Items[i][j].setNumber(rowList.get(j).getNumber());
                            break;
                        case RIGHT:
                            game2048Items[i][complexity - 1 - j].setNumber(rowList.get(j).getNumber());
                            break;
                        case UP:
                            game2048Items[j][i].setNumber(rowList.get(j).getNumber());
                            break;
                        case DOWN:
                            game2048Items[complexity - 1 - j][i].setNumber(rowList.get(j).getNumber());
                            break;
                    }
                } else {
                    switch (action) {
                        case LEFT:
                            game2048Items[i][j].setNumber(0);
                            break;
                        case RIGHT:
                            game2048Items[i][complexity - 1 - j].setNumber(0);
                            break;
                        case UP:
                            game2048Items[j][i].setNumber(0);
                            break;
                        case DOWN:
                            game2048Items[complexity - 1 - j][i].setNumber(0);
                            break;
                    }
                }
            }
        }
        generateViewAuto();
    }

    /**
     * Return the row index by action.
     * Adapted from https://blog.csdn.net/yin_zihao/article/details/50782110
     *
     * @param action the action to be processed
     * @param row    the row index
     * @param col    the column index
     * @return the row index
     */
    private int getRowIndexByAction(Game2048Layout.Action action, int row, int col) {
        int rowIndex = -1;
        switch (action) {
            case LEFT:
            case RIGHT:
                rowIndex = row;
                break;
            case UP:
                rowIndex = col;
                break;
            case DOWN:
                rowIndex = complexity - 1 - col;
                break;
        }
        return rowIndex;
    }

    /**
     * Return the column index by action.
     * Adapted from https://blog.csdn.net/yin_zihao/article/details/50782110
     *
     * @param action the action to be processed
     * @param row    the row index
     * @param col    the column index
     * @return the column index
     */
    private int getColIndexByAction(Game2048Layout.Action action, int row, int col) {
        int colIndex = -1;
        switch (action) {
            case LEFT:
                colIndex = col;
                break;
            case RIGHT:
                colIndex = complexity - 1 - col;
                break;
            case UP:
            case DOWN:
                colIndex = row;
                break;
        }
        return colIndex;
    }


    /**
     * Undo one step
     *
     * @param context the context of the tiles
     */
    public void undo(Context context) {
        if (states.isEmpty()) {
            Toast.makeText(context, "No more steps to undo!", Toast.LENGTH_SHORT).show();
            return;
        }
        Game2048Item[][] prevStates = states.pop();
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                game2048Items[i][j].setNumber(prevStates[i][j].getNumber());
            }
        }
        int lastScore = game2048ScoreManager.popScore();
        onGame2048Listener.onScoreChange(lastScore);
        game2048ScoreManager.setScoreChange(lastScore);
    }

    /**
     * Save the states
     *
     * @param context the context of the tiles, required by the clone of Game2048Item
     */
    void saveStates(Context context) {
        Game2048Item[][] newArray = new Game2048Item[complexity][complexity];
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                newArray[i][j] = game2048Items[i][j].clone(context);
            }
        }
        states.add(newArray);
    }

    /**
     * Set the On Game Listener, responsible for real tile updating, listening the game
     *
     * @param onGame2048Listener the Listener to be setted
     */
    void setGame2048Listener(OnGame2048Listener onGame2048Listener) {
        this.onGame2048Listener = onGame2048Listener;
    }

    /**
     * Restart the game, reset all necessary dependent fields
     * and restart the game with a board of the same size
     */
    void reStart() {
        resetSameSizeBoard();
        game2048ScoreManager.setScoreChange(0);
        game2048ScoreManager.resetScores();
        onGame2048Listener.onScoreChange(0);
        states = new Stack<>();
        isFirst = true;
        generateViewAuto();
    }

    /**
     * Pseudo randomly generate a number, and put it inside a empty slot in the board.
     */
    private void generateOneNumber() {
        int randomRow = new Random().nextInt(complexity);
        int randomCol = new Random().nextInt(complexity);
        Game2048Item item = game2048Items[randomRow][randomCol];
        while (item.getNumber() != 0) {
            randomRow = new Random().nextInt(complexity);
            randomCol = new Random().nextInt(complexity);
            item = game2048Items[randomRow][randomCol];
        }
        item.setNumber(Math.random() > PROBABILITY_THRESHOLD ? 4 : 2);
        Animation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, PIVOT_VALUE, Animation.RELATIVE_TO_SELF, PIVOT_VALUE);
        scaleAnimation.setDuration(ANIMATION_DURATION);
        item.startAnimation(scaleAnimation);
    }

    /**
     * Generate one view, under the condition that either a merge has
     * happened or a move has happened
     * This eliminates the problem of generating a new tile when no change to the board
     * has actually happened
     */
    private void generateOneView() {
        if (isMergeHappen || isMoveHappen) {
            generateOneNumber();
            isMoveHappen = isMergeHappen = false;
        }
    }

    /**
     * Generate four numbers, randomly at the start of the game
     */
    private void generateStartingTiles() {
        for (int i = 0; i < 4; i++) {
            generateOneNumber();
        }
        for (int row = 0; row < complexity; row++) {
            for (int col = 0; col < complexity; col++) {
                Game2048Item item = game2048Items[row][col];
                Animation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                        Animation.RELATIVE_TO_SELF, PIVOT_VALUE, Animation.RELATIVE_TO_SELF, PIVOT_VALUE);
                scaleAnimation.setDuration(ANIMATION_DURATION);
                item.startAnimation(scaleAnimation);
            }
        }
        isMoveHappen = isMergeHappen = false;
    }

    /**
     * Generate the numbers for the board
     */
    void generateViewAuto() {
        if (isWinning()) {
            game2048ScoreManager.updateHighScore();
            onGame2048Listener.onGameWinning();
            return;
        }
        if (isGameOver()) {
            game2048ScoreManager.updateHighScore();
            onGame2048Listener.onGameOver();
            return;
        }
        if (isFirst()) {
            generateStartingTiles();
            isFirst = false;
        }
        if (hasEmpty()) {
            generateOneView();
        }
    }
}
