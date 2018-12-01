package fall2018.csc2017.gamecentre.games.slidingtiles;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;

/**
 * The sliding tiles board.
 */
class SlidingTilesBoard extends Observable implements Serializable, Iterable<Tile> {
    /**
     * The number of rows.
     */
    private final int numRows;

    /**
     * The number of rows.
     */
    private final int numCols;

    /**
     * The tiles on the board in row-major order.
     */
    private final Tile[][] tiles;

    /**
     * The id of the blank tile.
     */
    final int blank = 25;

    @NonNull
    @Override
    public Iterator<Tile> iterator() {
        return new TileIterator();
    }

    /**
     * The iterator class for Board.
     */
    class TileIterator implements Iterator<Tile> {
        final int[] nextIndex = {0, 0};

        @Override
        public boolean hasNext() {
            if (nextIndex[0] < numRows - 1) {
                return nextIndex[1] <= numCols - 1;
            } else {
                return nextIndex[1] < numCols - 1;
            }
        }

        @Override
        public Tile next() {
            if (!hasNext()) {
                String msg = "Reached end of list";
                throw new NoSuchElementException(msg);
            }
            Tile result = getTile(nextIndex[0], nextIndex[1]);
            // change nextIndex
            if (nextIndex[1] < numCols - 1) {
                nextIndex[1]++;
            } else {
                nextIndex[0]++;
                nextIndex[1] = 0;
            }
            return result;
        }
    }

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == numRows * numCols
     *
     * @param tiles the tiles for the board
     */
    SlidingTilesBoard(List<Tile> tiles, int complexity) {
        Iterator<Tile> iter = tiles.iterator();
        this.numRows = complexity;
        this.numCols = complexity;
        this.tiles = new Tile[numRows][numCols];
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    void swapTiles(int row1, int col1, int row2, int col2) {
        Tile media = getTile(row1, col1);
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = media;
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        return "Board{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    int getNumRows() {
        return numRows;
    }

    int getNumCols() {
        return numCols;
    }

}
