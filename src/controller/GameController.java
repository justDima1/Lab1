package controller;

import model.City;
import model.Player;
import model.buildings.Building;
import model.buildings.Tavern;
import model.buildings.TownHall;
import model.heroes.Hero;
import model.map.GameMap;
import model.units.*;
import model.units.Stack;
import view.CityView;
import view.MapView;

import java.util.*;

public class GameController {
    private City playerCity;
    private Hero hero;
    private Hero aiHero;
    private MapView mapView;
    private int mapWidth;
    private int mapHeight;
    private Scanner scanner;
    private GameMap gameMap;
    private int currentPlayer;
    private Random random = new Random();
    private int movesPerTurn = 5;
    private int movesLeft = movesPerTurn;
    private BattleController battleController = new BattleController();
    private int turnsSinceLastCollection = 5;
    private CityView cityView;
    private Tavern tavern;
    List<Player> players;
    private List<Hero> heroes;
    private boolean playerTurnStarted = false;
    private Player player;
    private Hero currentHero;
    protected AIController aiController;
    private int turnsCount = 0;

    public GameController() {
        scanner = new Scanner(System.in);
        mapWidth = 10;
        mapHeight = 10;
        hero = new Hero(1, 1, "Саня");
        aiHero = new Hero(8, 8, "Саня");
        battleController = new BattleController();
        cityView = new CityView(scanner);

        for (int i = 0; i < 15; i++) {
            hero.getArmy().addUnit(new Pikeman());
        }
        for (int i = 0; i < 10; i++) {
            hero.getArmy().addUnit(new Archer());
        }
        for (int i = 0; i < 7; i++) {
            hero.getArmy().addUnit(new Swordsman());
        }
        for (int i = 0; i < 10; i++) {
            aiHero.getArmy().addUnit(new Pikeman());
        }
        for (int i = 0; i < 5; i++) {
            aiHero.getArmy().addUnit(new Archer());
        }
        for (int i = 0; i < 3; i++) {
            aiHero.getArmy().addUnit(new Swordsman());
        }
        mapView = new MapView();
        gameMap = new GameMap(mapWidth, mapHeight);
        currentPlayer = 1;
        playerCity = new City("Орешек", 0, 0);
    }
    public List<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

        public void handleHeroInCity() {
            Hero currentHero = players.get(0).getCurrentHero();
            cityView.viewHeroArmy(currentHero); //армия перед меню
            cityView.displayCityMenu(playerCity, currentHero, player);

            int choice = cityView.getChoice();
            switch (choice) {
                case 1:
                    cityView.viewBuildings(playerCity);
                    handleHeroInCity();
                    break;
                case 2:
                    cityView.buildBuilding(playerCity, currentHero);
                    if (playerCity.getBuildings().stream().anyMatch(building -> building.getClass().getSimpleName().equals("Stable"))) {
                        currentHero.setStableBonusTurnsLeft(5); //Используем currentHero
                        System.out.println("Бонус от конюшни активирован");
                    }
                    cityView.displayCityMenu(playerCity, currentHero, player);
                    handleHeroInCity();
                    break;
                case 3:
                    cityView.recruitUnits(playerCity, currentHero);
                    cityView.displayCityMenu(playerCity, currentHero, player);
                    handleHeroInCity();
                    break;
                case 4:
                    cityView.viewHeroArmy(currentHero);
                    handleHeroInCity();
                    break;
                case 5:
                    System.out.println("Выходим на свежий воздух...");
                    int exitX = 1;
                    int exitY = 1;
                    // Check if the exit coordinates are valid
                    if (exitX >= 0 && exitX < mapWidth && exitY >= 0 && exitY < mapHeight) {
                        String tile = gameMap.getMap()[exitX][exitY];
                        if (!tile.equals("T") && !tile.equals("X") && !(exitX == aiHero.getX() && exitY == aiHero.getY())) {
                            currentHero.setX(exitX);
                            currentHero.setY(exitY);
                            System.out.println("Hero exited to (" + exitX + ", " + exitY + ")");
                        } else {
                            System.out.println("The exit coordinates are not available.");
                        }
                    } else {
                        System.out.println("The exit coordinates are out of the map.");
                    }
                    break;
                case 6:
                    if (playerCity.hasTownHall()) {
                        TownHall townHall = playerCity.getTownHall();
                        if (currentHero.getGold() >= townHall.getUpgradeCost()) { //Используем currentHero
                            currentHero.setGold(currentHero.getGold() - townHall.getUpgradeCost()); //Используем currentHero
                            townHall.upgrade();
                            System.out.println("Кремль прокачен до " + townHall.getLevel() + "!");
                        } else {
                            System.out.println("Не хватает золота на Кремль.");
                        }
                    } else {
                        System.out.println("No TownHall.");
                    }
                case 7:
                    visitTavern();
                    break;
                default:
                    System.out.println("Invalid choice.");
                    handleHeroInCity();
            }
        }

    public void getAvailableExitCoordinates() {
        List<int[]> availableCoordinates = new ArrayList<>();
        int cityX = playerCity.getX();
        int cityY = playerCity.getY();
        int[][] potentialCoordinates = {
                {cityX - 1, cityY},
                {cityX + 1, cityY},
                {cityX, cityY - 1},
                {cityX, cityY + 1},
                {cityX - 1, cityY - 1},
                {cityX - 1, cityY + 1},
                {cityX + 1, cityY - 1},
                {cityX + 1, cityY + 1}
        };
        for (int[] coord : potentialCoordinates) {
            int x = coord[0];
            int y = coord[1];

            if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight) {
                String tile = gameMap.getMap()[x][y];
                if (!tile.equals("T") && !tile.equals("X") && !(x == aiHero.getX() && y == aiHero.getY())) {
                    availableCoordinates.add(new int[]{x, y});
                }
            }
        }
    }
    public void startGame() {
        players = new ArrayList<>(); // Создаем список игроков

        Hero initialHero = new Hero(1, 1, "Саня"); // Создаем первого героя и даем ему имя "Саня"
        Player player1 = new Player("Player", initialHero, 1000);
        players.add(player1); // Добавляем игрока в список игроков

        Hero aiHero = new Hero(8, 8, "СаняИИ"); // Создаем второго героя (AI) и даем ему имя "СаняИИ"
        Player player2 = new Player("AI", aiHero, 1000);
        player2.setCity(new City("ИИ Сити", 8, 8));
        players.add(player2);

        players.get(0).setCurrentHero(initialHero);
        players.get(1).setCurrentHero(aiHero);

        aiController = new AIController(mapWidth, mapHeight, this, players.get(1), gameMap);
        startPlayerTurn();

        while (players.get(0).getCurrentHero() != null && players.get(1).getCurrentHero() != null) {
            Hero currentHero = players.get(0).getCurrentHero();
            Hero currentAIHero = players.get(1).getCurrentHero(); //  Получаем текущего героя AI

            mapView.displayMap(currentHero.getX(), currentHero.getY(), currentAIHero.getX(), currentAIHero.getY(), mapWidth, mapHeight, gameMap.getTerrain(), gameMap.getMap(), 0, 0, 9, 9, playerCity, players.get(0).getHeroes(), currentHero);
            displayHeroStats();
            processInput();
        }
        System.out.println("Игра окончена!");
        if (players.get(0).getCurrentHero() == null) {
            System.out.println("ИИ победил!");
        } else {
            System.out.println("Игрок победил!");
        }
    }
    private void displayHeroStats() {
        Hero currentHero = players.get(0).getCurrentHero();
        movesLeft = currentHero.getMovesLeft();

        System.out.println("Золота в казне: " + currentHero.getGold());
        System.out.println("Гемов : " + currentHero.getGems());
        System.out.println("Осталось шагов до изнеможения: " + movesLeft);
        System.out.println("Enter command: (w, a, s, d, q, e, z, x, f, g, quit)");
    }
    /*private void displayAiHeroStats() {
        System.out.println("AI Gold: " + aiHero.getGold());
        System.out.println("AI Gems: " + aiHero.getGems());
    }*/
    private void displayAiArmy() {
        Hero aiHero = players.get(1).getCurrentHero();
        Map<String, Integer> unitCounts = new HashMap<>();

        aiHero.getArmy().getUnits().forEach(unit -> {
            String unitType = unit.getClass().getSimpleName();
            unitCounts.put(unitType, unitCounts.getOrDefault(unitType, 0) + 1);
        });

        System.out.println("Армия ИИ:");
        unitCounts.forEach((unitType, count) -> System.out.println("- " + unitType + ": " + count));
    }
    
    public void aiTurn() {

        if (currentPlayer == 2) {
            System.out.println("AI turn started");
            Hero aiHero = players.get(1).getCurrentHero();
            Hero playerHero = players.get(0).getCurrentHero();
            if (aiHero == null || playerHero == null) {
                System.out.println("Игра окончена!");
                return;
            }
            Random random = new Random();
            int dx = random.nextInt(3) - 1;
            int dy = random.nextInt(3) - 1;

            if (dx == 0 && dy == 0) {
                dx = 1;
            }
            if (moveAiHero(dx, dy, aiHero)) {
                System.out.println("ИИ пошел на (" + aiHero.getX() + ", " + aiHero.getY() + ")");
            }
            if (aiHero.getX() == playerHero.getX() && aiHero.getY() == playerHero.getY()) {
                System.out.println("Битва начинается!");
                battle(playerHero, aiHero);
            }
            System.out.println("AI turn finished");
            currentPlayer = 1;

            startPlayerTurn();
            playerTurnStarted = false;
        }
    }

    private boolean moveAiHero(int dx, int dy, Hero currentHero) {
        if (currentHero.getMovesLeft() > 0) {
            int newX = currentHero.getX() + dx;
            int newY = currentHero.getY() + dy;
            if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight) {
                String tile = gameMap.getMap()[newX][newY];
                if (!tile.equals("T") && !tile.equals("X")) {
                    if (tile.equals("G")) {
                        currentHero.setGold(currentHero.getGold() + 500);
                        gameMap.getMap()[newX][newY] = ".";
                    } else if (tile.equals("Z")) {
                        if (turnsSinceLastCollection >= 5) {
                            currentHero.setGems(currentHero.getGems() + 5);
                            turnsSinceLastCollection = 0;
                            System.out.println("Собрано 5 кристаллов с шахты!");
                        } else {
                            System.out.println("Шахта пока пуста.  " + (5 - turnsSinceLastCollection) + " ходов.");
                        }
                    }
                    currentHero.setX(newX);
                    currentHero.setY(newY);
                    System.out.println("ИИ передвинулся   (" + newX + ", " + newY + ")");
                    currentHero.setMovesLeft(currentHero.getMovesLeft() - 1);
                    return true;
                } else {
                    System.out.println("ИИ не может пройти.");
                    return false;
                }
            } else {
                System.out.println("ИИ не может пройти.");
                return false;
            }
        } else {
            System.out.println("ИИ устал.");
            return false;
        }
    }

    private void visitTavern() {
        System.out.println("\n--- Трактир ---");
        List<Building> buildings = playerCity.getBuildings();
        Tavern tavern = null;
        for (Building building : buildings) {
            if (building instanceof Tavern) {
                tavern = (Tavern) building;
                break;
            }
        }
        if (tavern == null) {
            System.out.println("В городе нет таверны.");
            return;
        }
        List<Hero> availableHeroes = tavern.getAvailableHeroes();
        if (availableHeroes.isEmpty()) {
            System.out.println("В таверне нет доступных героев.");
            return;
        }

        System.out.println("Доступные герои:");
        for (int i = 0; i < availableHeroes.size(); i++) {
            Hero hero = availableHeroes.get(i);
            String heroName = "";
            if(i == 0){
                heroName = "Соня";
            }
            else if(i == 1){
                heroName = "Вика";
            }

            System.out.println((i + 1) + ". " + heroName );
        }

        System.out.print("Выберите героя для покупки (или 0 для отмены): ");
        int choice = getChoice();

        if (choice > 0 && choice <= availableHeroes.size()) {
            Hero selectedHero = tavern.buyHero(choice - 1, players.get(0));
            if (selectedHero != null) {
                System.out.println("Вы купили героя!");
                if(choice - 1 == 0){
                    selectedHero.setName("Соня");
                }
                else if(choice - 1 == 1){
                    selectedHero.setName("Вика");
                }
                //   List<Hero> heroes = players.get(0).getHeroes();
                players.get(0).getHeroes().add(selectedHero);
                System.out.println("Теперь у вас " + players.get(0).getHeroes().size() + " героев");
            }
        }
    }

    private List<Stack> createUnitStacks(Army army) {
        List<Stack> stacks = new ArrayList<>();
        Map<Class<? extends Unit>, Integer> unitCounts = new HashMap<>();

        // Считаем количество юнитов каждого типа
        for (Unit unit : army.getUnits()) {
            Class<? extends Unit> unitClass = unit.getClass();
            unitCounts.put(unitClass, unitCounts.getOrDefault(unitClass, 0) + 1);
        }

        // Создаем стеки
        for (Map.Entry<Class<? extends Unit>, Integer> entry : unitCounts.entrySet()) {
            Class<? extends Unit> unitClass = entry.getKey();
            Integer count = entry.getValue();
            stacks.add(new Stack(unitClass, count));
        }
        return stacks;
    }

    public void battle(Hero hero1, Hero hero2) {
        BattleController battleController = new BattleController();
        System.out.println("Началась битва между " + hero1.getName() + " и " + hero2.getName() + "!");
        battleController.startBattle(hero1,hero2);
        if (!hero1.getArmy().getUnits().isEmpty() && hero2.getArmy().getUnits().isEmpty()) {
            players.get(1).setCurrentHero(null);
            System.out.println("Игрок проиграл");
        } else if (hero1.getArmy().getUnits().isEmpty() && !hero2.getArmy().getUnits().isEmpty()) {
            players.get(0).setCurrentHero(null);
            System.out.println("ИИ проиграл");
        }
    }

    public void processInput() {
        if (currentPlayer == 1) {
            Hero currentHero = null;
            Hero aiHero = null;
            try {
                currentHero = players.get(0).getCurrentHero(); // Получаем текущего героя
                aiHero = players.get(1).getCurrentHero(); // Получаем героя ИИ
            } catch (Exception e) {
                System.exit(0);
            }


            if (!playerTurnStarted) {
                startPlayerTurn();
                playerTurnStarted = true;
            }

            // Проверяем, остались ли герои в живых
            if (currentHero == null) {
                System.out.println("ИИ победил! Герой уничтожен.");
                return;
            }
            if (aiHero == null) {
                System.out.println("Герой победил! ИИ уничтожен.");
                return;
            }

            if (currentHero.getX() == playerCity.getX() && currentHero.getY() == playerCity.getY()) {
                handleHeroInCity();
            }

            if (currentHero.getMovesLeft() > 0) {
                try {
                    String command = scanner.nextLine();
                    boolean moved = false;

                    switch (command) {
                        case "w":
                            moved = moveHero(0, -1, currentHero);
                            break;
                        case "a":
                            moved = moveHero(-1, 0, currentHero);
                            break;
                        case "s":
                            moved = moveHero(0, 1, currentHero);
                            break;
                        case "d":
                            moved = moveHero(1, 0, currentHero);
                            break;
                        case "q":
                            moved = moveHero(-1, -1, currentHero);
                            break;
                        case "e":
                            moved = moveHero(1, -1, currentHero);
                            break;
                        case "z":
                            moved = moveHero(-1, 1, currentHero);
                            break;
                        case "x":
                            moved = moveHero(1, 1, currentHero);
                            break;
                        case "g":
                            switchHero();
                            break;
                        case "f":
                            currentPlayer = 2;
                            aiTurn();
                            break;
                        case "ai":
                            displayAiArmy();
                            break;
                        case "addmoney":
                            currentHero.setGold(currentHero.getGold() + 5000);
                            break;
                        case "quit":
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid command.");
                    }

                    if (moved) {
                        currentHero.setMovesLeft(currentHero.getMovesLeft() - 1);
                    }

                    displayHeroStats();

                } catch (Exception e) {
                    System.out.println("Error reading input: " + e.getMessage());
                } finally {
                    scanner = new Scanner(System.in);
                }
            } else {
                currentPlayer = 2;
                aiTurn();
            }
        }
    }


    public int getChoice() {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    private void switchHero() {
        List<Hero> heroes = players.get(0).getHeroes();
        if (heroes.size() < 2) {
            System.out.println("У вас недостаточно героев для переключения.");
            return;
        }

        System.out.println("\nВыберите героя для переключения:");
        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            String heroName = hero.getName() == null ? "Без имени" : hero.getName();
            if(i == 0){
                heroName = "Соня";
            }
            else if(i == 1){
                heroName = "Вика";
            }

            System.out.println((i + 1) + ". " + heroName + " Здоровье: " + hero.getGold() + ", Гемы: " + hero.getGems());
        }

        System.out.print("Введите номер героя: ");
        int choice = getChoice();

        if (choice > 0 && choice <= heroes.size()) {
            Hero selectedHero = heroes.get(choice - 1);
            players.get(0).setCurrentHero(selectedHero);

            System.out.println("Вы переключились на героя.");
        } else {
            System.out.println("Неверный выбор.");
        }
    }
    public boolean moveHero(int dx, int dy, Hero currentHero) {
        if (currentHero == null) {
            System.out.println("Нет текущего героя!");
            return false;
        }
        if (currentHero.getMovesLeft() > 0) {
            int newX = currentHero.getX() + dx;
            int newY = currentHero.getY() + dy;

            if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight) {
                String tile = gameMap.getMap()[newX][newY];
                if (!tile.equals("T") && !tile.equals("X")) {
                    if (tile.equals("G")) {
                        currentHero.setGold(currentHero.getGold() + 500);
                        gameMap.getMap()[newX][newY] = ".";
                    } else if (tile.equals("Z")) {
                        if (turnsSinceLastCollection >= 5) {
                            currentHero.setGems(currentHero.getGems() + 5);
                            turnsSinceLastCollection = 0;
                            System.out.println("Собрано 5 кристаллов с шахты!");
                        } else {
                            System.out.println("Шахта пока пуста. Осталось " + (5 - turnsSinceLastCollection) + " ходов.");
                        }
                    }

                    // Проверка столкновения с AI
                    Hero aiHero = players.get(1).getCurrentHero();
                    if (newX == aiHero.getX() && newY == aiHero.getY()) {
                        System.out.println("Битва начинается!");
                        battle(currentHero, aiHero);
                        return false; // Битва началась, передвижения не будет
                    }

                    currentHero.setX(newX);
                    currentHero.setY(newY);
                    System.out.println("Передвинулся на  (" + newX + ", " + newY + ")");
                    return true;
                } else {
                    System.out.println("Стой! Убьешься.");
                    return false;
                }
            } else {
                System.out.println("Стой! Убьешься.");
                return false;
            }
        } else {
            System.out.println("Герой устал и никуда не пойдет");
            return false;
        }
    }

    public void startPlayerTurn() {
        Hero currentHero = players.get(0).getCurrentHero();
        currentHero.setMovesLeft(5);

    }


    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        
    }

    public void setHero(Hero hero) {
    }

    public void handleHeroInCity(Hero hero, City playerCity, Scanner scanner) {
    }

    public Hero getHero() {
        return hero;
    }

    public City getPlayerCity() {
        return playerCity;
    }

    public void moveHeroTo(int x, int y) {
        hero.setX(x);
        hero.setY(y);
    }

    public void increaseTurnsSinceLastCollection() {
        turnsSinceLastCollection++;
    }
    public int getTurnsSinceLastCollection() {
        return turnsSinceLastCollection;
    }
    public void setTurnsSinceLastCollection(int turnsSinceLastCollection) {
        this.turnsSinceLastCollection = turnsSinceLastCollection;
    }
    public void processInputForTest(Hero hero) {
        // Сначала проверим, находится ли герой на клетке с шахтой
        if (hero.getX() >= 0 && hero.getX() < mapWidth && hero.getY() >= 0 && hero.getY() < mapHeight) {
            String tile = gameMap.getMap()[hero.getX()][hero.getY()];
            if (tile.equals("Z")) {
                // Теперь проверим, прошло ли достаточно времени с последнего сбора гемов
                if (turnsSinceLastCollection >= 5) {
                    // Сброс счетчика и начисление гемов
                    hero.setGems(hero.getGems() + 5);
                    turnsSinceLastCollection = 0;
                    System.out.println("Собрано 5 кристаллов с шахты!");
                } else {
                    // Информируем игрока о необходимости подождать
                    System.out.println("Шахта пока пуста. Осталось " + (5 - turnsSinceLastCollection) + " ходов.");
                }
            } else if (tile.equals("G")) {
                hero.setGold(hero.getGold() + 500);
                gameMap.getMap()[hero.getX()][hero.getY()] = ".";
            }
        } else {
            System.out.println("Герой не на карте или на недопустимой клетке.");
        }
    }
}