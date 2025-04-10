package view;

import controller.GameController;
import model.City;
import model.Player;
import model.buildings.*;
import model.heroes.Hero;
import model.map.GameMap;
import model.units.*;

import java.util.*;

public class CityView {
    private int mapWidth;
    private int mapHeight;
    private GameController gameController;
    private GameMap gameMap;
    private Random random = new Random();
    private Hero aiHero;

    private Scanner scanner;
    //private Hero currentHero;
    private Player player;

    public CityView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void displayCityMenu(City city, Hero currentHero, Player player) { // Добавляем Player player
        System.out.println("\n--- " + city.getName() + " ---");
        System.out.println("Золота в казне: " + currentHero.getGold());
        System.out.println("Gems: " + currentHero.getGems());
        System.out.println("Income: " + city.getGoldIncome());
        System.out.println("\nЗдания:");

        List<Building> buildings = city.getBuildings();
        for (int i = 0; i < buildings.size(); i++) {
            Building building = buildings.get(i);
            String buildingInfo = (i + 1) + ". " + building.getName();
            if (building instanceof TownHall) {
                TownHall townHall = (TownHall) building;
                buildingInfo += " (Уровень: " + townHall.getLevel() + ", Доход: " + city.getGoldIncome() + ")";
            }
            System.out.println(buildingInfo);
        }

        System.out.println("\nActions:");
        System.out.println("1. Посмотреть на город");
        System.out.println("2. Построить здания");
        System.out.println("3. Нанять новых рекрутов");
        System.out.println("4. Посмотреть свою банду");
        System.out.println("5. Выйти на улицу");
        System.out.println("6. Прокачать Кремль (Нужно: 500)");
        System.out.println("7. Поситить Трактир");
        System.out.print("Ваш выбор: ");
    }

    public int getChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (Exception e) {
            System.out.println("Invalid input.");
            scanner.nextLine(); // Consume invalid input
            return -1;
        }
    }

    public void viewBuildings(City city) {
        System.out.println("\n--- Зданий в  " + city.getName() + " ---");
        for (int i = 0; i < city.getBuildings().size(); i++) {
            System.out.println((i + 1) + ". " + city.getBuildings().get(i));
        }
    }

    public void buildBuilding(City city, Hero currentHero) {
        displayAvailableBuildings();
        int choice = getChoice();
        if (choice > 0 && choice <= BuildingType.values().length) {
            BuildingType selectedBuilding = BuildingType.values()[choice - 1];

            if (currentHero.getGold() >= selectedBuilding.getCostGold() && currentHero.getGems() >= selectedBuilding.getCostGems()) { //Используем currentHero
                currentHero.setGold(currentHero.getGold() - selectedBuilding.getCostGold()); //Используем currentHero
                currentHero.setGems(currentHero.getGems() - selectedBuilding.getCostGems()); //Используем currentHero

                city.addBuilding(BuildingFactory.createBuilding(selectedBuilding));
                System.out.println(selectedBuilding.getName() + " constructed successfully!");
            } else {
                System.out.println("Недостаточно золота и/или гемов для строительства " + selectedBuilding.getName());
            }
        } else if (choice != 0) {
            System.out.println("Неверный выбор.");
        }
    }
    public void recruitUnits(City city, Hero currentHero) {
        List<RecruitBuilding> recruitBuildings = city.getRecruitBuildings();
        if (recruitBuildings.isEmpty()) {
            System.out.println("Нет зданий");
            return;
        }
        System.out.println("\n- Доступные юниты -");
        int buildingIndex = 1;
        for (RecruitBuilding recruitBuilding : recruitBuildings) {
            System.out.println(buildingIndex + ". " + recruitBuilding.getClass().getSimpleName() + ":");
            List<Class<? extends Unit>> availableUnits = recruitBuilding.getAvailableUnits();
            for (int i = 0; i < availableUnits.size(); i++) {
                Class<? extends Unit> unitClass = availableUnits.get(i);
                int cost = recruitBuilding.getRecruitCost(unitClass);
                System.out.println("   " + (i + 1) + ". " + unitClass.getSimpleName() + " (Gold: " + cost + ")");
            }
            buildingIndex++;
        }
        System.out.print("Выберите здание ( 0 для отмены): ");
        int buildingChoice = getChoice();
        if (buildingChoice > 0 && buildingChoice <= recruitBuildings.size()) {
            RecruitBuilding selectedBuilding = recruitBuildings.get(buildingChoice - 1);
            List<Class<? extends Unit>> availableUnits = selectedBuilding.getAvailableUnits();
            System.out.print("Выберите юнита (или 0 для отмены): ");
            int unitChoice = getChoice();
            if (unitChoice > 0 && unitChoice <= availableUnits.size()) {
                Class<? extends Unit> selectedUnitClass = availableUnits.get(unitChoice - 1);
                int unitCost = selectedBuilding.getRecruitCost(selectedUnitClass);
                System.out.print("Введите количество ( 0 для отмены): ");
                int unitCount = getChoice();
                int totalCost = unitCount * unitCost;
                if (selectedUnitClass.equals(Angel.class)) {
                    if (currentHero.getGold() >= totalCost && currentHero.getGems() >= 2 * unitCount) {
                        for (int i = 0; i < unitCount; i++) {
                            try {
                                Unit unit = selectedUnitClass.getDeclaredConstructor().newInstance();
                                currentHero.getArmy().addUnit(unit);
                            } catch (Exception e) {
                                System.out.println("Ошибка : " + e.getMessage());
                            }
                        }
                        currentHero.setGold(currentHero.getGold() - totalCost);
                        currentHero.setGems(currentHero.getGems() - 2 * unitCount);
                        System.out.println("Нанято " + unitCount + " " + selectedUnitClass.getSimpleName() + ". Остаток золота: " + currentHero.getGold() + ", Gems: " + currentHero.getGems());
                    } else {
                        System.out.println("Недостаточно золота  " + unitCount + " " + selectedUnitClass.getSimpleName() + ".");
                    }
                } else if (currentHero.getGold() >= totalCost) {
                    for (int i = 0; i < unitCount; i++) {
                        try {
                            Unit unit = selectedUnitClass.getDeclaredConstructor().newInstance();
                            currentHero.getArmy().addUnit(unit);
                        } catch (Exception e) {
                            System.out.println("Ошибка : " + e.getMessage());
                        }
                    }
                    currentHero.setGold(currentHero.getGold() - totalCost);
                    System.out.println("Нанято " + unitCount + " " + selectedUnitClass.getSimpleName() + ". Остаток золота: " + currentHero.getGold());
                } else {
                    int maxUnits = currentHero.getGold() / unitCost;
                    if (maxUnits > 0) {
                        for (int i = 0; i < maxUnits; i++) {
                            try {
                                Unit unit = selectedUnitClass.getDeclaredConstructor().newInstance();
                                currentHero.getArmy().addUnit(unit);
                            } catch (Exception e) {
                                System.out.println("Ошибка : " + e.getMessage());
                            }
                        }
                        currentHero.setGold(currentHero.getGold() - maxUnits * unitCost);
                        System.out.println("Недостаточно золота для найма " + unitCount + " " + selectedUnitClass.getSimpleName() + ". Нанято " + maxUnits + ".");
                    } else {
                        System.out.println("Недостаточно золота " + selectedUnitClass.getSimpleName() + ".");
                    }
                }
            } else if (unitChoice != 0) {
                System.out.println("Неверный выбор юнита.");
            }
        } else {
            System.out.println("Неверный выбор здания.");
        }
    }
    public void displayAvailableBuildings() {
        System.out.println("\n--- Доступные здания для строительства ---");
        for (BuildingType buildingType : BuildingType.values()) {
            System.out.print((buildingType.ordinal() + 1) + ". " + buildingType.getName() + " (Золото: " + buildingType.getCostGold());
            if (buildingType.getCostGems() > 0) {
                System.out.print(", Кристаллы: " + buildingType.getCostGems());
            }
            System.out.println(")");
        }
        System.out.print("Введите номер здания для строительства (или 0 для отмены): ");
    }
    public void viewHeroArmy(Hero hero) {
        System.out.println("\n--- Войско героя ---");
        // Создаем Map для подсчета юнитов
        Map<Class<? extends Unit>, Integer> unitCounts = new HashMap<>();
        for (Unit unit : hero.getArmy().getUnits()) {
            Class<? extends Unit> unitClass = unit.getClass();
            unitCounts.put(unitClass, unitCounts.getOrDefault(unitClass, 0) + 1);
        }
        // Выводим количество каждого типа юнитов
        System.out.println("Archer: " + unitCounts.getOrDefault(Archer.class, 0));
        System.out.println("Swordsman: " + unitCounts.getOrDefault(Swordsman.class, 0));
        System.out.println("Pikeman: " + unitCounts.getOrDefault(Pikeman.class, 0));
        // Выводим остальных юнитов, которых нет в стартовом наборе
        for (Map.Entry<Class<? extends Unit>, Integer> entry : unitCounts.entrySet()) {
            Class<? extends Unit> unitClass = entry.getKey();
            Integer count = entry.getValue();
            if (!unitClass.equals(Archer.class) && !unitClass.equals(Swordsman.class) && !unitClass.equals(Pikeman.class)) {
                System.out.println(unitClass.getSimpleName() + ": " + count);
            }
        }
    }

}