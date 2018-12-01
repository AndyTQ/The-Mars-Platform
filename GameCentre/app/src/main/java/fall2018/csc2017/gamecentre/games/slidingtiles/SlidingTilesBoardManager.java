package fall2018.csc2017.gamecentre.games.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fall2018.csc2017.gamecentre.User;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class SlidingTilesBoardManager implements Serializable {
    /**
     * The board being managed.
     */
    private SlidingTilesBoard board;

    /**
     * The user. This will be currently logged in user.
     */
    private final User user;

    /**
     * The complexity of the board.
     */
    private final int complexity;

    /**
     * The number of swaps (for the sake of score counting).
     */
    private int numOfSwaps;

    /**
     * The duration of the current game play.
     */
    private long duration = 0;

    /**
     * Whether the board has customized image.
     */
    private boolean customized = false;

    /**
     * Return the current board.
     *
     * @return the board
     */
    SlidingTilesBoard getBoard() {
        return board;
    }

    /**
     * Getting the currently logged in user.
     *
     * @return the user of the gameplay
     */
    public User getUser() {
        return user;
    }

    /**
     * Getting the complexity of the board.
     *
     * @return the complexity of the board
     */
    int getComplexity() {
        return complexity;
    }

    /**
     * Return the number of swaps.
     *
     * @return the number of swaps
     */
    int getNumOfSwaps() {
        return numOfSwaps;
    }

    /**
     * Increase the number of swaps by one.
     */
    void incrementNumOfSwaps() {
        numOfSwaps++;
    }

    /**
     * Manage a new shuffled board, with nr NUM_ROWS and nc NUM_COLS
     *
     * @param user  the user of this game
     * @param compl The number of the rows and columns
     */
    SlidingTilesBoardManager(User user, int compl) {
        this.user = user;
        complexity = compl;
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = compl * compl;
        for (int tileNum = 0; tileNum != numTiles - 1; tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        tiles.add(new Tile(24)); // id = 24 -> Blank Tile.
        Collections.shuffle(tiles);
        while (!checkSolvable(new SlidingTilesBoard(tiles, compl))) {
            Collections.shuffle(tiles);
        }
        this.board = new SlidingTilesBoard(tiles, compl);
    }

    /**
     * Manage a new shuffled board, with nr NUM_ROWS and nc NUM_COLS
     *
     * @param user  the user of this game
     * @param compl The number of the rows and columns
     */
    SlidingTilesBoardManager(User user, int compl, boolean customized) {
        this.customized = customized;
        this.user = user;
        complexity = compl;
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = compl * compl;
        for (int tileNum = 0; tileNum != numTiles - 1; tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        tiles.add(new Tile(24)); // id = 24 -> Blank Tile.
        Collections.shuffle(tiles);
        while (!checkSolvable(new SlidingTilesBoard(tiles, compl))) {
            Collections.shuffle(tiles);
        }
        this.board = new SlidingTilesBoard(tiles, compl);

    }

    void setBoard(SlidingTilesBoard board){
        this.board = board;
    }

    /**
     * check if the board newly generated is solvable.
     * @param board newly generated board
     * @return if the board is solvable
     */
    boolean checkSolvable(SlidingTilesBoard board) {
        int inversions = 0;
        int blankrow = 0;
        for (int i = 0; i != complexity * complexity - 1; i++) {
            for (int j = i + 1; j != complexity * complexity; j++) {
                int Icol = i % complexity;
                int Irow = i / complexity;
                int Jcol = j % complexity;
                int Jrow = j / complexity;
                if (board.getTile(Irow, Icol).getId() == 25) {
                    blankrow = Irow;
                }
                if (board.getTile(Irow, Icol).compareTo(board.getTile(Jrow, Jcol)) > 0 && board.getTile(Irow, Icol).getId() !=25 ) {
                    inversions++;
                }
            }
        }
        if (complexity == 3 || complexity == 5) {
            return (inversions % 2 == 0);
        }
        else if (complexity == 4) {
            return (blankrow % 2 == 1 && inversions % 2 == 0) || (blankrow % 2 == 0 && inversions % 2 == 1);
        }else{return false;}
    }

    /**
     * check if the profile image is customized
     * @return if the profile image is customized
     */
    boolean isCustomized(){
        return this.customized;
    }

    /**
     * set the current elapsed time .
     *
     * @param duration The duration
     */
    void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * get the current elapsed time.
     *
     * @return the duration
     */
    long getDuration() {
        return this.duration;
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    boolean puzzleSolved() {
        Tile prev = board.getTile(0, 0);  // This is the first tile.
        for (Tile now : board) {
            if (now.compareTo(prev) < 0) {
                return false;
            } else {
                prev = now;
            }
        }
        // Check the last tile and the second last tile
        return board.getTile(complexity - 1,  complexity - 1).compareTo(prev) >= 0;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {

        int row = position / complexity;
        int col = position % complexity;
        int blankId = board.blank;  // Originally board.numTiles(). This is now a final int.
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == complexity - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == complexity - 1 ? null : board.getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    int touchMove(int position) {

        int row = position / complexity;
        int col = position % complexity;
        int blankId = board.blank;  // Originally board.numTiles(). This is now a final int.

        // 3 cases: 1.corner 2.edge 3.middle
        // 4 directions to check: up, down, left, right
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == complexity - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == complexity - 1 ? null : board.getTile(row, col + 1);

        if (above != null && above.getId() == blankId) {
            board.swapTiles(row, col, row - 1, col);
            return countPosition(row - 1, col);
        } else if (below != null && below.getId() == blankId) {
            board.swapTiles(row, col, row + 1, col);
            return countPosition(row + 1, col);
        } else if (left != null && left.getId() == blankId) {
            board.swapTiles(row, col, row, col - 1);
            return countPosition(row, col - 1);
        } else if (right != null && right.getId() == blankId) {
            board.swapTiles(row, col, row, col + 1);
            return countPosition(row, col + 1);
        }
        return -1;
    }

    /**
     * Return a position ranged 0 - the size of board - 1 based on row and col
     *
     * @param row the row
     * @param col the col
     * @return the position
     */
    private int countPosition(int row, int col) {
        return complexity * row + col;
    }
}
