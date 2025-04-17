package controller;

import model.heroes.Hero;
import model.Player;
import model.map.GameMap;
import model.City;
import controller.AIController;
import view.MapView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerName; // Имя игрока
    private List<Player> players;
    private GameMap gameMap;
    private int mapWidth;
    private int mapHeight;
    private AIController aiController;
    private City playerCity; // Город игрока

    // Конструктор
    public GameSession(String playerName, GameMap gameMap, int mapWidth, int mapHeight) {
        this.playerName = playerName;
        this.gameMap = gameMap;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.players = new ArrayList<>();
    }

    // Геттеры и сеттеры для всех полей (необходимы для сохранения и загрузки)
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public AIController getAiController() {
        return aiController;
    }

    public void setAiController(AIController aiController) {
        this.aiController = aiController;
    }

    public City getPlayerCity() {
        return playerCity;
    }

    public void setPlayerCity(City playerCity) {
        this.playerCity = playerCity;
    }

    public void initializeGame(GameMap gameMap) {
        this.gameMap = gameMap;
        players = new ArrayList<>(); // Создаем список игроков

        Hero initialHero = new Hero(1, 1, "Саня");
        Player player1 = new Player("Player", initialHero, 1000);
        player1.setCurrentHero(initialHero);
        players.add(player1); // Добавляем игрока в список игроков

        Hero aiHero = new Hero(8, 8, "СаняИИ");
        Player player2 = new Player("AI", aiHero, 1000);
        player2.setCity(new City("ИИ Сити", 8, 8));
        player2.setCurrentHero(aiHero);
        players.add(player2);
    }
}
