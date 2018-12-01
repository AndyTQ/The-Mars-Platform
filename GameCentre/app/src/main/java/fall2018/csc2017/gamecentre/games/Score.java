package fall2018.csc2017.gamecentre.games;

import android.support.annotation.NonNull;

/**
 * Each play's score.
 */
public class Score implements Comparable<Score> {
    /**
     * The numeric value of the score.
     */
    private int scoreNum;
    /**
     * the user's name
     */
    private String name;

    /**
     * The required empty constructor for firebase database.
     */
    public Score() {
    }

    /**
     * The constructor of Score class
     *
     * @param name     the name of the user
     * @param scoreNum the numeric value of the score.
     */
    public Score(String name, int scoreNum) {
        this.scoreNum = scoreNum;
        this.name = name;
    }

    /**
     * Getter for scoreNum.
     *
     * @return the score number.
     */
    public int getScoreNum() {
        return scoreNum;
    }

    @Override
    public int compareTo(@NonNull Score o) {
        //return 0 if equal
        //1 if passed greater than this
        //-1 if this greater than passed
        return Integer.compare(o.scoreNum, scoreNum);
    }

    /**
     * Getter for the score's corresponding player name.
     *
     * @return player name.
     */
    public String getName() {
        return name;
    }

}
