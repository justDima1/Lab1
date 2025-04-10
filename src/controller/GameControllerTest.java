package controller;

import model.City;
import model.Player;
import model.heroes.Hero;
import model.map.GameMap;
import model.units.Archer;
import model.units.Pikeman;
import model.units.Swordsman;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controller.GameController;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest  {
    private GameController gameController;
    private GameMap gameMap;
    private Hero hero;
    private City playerCity;
    private AIController aiController;
    private Player player1;
    private Player player2;
    private List<Player> players;
    private Hero initialHero;
    private Hero aiHero;
    private final int mapWidth = 10;
    private final int mapHeight = 10;
    private Scanner scannerMock;
    private GameController mapView;
    private City currentHero;

    @BeforeEach
    void setUp() {
        initialHero = new Hero(1, 1, "TestHero"); // Создаем героя
        aiHero = new Hero(8, 8, "AIHero"); // Создаем AI героя

        player1 = new Player("Player", initialHero, 1000);
        player2 = new Player("AI", aiHero, 1000);

        playerCity = new City("TestCity", 0, 0);
        gameMap = new GameMap(mapWidth, mapHeight);
        gameController = new GameController();
        gameController.setGameMap(gameMap);

        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        gameController.setPlayers(players);
        gameController.getPlayerCity().setX(1);
        gameController.getPlayerCity().setY(1);
    }

    @Test
    public void startGameTest() {
        GameController gameController = new GameController();
        gameController.startGame();
        assertNotNull(gameController.getPlayers(), "Список players не должен быть null");
        assertFalse(gameController.getPlayers().isEmpty(), "Список players не должен быть пустым");
        assertEquals(2, gameController.getPlayers().size(), "В списке players должно быть 2 игрока");
        assertNotNull(gameController.getPlayers().get(0).getCurrentHero(), "У первого игрока должен быть герой");
        assertEquals(1, gameController.getPlayers().get(0).getCurrentHero().getX(), "X координата героя должна быть 1");
        assertEquals(1, gameController.getPlayers().get(0).getCurrentHero().getY(), "Y координата героя должна быть 1");
        assertNotNull(gameController.getPlayers().get(1).getCurrentHero(), "У второго игрока (AI) должен быть герой");
        assertEquals(8, gameController.getPlayers().get(1).getCurrentHero().getX(), "X координата героя AI должна быть 8");
        assertEquals(8, gameController.getPlayers().get(1).getCurrentHero().getY(), "Y координата героя AI должна быть 8");
        assertNotNull(gameController.aiController, "aiController не должен быть null");
    }
    @Test
    void handleHeroInCityTest() {
        gameController.getPlayerCity().setX(initialHero.getX());
        gameController.getPlayerCity().setY(initialHero.getY());
        int goldBefore = initialHero.getGold();

        gameController.handleHeroInCity();

        assertTrue(initialHero.getArmy().getUnits().size() > 0 || initialHero.getGold() > goldBefore);
    }
    @Test
    public void testHeroMovesToCity() {
        Hero hero = gameController.getPlayers().get(0).getCurrentHero();
        gameController.moveHeroTo(playerCity.getX(), playerCity.getY());
        assertEquals(playerCity.getX(), hero.getX());
        assertEquals(playerCity.getY(), hero.getY());
        System.out.println( " Герой находится на клетке замка!");
    }

    @Test
    public void battleTest() {
        List<Player> players = new ArrayList<>(); // Создаем список игроков
        Hero hero1 = new Hero(1, 1, "Тестовый герой 1");
        Hero hero2 = new Hero(8, 8, "Тестовый герой 2");
        Player player1 = new Player("Player", hero1, 1000);
        players.add(player1); // Добавляем игрока в список игроков
        Player player2 = new Player("AI", hero2, 1000);
        players.add(player2);
        //Добавляем тестовых юнитов
        for (int i = 0; i < 15; i++) {
            hero1.getArmy().addUnit(new Pikeman());
        }
        for (int i = 0; i < 10; i++) {
            hero1.getArmy().addUnit(new Archer());
        }
        for (int i = 0; i < 7; i++) {
            hero1.getArmy().addUnit(new Swordsman());
        }
        for (int i = 0; i < 10; i++) {
            hero2.getArmy().addUnit(new Pikeman());
        }
        for (int i = 0; i < 5; i++) {
            hero2.getArmy().addUnit(new Archer());
        }
        for (int i = 0; i < 3; i++) {
            hero2.getArmy().addUnit(new Swordsman());
        }

        BattleController battleController = new BattleController();
        System.out.println("Началась битва между " + hero1.getName() + " и " + hero2.getName() + "!");
        battleController.startBattle(hero1, hero2);
        if (!hero1.getArmy().getUnits().isEmpty() && hero2.getArmy().getUnits().isEmpty()) {
            player2.setCurrentHero(null);
            System.out.println("Игрок победил");
        } else if (hero1.getArmy().getUnits().isEmpty() && !hero2.getArmy().getUnits().isEmpty()) {
            player1.setCurrentHero(null);
            System.out.println("ИИ победил");
        }
    }
    @Test
    public void testCollectGold() {
        // Создаем героя
        Hero hero = new Hero(1, 1, "TestHero");

        // Создаем GameMap (предполагается, что он уже инициализирован в GameController)
        GameMap gameMap = new GameMap(10, 10); //  Замените 10 на желаемый размер карты

        // Устанавливаем клетку с золотом
        int goldX = 2;
        int goldY = 2;
        gameMap.getMap()[goldX][goldY] = "G";

        // Запоминаем начальное количество золота
        int initialGold = hero.getGold();

        // Моделируем перемещение героя на клетку с золотом
        hero.setX(goldX);
        hero.setY(goldY);
        if (gameMap.getMap()[hero.getX()][hero.getY()].equals("G")) {
            hero.setGold(hero.getGold() + 500);
            gameMap.getMap()[hero.getX()][hero.getY()] = ".";
            System.out.println("Герой собрал золото!");
            System.out.println("Текущий баланс золота: " + hero.getGold());
        }

        // Проверяем, что количество золота увеличилось
        assertTrue(hero.getGold() > initialGold, "Золото не было собрано!");
        assertEquals(initialGold + 500, hero.getGold(), "Баланс золота не увеличился на 500!");
    }

    @Test
    void testCollectGemsFromMine() {
        GameController gameController = new GameController();
        List<Player> players = new ArrayList<>();

        Hero hero = new Hero(1, 1, "TestHero");
        Player player = new Player("TestPlayer", hero, 100);
        players.add(player);
        gameController.setPlayers(players);
        gameController.setTurnsSinceLastCollection(5);

        Hero currentHero = gameController.getPlayers().get(0).getCurrentHero();

        if (currentHero != null) {
            int initialGems = hero.getGems();
            int initialX = hero.getX();
            int initialY = hero.getY();

            // Подготовка к сбору гемов
            currentHero.setX(3);
            currentHero.setY(3);
            gameController.getGameMap().getMap()[3][3] = "Z";

            // Устанавливаем координаты героя в соответствие с шахтой
            currentHero.setX(3);
            currentHero.setY(3);
            gameController.getGameMap().getMap()[3][3] = "Z";

            // Собираем гемы в первый раз
            System.out.println("Собираем 5 гемов");

            // Симулируем ввод команды
            gameController.processInputForTest(hero);

            int gemsAfterFirstTry = hero.getGems();
            System.out.println("Собрано гемов: " + (gemsAfterFirstTry - initialGems)); // Выводим количество собранных гемов

            assertTrue(gemsAfterFirstTry > initialGems, "Гемы не были собраны при первой попытке.");
            assertEquals(5, gemsAfterFirstTry - initialGems, "Неверное количество гемов после первой попытки.");
            assertEquals(0, gameController.getTurnsSinceLastCollection(), "Счетчик ходов не сбросился после сбора.");

            // Возвращаем героя на исходную позицию и очищаем шахту
            currentHero.setX(initialX);
            currentHero.setY(initialY);
            gameController.getGameMap().getMap()[3][3] = ".";
            gameController.setTurnsSinceLastCollection(0);

            // Пропускаем 5 ходов
            System.out.println("Пропускаем 5 ходов...");
            for (int i = 0; i < 5; i++) {
                gameController.increaseTurnsSinceLastCollection();
                System.out.println("Прошло ходов: " + (i + 1)); // Выводим номер хода
                System.out.println("Следующий сбор гемов через " + (5 - gameController.getTurnsSinceLastCollection()) + " ходов."); // Выводим счетчик ходов
            }

            // Снова собираем гемы
            currentHero.setX(3);
            currentHero.setY(3);
            gameController.getGameMap().getMap()[3][3] = "Z";

            System.out.println("Собираем гемы во второй раз...");
            gameController.processInputForTest(hero);

            int gemsAfterSecondTry = hero.getGems();
            System.out.println("Собрано гемов: " + (gemsAfterSecondTry - gemsAfterFirstTry)); // Выводим количество собранных гемов

            assertTrue(gemsAfterSecondTry > gemsAfterFirstTry, "Гемы не были собраны при повторной попытке.");
            assertEquals(5, gemsAfterSecondTry - gemsAfterFirstTry, "Неверное количество гемов после второй попытки.");

            // Возвращаем героя на исходную позицию и очищаем шахту
            currentHero.setX(initialX);
            currentHero.setY(initialY);
            gameController.getGameMap().getMap()[3][3] = ".";
        } else {
            System.err.println("Hero is null, test cannot proceed."); // Выводим сообщение об ошибке, если hero == null
        }
    }
}