package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leaderboard {
    private List<Score> scores;
    private String filename = "leaderboard.dat";

    public Leaderboard() {
        scores = new ArrayList<>();
        loadScores();
    }

    public void addScore(String playerName, int score) {
        scores.add(new Score(playerName, score));
        Collections.sort(scores); // Сортируем после добавления нового рекорда
        saveScores();
    }

    public List<Score> getScores() {
        return scores;
    }
    public void mergeDuplicateScores() {
        Map<String, Integer> playerScoreMap = new HashMap<>();
        List<Score> mergedScores = new ArrayList<>();

        for (Score score : scores) {
            String playerName = score.getPlayerName();
            int currentScore = score.getScore();

            if (playerScoreMap.containsKey(playerName)) {
                // Если игрок уже есть в карте, добавляем очки к его сумме
                playerScoreMap.put(playerName, playerScoreMap.get(playerName) + currentScore);
            } else {
                // Если игрока нет в карте, добавляем его и его очки
                playerScoreMap.put(playerName, currentScore);
            }
        }

        // Создаем новые объекты Score из карты
        for (Map.Entry<String, Integer> entry : playerScoreMap.entrySet()) {
            mergedScores.add(new Score(entry.getKey(), entry.getValue()));
        }

        // Сортируем объединенные рекорды
        Collections.sort(mergedScores);
        this.scores = mergedScores;
        saveScores();
    }

    public void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении таблицы лидеров: " + e.getMessage());
        }
    }

    public void loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            scores = (List<Score>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке таблицы лидеров: " + e.getMessage());
        }
    }
}