package controller;

import model.BattleMap;
import model.heroes.Hero;
import model.units.Archer;
import model.units.Pikeman;
import model.units.Swordsman;
import model.units.Unit;
import model.units.Army; // Импортируем класс Army

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BattleController {

    private BattleMap battleMap;
    private Map<Class<? extends Unit>, String> unitSymbols;

    public BattleController() {
        this.battleMap = new BattleMap(15, 10); // 15x10 battle map
        this.unitSymbols = new HashMap<>();
        unitSymbols.put(Pikeman.class, "P");
        unitSymbols.put(Archer.class, "A");
        unitSymbols.put(Swordsman.class, "S");
    }
    // Добавляем метод startBattle
    public void startBattle(Army attackerArmy, Army defenderArmy) {
        Random random = new Random();

        // Determine turn order (random for now)
        boolean attackerTurn = random.nextBoolean();

        // Battle loop until one army is empty
        while (!attackerArmy.getUnits().isEmpty() && !defenderArmy.getUnits().isEmpty()) {
            if (attackerTurn) {
                attack(attackerArmy, defenderArmy);
            } else {
                attack(defenderArmy, attackerArmy);
            }

            attackerTurn = !attackerTurn; // Switch turns
        }
    }

    private void displayArmy(Hero hero, String side) {
        System.out.println(side + " Army:");
        List<Unit> units = hero.getArmy().getUnits();
        Map<Class<? extends Unit>, Integer> unitCounts = new HashMap<>();
        for (Unit unit : units) {
            Class<? extends Unit> unitClass = unit.getClass();
            unitCounts.put(unitClass, unitCounts.getOrDefault(unitClass, 0) + 1);
        }

        for (Map.Entry<Class<? extends Unit>, Integer> entry : unitCounts.entrySet()) {
            Class<? extends Unit> unitClass = entry.getKey();
            int count = entry.getValue();
            String symbol = unitSymbols.get(unitClass);
            System.out.println(symbol + ": " + count);
        }
    }

    private void attack(Army attackerArmy, Army defenderArmy) {
        Random random = new Random();

        // Choose a random unit from each army
        List<Unit> attackerUnits = attackerArmy.getUnits();
        List<Unit> defenderUnits = defenderArmy.getUnits();

        if (attackerUnits.isEmpty() || defenderUnits.isEmpty()) {
            return; // Exit if one of the armies is empty
        }

        Unit attackerUnit = attackerUnits.get(random.nextInt(attackerUnits.size()));
        Unit defenderUnit = defenderUnits.get(random.nextInt(defenderUnits.size()));

        // Calculate damage
        int damage = attackerUnit.getAttack() - defenderUnit.getDefense();
        if (damage < 0) {
            damage = 0; // Minimum damage is 0
        }

        // Apply damage
        int defenderHealth = defenderUnit.getHealth() - damage;
        defenderUnit.setHealth(defenderHealth);

        System.out.println("Attacker attacks! " + "Defender Health: " + defenderHealth);
        // Check if defender unit is dead
        if (defenderHealth <= 0) {
            System.out.println("Defender unit died!");
            defenderUnits.remove(defenderUnit);
        }
    }
}