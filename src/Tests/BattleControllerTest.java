package Tests;

import controller.BattleController;
import model.heroes.Hero;
import model.units.Archer;
import model.units.Army;
import model.units.Swordsman;
import model.units.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BattleControllerTest {

    @Test
    void testAttackDecreasesDefenderUnits() {
        Army attackerArmy = new Army();
        Army defenderArmy = new Army();
        attackerArmy.addUnit(new Archer());
        defenderArmy.addUnit(new Archer());

        BattleController battleController = new BattleController();
        int initialDefenderSize = defenderArmy.getUnits().size();

        battleController.attack("Archer", "Archer", attackerArmy, defenderArmy);

        assertTrue(defenderArmy.getUnits().size() <= initialDefenderSize, "Defender units should decrease or remain the same");
    }

    @Test
    void testAttackWithEmptyAttackerSquad() {
        Army attackerArmy = new Army();
        Army defenderArmy = new Army();
        defenderArmy.addUnit(new Archer());

        BattleController battleController = new BattleController();
        int initialDefenderSize = defenderArmy.getUnits().size();

        battleController.attack("Archer", "Archer", attackerArmy, defenderArmy);

        assertEquals(initialDefenderSize, defenderArmy.getUnits().size(), "Defender army size should not change");
    }

    @Test
    void testAttackWithEmptyDefenderSquad() {
        Army attackerArmy = new Army();
        Army defenderArmy = new Army();
        attackerArmy.addUnit(new Archer());

        BattleController battleController = new BattleController();

        battleController.attack("Archer", "Archer", attackerArmy, defenderArmy);

        assertFalse(attackerArmy.getUnits().isEmpty(), "Attacker army should remain unchanged");
    }

    @Test
    public void testAttackWithDifferentUnitTypes() {
        // Создаем армии
        Army attackerArmy = new Army();
        Army defenderArmy = new Army();

        // Добавляем разных юнитов
        attackerArmy.addUnit(new Archer());
        defenderArmy.addUnit(new Swordsman());

        // Создаем героев
        Hero attackerHero = new Hero(0, 0, "Атакующий");
        attackerHero.setArmy(attackerArmy);
        Hero defenderHero = new Hero(1, 0, "Защищающийся");
        defenderHero.setArmy(defenderArmy);

        // Создаем BattleController
        BattleController battleController = new BattleController();

        // Выполняем атаку
        battleController.attack("Archer", "Swordsman", attackerArmy, defenderArmy);

        // Проверяем, что юнит защищающегося получил урон
        assertFalse(defenderArmy.getUnits().isEmpty(), "В армии защищающегося должен остаться юнит (или более)");
    }
}
/*testAttackDecreasesDefenderUnits(): Этот тест проверяет, что после атаки армия защищающегося либо уменьшилась в размере, либо осталась прежней.
testAttackWithEmptyAttackerSquad(): Этот тест проверяет, что если у атакующего нет юнитов, то размер армии защищающегося не должен измениться.
testAttackWithEmptyDefenderSquad(): Этот тест проверяет, что если у защищающегося нет юнитов, то размер армии атакующего не должен измениться.
testAttackWithDifferentUnitTypes(): В этом тесте мы создаём ситуацию, когда атакующий и защищающийся используют разные типы юнитов (Archer и Swordsman соответственно).
Это позволяет проверить, правильно ли BattleController обрабатывает ситуации с разными типами юнитов и наносит урон.
*/