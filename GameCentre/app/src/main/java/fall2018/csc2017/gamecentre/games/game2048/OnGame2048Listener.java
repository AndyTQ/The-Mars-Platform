package fall2018.csc2017.gamecentre.games.game2048;

/**
 * Interface for implementation of the on game listener for 2048
 * Since this game requires real time score update, we will implement this separately.
 */
interface OnGame2048Listener {
    /**
     * When the score is updated, do stuff
     *
     * @param score the score changed, to be displayed
     */
    void onScoreChange(int score);

    /**
     * Do things when the game is over
     */
    void onGameOver();

    /**
     * Do things when the game is won, i.e. 2048 is reached in our game...
     */
    void onGameWinning();
}
