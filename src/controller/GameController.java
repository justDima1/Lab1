package controller;

import model.BattleMap;
import model.City;
import model.Player;
import model.buildings.*;
import model.heroes.Hero;
import model.map.GameMap;
import model.units.*;
import model.units.Stack;
import view.CityView;
import view.MapView;
import java.util.*;
import java.util.Random;

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
    private BattleController battleController;
    private int turnsSinceLastCollection = 5;
    private CityView cityView;
    private Tavern tavern;
    private List<Player> players;
    private List<Hero> heroes;
    private boolean playerTurnStarted = false;
    private Player player;
    private Hero currentHero;

    public GameController() {
        scanner = new Scanner(System.in);
        mapWidth = 10;
        mapHeight = 10;
        hero = new Hero(1, 1);
        aiHero = new Hero(8, 8);
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
    private void handleHeroInCity() {
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
                //System.out.println("\n--- Армия после мобилизации ---");
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
    private List<int[]> getAvailableExitCoordinates() {
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
        return availableCoordinates;
    }
    public void startGame() {
        players = new ArrayList<>(); // Создаем список игроков

        Hero initialHero = new Hero(1, 1); // Создаем первого героя
        Player player1 = new Player("Player", initialHero, 1000);
        players.add(player1); // Добавляем игрока в список игроков
        Player player2 = new Player("AI", aiHero, 1000);
        players.add(player2);
        players.get(0).setCurrentHero(initialHero);
        //players.get(0).getHeroes().add(initialHero);

        while (true) {
            Hero currentHero = players.get(0).getCurrentHero();
            List<Hero> heroes = players.get(0).getHeroes();
            mapView.displayMap(hero.getX(), hero.getY(), aiHero.getX(), aiHero.getY(), mapWidth, mapHeight, gameMap.getTerrain(), gameMap.getMap(), 0, 0, 9, 9, playerCity, heroes, currentHero);
            displayHeroStats();
            displayAiHeroStats();
            processInput();
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
    private void displayAiHeroStats() {
        System.out.println("AI Gold: " + aiHero.getGold());
        System.out.println("AI Gems: " + aiHero.getGems());
    }

    public void aiTurn() {
        currentPlayer = 2;
        System.out.println("AI turn started");
        Hero aiHero = players.get(1).getCurrentHero();
        // aiHero.setMovesLeft(5);
        Hero playerHero = players.get(0).getCurrentHero();
        if (aiHero == null) {
            System.out.println("Герой победил! ИИ уничтожен.");
            System.exit(0);
        }
        // Проверяем, не началась ли битва в начале хода ИИ
        if (aiHero.getX() == playerHero.getX() && aiHero.getY() == playerHero.getY()) {
            battle(playerHero, aiHero); // Start battle if heroes are on the same tile
            System.out.println("AI turn finished");
            currentPlayer = 1;

            Hero currentHero2 = players.get(0).getCurrentHero();
            List<Hero> heroes = players.get(0).getHeroes();
            //mapView.displayMap(hero.getX(), hero.getY(), aiHero.getX(), aiHero.getY(), mapWidth, mapHeight, gameMap.getTerrain(), gameMap.getMap(), 0, 0, 9, 9, playerCity, heroes, currentHero2);
            displayHeroStats();
            return; // Завершаем ход ИИ, если началась битва
        }
        // Если битва не началась, ИИ делает ход
        Random random = new Random();
        int dx = random.nextInt(3) - 1; // -1, 0 или 1
        int dy = random.nextInt(3) - 1; // -1, 0 или 1
        // Проверяем, что ИИ не пытается стоять на месте
        if (dx == 0 && dy == 0) {
            dx = 1;
        }

        moveAiHero(dx, dy, aiHero);
        aiHero.setMovesLeft(aiHero.getMovesLeft() - 1);

        System.out.println("AI turn finished");
        currentPlayer = 1;
        Hero currentHero = players.get(0).getCurrentHero();
        TownHall townHall = null;
        if (playerCity != null && playerCity.getBuildings() != null) {

            for (Building building : playerCity.getBuildings()) {
                if (building instanceof TownHall) {
                    townHall = (TownHall) building;
                    break;
                }
            }

            int goldIncome = 250;
            if (townHall != null) {
                goldIncome = townHall.getGoldIncome();
            }
            currentHero.setGold(currentHero.getGold() + goldIncome);
            System.out.println("Собрано " + goldIncome + " золота налогов с граждан");
        }

        Hero currentHero2 = players.get(0).getCurrentHero();
        List<Hero> heroes = players.get(0).getHeroes();
        mapView.displayMap(hero.getX(), hero.getY(), aiHero.getX(), aiHero.getY(), mapWidth, mapHeight, gameMap.getTerrain(), gameMap.getMap(), 0, 0, 9, 9, playerCity, heroes, currentHero2);
        displayHeroStats();
        startPlayerTurn();
        playerTurnStarted = false;
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
    public boolean moveAiHero(int dx, int dy, Hero currentHero) {
        int newX = currentHero.getX() + dx;
        int newY = currentHero.getY() + dy;

        if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight) {
            String tile = gameMap.getMap()[newX][newY];
            if (!tile.equals("T") && !tile.equals("X")) {
                  /*if (tile.equals("G")) {
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
                    }*/

                currentHero.setX(newX);
                currentHero.setY(newY);
                Hero currentHero2 = players.get(0).getCurrentHero();
                List<Hero> heroes = players.get(0).getHeroes();

                //mapView.displayMap(hero.getX(), hero.getY(), aiHero.getX(), aiHero.getY(), mapWidth, mapHeight, gameMap.getTerrain(), gameMap.getMap(), 0, 0, 9, 9, playerCity, heroes, currentHero2);
                System.out.println("пошел на  (" + newX + ", " + newY + ")");
                return true;
            } else {
                System.out.println("Стой! ушибешься");
                return false;
            }
        } else {
            System.out.println("Стой! ушибешься");
            return false;
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
    private void battle(Hero hero1, Hero hero2) {
        System.out.println("Начинается битва между героями!");
        Army army1 = hero1.getArmy();
        Army army2 = hero2.getArmy();
        String winner = null;
        Random random = new Random();
        boolean hero1First = random.nextBoolean();
        // 1. Создаем стеки юнитов (группы юнитов одного типа)
        List<Stack> stacks1 = createUnitStacks(army1);
        List<Stack> stacks2 = createUnitStacks(army2);

        while (!stacks1.isEmpty() && !stacks2.isEmpty()) {
            // 2. Выбираем атакующий и защищающийся стеки
            Stack attacker = stacks1.get(0);
            Stack defender = stacks2.get(0);
            // 3. Атака
            System.out.println(attacker.getUnitType().getSimpleName() + " атакует " + defender.getUnitType().getSimpleName() + " и наносит " + attacker.getTotalAttack() + " урона!");
            defender.takeDamage(attacker.getTotalAttack());
            // 4. Проверяем, выжил ли защищающийся стек
            if (defender.getSize() <= 0) {
                System.out.println(defender.getUnitType().getSimpleName() + " уничтожен!");
                stacks2.remove(0);
            } else {
                System.out.println("Осталось " + defender.getSize() + " " + defender.getUnitType().getSimpleName());
            }

            // 5. Меняем очереди
            stacks1.remove(0);
            if (!stacks2.isEmpty()) {
                stacks1.add(stacks2.remove(0));
            }
        }

        // 6. Определяем победителя
        if (stacks1.isEmpty()) {
            System.out.println("ИИ победил!");
            winner = "ИИ";
            hero2.setGold(hero2.getGold() + 500); // награждаем ИИ
            if (hero1 != null) {
                gameMap.getMap()[hero1.getX()][hero1.getY()] = "."; // очищаем клетку карты
            }
        } else if (stacks2.isEmpty()) {
            System.out.println("Герой победил!");
            winner = "Герой";
            hero1.setGold(hero1.getGold() + 500); // награждаем героя
            if (hero2 != null) {
                gameMap.getMap()[hero2.getX()][hero2.getY()] = "."; // очищаем клетку карты
            }
        } else {
            System.out.println("Ничья!");
            winner = null;
        }
        // Завершаем игру, если кто-то проиграл
        if (winner != null) {
            if (winner.equals("Герой")) {
                aiHero = null; // убираем героя ИИ
                System.out.println("ИИ был уничтожен.");
                System.exit(0);
            } else if (winner.equals("ИИ")) {
                hero = null; // убираем героя игрока
                System.out.println("Герой был уничтожен.");
                System.exit(0);
            }
        }
    }
    public void processInput() {
        if (currentPlayer == 1) {
            //System.out.println("processInput() called");
            //System.out.println("currentPlayer = " + currentPlayer);
            Hero currentHero = players.get(0).getCurrentHero(); //Получаем текущего героя

            if (!playerTurnStarted) {
                startPlayerTurn();
                playerTurnStarted = true;
            }

            if (aiHero == null && hero == null) {
                System.out.println("Игра окончена! Ничья.");
                System.exit(0);
            }
            if (aiHero == null) {
                System.out.println("Герой победил! ИИ уничтожен.");
                System.exit(0);
            }
            if (hero == null) {
                System.out.println("ИИ победил! Герой уничтожен.");
                System.exit(0);
            }
            if (currentHero.getX() == playerCity.getX() && currentHero.getY() == playerCity.getY()) {
                currentHero = players.get(0).getCurrentHero();
                handleHeroInCity();
            }
            if (currentHero.getMovesLeft() > 0) {
                try {
                    String command = scanner.nextLine();
                    //currentHero.setMovesLeft(currentHero.getMovesLeft() - 1);
                    boolean moved = false;
                    switch (command) {
                        case "w":
                            moved = moveHero(-1, 0, currentHero);
                            break;
                        case "a":
                            moved = moveHero(0, -1, currentHero);
                            break;
                        case "s":
                            moved = moveHero(1, 0, currentHero);
                            break;
                        case "d":
                            moved = moveHero(0, 1, currentHero);
                            break;
                        case "q":
                            moved = moveHero(-1, -1, currentHero);
                            break;
                        case "e":
                            moved = moveHero(-1, 1, currentHero);
                            break;
                        case "z":
                            moved = moveHero(1, -1, currentHero);
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
                        currentHero.setMovesLeft(currentHero.getMovesLeft()-1);
                    }

                    if (currentHero.getX() == aiHero.getX() && currentHero.getY() == aiHero.getY()) {
                        System.out.println("Битва начинается!");
                        System.out.println("Координаты героя: (" + currentHero.getX() + ", " + currentHero.getY() + ")");
                        System.out.println("Координаты ИИ: (" + aiHero.getX() + ", " + aiHero.getY() + ")");
                        battle(currentHero, aiHero);
                        return;
                    }
                    Hero currentHero2 = players.get(0).getCurrentHero();
                    List<Hero> heroes = players.get(0).getHeroes();
                    displayHeroStats();

                    if (players.get(0).getCurrentHero() == null) {
                        System.out.println("ИИ победил! Герой уничтожен");
                        System.exit(0); // End
                    }

                    if (aiHero == null) {
                        System.out.println("Герой победил! ИИ уничтожен.");
                        System.exit(0); // End
                    }

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
    private boolean moveHero(int dx, int dy, Hero currentHero) {
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
}