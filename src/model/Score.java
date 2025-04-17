package model;

import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable {
    private String playerName;
    private int score;

    public Score(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Score other) {
        // Сортируем по убыванию очков
        return Integer.compare(other.score, this.score);
    }
}