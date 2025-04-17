package controller;

import model.City;
import model.heroes.Hero;
import model.map.GameMap;
import model.Player;
import model.units.Archer;
import model.units.Pikeman;
import model.units.Swordsman;

import java.util.Random;

public class AIController {
    private int mapWidth;
    private int mapHeight;
    private GameController gameController;
    private Player aiPlayer;
    private GameMap gameMap;
    private Random random = new Random();
    private int turnsSinceLastRecruit = 0;

    public AIController(int mapWidth, int mapHeight, GameController gameController, Player aiPlayer, GameMap gameMap) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.gameController = gameController;
        this.aiPlayer = aiPlayer;
        this.gameMap = gameMap;
    }

    public void recruitUnits() {
        turnsSinceLastRecruit++;
        // Условие для найма юнитов только раз в 5 ходов
        if (turnsSinceLastRecruit >= 5) {
            int gold = aiPlayer.getGold().getAmount();
            if (gold >= 200) {
                int pikemanCost = 100;
                int archerCost = 150;
                int swordsmanCost = 200;

                while (gold >= swordsmanCost) {
                    aiPlayer.addUnit(new Swordsman());
                    gold -= swordsmanCost;
                    aiPlayer.getGold().setAmount(gold);
                }
                while (gold >= archerCost) {
                    aiPlayer.addUnit(new Archer());
                    gold -= archerCost;
                    aiPlayer.getGold().setAmount(gold);
                }
                while (gold >= pikemanCost) {
                    aiPlayer.addUnit(new Pikeman());
                    gold -= pikemanCost;
                    aiPlayer.getGold().setAmount(gold);
                }
                turnsSinceLastRecruit = 0; // Сбрасываем счетчик после вербовки
            }
            System.out.println("addUnitsToArmy() вызывается!");
            System.out.println("В армию СаняИИ добавлено подкрепление!");

        }
    }

    private void applyCityIncome(City city, Hero hero) {
        int income = city.getGoldIncome();
        // Тут должен быть player а не hero
        aiPlayer.getGold().setAmount(aiPlayer.getGold().getAmount() + income);
        System.out.println("Начислен доход от города: " + income + " золота.");
    }

    public void moveAiHero() {
        // Логика перемещения героя (пример)
        int dx = 0;
        int dy = 0;
        while (dx == 0 && dy == 0) {
            dx = random.nextInt(3) - 1; // -1, 0, 1
            dy = random.nextInt(3) - 1; // -1, 0, 1
        }
        moveAiHero(dx, dy, aiPlayer.getCurrentHero());
    }

    private boolean moveAiHero(int dx, int dy, Hero currentHero) {
        if (currentHero.getMovesLeft() > 0) {
            int newX = currentHero.getX() + dx;
            int newY = currentHero.getY() + dy;
            if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight) {
                //String tile = gameMap.getMap()[newY][newX];
                //if (!tile.equals("T") && !tile.equals("X")) {
                currentHero.setX(newX);
                currentHero.setY(newY);
                currentHero.setMovesLeft(currentHero.getMovesLeft() - 1);
                return true;
                //} else {
                //    return false;
                //}
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}