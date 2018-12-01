package fall2018.csc2017.gamecentre.games.slidingtiles;

import java.util.Stack;

class SlidingTilesMovementController {
    /**
     * A BoardManager object
     */
    private SlidingTilesBoardManager boardManager = null;

    /**
     * The game activity for the game.
     */

    private SlidingTilesGameActivity gameActivity;

    /**
     * Position of each recoverable states.
     */
    private final Stack<Integer> states = new Stack<>();

    /**
     * Getter for the stack of states
     *
     * @return Stack of states (history of swaps)
     */
    Stack getStates() {
        return states;
    }

    /**
     * Constructor...
     */
    SlidingTilesMovementController() {
    }

    /**
     * The undo limit left, while playing the game
     */
    private int undoLimitCurrent;

    /**
     * The undo limit that player sets, from starting activity.
     */
    private int undoLimitPlayerSet;

    /**
     * Set the current Board Manager to the passed in boardManager
     *
     * @param boardManager a boardManager to replace with
     */
    void setBoardManager(SlidingTilesBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    /**
     * Set the game activity.
     * @param gameActivity this sliding tiles gameActivity
     */
    void setGameActivity(SlidingTilesGameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    /**
     * On tapping a button of tile, process everything.
     * @param position the position of the tile
     */
    void processTapMovement(int position) {
        if (boardManager.isValidTap(position)) {
            int undoPosition = boardManager.touchMove(position);
            states.add(undoPosition);
            increaseUndoLimitCurrent();
            boardManager.incrementNumOfSwaps();
            if (boardManager.puzzleSolved()) {
                gameActivity.onGameOver();
            }
        } else {
            gameActivity.makeInvalidTapText();
        }
    }

    void undo(){
        if (undoLimitCurrent > 0) {
            if (!states.isEmpty()) {
                int prev_ = states.pop();
                boardManager.touchMove(prev_);
                gameActivity.makeToastUndoText();
                decreaseUndoLimitCurrent();
            } else {
                gameActivity.MakeToastNoMoreSteps();
            }
        } else {
            gameActivity.MakeToastUndoLimitExceeded();
        }
    }

    void setUndoLimit(int undoLimit) {
        this.undoLimitCurrent = undoLimit;
        this.undoLimitPlayerSet = undoLimit;
    }

    void setUndoLimitCurrent(int undoLimitCurrent){
        this.undoLimitCurrent = undoLimitCurrent;
    }

    /**
     * Increase undoLimitCurrent if the user makes a move.
     */
     void increaseUndoLimitCurrent() {
        if (undoLimitCurrent < undoLimitPlayerSet) {
            undoLimitCurrent++;
        }
    }

    void decreaseUndoLimitCurrent() {
        undoLimitCurrent--;
    }

    int getUndoLimitCurrent() {
        return this.undoLimitCurrent;
    }
}
