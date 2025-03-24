package view;

import model.City;
import model.heroes.Hero;
import model.map.TerrainType;
import java.util.List;

public class MapView {
    public void displayMap(int heroX, int heroY, int aiHeroX, int aiHeroY, int mapWidth, int mapHeight, TerrainType[][] terrain, String[][] mapData, int castleX, int castleY, int enemyCastleX, int enemyCastleY, City city, List<Hero> heroes, Hero currentHero) {
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                boolean heroDisplayed = false;

                // Проверяем, есть ли герой в текущей клетке
                for (Hero hero : heroes) {
                    if (i == hero.getX() && j == hero.getY()) {
                        // Определяем, какой символ использовать для текущего героя
                        if (hero == currentHero) {
                            System.out.print("H "); // Текущий герой
                        } else {
                            System.out.print("h "); // Остальные герои
                        }
                        heroDisplayed = true;
                        break; // Прерываем цикл, так как герой уже отображен
                    }
                }

                if (!heroDisplayed) {
                    if (i == aiHeroX && j == aiHeroY) {
                        System.out.print("A "); // Отображаем AI-героя
                    } else if (i == city.getX() && j == city.getY()) {
                        System.out.print("C "); // Отображаем город
                    } else if (i == castleX && j == castleY) {
                        System.out.print("C "); // Отображаем замок
                    } else if (i == enemyCastleX && j == enemyCastleY) {
                        System.out.print("E "); // Отображаем вражеский замок
                    } else if (!mapData[i][j].equals(".")) {
                        System.out.print(mapData[i][j] + " "); // Отображаем ресурсы
                    } else {
                        if (terrain[i][j] == TerrainType.GRASS) {
                            System.out.print(". "); // Отображаем траву
                        } else {
                            System.out.print("~ "); // Отображаем воду
                        }
                    }
                }
            }
            System.out.println();
        }
    }
}