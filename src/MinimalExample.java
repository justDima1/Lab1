public class MinimalExample {

    public static void main(String[] args) {
        int width = 10;
        int height = 10;
        String[][] map = new String[width][height];

        // Заполняем карту травой
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = ".";
            }
        }

        // Размещаем замки
        map[0][0] = "C";
        map[width - 1][height - 1] = "E";

        // Размещаем героя
        int heroX = 1;
        int heroY = 1;

        // Отображаем карту
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == heroX && j == heroY) {
                    System.out.print("H ");
                } else {
                    System.out.print(map[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}
/*public void aiTurn() {
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
    public boolean moveAiHero(int dx, int dy, Hero currentHero) {
        int newX = currentHero.getX() + dx;
        int newY = currentHero.getY() + dy;

        if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight) {
            String tile = gameMap.getMap()[newX][newY];
            if (!tile.equals("T") && !tile.equals("X")) {

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
    }*/
