package fall2018.csc2017.gamecentre.games.game2048;

import org.junit.Test;

import fall2018.csc2017.gamecentre.User;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


public class Game2048ScoreManagerTest {
    private final User user = mock(User.class);

    @Test
    public void TestCalculateScore() {
        int complexity = 4;
        Game2048ScoreManager scoreManager = new Game2048ScoreManager(user, complexity);
        scoreManager.setScoreChange(100);
        assertEquals(100, scoreManager.calculateScore());
        scoreManager.increaseScoreBy(10);
        assertEquals(120, scoreManager.calculateScore());
        scoreManager.saveScore(2);
        int scorePoped = scoreManager.popScore();
        assertEquals(2, scorePoped);
        scoreManager.resetScores();
        assertTrue(scoreManager.isScoresEmpty());
    }
}
