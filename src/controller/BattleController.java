package controller;

import model.heroes.Hero;
import model.units.Archer;
import model.units.Pikeman;
import model.units.Swordsman;
import model.units.Angel;
import model.units.Cavalrist;
import model.units.Paladin;
import model.units.Unit;
import model.units.Army;

import java.util.*;

public class BattleController {

    private Scanner scanner;

    public BattleController() {
        this.scanner = new Scanner(System.in);
    }

    public void startBattle(Hero hero1, Hero hero2) {
        Army attackerArmy = hero1.getArmy();
        Army defenderArmy = hero2.getArmy();

        System.out.println("Началась битва между " + hero1.getName() + " и " + hero2.getName() + "!");

        while (!attackerArmy.isEmpty() && !defenderArmy.isEmpty()) {
            //  Ход игрока
            if (!attackerArmy.isEmpty()) {
                playerTurn(hero1, hero2);
            } else {
                System.out.println(hero1.getName() + " не имеет больше юнитов для атаки.");
                break;
            }

            //  Ход ИИ
            if (!defenderArmy.isEmpty()) {
                aiTurn(hero2, hero1);
            } else {
                System.out.println(hero2.getName() + " не имеет больше юнитов для атаки.");
                break;
            }
        }

        //  Определение победителя
        if (attackerArmy.isEmpty()) {
            System.out.println(hero2.getName() + " победил!");
        } else {
            System.out.println(hero1.getName() + " победил!");
        }
    }

    private void playerTurn(Hero attackerHero, Hero defenderHero) {
        Army attackerArmy = attackerHero.getArmy();
        Army defenderArmy = defenderHero.getArmy();

        System.out.println("\n--- Ход " + attackerHero.getName() + " ---");
        displayArmies(attackerHero, defenderHero);

        //  Выбор атакующего отряда
        List<String> attackerUnits = getSquads(attackerArmy);
        String attackerUnit = chooseAttacker(attackerUnits);
        if (attackerUnit == null) {
            return;
        }

        //  Выбор цели
        List<String> defenderUnits = getSquads(defenderArmy);
        String defenderUnit = chooseTarget(defenderUnits);
        if (defenderUnit == null) {
            return;
        }

        //  Атака
        attack(attackerUnit, defenderUnit, attackerArmy, defenderArmy);
    }

    private void aiTurn(Hero attackerHero, Hero defenderHero) {
        Army attackerArmy = attackerHero.getArmy();
        Army defenderArmy = defenderHero.getArmy();

        System.out.println("\n--- Ход " + attackerHero.getName() + " (AI) ---");

        if (attackerArmy.getUnits().isEmpty()) {
            System.out.println("У " + attackerHero.getName() + " (AI) нет юнитов для атаки!");
            return;
        }

        if (defenderArmy.getUnits().isEmpty()) {
            System.out.println("У " + defenderHero.getName() + " нет целей для атаки!");
            return;
        }

        String attackerUnit = attackerArmy.getUnits().get(new Random().nextInt(attackerArmy.getUnits().size())).getClass().getSimpleName();
        String defenderUnit = defenderArmy.getUnits().get(new Random().nextInt(defenderArmy.getUnits().size())).getClass().getSimpleName();

        attack(attackerUnit, defenderUnit, attackerArmy, defenderArmy);
    }

    private String chooseAttacker(List<String> attackerUnits) {

        if (attackerUnits.isEmpty()) {
            System.out.println("У вас нет юнитов для атаки!");
            return null;
        }

        System.out.println("Выберите отряд для атаки:");
        for (int i = 0; i < attackerUnits.size(); i++) {
            System.out.println((i + 1) + ". " + attackerUnits.get(i));
        }

        int choice = -1;
        while (choice < 1 || choice > attackerUnits.size()) {
            System.out.print("Введите номер отряда: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите число.");
            }
        }

        return attackerUnits.get(choice - 1);
    }

    private String chooseTarget(List<String> defenderUnits) {
        if (defenderUnits.isEmpty()) {
            System.out.println("У противника нет целей для атаки!");
            return null;
        }

        System.out.println("Выберите цель для атаки:");
        for (int i = 0; i < defenderUnits.size(); i++) {
            System.out.println((i + 1) + ". " + defenderUnits.get(i));
        }

        int choice = -1;
        while (choice < 1 || choice > defenderUnits.size()) {
            System.out.print("Введите номер цели: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите число.");
            }
        }

        return defenderUnits.get(choice - 1);
    }

    public void attack(String attackerUnitType, String defenderUnitType, Army attackerArmy, Army defenderArmy) {
        System.out.println(attackerUnitType + " атакует " + defenderUnitType + "!");

        // Получаем отряд атакующих
        List<Unit> attackerSquad = new ArrayList<>();
        for (Unit unit : attackerArmy.getUnits()) {
            if (Objects.equals(unit.getClass().getSimpleName(), attackerUnitType)) {
                attackerSquad.add(unit);
            }
        }

        // Получаем отряд защищающихся
        List<Unit> defenderSquad = new ArrayList<>();
        for (Unit unit : defenderArmy.getUnits()) {
            if (Objects.equals(unit.getClass().getSimpleName(), defenderUnitType)) {
                defenderSquad.add(unit);
            }
        }

        if (attackerSquad.isEmpty() || defenderSquad.isEmpty()) {
            System.out.println("Нет юнитов для атаки или защиты!");
            return;
        }

        // Определяем базовый урон и здоровье юнита
        int baseDamage = attackerSquad.get(0).getAttack();
        int defenderHealth = defenderSquad.get(0).getHealth();

        // Суммарный урон отряда
        int totalDamage = baseDamage * attackerSquad.size();

        // Количество убитых юнитов
        int killedUnits = Math.min(totalDamage / defenderHealth, defenderSquad.size());
        System.out.println(totalDamage);
        System.out.println(defenderHealth);
        // Удаляем убитых юнитов
        int unitsRemoved = 0;
        List<Unit> unitsToRemove = new ArrayList<>(); // Список на удаление
        for (int i = 0; i < defenderArmy.getUnits().size(); i++) {
            if (unitsRemoved >= killedUnits) break;
            if (Objects.equals(defenderArmy.getUnits().get(i).getClass().getSimpleName(), defenderUnitType)) {
                unitsToRemove.add(defenderArmy.getUnits().get(i));
                unitsRemoved++;
            }
        }

        // Удаляем юниты из списка
        for (Unit unitToRemove : unitsToRemove) {
            defenderArmy.removeUnit(unitToRemove);
        }

        if (killedUnits > 0) {
            System.out.println("Убито " + killedUnits + " юнитов.");
        } else {
            System.out.println("Никто не погиб");
        }

        System.out.println("Сражение завершено.");
    }
    private void displayArmies(Hero hero1, Hero hero2) {
        System.out.println("\n--- Армия " + hero1.getName() + "(Вы)---");
        displayArmy(hero1.getArmy());
        System.out.println("\n--- Армия " + hero2.getName() + "(ИИ) ---");
        displayArmy(hero2.getArmy());
    }

    private void displayArmy(Army army) {
        Map<String, Integer> unitCounts = new HashMap<>();

        for (Unit unit : army.getUnits()) {
            String unitType = unit.getClass().getSimpleName();
            unitCounts.put(unitType, unitCounts.getOrDefault(unitType, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : unitCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private List<String> getSquads(Army army) {
        List<String> squads = new ArrayList<>();
        Map<String, Integer> unitCounts = new HashMap<>();

        for (Unit unit : army.getUnits()) {
            String unitType = unit.getClass().getSimpleName();
            unitCounts.put(unitType, unitCounts.getOrDefault(unitType, 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : unitCounts.entrySet()) {
            squads.add(entry.getKey());
        }
        return squads;
    }

}