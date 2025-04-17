package Tests;
import model.Leaderboard;
import model.Score;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LeaderboardTest {

    @Test
    void testAddScore() {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.addScore("TestUser", 100);
        List<Score> scores = leaderboard.getScores();
    }

    @Test
    void testGetScoresSorted() {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.addScore("User1", 50);
        leaderboard.addScore("User2", 100);
        List<Score> scores = leaderboard.getScores();
    }

    @Test
    void testMergeDuplicateScores() {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.addScore("TestUser", 50);
        leaderboard.addScore("TestUser", 100);
        leaderboard.mergeDuplicateScores();
        List<Score> scores = leaderboard.getScores();
    }

    @Test
    void testSaveAndLoadScores() {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.addScore("User1", 50);
        leaderboard.saveScores();
        Leaderboard newLeaderboard = new Leaderboard();
        newLeaderboard.loadScores();
        List<Score> scores = newLeaderboard.getScores();

    }
}