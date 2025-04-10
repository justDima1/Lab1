package controller;

import model.heroes.Hero;
import model.units.Archer;
import model.units.Army;
import model.units.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BattleControllerTest {

    @Test
    public void testAttackRemovesCorrectNumberOfUnitsFromDefenderArmy() {
        // Создаем армии для атакующего и защищающегося
        Army attackerArmy = new Army();
        Army defenderArmy = new Army();

        // Создаем юнитов
        int initialAttackerCount = 10;
        int initialDefenderCount = 5;
        for (int i = 0; i < initialAttackerCount; i++) {
            attackerArmy.addUnit(new Archer());
        }
        for (int i = 0; i < initialDefenderCount; i++) {
            defenderArmy.addUnit(new Archer());
        }

        // Создаем героев и связываем армии с героями
        Hero attackerHero = new Hero(0, 0, "Атакующий");
        attackerHero.setArmy(attackerArmy);
        Hero defenderHero = new Hero(1, 0, "Защищающийся");
        defenderHero.setArmy(defenderArmy);

        // Создаем BattleController (может потребоваться настройка для теста)
        BattleController battleController = new BattleController();

        // Параметры атаки
        String attackerUnitType = "Archer";
        String defenderUnitType = "Archer";

        // Получаем базовые значения урона и здоровья
        int baseDamage = new Archer().getAttack();
        int defenderHealth = new Archer().getHealth();

        // Вычисляем ожидаемое количество убитых юнитов
        int totalDamage = baseDamage * initialAttackerCount;
        int expectedKilledUnits = totalDamage / defenderHealth;

        // Вызываем метод атаки
        battleController.attack(attackerUnitType, defenderUnitType, attackerArmy, defenderArmy);

        // Проверяем, что количество юнитов в армии защищающегося уменьшилось на ожидаемое значение
        int actualRemainingUnits = defenderArmy.getUnits().size();
        int expectedRemainingUnits = initialDefenderCount - expectedKilledUnits;
        if (expectedRemainingUnits < 0) {
            expectedRemainingUnits = 0; // Не может быть меньше нуля
        }

        assertEquals(expectedRemainingUnits, actualRemainingUnits, "Неверное количество оставшихся юнитов у защитника");
    }
}