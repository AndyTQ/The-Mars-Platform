package fall2018.csc2017.gamecentre.games.slidingtiles;

import android.support.annotation.NonNull;
import java.io.Serializable;
import fall2018.csc2017.gamecentre.R;

/**
 * A Tile in a sliding tiles puzzle.
 */
public class Tile implements Comparable<Tile>, Serializable {

    /**
     * The background id to find the tile image.
     */
    private final int background;

    /**
     * The unique id.
     */
    private final int id;

    /**
     * Return the background id.
     *
     * @return the background id
     */
    public int getBackground() {
        return background;
    }

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    /**
     * A Tile with id and background. The background may not have a corresponding image.
     *
     * @param id         the id
     * @param background the background
     */
    Tile(int id, int background) {
        this.id = id;
        this.background = background;
    }

    /**
     * A tile with a background id; look up and set the id.
     * THIS IS A NEW VERSION. BELOW IS THE OLD VERSION
     *
     * @param backgroundId The id of the background
     */
    Tile(int backgroundId) {
        id = backgroundId + 1;
        switch (backgroundId + 1) {
            case 1:
                background = R.drawable.square_tile_1;
                break;
            case 2:
                background = R.drawable.square_tile_2;
                break;
            case 3:
                background = R.drawable.square_tile_3;
                break;
            case 4:
                background = R.drawable.square_tile_4;
                break;
            case 5:
                background = R.drawable.square_tile_5;
                break;
            case 6:
                background = R.drawable.square_tile_6;
                break;
            case 7:
                background = R.drawable.square_tile_7;
                break;
            case 8:
                background = R.drawable.square_tile_8;
                break;
            case 9:
                background = R.drawable.square_tile_9;
                break;
            case 10:
                background = R.drawable.square_tile_10;
                break;
            case 11:
                background = R.drawable.square_tile_11;
                break;
            case 12:
                background = R.drawable.square_tile_12;
                break;
            case 13:
                background = R.drawable.square_tile_13;
                break;
            case 14:
                background = R.drawable.square_tile_14;
                break;
            case 15:
                background = R.drawable.square_tile_15;
                break;
            case 16:
                background = R.drawable.square_tile_16;
                break;
            case 17:
                background = R.drawable.square_tile_17;
                break;
            case 18:
                background = R.drawable.square_tile_18;
                break;
            case 19:
                background = R.drawable.square_tile_19;
                break;
            case 20:
                background = R.drawable.square_tile_20;
                break;
            case 21:
                background = R.drawable.square_tile_21;
                break;
            case 22:
                background = R.drawable.square_tile_22;
                break;
            case 23:
                background = R.drawable.square_tile_23;
                break;
            case 24:
                background = R.drawable.square_tile_24;
                break;
            case 25:
                background = R.drawable.square_tile_blank;
                break;
            default:
                background = R.drawable.square_tile_blank;
        }
    }

    @Override
    public int compareTo(@NonNull Tile o) {
        return this.id - o.id;
    }
}
