package view;

import controller.GameController;
import model.City;
import model.Player;
import model.buildings.*;
import model.heroes.Hero;
import model.units.*;

import java.util.*;

public class CityView {
    private GameController gameController;
    private Scanner scanner;

    public CityView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void displayCityMenu(City city, Hero currentHero, Player player) { // Добавляем Player player
        System.out.println("\n--- " + city.getName() + " ---");
        System.out.println("Золота в казне: " + player.getGold().getAmount()); // Изменено
        System.out.println("Gems: " + player.getGems().getAmount()); // Изменено
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

    public void buildBuilding(City city, Hero currentHero, Player player) { // Добавляем параметр Player player
        displayAvailableBuildings();
        int choice = getChoice();
        if (choice > 0 && choice <= BuildingType.values().length) {
            BuildingType selectedBuilding = BuildingType.values()[choice - 1];

            if (player.getGold().getAmount() >= selectedBuilding.getCostGold() && player.getGems().getAmount() >= selectedBuilding.getCostGems()) { // Используем player
                player.getGold().setAmount(player.getGold().getAmount() - selectedBuilding.getCostGold()); // Используем player
                player.getGems().setAmount(player.getGems().getAmount() - selectedBuilding.getCostGems()); // Используем player
                city.addBuilding(BuildingFactory.createBuilding(selectedBuilding));
                System.out.println(selectedBuilding.getName() + " constructed successfully!");
            } else {
                System.out.println("Недостаточно золота и/или гемов для строительства " + selectedBuilding.getName());
            }
        } else if (choice != 0) {
            System.out.println("Неверный выбор.");
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
        if (hero == null || hero.getArmy() == null) {
            System.out.println("У героя нет армии.");
            return;
        }

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

    public void recruitUnits(City city, Hero currentHero, Player player) {
        List<RecruitBuilding> recruitBuildings = city.getRecruitBuildings();
        if (recruitBuildings.isEmpty()) {
            System.out.println("Нет зданий для найма юнитов в городе.");
            return;
        }

        System.out.println("\n--- Доступные для найма здания ---");
        int buildingIndex = 1;
        for (RecruitBuilding recruitBuilding : recruitBuildings) {
            System.out.println(buildingIndex + ". " + recruitBuilding.getClass().getSimpleName() + ":");
            List<Class<? extends Unit>> availableUnits = recruitBuilding.getAvailableUnits();
            for (int i = 0; i < availableUnits.size(); i++) {
                Class<? extends Unit> unitClass = availableUnits.get(i);
                int cost = recruitBuilding.getRecruitCost(unitClass);
                System.out.println("   " + (i + 1) + ". " + unitClass.getSimpleName() + " (Золото: " + cost + ")");
            }
            buildingIndex++;
        }

        System.out.print("Выберите здание (0 для отмены): ");
        int buildingChoice = getChoice();

        if (buildingChoice > 0 && buildingChoice <= recruitBuildings.size()) {
            RecruitBuilding selectedBuilding = recruitBuildings.get(buildingChoice - 1);
            List<Class<? extends Unit>> availableUnits = selectedBuilding.getAvailableUnits();

            System.out.print("Выберите юнита (0 для отмены): ");
            int unitChoice = getChoice();

            if (unitChoice > 0 && unitChoice <= availableUnits.size()) {
                Class<? extends Unit> selectedUnitClass = availableUnits.get(unitChoice - 1);
                int unitCost = selectedBuilding.getRecruitCost(selectedUnitClass);

                System.out.print("Введите количество (0 для отмены): ");
                int unitCount = getChoice();

                int totalCost = unitCount * unitCost;

                if (totalCost <= 0) {
                    System.out.println("Что то пошло не так");
                    return;
                }

                if (selectedUnitClass.equals(Angel.class)) {

                    if (player.getGold().getAmount() >= totalCost && player.getGems().getAmount() >= 2 * unitCount) {
                        player.getGold().setAmount(player.getGold().getAmount() - totalCost); // Используем player

                        player.getGems().setAmount(player.getGems().getAmount() - 2 * unitCount); // Используем player
                        for (int i = 0; i < unitCount; i++) {
                            try {
                                Unit unit = selectedUnitClass.getDeclaredConstructor().newInstance();
                                currentHero.getArmy().addUnit(unit);
                                player.getUnits().add(unit);// Добавляем юнита в армию героя // Добавляем юнита игроку

                            } catch (Exception e) {
                                System.out.println("Ошибка : " + e.getMessage());
                            }
                        }
                        System.out.println("Успешно нанято " + unitCount + " " + selectedUnitClass.getSimpleName() + ".");

                    } else {
                        System.out.println("Недостаточно золота и/или гемов для найма ангелов.");
                    }
                }
                else if (player.getGold().getAmount() >= totalCost) { // Если хватает золота
                    player.getGold().setAmount(player.getGold().getAmount() - totalCost);
                    for (int i = 0; i < unitCount; i++) {
                        try {
                            Unit unit = selectedUnitClass.getDeclaredConstructor().newInstance();
                            currentHero.getArmy().addUnit(unit);
                            player.getUnits().add(unit);

                        } catch (Exception e) {
                            System.out.println("Ошибка : " + e.getMessage());
                        }
                    }
                    System.out.println("Нанято " + unitCount + " " + selectedUnitClass.getSimpleName() + ". Остаток золота: " + player.getGold().getAmount());

                }
                else {
                    System.out.println("Недостаточно золота для найма юнитов.");
                }

            }  else if (unitChoice != 0) {
                System.out.println("Неверный выбор юнита.");
            }
        } else if (buildingChoice != 0) {
            System.out.println("Неверный выбор здания.");
        }
    }
}