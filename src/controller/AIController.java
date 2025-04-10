package controller;

import model.map.GameMap;
import model.heroes.Hero;
import model.units.Archer;
import model.units.Pikeman;
import model.units.Swordsman;
import model.Player;

import java.util.Random;

public class AIController {
    private final int mapWidth;
    private final int mapHeight;
    private final GameController gameController; // Теперь ссылка на GameController
    private Hero aiHero;
    private final GameMap gameMap;
    private final Random random = new Random();
    private int turnsSinceLastReinforcement = 0;
    private final Player aiPlayer;

    public AIController(int mapWidth, int mapHeight, GameController gameController, Player aiPlayer, GameMap gameMap) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.gameController = gameController;
        this.aiHero = aiPlayer.getCurrentHero();
        this.gameMap = gameMap;
        this.aiPlayer = aiPlayer;
    }

    public void aiTurn() {
        turnsSinceLastReinforcement++;
        if (turnsSinceLastReinforcement >= 5) {
            addUnitsToArmy();
            turnsSinceLastReinforcement = 0;
        }

        if (aiHero != null) { // Проверка, что aiHero не null
            moveAiHero();
            recruitUnits();
        }
    }

    public void addUnitsToArmy() {
        //  добавляем 5 пикинеров и 3 лучника
        for (int i = 0; i < 8; i++) {
            aiHero.getArmy().addUnit(new Pikeman());
        }
        for (int i = 0; i < 5; i++) {
            aiHero.getArmy().addUnit(new Archer());
        }
        for (int i = 0; i < 2; i++) {
            aiHero.getArmy().addUnit(new Swordsman());
        }
        System.out.println("В армию " + aiHero.getName() + " добавлено подкрепление!");
    }

    private void moveAiHero() {
        int dx = random.nextInt(3) - 1; // -1, 0, 1
        int dy = random.nextInt(3) - 1; // -1, 0, 1

        if (aiHero.getMovesLeft() > 0) {
            int newX = aiHero.getX() + dx;
            int newY = aiHero.getY() + dy;

            if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight) {
                String tile = gameMap.getMap()[newX][newY];
                if (!tile.equals("T") && !tile.equals("X")) {
                    if (tile.equals("G")) {
                        aiHero.setGold(aiHero.getGold() + 500);
                        gameMap.getMap()[newX][newY] = ".";
                    } else if (tile.equals("Z")) {
                        aiHero.setGems(aiHero.getGems() + 5);
                    }

                    aiHero.setX(newX);
                    aiHero.setY(newY);
                    aiHero.setMovesLeft(aiHero.getMovesLeft() - 1);
                    System.out.println("AI Hero moved to (" + newX + ", " + newY + ")");
                } else {
                    aiHero.setMovesLeft(aiHero.getMovesLeft() - 1);
                }
            } else {
                aiHero.setMovesLeft(aiHero.getMovesLeft() - 1);
            }
        }
        //  Дополнительный ход если остались ходы
        if (aiHero.getMovesLeft() > 0) {
            moveAiHero();
        }
    }

    private void recruitUnits() {
        int gold = aiHero.getGold();
        if (gold >= 200) {
            int pikemanCost = 100;
            int archerCost = 150;
            int swordsmanCost = 200;

            while (gold >= swordsmanCost) {
                aiHero.getArmy().addUnit(new Swordsman());
                gold -= swordsmanCost;
            }
            while (gold >= archerCost) {
                aiHero.getArmy().addUnit(new Archer());
                gold -= archerCost;
            }
            while (gold >= pikemanCost) {
                aiHero.getArmy().addUnit(new Pikeman());
                gold -= pikemanCost;
            }

            aiHero.setGold(gold);
        }
    }
    private void checkAndStartBattle(Hero aiHero) {
        //  Проверяем, находится ли герой ИИ в той же клетке, что и герой игрока
        Hero playerHero = gameController.getPlayers().get(0).getCurrentHero();
        if (aiHero.getX() == playerHero.getX() && aiHero.getY() == playerHero.getY()) {
            gameController.battle(playerHero, aiHero);
        }
    }
    public Hero getAiHero() {
        return aiHero;
    }
}