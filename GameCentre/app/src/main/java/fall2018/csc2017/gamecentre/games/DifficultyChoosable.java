package fall2018.csc2017.gamecentre.games;

/**
 * The interface for a Starting Activity.
 * Some games might have different difficulties for users to choose.
 */
public interface DifficultyChoosable {
    /**
     *  The selector for user to choose difficulty. Probably a spinner.
     */
    void addDifficultySelector();
}
